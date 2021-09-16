package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.PersistException;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 16, 2009
 * @version $Id: QueryException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class QueryException extends PersistException {
	private static final long serialVersionUID = -5585083095838431318L;

	public QueryException(String message) {
		super(message);
	}

	public QueryException(String message, Throwable ex) {
		super(message, ex);
	}
}
