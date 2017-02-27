package cn.devezhao.persist4j.exception.jdbc;

import java.sql.SQLException;

import cn.devezhao.persist4j.exception.JDBCException;


/**
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/06/08
 * @version $Id: GenericJDBCException.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class GenericJDBCException extends JDBCException {
	private static final long serialVersionUID = 3557785158242479097L;

	public GenericJDBCException(String message, SQLException root) {
		super(message, root);
	}

	public GenericJDBCException(String message, SQLException root, String sql) {
		super(message, root, sql);
	}
}
