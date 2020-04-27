package cn.devezhao.persist4j.query.compiler.antlr;

import java.io.StringReader;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: ParserHelper.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class ParserHelper {

	/**
	 * @param ajql
	 * @return
	 */
	public static AjQLParser createAjQLParser(String ajql) {
		return createAjQLParser(ajql, false);
	}
	
	/**
	 * @param ajql
	 * @param isThrow
	 * @return
	 */
	public static AjQLParser createAjQLParser(String ajql, boolean isThrow) {
		AjQLLexer l = new AjQLLexer(new StringReader(ajql));
		return (isThrow) ? new ThrowerAjQLParser(l) : new AjQLParser(l);
	}

	/**
	 * @param type
	 * @return
	 * @see #isAggregatorWithMode(int)
	 * @see #isAggregatorWithNested(int)
	 */
	public static boolean isAggregator(int type) {
		return type == AjQLParserTokenTypes.MIN
				|| type == AjQLParserTokenTypes.MAX
				|| type == AjQLParserTokenTypes.AVG
				|| type == AjQLParserTokenTypes.SUM
				|| type == AjQLParserTokenTypes.COUNT
				|| type == AjQLParserTokenTypes.QUARTER
				|| isAggregatorWithMode(type)
				|| isAggregatorWithNested(type);
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static boolean isAggregatorWithMode(int type) {
		return type == AjQLParserTokenTypes.DATE_FORMAT;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static boolean isAggregatorWithNested(int type) {
		return type == AjQLParserTokenTypes.CONCAT;
	}

	/**
	 * @param type
	 * @return
	 */
	public static boolean isOperator(int type) {
		return type == AjQLParserTokenTypes.EQ
				|| type == AjQLParserTokenTypes.LT
				|| type == AjQLParserTokenTypes.GT
				|| type == AjQLParserTokenTypes.LE
				|| type == AjQLParserTokenTypes.GE
				|| type == AjQLParserTokenTypes.SQL_NE
				|| type == AjQLParserTokenTypes.LIKE
				|| type == AjQLParserTokenTypes.BETWEEN
				|| type == AjQLParserTokenTypes.BAND
				|| type == AjQLParserTokenTypes.NBAND
				|| type == AjQLParserTokenTypes.MATCH;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static boolean isInIgnore(int type) {
		return type == AjQLParserTokenTypes.AND
			|| type == AjQLParserTokenTypes.OR
			|| type == AjQLParserTokenTypes.IS
			|| type == AjQLParserTokenTypes.NOT
			|| type == AjQLParserTokenTypes.LPAREN
			|| type == AjQLParserTokenTypes.RPAREN
			|| type == AjQLParserTokenTypes.COMMA
			|| isOperator(type)
			|| isInIgnoreValue(type);
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static boolean isInIgnoreValue(int type) {
		return type == AjQLParserTokenTypes.TRUE
			|| type == AjQLParserTokenTypes.FALSE
			|| type == AjQLParserTokenTypes.LITERAL
			|| type == AjQLParserTokenTypes.NULL;
	}
}
