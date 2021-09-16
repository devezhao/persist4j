package cn.devezhao.persist4j.exception.jdbc;

import cn.devezhao.persist4j.exception.JdbcException;

import java.sql.SQLException;


/**
 * Implementation of JDBCException indicating a problem acquiring lock on the
 * database.
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/06/08
 * @version $Id: LockAcquisitionException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class LockAcquisitionException extends JdbcException {
	private static final long serialVersionUID = 7645330533080871391L;

	public LockAcquisitionException(String message, SQLException root) {
		super(message, root);
	}

	public LockAcquisitionException(String message, SQLException root,
			String sql) {
		super(message, root, sql);
	}
}
