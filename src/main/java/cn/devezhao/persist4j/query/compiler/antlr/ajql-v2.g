header {
// $Id$
org.qdss.persist.query.compiler.antlr;
}

// PAESER ***************************************************************************
class QueryParser extends Parser;
options {
	buildAST = true;
	k = 2;
}

// Only select statement on currently
statement
	: selectStatement (SEMICOLON!)? EOF!
	;


selectStatement
	: queryExpression
	;
	
queryExpression
	: subQueryExpression (unionOperator subQueryExpression)* (orderByClause)?
	;
	
subQueryExpression
	: querySpecification
	| LPAREN querySpecification RPAREN
	;
	
querySpecification
	: selectClause
	(fromClause)?
	(whereClause)?
	(groupByClause (havingClause)?)?
	;
	
selectClause
	: SELECT^ (ALL | DISTINCT)? selectList
	;
	
whereClause
	: WHERE^ searchCondition
	;
	
orderByClause
	: ORDER^ BY expression (ASC | DESC)? (COMMA expression (ASC | DESC)?)*
	;
	
groupByClause
	: GROUP! BY (ALL)? expression (COMMA expression)*
	;
	
havingClause
	: HAVING! searchCondition
	;

searchCondition
	: subSearchCondition ((AND | OR) subSearchCondition)*
	;

subSearchCondition
	: (NOT)? ((LPAREN searchCondition RPAREN) => LPAREN searchCondition RPAREN | predicate)
	;
	
predicate
	: 
	( expression 
		(
		IS (NOT)? NULL
		| (NOT)? (
			LIKE expression
			| BETWEEN expression AND expression
			| IN LPAREN ( (selectStatement) => selectStatement | expression (COMMA expression)* ) RPAREN 
			)
		)
    | EXISTS LPAREN selectStatement RPAREN
    )
    ;

selectList
	: selectItem (COMMA! selectItem)*
	;
	
selectItem
	: column | expression
	;

fromClause
	: FROM! tableSource (COMMA tableSource)?
	;

tableSource
	: dbObject | Variable
	;
	
column
	: (PLUS)* (dbObject | LPAREN column RPAREN)
	;

expression
	: subExpression (binaryOperator subExpression)*
	;
	
subExpression
	: (unaryOperator)?
	(constant | Variable | dbObject
	| LPAREN ((selectStatement) => selectStatement | expression) RPAREN)
	;

dbObject
	: NonQuotedIdentifier | QuotedIdentifier
	;

stringLiteral
	: UnicodeStringLiteral | ASCIIStringLiteral
	;
	
constant
	: Int | Real | NULL | stringLiteral | HexLiteral
	;
	
unaryOperator
	: PLUS | MINUS | TILDE
	;
	
binaryOperator
	: arithmeticOperator | bitwiseOperator
	;
	
arithmeticOperator
	: PLUS | MINUS | STAR | DIVIDE | MOD
	;
	
bitwiseOperator
	: AMPERSAND | TILED | BITWISEOR | BITWISEXOR
	;
	
logicalOperator
	: ALL | AND | BETWEEN | EXISTS | IN | LIKE | NOT | OR
	;
	
unionOperator
	: UNION (ALL)?
	;


// LEXER ****************************************************************************
class QueryLexer extends Lexer;
options {
	testLiterals = false;
	k = 2;
	caseSensitive = false;
	caseSensitiveLiterals = false;
	charVocabulary = '\u0000'..'\uFFFE';
}

tokens {
	ALL = "all";
	AND = "and";
	ASC = "asc";
	BETWEEN = "between";
	BINARY = "binary";
	BY = "by";
	CASE = "case";
	CAST = "cast";
	CURRENT_DATE = "current_date";
	CURRENT_TIME = "current_time";
	CURRENT_TIMESTAMP = "current_timestamp";
	DELETE = "delete";
	DESC = "desc";
	DISTINCT = "distinct";
	ELSE = "else" ;
	END = "end" ;
	EXISTS = "exists";
	EXIT = "exit";
	FROM = "from";
	FULL = "full";
	HAVING = "having";
	IF = "if";
	IN = "in";
	IS = "is";
	LEFT = "left";
	LIKE = "like";
	NOT = "not";
	NULL = "null";
	ON = "on";
	OR = "or";
	ORDER = "order";
	RIGHT = "right";
	SELECT = "select";
	SET = "set";
	THEN = "then";
	TOP = "top";
	UNION = "union";
	UNIQUE = "unique";
	UPDATE = "update";
	WHEN = "when";
	WHERE = "where";
	WHILE = "while";
	WITH = "with";
}

// Operators
protected DOT :;
COLON : ':' ;
COMMA : ',' ;
SEMICOLON : ';' ;

LPAREN : '(' ;
RPAREN : ')' ;

ASSIGNEQUAL : '=' ;
NOTEQUAL1 : "<>" ;
NOTEQUAL2 : "!=" ;
LESSTHAN : '<' ;
LESSTHANOREQUALTO : "<=" ;
GREATERTHAN : '>' ;
GREATERTHANOREQUALTO : ">=" ;

DIVIDE : '/' ;
PLUS : '+' ;
MINUS : '-' ;
STAR : '*';
MOD: '%' ;

AMPERSAND : '&' ;
TILDE : '~' ;
BITWISEOR : '|' ;
BITWISEXOR : '^' ;

Whitespace
	: (' ' | '\t' | '\n' | '\r')
	{ _ttype = Token.SKIP; }
	;
	
SingleLineComment
	: "--"( ~('\r' | '\n') )*
	{ _ttype = Token.SKIP; }
	;

// Literals
protected
Letter
	: 'a'..'z' | '_' | '#' | '\u0080'..'\ufffe'
	;
	
protected
Digit
	: '0'..'9'
	;

protected
Int :;

protected
Real :;

protected
Exponent
	: 'e' ('+' | '-')? (Digit)+
	;
	
Number
	: ( (Digit)+ ('.' | 'e') ) => (Digit)+ ( '.' (Digit)* (Exponent)? | Exponent ) { _ttype = Real; }
	| '.' { _ttype = DOT; } ( (Digit)+ (Exponent)? { _ttype = Real; } )?
	| (Digit)+ { _ttype = Int; }
	| "0x" ('a'..'f' | Digit)* { _ttype = HexLiteral; }  // "0x" is valid hex literal
	;
	
NonQuotedIndetifier
	options { testLiterals = true; }
	: ('a'..'z' | '_' | '#' | '\u0080'..'\ufffe') (Letter | Digit)*
	;
	
QuotedIndetifier
	: '"' (~'"')* '"' ('"' (~'"')* '"')*
	| '[' (~']')* ']' (']' (~']')* ']')*
	;

Variable
	options { testLiterals = true; }
	: '$' (Letter | Digit)+
	;
	
ASCIIStringLiteral
	: '\'' (~'\'')* '\'' ( '\'' (~'\'')* '\'' )*
	;

UnicodeStringLiteral
	:
	'n' '\'' (~'\'')* '\'' ( '\'' (~'\'')* '\'' )*
	;

protected
HexLiteral
	: // "0x" ('0'..'9' | 'a'..'f')*
	;