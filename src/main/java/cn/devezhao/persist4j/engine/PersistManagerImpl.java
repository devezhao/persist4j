package cn.devezhao.persist4j.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import cn.devezhao.persist4j.DataAccessException;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.PersistException;
import cn.devezhao.persist4j.PersistManager;
import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.Editor;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.editor.DecimalEditor;
import cn.devezhao.persist4j.dialect.editor.DoubleEditor;
import cn.devezhao.persist4j.exception.SqlExceptionConverter;
import cn.devezhao.persist4j.metadata.CascadeModel;
import cn.devezhao.persist4j.util.SqlHelper;

/**
 * 持久化对象
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: PersistManagerImpl.java 20 2009-02-10 03:35:10Z
 *          zhaofang123@gmail.com $
 */
public class PersistManagerImpl extends JdbcSupport implements PersistManager {
	private static final long serialVersionUID = 1204654171280975411L;
	
	private static final Log LOG = LogFactory.getLog(PersistManagerImpl.class);
	
	transient private PersistManagerFactory managerFactory;
	
	public PersistManagerImpl(PersistManagerFactory managerFactory) {
		super();
		this.managerFactory = managerFactory;
	}

	public PersistManagerFactory getPersistManagerFactory() {
		return managerFactory;
	}

	public Record save(final Record record) throws DataAccessException {
		Validate.isTrue(record.getPrimary() == null);
		
		Entity e = record.getEntity();
		final ID eId = ID.newId(e.getEntityCode());
		
		StringBuilder insert = new StringBuilder("insert into ");
		insert.append(quote(e.getPhysicalName())).append(" ( ").append(quote(e.getPrimaryField().getPhysicalName()));
		StringBuilder valuesclause = new StringBuilder("values ( ?");
		
		final List<Field> fieldList = new LinkedList<Field>();
		fieldList.add(e.getPrimaryField());
		for (Iterator<String> iter = record.getAvailableFieldIterator(); iter.hasNext(); ) {
			Field field = e.getField( iter.next() );
			fieldList.add( field );
			insert.append(", ").append(quote(field.getPhysicalName()));
			valuesclause.append(", ?");
		}
		insert.append(" ) ").append(valuesclause.append(" )"));
		
		final String sql = insert.toString();
		valuesclause = null;
		insert = null;
		
		int affected = 0;
		final List<String> refsSqls = new ArrayList<String>();
		try {
			affected = execute(new StatementCallback(){
				public String getSql() {
					return sql;
				}
				
				public Object doInParameters(PreparedStatement pstmt) throws SQLException {
					int index = 1;
					for (Field field : fieldList) {
						Editor editor = field.getType().getFieldEditor();
						Object value = null;
						
						try {
							if (field.getType() == FieldType.PRIMARY) {
								value = eId;
							} else {
								value = record.getObjectValue(field.getName());
							}
							
							if (field.getType() == FieldType.DOUBLE
									&& field.getDecimalScale() != FieldType.DEFAULT_DECIMAL_SCALE) {
								((DoubleEditor) editor).set(pstmt, index++, value, field.getDecimalScale());
							} else if (field.getType() == FieldType.DECIMAL
									&& field.getDecimalScale() != FieldType.DEFAULT_DECIMAL_SCALE) {
								((DecimalEditor) editor).set(pstmt, index++, value, field.getDecimalScale());
							} else {
								editor.set(pstmt, index++, value);
							}
						} catch (Exception ex) {
							throw new PersistException(field.getOwnEntity().getName() + "#" + field.getName(), ex);
						}
					}
					return null;
				}
			});
			
			if (!refsSqls.isEmpty()) {
				int[] batchExec = executeBatch(refsSqls.toArray(new String[refsSqls.size()]));
				for (int be : batchExec) {
					affected += be;
				}
			}
		} catch (SQLException sqlex) {
			throw SqlExceptionConverter.convert(sqlex, "#INSERT", sql);
		}
		
		LOG.debug("total affected " + affected + " rows");
		
		record.setID(e.getPrimaryField().getName(), eId);
		return record;
	}

	public Record update(final Record record) throws DataAccessException {
		Validate.notNull(record.getPrimary());
		
		Entity e = record.getEntity();
		
		StringBuilder update = new StringBuilder("update ");
		update.append(quote(e.getPhysicalName())).append(" set ");
		
		final List<Field> fieldList = new LinkedList<Field>();
		Iterator<String> iter = record.getAvailableFieldIterator();
		boolean first = true;
		while(iter.hasNext()) {
			Field field = e.getField(iter.next());
			if (field.getType() == FieldType.PRIMARY) {
				continue;
			}
			
			fieldList.add(field);
			if (first) {
				first = false;
			} else {
				update.append(", ");
			}
			update.append(quote(field.getPhysicalName())).append(" = ?");
		}
		update.append(" where ").append(quote(e.getPrimaryField().getPhysicalName())).append(" = ?");
		fieldList.add(e.getPrimaryField());
		
		final String sql = update.toString();
		update = null;
		
		int affected = 0;
		try {
			if (!first) {
				affected = execute(new StatementCallback(){
					public String getSql() {
						return sql;
					}
					
					public Object doInParameters(PreparedStatement pstmt) throws SQLException {
						int index = 1;
						for (Field field : fieldList) {
							Editor editor = field.getType().getFieldEditor();
							Object value = record.getObjectValue(field.getName());
							
							if (NullValue.is(value)) {
								pstmt.setNull(index++, managerFactory.getDialect().getSQLType(editor.getType()));
							} else if (field.getType() == FieldType.DOUBLE
									&& field.getDecimalScale() != FieldType.DEFAULT_DECIMAL_SCALE) {
								((DoubleEditor) editor).set(pstmt, index++, value, field.getDecimalScale());
							} else if (field.getType() == FieldType.DECIMAL
									&& field.getDecimalScale() != FieldType.DEFAULT_DECIMAL_SCALE) {
								((DecimalEditor) editor).set(pstmt, index++, value, field.getDecimalScale());
							} else {
								editor.set(pstmt, index++, value);
							}
						}
						return null;
					}
				});
			}
		} catch (SQLException sqlex) {
			throw SqlExceptionConverter.convert(sqlex, "#UPDATE", sql);
		}
		LOG.debug("total affected " + affected + " rows");
		
		return record;
	}

