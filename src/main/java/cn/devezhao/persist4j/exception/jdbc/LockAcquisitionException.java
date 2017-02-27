package cn.devezhao.persist4j.exception.jdbc;

import java.sql.SQLException;

import cn.devezhao.persist4j.exception.JDBCException;


/**
 * Implementation of JDBCException indicating a problem acquiring lock on the
 * database.
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/06/08
 * @version $Id: LockAcquisitionException.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class LockAcquisitionException extends JDBCException {
	private static final long serialVersionUID = 7645330533080871391L;

	public LockAcquisitionException(String message, SQLException root) {
		super(message, root);
	}

	public LockAcquisitionException(String message, SQLException root,
			String sql) {
		super(message, root, sql);
	}
}
