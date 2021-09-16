package cn.devezhao.persist4j.exception.jdbc;

import cn.devezhao.persist4j.exception.JdbcException;

import java.sql.SQLException;


/**
 * Implementation of JDBCException indicating that the requested DML operation
 * resulted in a violation of a defined integrity constraint.
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/19/08
 * @version $Id: ConstraintViolationException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class ConstraintViolationException extends JdbcException {
	private static final long serialVersionUID = -6763398332806161179L;

	private String constraintName;

	public ConstraintViolationException(String message, SQLException root,
			String constraintName) {
		super(message, root);
		this.constraintName = constraintName;
	}

	public ConstraintViolationException(String message, SQLException root,
			String sql, String constraintName) {
		super(message, root, sql);
		this.constraintName = constraintName;
	}

	/**
	 * Returns the name of the violated constraint, if known.
	 * 
	 * @return The name of the violated constraint, or null if not known.
	 */
	public String getConstraintName() {
		return constraintName;
	}
}
