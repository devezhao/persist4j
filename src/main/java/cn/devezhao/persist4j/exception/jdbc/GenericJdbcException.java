package cn.devezhao.persist4j.exception.jdbc;

import cn.devezhao.persist4j.exception.JdbcException;

import java.sql.SQLException;


/**
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/06/08
 * @version $Id: GenericJDBCException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class GenericJdbcException extends JdbcException {
	private static final long serialVersionUID = 3557785158242479097L;

	public GenericJdbcException(String message, SQLException root) {
		super(message, root);
	}

	public GenericJdbcException(String message, SQLException root, String sql) {
		super(message, root, sql);
	}
}
