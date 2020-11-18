// $ANTLR 2.7.7 (2006-11-01): "ajql.g" -> "AjQLParser.java"$

package cn.devezhao.persist4j.query.compiler.antlr;

import antlr.*;
import antlr.collections.AST;
import antlr.collections.impl.BitSet;

public class AjQLParser extends antlr.LLkParser implements AjQLParserTokenTypes {

	protected AjQLParser(TokenBuffer tokenBuf, int k) {
		super(tokenBuf, k);
		tokenNames = _tokenNames;
		buildTokenTypeASTClassMap();
		astFactory = new ASTFactory(getTokenTypeToASTClassMap());
	}

	public AjQLParser(TokenBuffer tokenBuf) {
		this(tokenBuf, 2);
	}

	protected AjQLParser(TokenStream lexer, int k) {
		super(lexer, k);
		tokenNames = _tokenNames;
		buildTokenTypeASTClassMap();
		astFactory = new ASTFactory(getTokenTypeToASTClassMap());
	}

	public AjQLParser(TokenStream lexer) {
		this(lexer, 2);
	}

	public AjQLParser(ParserSharedInputState state) {
		super(state, 2);
		tokenNames = _tokenNames;
		buildTokenTypeASTClassMap();
		astFactory = new ASTFactory(getTokenTypeToASTClassMap());
	}

	public final void statement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;

