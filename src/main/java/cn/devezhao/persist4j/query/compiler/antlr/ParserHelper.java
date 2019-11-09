package cn.devezhao.persist4j.query.compiler.antlr;

import java.io.StringReader;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: ParserHelper.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class ParserHelper {

	public static AjQLParser createAjQLParser(String ajql) {
		return createAjQLParser(ajql, false);
	}
	
	public static AjQLParser createAjQLParser(String ajql, boolean isThrow) {
		AjQLLexer l = new AjQLLexer(new StringReader(ajql));
		return (isThrow) ? new ThrowerAjQLParser(l) : new AjQLParser(l);
	}

	public static boolean isAggregator(int ttype) {
		return ttype == AjQLParserTokenTypes.MIN
				|| ttype == AjQLParserTokenTypes.MAX
				|| ttype == AjQLParserTokenTypes.AVG
				|| ttype == AjQLParserTokenTypes.SUM
				|| ttype == AjQLParserTokenTypes.COUNT
				|| ttype == AjQLParserTokenTypes.DATE_FORMAT;
	}
	
	public static boolean hasAggregatorMode(int ttype) {
		return ttype == AjQLParserTokenTypes.DATE_FORMAT;
	}

	public static boolean isOperator(int ttype) {
		return ttype == AjQLParserTokenTypes.EQ
				|| ttype == AjQLParserTokenTypes.LT
				|| ttype == AjQLParserTokenTypes.GT
				|| ttype == AjQLParserTokenTypes.LE
				|| ttype == AjQLParserTokenTypes.GE
				|| ttype == AjQLParserTokenTypes.SQL_NE
				|| ttype == AjQLParserTokenTypes.LIKE
				|| ttype == AjQLParserTokenTypes.BETWEEN
				|| ttype == AjQLParserTokenTypes.BAND
				|| ttype == AjQLParserTokenTypes.NBAND
				|| ttype == AjQLParserTokenTypes.MATCH;
	}
	
	public static boolean isInIgnoreValue(int ttype) {
		return ttype == AjQLParserTokenTypes.TRUE
			|| ttype == AjQLParserTokenTypes.FALSE
			|| ttype == AjQLParserTokenTypes.LITERAL
			|| ttype == AjQLParserTokenTypes.NULL;
	}
	
	public static boolean isInIgnore(int ttype) {
		return ttype == AjQLParserTokenTypes.AND
			|| ttype == AjQLParserTokenTypes.OR
			|| ttype == AjQLParserTokenTypes.IS
			|| ttype == AjQLParserTokenTypes.NOT
			|| ttype == AjQLParserTokenTypes.LPAREN
			|| ttype == AjQLParserTokenTypes.RPAREN
			|| ttype == AjQLParserTokenTypes.COMMA
			|| isOperator(ttype)
			|| isInIgnoreValue(ttype);
	}
}
