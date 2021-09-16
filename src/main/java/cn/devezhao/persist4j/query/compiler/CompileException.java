package cn.devezhao.persist4j.query.compiler;

import cn.devezhao.persist4j.PersistException;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: CompileException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class CompileException extends PersistException {
	private static final long serialVersionUID = -1344641792787972588L;

	public CompileException(String message) {
		super(message);
	}

	public CompileException(String message, Throwable ex) {
		super(message, ex);
	}
}
