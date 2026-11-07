package cn.devezhao.persist4j.engine;

import cn.devezhao.persist4j.DataAccessException;
import cn.devezhao.persist4j.util.SqlHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: JdbcSupport.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
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
			throw new DataAccessException("Unexpected exception on execute", unex);
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
			stmt.setQueryTimeout(getTimeout());

			if (SqlHelper.supportsBatchUpdates(connect)) {
				for (String sql : sqls) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("add sql to batch: " + sql);
					}

					stmt.addBatch(sql);
				}
				rowsAffected = stmt.executeBatch();
			} else {
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
				throw new DataAccessException("Unexpected exception on executeBatch", unex);
			}
		} finally {
			SqlHelper.close(stmt);
			releaseConnection(connect);
		}
		return rowsAffected;
	}
	
	protected ResultSet nativeQuery(String sql) throws SQLException, DataAccessException {
		return nativeQuery(sql, null);
	}
	
	protected ResultSet nativeQuery(String sql, String param) throws SQLException, DataAccessException {
		Connection connect = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs;
		
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setQueryTimeout(getTimeout());
			if (param != null) {
				pstmt.setString(1, param);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("exec query: " + sql);
			}
			
			rs = pstmt.executeQuery();
		} catch (Throwable ex) {
			SqlHelper.close(pstmt);
			releaseConnection(connect);
			if (ex instanceof SQLException) {
				throw (SQLException) ex;
			}
			throw new DataAccessException("Unexpected exception on nativeQuery", ex);
		}
		return rs;
	}
	
	public int[] executeBatch(StatementCallback[] callbacks) throws SQLException, DataAccessException {
		if (callbacks == null || callbacks.length == 0) {
			return new int[0];
		}
		
		Connection connect = getConnection();
		PreparedStatement pstmt = null;
		try {
			boolean autoCommit = connect.getAutoCommit();
			if (autoCommit) {
				connect.setAutoCommit(false);
			}
			try {
				int[] rowsAffected = new int[callbacks.length];
				for (int i = 0; i < callbacks.length; i++) {
					StatementCallback callback = callbacks[i];
					if (LOG.isDebugEnabled()) {
						LOG.debug("exec sql: " + callback.getSql());
					}
					
					pstmt = connect.prepareStatement(callback.getSql());
					pstmt.setQueryTimeout(getTimeout());
					callback.doInParameters(pstmt);
					rowsAffected[i] = pstmt.executeUpdate();
					SqlHelper.close(pstmt);
					pstmt = null;
				}
				if (autoCommit) {
					connect.commit();
				}
				return rowsAffected;
			} catch (Throwable ex) {
				if (autoCommit) {
					try {
						connect.rollback();
					} catch (SQLException rbex) {
						LOG.warn("Could not roll back", rbex);
					}
				}
				if (ex instanceof SQLException) {
					throw (SQLException) ex;
				}
				throw new DataAccessException("Unexpected exception on executeBatch", ex);
			} finally {
				if (autoCommit) {
					try {
						connect.setAutoCommit(true);
					} catch (SQLException e) {
						LOG.debug("Could not restore auto-commit", e);
					}
				}
			}
		} finally {
			SqlHelper.close(pstmt);
			releaseConnection(connect);
		}
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
