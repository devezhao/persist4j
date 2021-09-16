package cn.devezhao.persist4j;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 数据访问错误
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: DataAccessException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class DataAccessException extends NestableRuntimeException {
	private static final long serialVersionUID = -8609924407313152851L;

	public DataAccessException() {
		super();
	}
	
	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}