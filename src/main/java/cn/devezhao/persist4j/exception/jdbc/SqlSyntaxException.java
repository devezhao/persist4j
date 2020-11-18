package cn.devezhao.persist4j.exception.jdbc;

import cn.devezhao.persist4j.exception.JdbcException;

import java.sql.SQLException;


/**
 * Sql语法错误异常
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/06/08
 * @version $Id: SqlSyntaxException.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class SqlSyntaxException extends JdbcException {
	private static final long serialVersionUID = 7095739972833914851L;

	public SqlSyntaxException(String message, SQLException root) {
		super(message, root);
	}

	public SqlSyntaxException(String message, SQLException root, String sql) {
		super(message, root, sql);
	}
}
