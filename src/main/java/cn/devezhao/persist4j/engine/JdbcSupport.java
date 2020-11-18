package cn.devezhao.persist4j.engine;

import cn.devezhao.persist4j.DataAccessException;
import cn.devezhao.persist4j.util.SqlHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

/**
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: JdbcSupport.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public abstract class JdbcSupport {
	
	private static final Log LOG = LogFactory.getLog(JdbcSupport.class);

	private static final int TIMEOUT_DEFAULT = 5 * 60;
	
	private int timeout;
	
	/**
	 */
	protected JdbcSupport() {
		this(TIMEOUT_DEFAULT);
	}
	
	/**
	 * @param timeout
	 */
	protected JdbcSupport(int timeout) {
		super();
		this.timeout = timeout;
	}
	
	public int execute(StatementCallback callback) throws SQLException, DataAccessException {
		Connection connect = getConnection();
		PreparedStatement pstmt = null;
		
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("exec sql: " + callback.getSql());
			}
				
			pstmt = connect.prepareStatement(callback.getSql());
			pstmt.setQueryTimeout(getTimeout());
			callback.doInParameters(pstmt);
			return pstmt.executeUpdate();
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (Throwable unex) {
			throw new DataAccessException("Unexception on execute", unex);
		} finally {
			SqlHelper.close(pstmt);
			releaseConnection(connect);
		}
	}
	
	public int[] executeBatch(final String[] sqls) throws SQLException, DataAccessException {
		if (sqls.length == 1) {
			int exec = execute(new StatementCallback(){
				@Override
                public String getSql() {
					return sqls[0];
				}
				@Override
                public Object doInParameters(PreparedStatement pstmt) {
					return null;
				}
			});
			return new int[] { exec };
		}
		
		Connection connect = getConnection();
		Statement stmt = null;
		int[] rowsAffected;
		try {
			stmt = connect.createStatement();
			
			if (SqlHelper.supportsBatchUpdates(connect)) {
				stmt.setQueryTimeout(TIMEOUT_DEFAULT * 3);
				for (String sql : sqls) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("add sql to batch: " + sql);
					}

					stmt.addBatch(sql);
				}
				rowsAffected = stmt.executeBatch();
			} else {
				stmt.setQueryTimeout(TIMEOUT_DEFAULT);
				rowsAffected = new int[sqls.length];
				for (int i = 0; i < sqls.length; i++) {
					String crtSql = sqls[i];
					
					if (LOG.isDebugEnabled()) {
						LOG.debug("exec sql(batch): " + crtSql);
					}
					
					if (!stmt.execute(crtSql)) {
						rowsAffected[i] = stmt.getUpdateCount();
					} else {
						SqlHelper.close(stmt);
						throw new DataAccessException("Invalid batch SQL statement: " + crtSql);
					}
				}
			}
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (Throwable unex) {
			if (unex instanceof DataAccessException) {
				throw (DataAccessException) unex;
			} else {
				throw new DataAccessException("Unexception on executeBatch", unex);
			}
		} finally {
			SqlHelper.close(stmt);
			releaseConnection(connect);
		}
		return rowsAffected;
	}
	
	protected ResultSet nativeQuery(String sql) throws SQLException, DataAccessException {
		Connection connect = getConnection();
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = connect.createStatement();
			stmt.setQueryTimeout(180);
			if (LOG.isDebugEnabled()) {
				LOG.debug("exec query: " + sql);
			}
			
			rs = stmt.executeQuery(sql);
		} catch (SQLException sqlex) {
			throw sqlex;
		} catch (Throwable unex) {
			throw new DataAccessException("Unexception on nativeQuery", unex);
		}
		return rs;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public int getTimeout() {
		return timeout;
	}

	abstract protected Connection getConnection();
	
	abstract protected void releaseConnection(Connection connect);
}
