package cn.devezhao.persist4j.query.compiler.antlr;

import antlr.RecognitionException;
import antlr.TokenStream;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: ThrowerAjQLParser.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $ 
 */
public class ThrowerAjQLParser extends AjQLParser {

	public ThrowerAjQLParser(TokenStream stream) {
		super(stream);
	}
	
	@Override
	public void reportError(RecognitionException ex) {
		throw new ParserException(ex);
	}
}