	public Record saveOrUpdate(Record record) throws DataAccessException {
		return (record.getPrimary() == null) ? save(record) : update(record);
	}

	public int delete(final ID id) throws DataAccessException {
		Validate.notNull(id);
		
		Entity entity = managerFactory.getMetadataFactory().getEntity(id.getEntityCode());
		Field[] referenceTos = entity.getReferenceToFields();
		
		final String delete = MessageFormat.format("delete from {0} where {1} = ''{2}''",
				quote(entity.getPhysicalName()),
				quote(entity.getPrimaryField().getPhysicalName()),
				id.toLiteral());
		
		final List<String> sqlList = new ArrayList<String>();
		sqlList.add(delete);
		
		Map<Field, ID[]> refToIdMap = null;
		if (referenceTos.length > 0) {
			refToIdMap = new HashMap<Field, ID[]>();
			
			for (Field rto : referenceTos) {
				if (rto.getCascadeModel() == CascadeModel.Ignore) {
					continue;
				}
				
				ID[] referenceToIds = getReferenceToIds(rto, id);
				if (referenceToIds.length > 0) {
					refToIdMap.put(rto, referenceToIds);
				}
			}
			
			for (Iterator<Map.Entry<Field, ID[]>> iter = refToIdMap.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<Field, ID[]> e = iter.next();
				Field cField = e.getKey();
				
				// TODO 不支持迭代级联删除
				
				if (cField.getType() == FieldType.REFERENCE || cField.getType() == FieldType.ANY_REFERENCE) {
					String formatted = null;
					if (cField.getCascadeModel() == CascadeModel.Delete) {
						formatted = "delete from {0} where {1} = ''{2}''";
					} else if (cField.getCascadeModel() == CascadeModel.RemoveLinks) {
						// set {1} = null, but some column can not be null
						if (cField.isNullable()) {
							formatted = "update {0} set {1} = NULL where {1} = ''{2}''";
						} else {
							formatted = "update {0} set {1} = '''' where {1} = ''{2}''";
						}
					} else {
						LOG.warn("Unknow CascadeModel: " + cField.getCascadeModel());
					}
					
					if (formatted != null) {
						String cql = MessageFormat.format(formatted,
								quote(cField.getOwnEntity().getPhysicalName()),
								quote(cField.getPhysicalName()),
								id.toLiteral() );
						sqlList.add(cql);
					}
				}
			}
		}
		
		int[] batchAffected = null;
		try {
			batchAffected = executeBatch(sqlList.toArray(new String[sqlList.size()]));
		} catch (SQLException sqlex) {
			throw SqlExceptionConverter.convert(sqlex, "#DELETE", null);
		}
		
		int affected = 0;
		for (int ba : batchAffected) {
			affected += ba;
		}
		LOG.debug("total affected " + affected + " rows");
		
		return affected;
	}

	public int[] delete(ID[] ids) throws DataAccessException {
		int[] rowsAffected = new int[ids.length];
		
		for (int i = 0; i < ids.length; i++) {
			rowsAffected[i] = delete(ids[i]);
		}
		return rowsAffected;
	}
	
	@Override
	protected Connection getConnection() {
		return DataSourceUtils.getConnection(managerFactory.getDataSource());
	}
	
	@Override
	protected void releaseConnection(Connection connect) {
		SqlHelper.close(connect, managerFactory.getDataSource());
	}
	
	// ----------------------------------------------------------------------------------------
	
	private String quote(String ident) {
		return this.managerFactory.getDialect().quote(ident);
	}
	
	private ID[] getReferenceToIds(Field referenceToField, ID masterId) {
		String sql = null;
		Entity own = referenceToField.getOwnEntity();
		if (referenceToField.getType() == FieldType.REFERENCE || referenceToField.getType() == FieldType.ANY_REFERENCE) {
			sql = MessageFormat.format("select {0} from {1} where {2} = ''{3}'' and {2} is not null",
					quote(own.getPrimaryField().getPhysicalName()),
					quote(own.getPhysicalName()),
					quote(referenceToField.getPhysicalName()),
					masterId.toLiteral());
		} else {
			return ID.EMPTY_ID_ARRAY;
		}
		
		List<ID> ids = new ArrayList<ID>();
		ResultSet rs = null;
		try {
			rs =  nativeQuery(sql);
			while (rs.next()) {
				String val = rs.getString(1);
				ids.add( ID.valueOf(val) );
			}
		} catch (SQLException sqlex) {
			throw SqlExceptionConverter.convert(sqlex, "#GET_REFERENCE_TO_IDS", sql);
		} finally {
			if (rs != null) {
				SqlHelper.close(rs);
				
				Connection conn = null;
				try {
					Statement stmt = rs.getStatement();
					conn = stmt == null ? null : stmt.getConnection();
					SqlHelper.close(stmt);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if (conn != null) {
					releaseConnection(conn);
				}
			}
		}
		
		if (ids.isEmpty()) {
			return ID.EMPTY_ID_ARRAY;
		}
		return ids.toArray(new ID[ids.size()]);
	}
}