		try { // for error handling
			selectStatement();
			astFactory.addASTChild(currentAST, returnAST);
			match(Token.EOF_TYPE);
			statement_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_0);
			} else {
				throw ex;
			}
		}
		returnAST = statement_AST;
	}

	public final void selectStatement() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectStatement_AST = null;

		try { // for error handling
			queryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			selectStatement_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_1);
			} else {
				throw ex;
			}
		}
		returnAST = selectStatement_AST;
	}

	public final void queryExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST queryExpression_AST = null;

		try { // for error handling
			subQueryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
				switch (LA(1)) {
				case ORDER: {
					orderByClause();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case EOF:
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			queryExpression_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_1);
			} else {
				throw ex;
			}
		}
		returnAST = queryExpression_AST;
	}

	public final void subQueryExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subQueryExpression_AST = null;

		try { // for error handling
			queryRule();
			astFactory.addASTChild(currentAST, returnAST);
			subQueryExpression_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_2);
			} else {
				throw ex;
			}
		}
		returnAST = subQueryExpression_AST;
	}

	public final void orderByClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderByClause_AST = null;

		try { // for error handling
			AST tmp2_AST = null;
			tmp2_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp2_AST);
			match(ORDER);
			AST tmp3_AST = null;
			tmp3_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp3_AST);
			match(BY);
			selectItem();
			astFactory.addASTChild(currentAST, returnAST);
			{
				switch (LA(1)) {
				case ASC: {
					AST tmp4_AST = null;
					tmp4_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp4_AST);
					match(ASC);
					break;
				}
				case DESC: {
					AST tmp5_AST = null;
					tmp5_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp5_AST);
					match(DESC);
					break;
				}
				case EOF:
				case COMMA:
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				_loop19: do {
					if ((LA(1) == COMMA)) {
						AST tmp6_AST = null;
						tmp6_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp6_AST);
						match(COMMA);
						selectItem();
						astFactory.addASTChild(currentAST, returnAST);
						{
							switch (LA(1)) {
							case ASC: {
								AST tmp7_AST = null;
								tmp7_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp7_AST);
								match(ASC);
								break;
							}
							case DESC: {
								AST tmp8_AST = null;
								tmp8_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp8_AST);
								match(DESC);
								break;
							}
							case EOF:
							case COMMA:
							case RPAREN: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
					} else {
						break _loop19;
					}

				} while (true);
			}
			orderByClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_1);
			} else {
				throw ex;
			}
		}
		returnAST = orderByClause_AST;
	}

	public final void queryRule() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST queryRule_AST = null;

		try { // for error handling
			selectClause();
			astFactory.addASTChild(currentAST, returnAST);
			fromClause();
			astFactory.addASTChild(currentAST, returnAST);
			{
				switch (LA(1)) {
				case WHERE: {
					whereClause();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case EOF:
				case ORDER:
				case GROUP:
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case GROUP: {
					groupByClause();
					astFactory.addASTChild(currentAST, returnAST);
					{
						switch (LA(1)) {
						case HAVING: {
							havingClause();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case EOF:
						case ORDER:
						case WITH:
						case RPAREN: {
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					{
						switch (LA(1)) {
						case WITH: {
							rollupClause();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case EOF:
						case ORDER:
						case RPAREN: {
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					break;
				}
				case EOF:
				case ORDER:
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			queryRule_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_2);
			} else {
				throw ex;
			}
		}
		returnAST = queryRule_AST;
	}

	public final void selectClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectClause_AST = null;

		try { // for error handling
			AST tmp9_AST = null;
			tmp9_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp9_AST);
			match(SELECT);
			{
				switch (LA(1)) {
				case DISTINCT: {
					AST tmp10_AST = null;
					tmp10_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp10_AST);
					match(DISTINCT);
					break;
				}
				case MAX:
				case MIN:
				case AVG:
				case SUM:
				case COUNT:
				case DATE_FORMAT:
				case YEAR:
				case QUARTER:
				case MONTH:
				case WEEK:
				case CONCAT:
				case NULL:
				case TRUE:
				case FALSE:
				case IDENT:
				case LPAREN:
				case QUOTED_STRING:
				case LITERAL:
				case NAMED_PARAM:
				case QUESTION_MARK:
				case PLUS:
				case MINUS: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			selectList();
			astFactory.addASTChild(currentAST, returnAST);
			selectClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_3);
			} else {
				throw ex;
			}
		}
		returnAST = selectClause_AST;
	}

	public final void fromClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromClause_AST = null;

		try { // for error handling
			AST tmp11_AST = null;
			tmp11_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp11_AST);
			match(FROM);
			dbObject();
			astFactory.addASTChild(currentAST, returnAST);
			fromClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_4);
			} else {
				throw ex;
			}
		}
		returnAST = fromClause_AST;
	}

	public final void whereClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST whereClause_AST = null;

		try { // for error handling
			AST tmp12_AST = null;
			tmp12_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp12_AST);
			match(WHERE);
			simpleCondition();
			astFactory.addASTChild(currentAST, returnAST);
			whereClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_5);
			} else {
				throw ex;
			}
		}
		returnAST = whereClause_AST;
	}

	public final void groupByClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST groupByClause_AST = null;

		try { // for error handling
			AST tmp13_AST = null;
			tmp13_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp13_AST);
			match(GROUP);
			AST tmp14_AST = null;
			tmp14_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(BY);
			selectItem();
			astFactory.addASTChild(currentAST, returnAST);
			{
				_loop22: do {
					if ((LA(1) == COMMA)) {
						AST tmp15_AST = null;
						tmp15_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp15_AST);
						match(COMMA);
						selectItem();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						break _loop22;
					}

				} while (true);
			}
			groupByClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_6);
			} else {
				throw ex;
			}
		}
		returnAST = groupByClause_AST;
	}

	public final void havingClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST havingClause_AST = null;

		try { // for error handling
			AST tmp16_AST = null;
			tmp16_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp16_AST);
			match(HAVING);
			simpleCondition();
			astFactory.addASTChild(currentAST, returnAST);
			havingClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_7);
			} else {
				throw ex;
			}
		}
		returnAST = havingClause_AST;
	}

	public final void rollupClause() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST rollupClause_AST = null;

		try { // for error handling
			AST tmp17_AST = null;
			tmp17_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp17_AST);
			match(WITH);
			AST tmp18_AST = null;
			tmp18_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp18_AST);
			match(ROLLUP);
			rollupClause_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_2);
			} else {
				throw ex;
			}
		}
		returnAST = rollupClause_AST;
	}

	public final void selectList() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectList_AST = null;

		try { // for error handling
			selectItem();
			astFactory.addASTChild(currentAST, returnAST);
			{
				_loop27: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						selectItem();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						break _loop27;
					}

				} while (true);
			}
			selectList_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_3);
			} else {
				throw ex;
			}
		}
		returnAST = selectList_AST;
	}

	public final void dbObject() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dbObject_AST = null;

		try { // for error handling
			AST tmp20_AST = null;
			tmp20_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp20_AST);
			match(IDENT);
			dbObject_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_8);
			} else {
				throw ex;
			}
		}
		returnAST = dbObject_AST;
	}

	public final void simpleCondition() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleCondition_AST = null;

		try { // for error handling
			subSimpleCondition();
			astFactory.addASTChild(currentAST, returnAST);
			{
				_loop44: do {
					if ((LA(1) == AND || LA(1) == OR)) {
						{
							switch (LA(1)) {
							case AND: {
								AST tmp21_AST = null;
								tmp21_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp21_AST);
								match(AND);
								break;
							}
							case OR: {
								AST tmp22_AST = null;
								tmp22_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp22_AST);
								match(OR);
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						subSimpleCondition();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						break _loop44;
					}

				} while (true);
			}
			simpleCondition_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_9);
			} else {
				throw ex;
			}
		}
		returnAST = simpleCondition_AST;
	}

	public final void selectItem() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectItem_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case NULL:
			case TRUE:
			case FALSE:
			case IDENT:
			case LPAREN:
			case QUOTED_STRING:
			case LITERAL:
			case NAMED_PARAM:
			case QUESTION_MARK:
			case PLUS:
			case MINUS: {
				column();
				astFactory.addASTChild(currentAST, returnAST);
				selectItem_AST = (AST) currentAST.root;
				break;
			}
			case MAX:
			case MIN:
			case AVG:
			case SUM:
			case COUNT:
			case DATE_FORMAT:
			case YEAR:
			case QUARTER:
			case MONTH:
			case WEEK:
			case CONCAT: {
				aggregate();
				astFactory.addASTChild(currentAST, returnAST);
				selectItem_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_10);
			} else {
				throw ex;
			}
		}
		returnAST = selectItem_AST;
	}

	public final void column() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST column_AST = null;

		try { // for error handling
			if ((LA(1) == IDENT) && (_tokenSet_10.member(LA(2)))) {
				dbObject();
				astFactory.addASTChild(currentAST, returnAST);
				column_AST = (AST) currentAST.root;
			} else if ((_tokenSet_11.member(LA(1))) && (_tokenSet_12.member(LA(2)))) {
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				column_AST = (AST) currentAST.root;
			} else {
				throw new NoViableAltException(LT(1), getFilename());
			}

		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_10);
			} else {
				throw ex;
			}
		}
		returnAST = column_AST;
	}

	public final void aggregate() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aggregate_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case MAX:
			case MIN:
			case AVG:
			case SUM:
			case YEAR:
			case QUARTER:
			case MONTH:
			case WEEK: {
				{
					switch (LA(1)) {
					case MIN: {
						AST tmp23_AST = null;
						tmp23_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp23_AST);
						match(MIN);
						break;
					}
					case MAX: {
						AST tmp24_AST = null;
						tmp24_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp24_AST);
						match(MAX);
						break;
					}
					case AVG: {
						AST tmp25_AST = null;
						tmp25_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp25_AST);
						match(AVG);
						break;
					}
					case SUM: {
						AST tmp26_AST = null;
						tmp26_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp26_AST);
						match(SUM);
						break;
					}
					case YEAR: {
						AST tmp27_AST = null;
						tmp27_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp27_AST);
						match(YEAR);
						break;
					}
					case QUARTER: {
						AST tmp28_AST = null;
						tmp28_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp28_AST);
						match(QUARTER);
						break;
					}
					case MONTH: {
						AST tmp29_AST = null;
						tmp29_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp29_AST);
						match(MONTH);
						break;
					}
					case WEEK: {
						AST tmp30_AST = null;
						tmp30_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp30_AST);
						match(WEEK);
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				match(LPAREN);
				column();
				astFactory.addASTChild(currentAST, returnAST);
				match(RPAREN);
				aggregate_AST = (AST) currentAST.root;
				break;
			}
			case COUNT: {
				AST tmp33_AST = null;
				tmp33_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp33_AST);
				match(COUNT);
				match(LPAREN);
				{
					switch (LA(1)) {
					case DISTINCT: {
						AST tmp35_AST = null;
						tmp35_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp35_AST);
						match(DISTINCT);
						break;
					}
					case NULL:
					case TRUE:
					case FALSE:
					case IDENT:
					case LPAREN:
					case QUOTED_STRING:
					case STAR:
					case LITERAL:
					case NAMED_PARAM:
					case QUESTION_MARK:
					case PLUS:
					case MINUS: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					switch (LA(1)) {
					case STAR: {
						AST tmp36_AST = null;
						tmp36_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp36_AST);
						match(STAR);
						break;
					}
					case NULL:
					case TRUE:
					case FALSE:
					case IDENT:
					case LPAREN:
					case QUOTED_STRING:
					case LITERAL:
					case NAMED_PARAM:
					case QUESTION_MARK:
					case PLUS:
					case MINUS: {
						column();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				match(RPAREN);
				aggregate_AST = (AST) currentAST.root;
				break;
			}
			case DATE_FORMAT: {
				aggregateWithMode();
				astFactory.addASTChild(currentAST, returnAST);
				aggregate_AST = (AST) currentAST.root;
				break;
			}
			case CONCAT: {
				aggregateWithFields();
				astFactory.addASTChild(currentAST, returnAST);
				aggregate_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_10);
			} else {
				throw ex;
			}
		}
		returnAST = aggregate_AST;
	}

	public final void expression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;

		try { // for error handling
			subExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
				_loop66: do {
					if ((_tokenSet_13.member(LA(1)))) {
						binaryOperator();
						astFactory.addASTChild(currentAST, returnAST);
						subExpression();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						break _loop66;
					}

				} while (true);
			}
			expression_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_14);
			} else {
				throw ex;
			}
		}
		returnAST = expression_AST;
	}

	public final void aggregateWithMode() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aggregateWithMode_AST = null;

		try { // for error handling
			AST tmp38_AST = null;
			tmp38_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp38_AST);
			match(DATE_FORMAT);
			match(LPAREN);
			column();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp40_AST = null;
			tmp40_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(COMMA);
			constantSimple();
			astFactory.addASTChild(currentAST, returnAST);
			match(RPAREN);
			aggregateWithMode_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_10);
			} else {
				throw ex;
			}
		}
		returnAST = aggregateWithMode_AST;
	}

	public final void constantSimple() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constantSimple_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case QUOTED_STRING: {
				AST tmp42_AST = null;
				tmp42_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(QUOTED_STRING);
				constantSimple_AST = (AST) currentAST.root;
				break;
			}
			case LITERAL: {
				AST tmp43_AST = null;
				tmp43_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp43_AST);
				match(LITERAL);
				constantSimple_AST = (AST) currentAST.root;
				break;
			}
			case NAMED_PARAM: {
				AST tmp44_AST = null;
				tmp44_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(NAMED_PARAM);
				constantSimple_AST = (AST) currentAST.root;
				break;
			}
			case QUESTION_MARK: {
				AST tmp45_AST = null;
				tmp45_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(QUESTION_MARK);
				constantSimple_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_15);
			} else {
				throw ex;
			}
		}
		returnAST = constantSimple_AST;
	}

	public final void aggregateWithFields() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aggregateWithFields_AST = null;

		try { // for error handling
			AST tmp46_AST = null;
			tmp46_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp46_AST);
			match(CONCAT);
			match(LPAREN);
			{
				if ((_tokenSet_16.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
					selectItem();
					astFactory.addASTChild(currentAST, returnAST);
				} else if ((LA(1) == QUOTED_STRING) && (LA(2) == COMMA || LA(2) == RPAREN)) {
					AST tmp48_AST = null;
					tmp48_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp48_AST);
					match(QUOTED_STRING);
				} else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			{
				_loop36: do {
					if ((LA(1) == COMMA)) {
						AST tmp49_AST = null;
						tmp49_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp49_AST);
						match(COMMA);
						{
							if ((_tokenSet_16.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
								selectItem();
								astFactory.addASTChild(currentAST, returnAST);
							} else if ((LA(1) == QUOTED_STRING) && (LA(2) == COMMA || LA(2) == RPAREN)) {
								AST tmp50_AST = null;
								tmp50_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp50_AST);
								match(QUOTED_STRING);
							} else {
								throw new NoViableAltException(LT(1), getFilename());
							}

						}
					} else {
						break _loop36;
					}

				} while (true);
			}
			match(RPAREN);
			aggregateWithFields_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_10);
			} else {
				throw ex;
			}
		}
		returnAST = aggregateWithFields_AST;
	}

	public final void subSimpleCondition() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subSimpleCondition_AST = null;

		try { // for error handling
			{
				if ((LA(1) == NOT) && (_tokenSet_18.member(LA(2)))) {
					AST tmp52_AST = null;
					tmp52_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp52_AST);
					match(NOT);
				} else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
				} else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			{
				boolean synPredMatched49 = false;
				if (((LA(1) == LPAREN) && (_tokenSet_18.member(LA(2))))) {
					int _m49 = mark();
					synPredMatched49 = true;
					inputState.guessing++;
					try {
						{
							match(LPAREN);
							simpleCondition();
							match(RPAREN);
						}
					} catch (RecognitionException pe) {
						synPredMatched49 = false;
					}
					rewind(_m49);
					inputState.guessing--;
				}
				if (synPredMatched49) {
					AST tmp53_AST = null;
					tmp53_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp53_AST);
					match(LPAREN);
					simpleCondition();
					astFactory.addASTChild(currentAST, returnAST);
					AST tmp54_AST = null;
					tmp54_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp54_AST);
					match(RPAREN);
				} else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_20.member(LA(2)))) {
					simplePredicate();
					astFactory.addASTChild(currentAST, returnAST);
				} else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			subSimpleCondition_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_21);
			} else {
				throw ex;
			}
		}
		returnAST = subSimpleCondition_AST;
	}

	public final void simplePredicate() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simplePredicate_AST = null;

		try { // for error handling
			{
				if ((_tokenSet_11.member(LA(1))) && (_tokenSet_22.member(LA(2)))) {
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				} else if ((_tokenSet_16.member(LA(1))) && (_tokenSet_23.member(LA(2)))) {
					selectItem();
					astFactory.addASTChild(currentAST, returnAST);
					{
						{
							switch (LA(1)) {
							case BETWEEN:
							case MATCH:
							case EQ:
							case LT:
							case GT:
							case LE:
							case GE:
							case SQL_NE:
							case BAND:
							case NBAND: {
								comparisonOperator();
								astFactory.addASTChild(currentAST, returnAST);
								expression();
								astFactory.addASTChild(currentAST, returnAST);
								break;
							}
							case IS: {
								AST tmp55_AST = null;
								tmp55_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp55_AST);
								match(IS);
								{
									switch (LA(1)) {
									case NOT: {
										AST tmp56_AST = null;
										tmp56_AST = astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp56_AST);
										match(NOT);
										break;
									}
									case NULL: {
										break;
									}
									default: {
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
								}
								AST tmp57_AST = null;
								tmp57_AST = astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp57_AST);
								match(NULL);
								break;
							}
							case NOT:
							case IN:
							case LIKE: {
								{
									switch (LA(1)) {
									case NOT: {
										AST tmp58_AST = null;
										tmp58_AST = astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp58_AST);
										match(NOT);
										break;
									}
									case IN:
									case LIKE: {
										break;
									}
									default: {
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
								}
								{
									switch (LA(1)) {
									case LIKE: {
										AST tmp59_AST = null;
										tmp59_AST = astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp59_AST);
										match(LIKE);
										AST tmp60_AST = null;
										tmp60_AST = astFactory.create(LT(1));
										astFactory.addASTChild(currentAST, tmp60_AST);
										match(QUOTED_STRING);
										break;
									}
									case IN: {
										AST tmp61_AST = null;
										tmp61_AST = astFactory.create(LT(1));
										astFactory.makeASTRoot(currentAST, tmp61_AST);
										match(IN);
										match(LPAREN);
										{
											switch (LA(1)) {
											case NULL:
											case TRUE:
											case FALSE:
											case QUOTED_STRING:
											case LITERAL:
											case NAMED_PARAM:
											case QUESTION_MARK: {
												constant();
												astFactory.addASTChild(currentAST, returnAST);
												{
													_loop59: do {
														if ((LA(1) == COMMA)) {
															AST tmp63_AST = null;
															tmp63_AST = astFactory.create(LT(1));
															astFactory.addASTChild(currentAST, tmp63_AST);
															match(COMMA);
															constant();
															astFactory.addASTChild(currentAST, returnAST);
														} else {
															break _loop59;
														}

													} while (true);
												}
												break;
											}
											case SELECT: {
												selectStatement();
												astFactory.addASTChild(currentAST, returnAST);
												break;
											}
											default: {
												throw new NoViableAltException(LT(1), getFilename());
											}
											}
										}
										match(RPAREN);
										break;
									}
									default: {
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
								}
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
					}
				} else if ((LA(1) == NOT || LA(1) == EXISTS)) {
					{
						switch (LA(1)) {
						case NOT: {
							AST tmp65_AST = null;
							tmp65_AST = astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp65_AST);
							match(NOT);
							break;
						}
						case EXISTS: {
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					AST tmp66_AST = null;
					tmp66_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp66_AST);
					match(EXISTS);
					match(LPAREN);
					{
						selectStatement();
						astFactory.addASTChild(currentAST, returnAST);
					}
					match(RPAREN);
				} else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			simplePredicate_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_21);
			} else {
				throw ex;
			}
		}
		returnAST = simplePredicate_AST;
	}

	public final void comparisonOperator() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST comparisonOperator_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case EQ: {
				AST tmp69_AST = null;
				tmp69_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(EQ);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case LT: {
				AST tmp70_AST = null;
				tmp70_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp70_AST);
				match(LT);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case GT: {
				AST tmp71_AST = null;
				tmp71_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp71_AST);
				match(GT);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case LE: {
				AST tmp72_AST = null;
				tmp72_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp72_AST);
				match(LE);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case GE: {
				AST tmp73_AST = null;
				tmp73_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
				match(GE);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case SQL_NE: {
				AST tmp74_AST = null;
				tmp74_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp74_AST);
				match(SQL_NE);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case BETWEEN: {
				AST tmp75_AST = null;
				tmp75_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp75_AST);
				match(BETWEEN);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case BAND: {
				AST tmp76_AST = null;
				tmp76_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(BAND);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case NBAND: {
				AST tmp77_AST = null;
				tmp77_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp77_AST);
				match(NBAND);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			case MATCH: {
				AST tmp78_AST = null;
				tmp78_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp78_AST);
				match(MATCH);
				comparisonOperator_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_11);
			} else {
				throw ex;
			}
		}
		returnAST = comparisonOperator_AST;
	}

	public final void constant() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case QUOTED_STRING:
			case LITERAL:
			case NAMED_PARAM:
			case QUESTION_MARK: {
				constantSimple();
				astFactory.addASTChild(currentAST, returnAST);
				constant_AST = (AST) currentAST.root;
				break;
			}
			case TRUE: {
				AST tmp79_AST = null;
				tmp79_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp79_AST);
				match(TRUE);
				constant_AST = (AST) currentAST.root;
				break;
			}
			case FALSE: {
				AST tmp80_AST = null;
				tmp80_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp80_AST);
				match(FALSE);
				constant_AST = (AST) currentAST.root;
				break;
			}
			case NULL: {
				AST tmp81_AST = null;
				tmp81_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp81_AST);
				match(NULL);
				constant_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_15);
			} else {
				throw ex;
			}
		}
		returnAST = constant_AST;
	}

	public final void subExpression() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subExpression_AST = null;

		try { // for error handling
			{
				switch (LA(1)) {
				case PLUS:
				case MINUS: {
					unaryOperator();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case NULL:
				case TRUE:
				case FALSE:
				case IDENT:
				case LPAREN:
				case QUOTED_STRING:
				case LITERAL:
				case NAMED_PARAM:
				case QUESTION_MARK: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case IDENT: {
					dbObject();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LPAREN: {
					AST tmp82_AST = null;
					tmp82_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp82_AST);
					match(LPAREN);
					{
						switch (LA(1)) {
						case SELECT: {
							selectStatement();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case NULL:
						case TRUE:
						case FALSE:
						case IDENT:
						case LPAREN:
						case QUOTED_STRING:
						case LITERAL:
						case NAMED_PARAM:
						case QUESTION_MARK:
						case PLUS:
						case MINUS: {
							expression();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					AST tmp83_AST = null;
					tmp83_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp83_AST);
					match(RPAREN);
					break;
				}
				default:
					if ((_tokenSet_24.member(LA(1))) && (_tokenSet_15.member(LA(2)))) {
						constant();
						astFactory.addASTChild(currentAST, returnAST);
					} else if ((_tokenSet_25.member(LA(1))) && (LA(2) == AND)) {
						constantSimple();
						astFactory.addASTChild(currentAST, returnAST);
						AST tmp84_AST = null;
						tmp84_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp84_AST);
						match(AND);
						constantSimple();
						astFactory.addASTChild(currentAST, returnAST);
					} else {
						throw new NoViableAltException(LT(1), getFilename());
					}
				}
			}
			subExpression_AST = (AST) currentAST.root;
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_15);
			} else {
				throw ex;
			}
		}
		returnAST = subExpression_AST;
	}

	public final void binaryOperator() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST binaryOperator_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case PLUS: {
				AST tmp85_AST = null;
				tmp85_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp85_AST);
				match(PLUS);
				binaryOperator_AST = (AST) currentAST.root;
				break;
			}
			case MINUS: {
				AST tmp86_AST = null;
				tmp86_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp86_AST);
				match(MINUS);
				binaryOperator_AST = (AST) currentAST.root;
				break;
			}
			case STAR: {
				AST tmp87_AST = null;
				tmp87_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp87_AST);
				match(STAR);
				binaryOperator_AST = (AST) currentAST.root;
				break;
			}
			case DIVIDE: {
				AST tmp88_AST = null;
				tmp88_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp88_AST);
				match(DIVIDE);
				binaryOperator_AST = (AST) currentAST.root;
				break;
			}
			case MOD: {
				AST tmp89_AST = null;
				tmp89_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp89_AST);
				match(MOD);
				binaryOperator_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_11);
			} else {
				throw ex;
			}
		}
		returnAST = binaryOperator_AST;
	}

	public final void unaryOperator() throws RecognitionException, TokenStreamException {

		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryOperator_AST = null;

		try { // for error handling
			switch (LA(1)) {
			case PLUS: {
				AST tmp90_AST = null;
				tmp90_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp90_AST);
				match(PLUS);
				unaryOperator_AST = (AST) currentAST.root;
				break;
			}
			case MINUS: {
				AST tmp91_AST = null;
				tmp91_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp91_AST);
				match(MINUS);
				unaryOperator_AST = (AST) currentAST.root;
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} catch (RecognitionException ex) {
			if (inputState.guessing == 0) {
				reportError(ex);
				recover(ex, _tokenSet_26);
			} else {
				throw ex;
			}
		}
		returnAST = unaryOperator_AST;
	}

	public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"select\"",
			"\"distinct\"", "\"from\"", "\"where\"", "\"and\"", "\"or\"", "\"order\"", "\"asc\"", "\"desc\"",
			"\"group\"", "\"by\"", "\"having\"", "\"with\"", "\"rollup\"", "\"max\"", "\"min\"", "\"avg\"", "\"sum\"",
			"\"count\"", "\"date_format\"", "\"year\"", "\"quarter\"", "\"month\"", "\"week\"", "\"concat\"", "\"is\"",
			"\"not\"", "\"null\"", "\"in\"", "\"like\"", "\"exists\"", "\"between\"", "\"true\"", "\"false\"",
			"\"match\"", "COMMA", "IDENT", "LPAREN", "RPAREN", "QUOTED_STRING", "STAR", "LITERAL", "NAMED_PARAM",
			"QUESTION_MARK", "PLUS", "MINUS", "DIVIDE", "MOD", "EQ", "LT", "GT", "LE", "GE", "SQL_NE", "BAND", "NBAND",
			"DOT", "COLON", "IDENT_START", "IDENT_LETTER", "DIGIT", "INT", "NUM", "ESCqs", "WS" };

	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap = null;
	};

	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

	private static final long[] mk_tokenSet_1() {
		long[] data = { 4398046511106L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

	private static final long[] mk_tokenSet_2() {
		long[] data = { 4398046512130L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

	private static final long[] mk_tokenSet_3() {
		long[] data = { 64L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

	private static final long[] mk_tokenSet_4() {
		long[] data = { 4398046520450L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

	private static final long[] mk_tokenSet_5() {
		long[] data = { 4398046520322L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

	private static final long[] mk_tokenSet_6() {
		long[] data = { 4398046610434L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());

	private static final long[] mk_tokenSet_7() {
		long[] data = { 4398046577666L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());

	private static final long[] mk_tokenSet_8() {
		long[] data = { 1152662893351780290L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());

	private static final long[] mk_tokenSet_9() {
		long[] data = { 4398046585858L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

	private static final long[] mk_tokenSet_10() {
		long[] data = { 1148423176515066946L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());

	private static final long[] mk_tokenSet_11() {
		long[] data = { 1103018468573184L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());

	private static final long[] mk_tokenSet_12() {
		long[] data = { 1152921486890212690L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());

	private static final long[] mk_tokenSet_13() {
		long[] data = { 4239716836704256L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());

	private static final long[] mk_tokenSet_14() {
		long[] data = { 1148423176515075906L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());

	private static final long[] mk_tokenSet_15() {
		long[] data = { 1152662893351780162L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());

	private static final long[] mk_tokenSet_16() {
		long[] data = { 1103019005181952L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());

	private static final long[] mk_tokenSet_17() {
		long[] data = { 4503258177470736L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());

	private static final long[] mk_tokenSet_18() {
		long[] data = { 1103037258792960L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());

	private static final long[] mk_tokenSet_19() {
		long[] data = { 1152920954850846482L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());

	private static final long[] mk_tokenSet_20() {
		long[] data = { 1152920954314237714L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());

	private static final long[] mk_tokenSet_21() {
		long[] data = { 4398046586626L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());

	private static final long[] mk_tokenSet_22() {
		long[] data = { 4502708421732114L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());

	private static final long[] mk_tokenSet_23() {
		long[] data = { 1152916539087782160L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());

	private static final long[] mk_tokenSet_24() {
		long[] data = { 255295003557888L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());

	private static final long[] mk_tokenSet_25() {
		long[] data = { 255086697644032L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());

	private static final long[] mk_tokenSet_26() {
		long[] data = { 258593538441216L, 0L };
		return data;
	}

	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

}
