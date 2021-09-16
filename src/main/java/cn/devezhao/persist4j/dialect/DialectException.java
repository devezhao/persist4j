package cn.devezhao.persist4j.dialect;

import cn.devezhao.persist4j.PersistException;

/**
 * 方言异常
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: DialectException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class DialectException extends PersistException {
	private static final long serialVersionUID = -5320452369990541421L;

	public DialectException() {
		this("Unknow dialect error.");
	}

	public DialectException(String message) {
		super(message);
	}

	public DialectException(String message, Throwable ex) {
		super(message, ex);
	}
}
