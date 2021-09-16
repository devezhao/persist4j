package cn.devezhao.persist4j;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * top-level exception
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 8, 2009
 * @version $Id: PersistException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class PersistException extends NestableRuntimeException {
	private static final long serialVersionUID = 3294784663323509115L;

	public PersistException(String message) {
		super(message);
	}

	public PersistException(String message, Throwable ex) {
		super(message, ex);
	}
}
