package cn.devezhao.persist4j.record;

import cn.devezhao.persist4j.PersistException;

/**
 * Invald value of field
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/14/08
 * @version $Id: FieldValueException.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class FieldValueException extends PersistException {
	private static final long serialVersionUID = -2063367127310548765L;

	public FieldValueException(String message) {
		super(message);
	}

	public FieldValueException(String message, Throwable ex) {
		super(message, ex);
	}
}
