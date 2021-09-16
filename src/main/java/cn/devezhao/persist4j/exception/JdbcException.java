package cn.devezhao.persist4j.exception;

import cn.devezhao.persist4j.DataAccessException;

import java.sql.SQLException;

/**
 * JDBC异常
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 11, 2009
 * @version $Id: JDBCException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class JdbcException extends DataAccessException {
	private static final long serialVersionUID = -6444431350667265965L;

	private SQLException sqlex;
	private String sql;

	public JdbcException(String message, Throwable ex) {
		super(message, ex);
	}

	public JdbcException(String message, SQLException root) {
		super(message, root);
		sqlex = root;
	}

	public JdbcException(String message, SQLException root, String sql) {
		super(message, root);
		this.sqlex = root; 
		this.sql = sql;
	}

	/**
	 * Get the SQLState of the underlying <tt>SQLException</tt>.
	 * 
	 * @see java.sql.SQLException
	 * @return String
	 */
	public String getSQLState() {
		return sqlex.getSQLState();
	}

	/**
	 * Get the <tt>errorCode</tt> of the underlying <tt>SQLException</tt>.
	 * 
	 * @see java.sql.SQLException
	 * @return int the error code
	 */
	public int getErrorCode() {
		return sqlex.getErrorCode();
	}

	/**
	 * Get the underlying <tt>SQLException</tt>.
	 * 
	 * @return SQLException
	 */
	public SQLException getSQLException() {
		return sqlex;
	}

	/**
	 * Get the actual SQL statement that caused the exception (may be null)
	 */
	public String getSQL() {
		return sql;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("\n** SQL Error ************************************************************************")
				.append("\nSQL:         ").append(sql)
				.append("\nCategory:    ").append(getClass())
				.append("\nVendorError: ").append(getErrorCode())
				.append("\nSQLState:    ").append(getSQLException().getSQLState())
				.append("\nCause:       ").append(sqlex)
				.append("\nMessage:     ").append(getMessage())
				.toString();
	}
}
