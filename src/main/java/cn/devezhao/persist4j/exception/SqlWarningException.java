package cn.devezhao.persist4j.exception;

import cn.devezhao.persist4j.DataAccessException;

import java.sql.SQLWarning;

/**
 * Sql警告类的异常
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 17, 2009
 * @version $Id: SqlWarningException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class SqlWarningException extends DataAccessException {
	private static final long serialVersionUID = -4242858921986055820L;

	public SqlWarningException(String message, SQLWarning ex) {
		super(message, ex);
	}

	public SQLWarning SQLWarning() {
		return (SQLWarning) getCause();
	}
}
