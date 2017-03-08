header {
// $Id$
package org.qdss.persist.query.compiler.antlr;
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
	
	MAX = "max";
	MIN = "min";
	AVG = "avg";
	SUM = "sum";
	COUNT = "count";
	
	IS = "is";
	NOT = "not";
	NULL = "null";
	IN = "in";
	LIKE = "like";
	EXISTS = "exists";
	BETWEEN = "between";
	
	TRUE = "true";
	FALSE = "false";
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
	  (groupByClause (havingClause)?)?
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
	: GROUP^ BY column (COMMA column)*
	;

havingClause
	: HAVING^ simpleCondition
	;

selectList
	: selectItem (COMMA! selectItem)*
	;
	
selectItem
	: column | aggregate
	;

column
	: dbObject | expression | ORDER | GROUP | BY
	;

dbObject
	: IDENT
	;

aggregate
	: (MIN^ | MAX^ | AVG^ | SUM^) LPAREN! column RPAREN!
	| COUNT^ LPAREN! (STAR | column) RPAREN!
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
	)
	;

constant
	: QUOTED_STRING
	| LITERAL
	| TRUE 
	| FALSE
	| NULL
	| NAMED_PARAM
	| QUESTION_MARK
	;

unaryOperator
	: PLUS | MINUS
	;
  
binaryOperator
	: PLUS | MINUS | STAR | DIVIDE | MOD
	;

comparisonOperator
	: EQ | LT | GT | LE | GE | SQL_NE
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
	: '_' | '$' | '&' | '^' | 'a'..'z'
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