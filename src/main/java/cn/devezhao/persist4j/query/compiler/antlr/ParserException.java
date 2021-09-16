package cn.devezhao.persist4j.query.compiler.antlr;

import antlr.ANTLRException;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: ParserException.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class ParserException extends NestableRuntimeException {
	private static final long serialVersionUID = -3556487161116352482L;
	
	public ParserException(String message) {
		super(message);
	}
	
	public ParserException(ANTLRException ex) {
		super(ex);
	}
}
