// Manual: https://www.antlr2.org/doc/antlr_2_7_5_ChineseVer.pdf
// Usage: 
// `java -cp antlr-2.7.7.jar antlr.Tool ajql.g`

header {
package cn.devezhao.persist4j.query.compiler.antlr;
}

// PARSER *********************************************************************************
class AjQLParser extends Parser;

options {
	buildAST = true;
	k = 2;
}

tokens {
	SELECT = "select";
	DISTINCT = "distinct";
	FROM = "from";
	WHERE = "where";
	AND = "and";
	OR = "or";
	ORDER = "order";
	ASC = "asc";
	DESC = "desc";
	GROUP = "group";
	BY = "by";
	HAVING = "having";
	WITH = "with";
	ROLLUP = "rollup";
	
	MAX = "max";
	MIN = "min";
	AVG = "avg";
	SUM = "sum";
	COUNT = "count";
	DATE_FORMAT = "date_format";
	//YEAR = "year";
	QUARTER = "quarter";
    //MONTH = "month";
	//WEEK = "week";
    //DATE = "date";
	CONCAT = "concat";
	
	IS = "is";
	NOT = "not";
	NULL = "null";
	IN = "in";
	LIKE = "like";
	EXISTS = "exists";
	BETWEEN = "between";
	
	TRUE = "true";
	FALSE = "false";
	
	MATCH = "match";
}

statement
	: selectStatement EOF!
	;

selectStatement
	: queryExpression
	;

queryExpression
	: subQueryExpression (orderByClause)?
	;

subQueryExpression
	: queryRule
	;

queryRule
	: selectClause 
	  fromClause 
	  (whereClause)? 
	  (groupByClause (havingClause)? (rollupClause)?)?
	;

selectClause
	: SELECT^ (DISTINCT)? selectList
	;
	
fromClause
	: FROM^ dbObject
	;

whereClause
	: WHERE^ simpleCondition
	;

orderByClause
	: ORDER^ BY selectItem (ASC | DESC)? ( COMMA selectItem (ASC | DESC)? )*
	;

groupByClause
	: GROUP^ BY selectItem (COMMA selectItem)*
	;

havingClause
	: HAVING^ simpleCondition
	;

rollupClause
	: WITH^ ROLLUP
	;

selectList
	: selectItem (COMMA! selectItem)*
	;
	
selectItem
	: column | aggregate
	;

column
	: dbObject | expression
	//| ORDER | GROUP | BY | YEAR | QUARTER | MONTH | WEEK | DATE
	;

dbObject
	: IDENT
	;

aggregateWithMode
	: DATE_FORMAT^ LPAREN! column COMMA constantSimple RPAREN!
	;

aggregateWithFields
	: CONCAT^ LPAREN! (selectItem | QUOTED_STRING) ( COMMA (selectItem | QUOTED_STRING) )* RPAREN!
	;
	
aggregate
	: (MIN^ | MAX^ | AVG^ | SUM^ | QUARTER^) LPAREN! column RPAREN!
	| COUNT^ LPAREN! (DISTINCT)? (STAR | column) RPAREN!
	| aggregateWithMode
	| aggregateWithFields
	;
	
simpleCondition
	: subSimpleCondition ( (AND | OR) subSimpleCondition )*
	;
	
subSimpleCondition
	: (NOT)? ( (LPAREN simpleCondition RPAREN) => LPAREN simpleCondition RPAREN | simplePredicate )
	;
	
simplePredicate
	: (expression | selectItem (
		( comparisonOperator expression
		| IS (NOT)? NULL
		| (NOT)? (
			LIKE QUOTED_STRING 
		  | IN^ LPAREN! (
			  constant (COMMA constant)* | (selectStatement) => selectStatement ) 
		  RPAREN! )
		)
	  ) | (NOT)? EXISTS^ LPAREN! (selectStatement) RPAREN!
	)
	;

expression
	: subExpression (binaryOperator subExpression)*
	;
  
subExpression
	: (unaryOperator)?
	( constant
	| dbObject
	| LPAREN ((selectStatement) => selectStatement | expression) RPAREN 
	| constantSimple AND constantSimple
	)
	;

constantSimple
	: QUOTED_STRING
	| LITERAL
	| NAMED_PARAM
	| QUESTION_MARK
	;

constant
	: constantSimple
	| TRUE 
	| FALSE
	| NULL
	;

unaryOperator
	: PLUS | MINUS
	;
  
binaryOperator
	: PLUS | MINUS | STAR | DIVIDE | MOD
	;

comparisonOperator
	: EQ | LT | GT | LE | GE | SQL_NE | BETWEEN | BAND | NBAND | MATCH
	;

// LEXER **********************************************************************************
class AjQLLexer extends Lexer;

options {
	k = 2;
	testLiterals = false;
	caseSensitive = false;
	caseSensitiveLiterals = false;
	charVocabulary = '\u0000'..'\uFFFE';
}

// -- Keywords --
DIVIDE: '/';
PLUS: '+';
MINUS: '-';
STAR: '*';
MOD: '%';

EQ: '=';
LT: '<';
GT: '>';
LE: "<=";
GE: ">=";
SQL_NE: "<>";
BAND: "&&";
NBAND: "!&";

DOT: '.';
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
COLON: ':';
QUESTION_MARK: '?';

// -- Parameter --
NAMED_PARAM
	: ( COLON (IDENT_LETTER)+ )
	;
//INDEX_PARAM
//	: QUESTION_MARK
//	;

// -- Operator --
//OP : '+' | '-' | '*' | '/' | '%' ;
// -- Logical Operator --
//LOP : '=' | '>' | ">=" | '<' | "<=" | "<>" ;

// -- Identifier --
IDENT options { testLiterals = true; }
	: IDENT_START (IDENT_LETTER)*
	;
protected
IDENT_START 
	: '_' | '$' | '&' | '#' | '^' | 'a'..'z'
	;
protected
IDENT_LETTER 
	: IDENT_START | DIGIT | DOT
	;

// -- Number --
protected
DIGIT : '0'..'9' ;
protected
INT : (DIGIT)+ ;
protected
NUM : INT ( DOT (DIGIT)+ )? ;

// -- Literal --
LITERAL : (IDENT_LETTER | '\u0080'..'\uFFFE')+ ;

// -- Quoted String --
QUOTED_STRING
	: '\''! ((ESCqs) => ESCqs | ~'\'')* '\''!
	;
ESCqs
	: '\'' '\''
	;

// -- Whitespace --
WS
	: ( ' '
	  | '\t'
	  | '\f'
	  | '\n' { newline(); }
	  | '\r' )
	  { $setType(Token.SKIP); }
	;