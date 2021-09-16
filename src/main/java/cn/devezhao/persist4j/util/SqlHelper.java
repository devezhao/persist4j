package cn.devezhao.persist4j.util;

import cn.devezhao.persist4j.exception.SqlExceptionConverter;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Sql helper.
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: SqlHelper.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public final class SqlHelper {

	private static final Log LOG = LogFactory.getLog(SqlHelper.class);
	
	/**
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			if (!rs.isClosed()) {
				rs.close();
			}
		} catch (Throwable ex) {
			logSQLException(ex, "Unable to close JDBC ResultSet.");
		}
	}

	/**
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		if (stmt == null) {
			return;
		}
		try {
			if (!stmt.isClosed()) {
				stmt.close();
			}
		} catch (Throwable ex) {
			logSQLException(ex, "Unable to close JDBC Statement.");
		}
	}

	/**
	 * deprecated Using  {@link DataSourceUtils#releaseConnection(Connection, DataSource)}}
	 * 
	 * @param connect
	 */
	@Deprecated
	public static void close(Connection connect) {
		if (connect == null) {
			return;
		}
		try {
			if (!connect.isClosed()) {
				connect.close();
			}
		} catch (Throwable ex) {
			logSQLException(ex, "Unable to close JDBC Connection.");
		}
	}
	
	/**
	 * {@link DataSourceUtils#releaseConnection(Connection, DataSource)}
	 * 
	 * @param connect
	 * @param dataSource
	 */
	public static void close(Connection connect, DataSource dataSource) {
		if (connect == null) {
			return;
		}
		Validate.notNull(dataSource);
		try {
			DataSourceUtils.releaseConnection(connect, dataSource);
		} catch (Throwable ex) {
			logSQLException(ex, "Unable to close JDBC Connection.");
		}
	}
	
	/**
	 * clear statement <tt>btach</tt>, <tt>warnings</tt>
	 * 
	 * @param stmt
	 */
	public static void clear(Statement stmt) {
		if ( stmt == null ) {
			return;
		}
		try {
			if (stmt instanceof PreparedStatement) {
				((PreparedStatement) stmt).clearParameters();
			}
			stmt.clearBatch();
			stmt.clearWarnings();
		} catch (Throwable ex) {
			logSQLException(ex, "Unable to clear JDBC Statement.");
		}
	}
	
	/**
	 * handle sql warnings
	 * 
	 * @param warning
	 * @param ignoreWarnings
	 */
	public static void handleWarnings(SQLWarning warning, boolean ignoreWarnings) {
		if (warning == null) {
			return;
		}
		
		if (ignoreWarnings) {
			if (LOG.isDebugEnabled()) {
				SQLWarning warningToLog = warning;
				while (warningToLog != null) {
					LOG.debug("SQLWarning ignored: SQL state '"
							+ warningToLog.getSQLState() + "', error code '"
							+ warningToLog.getErrorCode() + "', message ["
							+ warningToLog.getMessage() + "]");
					warningToLog = warningToLog.getNextWarning();
				}
			}
		} else {
			throw new SQLWarningException("Warning not ignored", warning);
		}
	}
	
	/**
	 * specify connection is supports batching
	 * 
	 * @param con
	 * @return
	 */
	public static boolean supportsBatchUpdates(Connection con) {
		try {
			DatabaseMetaData dbmd = con.getMetaData();
			if (dbmd.supportsBatchUpdates()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("JDBC driver supports batch updates");
				}
				return true;
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("JDBC driver does not support batch updates");
				}
			}
		} catch (SQLException ex) {
			LOG.debug("JDBC driver 'supportsBatchUpdates' method threw exception: " + ex);
		} catch (AbstractMethodError err) {
			LOG.debug("JDBC driver does not support JDBC 2.0 'supportsBatchUpdates' method: " + err);
		}
		return false;
	}
	
	/**
	 * logging (sql)exception
	 * 
	 * @param ex
	 * @param message
	 */
	static void logSQLException(Throwable ex, String message) {
		if (ex instanceof SQLException) {
			LOG.error(SqlExceptionConverter.convert((SQLException) ex, message, null));
		} else if (ex.getCause() instanceof SQLException) {
			LOG.error(SqlExceptionConverter.convert((SQLException) ex.getCause(), message, null));
		} else if (ex instanceof AbstractMethodError) {
			LOG.error("JDBC driver does not support. " + ex);
		} else {
			LOG.error("Unexception exception. " + ex);
		}
	}
	
	private SqlHelper() { }
}
