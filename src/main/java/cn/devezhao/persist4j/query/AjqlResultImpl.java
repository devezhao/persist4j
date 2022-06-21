package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.Editor;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.exception.SqlExceptionConverter;
import cn.devezhao.persist4j.query.compiler.JoinField;
import cn.devezhao.persist4j.query.compiler.ParameterItem;
import cn.devezhao.persist4j.query.compiler.SelectItem;
import cn.devezhao.persist4j.query.compiler.SelectItemType;
import cn.devezhao.persist4j.util.SqlHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: AjqlResultImpl.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class AjqlResultImpl implements Result {
	private static final long serialVersionUID = -4514273807145664184L;

	private static final Log LOG = LogFactory.getLog(AjqlResultImpl.class);
	
	static final Object[][] EMPTY_OBJECT_ARRAYS = new Object[0][];
	
	final private AjqlQuery query;
	
	private List<Object[]> dataCache;

	transient private PreparedStatement pstmt;
	
	protected AjqlResultImpl(AjqlQuery query) {
		this.query = query;
	}

	@Override
    public Object[][] array() {
		if (execQuery(0).isEmpty()) {
			return EMPTY_OBJECT_ARRAYS;
		}
		return dataCache.toArray(new Object[dataCache.size()][]);
	}
	
	@Override
    public Object[] unique() {
		query.setMaxResults(1);
		query.setLimit(1, query.offset);
		if (execQuery(1).isEmpty()) {
			return null;
		}
		return dataCache.get(0);
	}

	@Override
    public List<Record> list() {
		if (execQuery(0).isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Record> records = new ArrayList<>();
		for (Object[] row : dataCache) {
			records.add( bindRecord(row) );
		}
		return records;
	}

	@Override
    public Record record() {
		query.setMaxResults(1);
		query.setLimit(1, query.offset);
		if (execQuery(1).isEmpty()) {
			return null;
		}
		
		return bindRecord(dataCache.get(0));
	}
	
	@Override
    public Result reset() {
		if (dataCache != null) {
			dataCache = null;
		}
		return this;
	}
	
	/**
	 * start exec query
	 * 
	 * @param fetch
	 * @return
	 */
	protected List<Object[]> execQuery(int fetch) {
		if (dataCache != null) {
			return dataCache;
		}
		dataCache = new LinkedList<>();
		
		SelectItem[] selectItems = query.getSelectItems();
		
		Map<String, ParameterItem> inParamaters = query.getQueryCompiler().getInParameters();
		Map<ParameterItem, Object> paramatersMapping = null;
		if (!inParamaters.isEmpty()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Use in-paramaters : " + inParamaters);
			}
			
			paramatersMapping = new HashMap<>();
			Map<String, Object> inParamatersRaw = query.getInParameters();

			for (Map.Entry<String, ParameterItem> e : inParamaters.entrySet()) {
				boolean index = (e.getKey().charAt(0) != ':');

				Object pVal;
				if (index) {
					pVal = inParamatersRaw.get(e.getKey());
				} else {
					pVal = inParamatersRaw.get(e.getKey().substring(1));
				}

				if (pVal == null) {
					throw new QueryException("The " + ((index) ? "index" : "named") + " parameter `" + e.getKey() + "` unset");
				}
				paramatersMapping.put(e.getValue(), pVal);
			}
		}
		
		// Optimize limit offset
//		boolean limitOffset = query.getPersistManagerFactory().getDialect().supportsLimitOffset();
		String aSql = query.getQueryCompiler().getCompiledSql();
		if (query.limit > 0) {
			int offset = query.offset;
//			/*
//			 * supports limit-offset && limit offset not been specified && first-result has been specified
//			 */
//			if (limitOffset && offset <= 0 && query.getFirstResult() > 0)
//				offset = query.getFirstResult();
			
			aSql = query.getPersistManagerFactory().getDialect().insertLimit(
					aSql, query.limit, offset);
		}
		/*
		 * supports limit-offset, max-results has been specified
		 */
//		else if (limitOffset && (query.getMaxResults() > 0)) {
//			sql = query.getPersistManagerFactory().getDialect().insertLimit(
//					sql, query.getMaxResults(), query.getFirstResult());
//		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info(">> " + aSql);
		}
		
		SlowLogger.start();
		Connection connection = DataSourceUtils.getConnection(
				query.getPersistManagerFactory().getDataSource());
		ResultSet rs = null;
		
		try {
			/*
			 * cursor been forward-only and concur-read-only */
			pstmt = connection.prepareStatement(
					aSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			
			if (paramatersMapping != null && !paramatersMapping.isEmpty()) {
				for (Map.Entry<ParameterItem, Object> e : paramatersMapping.entrySet()) {
					ParameterItem pi = e.getKey();
					pi.getField().getType().getFieldEditor().set( pstmt, pi.getIndex(), e.getValue() );
				}
			}
			
			if (query.getTimeout() > 0) {
				pstmt.setQueryTimeout(query.getTimeout());
			}
			
			rs = pstmt.executeQuery();
			if (fetch > 0) {
				rs.setFetchSize(fetch);
			}
			
			if (query.getFirstResult() > 0) {
				rs.absolute(query.getFirstResult());
			}
			
			if (query.getMaxResults() <= 0) {
				while (rs.next()) {
					dataCache.add(readRow(selectItems, rs));
				}
			} else {
				int current = 0;
				while (current++ < query.getMaxResults() && rs.next()) {
					dataCache.add(readRow(selectItems, rs));
				}
			}
			
			// clean
			SqlHelper.close(rs);
			SqlHelper.clear(pstmt);
			
		} catch (SQLException sqlex) {
			throw SqlExceptionConverter.convert(sqlex, "#AJQL_QUERY", aSql);
		} finally {
			SqlHelper.close(rs);
			SqlHelper.close(pstmt);
			SqlHelper.close(connection, query.getPersistManagerFactory().getDataSource());
			SlowLogger.stop(query.getSlowLoggerTime(), dataCache.size(), aSql);
		}
		
		return dataCache;
	}
	
	// Read pre-row
	protected Object[] readRow(SelectItem[] selectItems, ResultSet rs) throws SQLException {
		Object[] row = new Object[selectItems.length];

		for (SelectItem item : selectItems) {
			if (item.getType() == SelectItemType.Aggregator) {
				Editor editor;
				String aggregator = ((JoinField) item).getAggregator();
				
				if ("COUNT".equalsIgnoreCase(aggregator)) {
					editor = FieldType.LONG.getFieldEditor();
					
				} else if ("DATE_FORMAT".equalsIgnoreCase(aggregator) 
						|| "CONCAT".equalsIgnoreCase(aggregator)) {
					editor = FieldType.STRING.getFieldEditor();
					
				} else if ("YEAR".contentEquals(aggregator)
						|| "QUARTER".contentEquals(aggregator)
						|| "MONTH".contentEquals(aggregator)
						|| "WEEK".contentEquals(aggregator)
						|| "DATE".contentEquals(aggregator)) {
					editor = FieldType.INT.getFieldEditor();
					
				} else {
					editor = item.getField().getType().getFieldEditor();
				}
				
				row[item.getIndex()] = editor.get(rs, item.getIndex() + 1);
				continue;
			}
			
			Editor editor = item.getField().getType().getFieldEditor();
			Object value = editor.get(rs, item.getIndex() + 1);
			if (value == null) {
				continue;
			}
			row[item.getIndex()] = value;
		}
		return row;
	}
	
	// Build a record for the row data
	private Record bindRecord(Object[] row) {
		QueryedRecord record = new QueryedRecord(
				query.getQueryCompiler().getRootEntity(), 
				query.getPersistManagerFactory().getSQLExecutorContext());
		
		for (SelectItem item : query.getSelectItems()) {
			Object v = row[item.getIndex()];
			if (v == null) {
                continue;
            }
			record.setObject(item.getFieldPath(), v);
		}
		record.setSelectItems(query.getSelectItems());
		record.completeAfter();
		return record;
	}

//	// Read labels of reference-list
//	private Object[][] readLabel(ID[] ids) throws SQLException {
//		Validate.isTrue(ids.length <= 20, "id too many, most be 20");
//		List<Object[]> list = new ArrayList<Object[]>();
//		
//		ID[][] idArrays = sortIdArray(ids);
//		MetadataFactory factory = query.getPersistManagerFactory().getMetadataFactory();
//		for (ID[] array : idArrays) {
//			ID meta = array[0];
//			Entity entity = factory.getEntity(meta.getEntityCode());
//			
//			StringBuilder ql = new StringBuilder("select ");
//			ql.append(entity.getPrimaryField().getName()).append(", ")
//				.append(entity.getNameField().getName())
//				.append(" from ").append(entity.getName())
//				.append(" where ").append(entity.getPrimaryField().getName()).append(" in ( '")
//				.append(StringUtils.join(array, "', '")).append("' )");
//			
//			QueryCompiler compiler = new QueryCompiler(ql.toString());
//			String subQl = compiler.compile(query.getPersistManagerFactory().getSQLExecutorContext());
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("Exec sub-query (n-reference-label) : " + subQl);
//			}
//			
//			ResultSet rs = pstmt.executeQuery(subQl);
//			
//			SelectItem[] selectItems = compiler.getSelectItems();
//			while (rs.next()) {
//				Editor i0Editor = selectItems[0].getField().getType().getFieldEditor();
//				Editor i1Editor = selectItems[1].getField().getType().getFieldEditor();
//				
//				Object i0Val = i0Editor.get(rs, selectItems[0].getIndex() + 1);
//				Object i1Val = i1Editor.get(rs, selectItems[1].getIndex() + 1);
//				
//				if (i0Val != null)
//					list.add(new Object[] { i0Val, i1Val });
//			}
//		}
//		
//		return list.toArray(new Object[list.size()][]);
//	}
	
//	// Quoted strings
//	private String quote(String string) {
//		return query.getPersistManagerFactory().getDialect().quote(string);
//	}
//	
//	// Throw unsupports exception
//	private void throwUnsupports() {
//		throw new UnsupportedOperationException();
//	}
	
//	// Sort id array
//	private ID[][] sortIdArray(ID[] ids) {
//		Map<Integer, List<ID>> map = new HashMap<Integer, List<ID>>();
//		
//		for (ID id : ids) {
//			List<ID> list = map.get(id.getEntityCode());
//			if (list == null) {
//				list = new ArrayList<ID>();
//				map.put(id.getEntityCode(), list);
//			}
//			list.add(id);
//		}
//		
//		ID[][] arrays = new ID[map.size()][];
//		int idx = 0;
//		for (List<ID> list : map.values()) {
//			arrays[idx++] = list.toArray(new ID[list.size()]);
//		}
//		return arrays;
//	}
}
