package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.exception.SqlExceptionConverter;
import cn.devezhao.persist4j.util.SqlHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Native sql query
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, May 25, 2009
 * @version $Id: NativeQueryImpl.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class NativeQueryImpl extends BaseQuery<NativeQuery> implements NativeQuery {
	private static final long serialVersionUID = -5302544598435577417L;
	
	private static final Log LOG = LogFactory.getLog(NativeQueryImpl.class);

	final private String sql;
	transient private PersistManagerFactory managerFactory;

	private Map<Integer, Object> inParameters = new HashMap<>();

	private Type[] parameterTypes = null;
	private Type[] returnTypes = null;

	transient private List<Object[]> dataCache = null;

	public NativeQueryImpl(String sql, PersistManagerFactory managerFactory) {
		this.sql = sql;
		this.managerFactory = managerFactory;
	}
	
	@Override
    public NativeQuery setInParameterType(Type... fTypes) {
		this.parameterTypes = fTypes;
		return this;
	}

	@Override
    public NativeQuery setReturnType(Type... fTypes) {
		this.returnTypes = fTypes;
		return this;
	}

	@Override
	public NativeQuery setParameter(int position, Object value) {
		this.inParameters.put(position, value);
		return this;
	}

	@Override
    public Object[][] array() {
		if (execQuery(0).isEmpty()) {
            return AjqlResultImpl.EMPTY_OBJECT_ARRAYS;
        }

		return dataCache.toArray(new Object[dataCache.size()][]);
	}

	@Override
    public Object[] unique() {
		setMaxResults(1);
		setLimit(1, offset);
		if (execQuery(1).isEmpty()) {
            return null;
        }
		
		return dataCache.get(0);
	}
	
	@Override
    public NativeQuery reset() {
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

		if (fetch == 1) {
            setMaxResults(1);
        }
		// else to all
		
		String aSql = sql;
		if (limit > 0) {
			aSql = managerFactory.getDialect().insertLimit(aSql, limit, offset);
		}
		if (LOG.isInfoEnabled()) {
			LOG.info(">> " + aSql);
		}

		SlowLogger.start();
		Connection connect = DataSourceUtils.getConnection(managerFactory
				.getDataSource());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = connect.prepareStatement(aSql);
			
			if (!inParameters.isEmpty()) {
				// set specify value
				if (parameterTypes != null && parameterTypes.length == inParameters.size()) {
					for (Map.Entry<Integer, Object> e : inParameters.entrySet()) {
						int idx = e.getKey() - 1;
						parameterTypes[idx].getFieldEditor().set( pstmt, idx + 1, e.getValue() );
					}
				} else {
					if (LOG.isDebugEnabled()) {
                        LOG.debug("Parameter type unset, use #setObject");
                    }
					
					// set generial value
					for (Map.Entry<Integer, Object> e : inParameters.entrySet()) {
						pstmt.setObject(e.getKey(), e.getValue());
					}
				}
			}
			
			if (getTimeout() > 0) {
                pstmt.setQueryTimeout(getTimeout());
            }

			rs = pstmt.executeQuery();
			int colCount = rs.getMetaData().getColumnCount();
			
			if (fetch > 0) {
                rs.setFetchSize(fetch);
            }
			if (getFirstResult() > 0) {
                rs.absolute(getFirstResult());
            }
			
			if (getMaxResults() <= 0) {
				while (rs.next()) {
					dataCache.add( readRow(rs, colCount) );
				}
			} else {
				int current = 0;
				while (current++ < getMaxResults() && rs.next()) {
					dataCache.add( readRow(rs, colCount) );
				}
			}
			
			return dataCache;
			
		} catch (SQLException sqlex) {
			throw SqlExceptionConverter.convert(sqlex, "#NATIVE_QUERY", aSql);
		} finally {
			SqlHelper.close(rs);
			SqlHelper.close(pstmt);
			SqlHelper.close(connect, managerFactory.getDataSource());
			SlowLogger.stop(getSlowLoggerTime(), dataCache.size(), aSql);
		}
	}
	
	// read row
	Object[] readRow(ResultSet rs, int colCount) throws SQLException {
		Object[] values = new Object[colCount];
		
		if (returnTypes != null && returnTypes.length == colCount) {
			for (int i = 0; i < colCount; i++) {
				values[i] = returnTypes[i].getFieldEditor().get(rs, i + 1);
			}
		} else {
			if (LOG.isDebugEnabled()) {
                LOG.debug("Return type unset, use #getObject");
            }
			
			for (int i = 0; i < colCount; i++) {
				values[i] = rs.getObject(i + 1);
			}
		}
		
		return values;
	}
}
