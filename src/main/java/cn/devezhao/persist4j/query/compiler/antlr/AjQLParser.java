// $ANTLR 2.7.7 (2006-11-01): "ajql.g" -> "AjQLParser.java"$

package cn.devezhao.persist4j.query.compiler.antlr;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;

import java.util.Hashtable;

import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

@SuppressWarnings("ALL")
public class AjQLParser extends LLkParser implements AjQLParserTokenTypes {

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

        try {      // for error handling
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

        try {      // for error handling
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

        try {      // for error handling
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

        try {      // for error handling
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

        try {      // for error handling
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
                _loop19:
                do {
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

        try {      // for error handling
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

        try {      // for error handling
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
                    case GROUP_CONCAT:
                    case LPAREN:
                    case QUOTED_STRING:
                    case IDENT:
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

        try {      // for error handling
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

        try {      // for error handling
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

        try {      // for error handling
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
                _loop22:
                do {
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

        try {      // for error handling
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

        try {      // for error handling
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

        try {      // for error handling
            selectItem();
            astFactory.addASTChild(currentAST, returnAST);
            {
                _loop30:
                do {
                    if ((LA(1) == COMMA)) {
                        match(COMMA);
                        selectItem();
                        astFactory.addASTChild(currentAST, returnAST);
                    } else {
                        break _loop30;
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

        try {      // for error handling
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

        try {      // for error handling
            subSimpleCondition();
            astFactory.addASTChild(currentAST, returnAST);
            {
                _loop47:
                do {
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
                        break _loop47;
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

        try {      // for error handling
            switch (LA(1)) {
                case NULL:
                case TRUE:
                case FALSE:
                case LPAREN:
                case QUOTED_STRING:
                case IDENT:
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
                case CONCAT:
                case GROUP_CONCAT: {
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

    public final void groupConcatClause() throws RecognitionException, TokenStreamException {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST groupConcatClause_AST = null;

        try {      // for error handling
            AST tmp23_AST = null;
            tmp23_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp23_AST);
            match(GROUP_CONCAT);
            match(LPAREN);
            {
                switch (LA(1)) {
                    case DISTINCT: {
                        AST tmp25_AST = null;
                        tmp25_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp25_AST);
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
                    case GROUP_CONCAT:
                    case LPAREN:
                    case QUOTED_STRING:
                    case IDENT:
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
            selectItem();
            astFactory.addASTChild(currentAST, returnAST);
            {
                switch (LA(1)) {
                    case SEPARATOR: {
                        AST tmp26_AST = null;
                        tmp26_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp26_AST);
                        match(SEPARATOR);
                        AST tmp27_AST = null;
                        tmp27_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp27_AST);
                        match(QUOTED_STRING);
                        break;
                    }
                    case RPAREN: {
                        break;
                    }
                    default: {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
            }
            match(RPAREN);
            groupConcatClause_AST = (AST) currentAST.root;
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_10);
            } else {
                throw ex;
            }
        }
        returnAST = groupConcatClause_AST;
    }

    public final void column() throws RecognitionException, TokenStreamException {

        returnAST = null;
        ASTPair currentAST = new ASTPair();
        AST column_AST = null;

        try {      // for error handling
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

        try {      // for error handling
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
                                AST tmp29_AST = null;
                                tmp29_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp29_AST);
                                match(MIN);
                                break;
                            }
                            case MAX: {
                                AST tmp30_AST = null;
                                tmp30_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp30_AST);
                                match(MAX);
                                break;
                            }
                            case AVG: {
                                AST tmp31_AST = null;
                                tmp31_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp31_AST);
                                match(AVG);
                                break;
                            }
                            case SUM: {
                                AST tmp32_AST = null;
                                tmp32_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp32_AST);
                                match(SUM);
                                break;
                            }
                            case YEAR: {
                                AST tmp33_AST = null;
                                tmp33_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp33_AST);
                                match(YEAR);
                                break;
                            }
                            case QUARTER: {
                                AST tmp34_AST = null;
                                tmp34_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp34_AST);
                                match(QUARTER);
                                break;
                            }
                            case MONTH: {
                                AST tmp35_AST = null;
                                tmp35_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp35_AST);
                                match(MONTH);
                                break;
                            }
                            case WEEK: {
                                AST tmp36_AST = null;
                                tmp36_AST = astFactory.create(LT(1));
                                astFactory.makeASTRoot(currentAST, tmp36_AST);
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
                    AST tmp39_AST = null;
                    tmp39_AST = astFactory.create(LT(1));
                    astFactory.makeASTRoot(currentAST, tmp39_AST);
                    match(COUNT);
                    match(LPAREN);
                    {
                        switch (LA(1)) {
                            case DISTINCT: {
                                AST tmp41_AST = null;
                                tmp41_AST = astFactory.create(LT(1));
                                astFactory.addASTChild(currentAST, tmp41_AST);
                                match(DISTINCT);
                                break;
                            }
                            case NULL:
                            case TRUE:
                            case FALSE:
                            case LPAREN:
                            case QUOTED_STRING:
                            case IDENT:
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
                                AST tmp42_AST = null;
                                tmp42_AST = astFactory.create(LT(1));
                                astFactory.addASTChild(currentAST, tmp42_AST);
                                match(STAR);
                                break;
                            }
                            case NULL:
                            case TRUE:
                            case FALSE:
                            case LPAREN:
                            case QUOTED_STRING:
                            case IDENT:
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
                case GROUP_CONCAT: {
                    groupConcatClause();
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

        try {      // for error handling
            subExpression();
            astFactory.addASTChild(currentAST, returnAST);
            {
                _loop69:
                do {
                    if ((_tokenSet_13.member(LA(1)))) {
                        binaryOperator();
                        astFactory.addASTChild(currentAST, returnAST);
                        subExpression();
                        astFactory.addASTChild(currentAST, returnAST);
                    } else {
                        break _loop69;
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

        try {      // for error handling
            AST tmp44_AST = null;
            tmp44_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp44_AST);
            match(DATE_FORMAT);
            match(LPAREN);
            column();
            astFactory.addASTChild(currentAST, returnAST);
            AST tmp46_AST = null;
            tmp46_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp46_AST);
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

        try {      // for error handling
            switch (LA(1)) {
                case QUOTED_STRING: {
                    AST tmp48_AST = null;
                    tmp48_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp48_AST);
                    match(QUOTED_STRING);
                    constantSimple_AST = (AST) currentAST.root;
                    break;
                }
                case LITERAL: {
                    AST tmp49_AST = null;
                    tmp49_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp49_AST);
                    match(LITERAL);
                    constantSimple_AST = (AST) currentAST.root;
                    break;
                }
                case NAMED_PARAM: {
                    AST tmp50_AST = null;
                    tmp50_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp50_AST);
                    match(NAMED_PARAM);
                    constantSimple_AST = (AST) currentAST.root;
                    break;
                }
                case QUESTION_MARK: {
                    AST tmp51_AST = null;
                    tmp51_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp51_AST);
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

        try {      // for error handling
            AST tmp52_AST = null;
            tmp52_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp52_AST);
            match(CONCAT);
            match(LPAREN);
            {
                if ((_tokenSet_16.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
                    selectItem();
                    astFactory.addASTChild(currentAST, returnAST);
                } else if ((LA(1) == QUOTED_STRING) && (LA(2) == COMMA || LA(2) == RPAREN)) {
                    AST tmp54_AST = null;
                    tmp54_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp54_AST);
                    match(QUOTED_STRING);
                } else {
                    throw new NoViableAltException(LT(1), getFilename());
                }

            }
            {
                _loop39:
                do {
                    if ((LA(1) == COMMA)) {
                        AST tmp55_AST = null;
                        tmp55_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp55_AST);
                        match(COMMA);
                        {
                            if ((_tokenSet_16.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
                                selectItem();
                                astFactory.addASTChild(currentAST, returnAST);
                            } else if ((LA(1) == QUOTED_STRING) && (LA(2) == COMMA || LA(2) == RPAREN)) {
                                AST tmp56_AST = null;
                                tmp56_AST = astFactory.create(LT(1));
                                astFactory.addASTChild(currentAST, tmp56_AST);
                                match(QUOTED_STRING);
                            } else {
                                throw new NoViableAltException(LT(1), getFilename());
                            }

                        }
                    } else {
                        break _loop39;
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

        try {      // for error handling
            {
                if ((LA(1) == NOT) && (_tokenSet_18.member(LA(2)))) {
                    AST tmp58_AST = null;
                    tmp58_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp58_AST);
                    match(NOT);
                } else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
                } else {
                    throw new NoViableAltException(LT(1), getFilename());
                }

            }
            {
                boolean synPredMatched52 = false;
                if (((LA(1) == LPAREN) && (_tokenSet_18.member(LA(2))))) {
                    int _m52 = mark();
                    synPredMatched52 = true;
                    inputState.guessing++;
                    try {
                        {
                            match(LPAREN);
                            simpleCondition();
                            match(RPAREN);
                        }
                    } catch (RecognitionException pe) {
                        synPredMatched52 = false;
                    }
                    rewind(_m52);
                    inputState.guessing--;
                }
                if (synPredMatched52) {
                    AST tmp59_AST = null;
                    tmp59_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp59_AST);
                    match(LPAREN);
                    simpleCondition();
                    astFactory.addASTChild(currentAST, returnAST);
                    AST tmp60_AST = null;
                    tmp60_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp60_AST);
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

        try {      // for error handling
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
                                    AST tmp61_AST = null;
                                    tmp61_AST = astFactory.create(LT(1));
                                    astFactory.addASTChild(currentAST, tmp61_AST);
                                    match(IS);
                                    {
                                        switch (LA(1)) {
                                            case NOT: {
                                                AST tmp62_AST = null;
                                                tmp62_AST = astFactory.create(LT(1));
                                                astFactory.addASTChild(currentAST, tmp62_AST);
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
                                    AST tmp63_AST = null;
                                    tmp63_AST = astFactory.create(LT(1));
                                    astFactory.addASTChild(currentAST, tmp63_AST);
                                    match(NULL);
                                    break;
                                }
                                case NOT:
                                case IN:
                                case LIKE: {
                                    {
                                        switch (LA(1)) {
                                            case NOT: {
                                                AST tmp64_AST = null;
                                                tmp64_AST = astFactory.create(LT(1));
                                                astFactory.addASTChild(currentAST, tmp64_AST);
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
                                                AST tmp65_AST = null;
                                                tmp65_AST = astFactory.create(LT(1));
                                                astFactory.addASTChild(currentAST, tmp65_AST);
                                                match(LIKE);
                                                AST tmp66_AST = null;
                                                tmp66_AST = astFactory.create(LT(1));
                                                astFactory.addASTChild(currentAST, tmp66_AST);
                                                match(QUOTED_STRING);
                                                break;
                                            }
                                            case IN: {
                                                AST tmp67_AST = null;
                                                tmp67_AST = astFactory.create(LT(1));
                                                astFactory.makeASTRoot(currentAST, tmp67_AST);
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
                                                                _loop62:
                                                                do {
                                                                    if ((LA(1) == COMMA)) {
                                                                        AST tmp69_AST = null;
                                                                        tmp69_AST = astFactory.create(LT(1));
                                                                        astFactory.addASTChild(currentAST, tmp69_AST);
                                                                        match(COMMA);
                                                                        constant();
                                                                        astFactory.addASTChild(currentAST, returnAST);
                                                                    } else {
                                                                        break _loop62;
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
                                AST tmp71_AST = null;
                                tmp71_AST = astFactory.create(LT(1));
                                astFactory.addASTChild(currentAST, tmp71_AST);
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
                    AST tmp72_AST = null;
                    tmp72_AST = astFactory.create(LT(1));
                    astFactory.makeASTRoot(currentAST, tmp72_AST);
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

        try {      // for error handling
            switch (LA(1)) {
                case EQ: {
                    AST tmp75_AST = null;
                    tmp75_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp75_AST);
                    match(EQ);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case LT: {
                    AST tmp76_AST = null;
                    tmp76_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp76_AST);
                    match(LT);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case GT: {
                    AST tmp77_AST = null;
                    tmp77_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp77_AST);
                    match(GT);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case LE: {
                    AST tmp78_AST = null;
                    tmp78_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp78_AST);
                    match(LE);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case GE: {
                    AST tmp79_AST = null;
                    tmp79_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp79_AST);
                    match(GE);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case SQL_NE: {
                    AST tmp80_AST = null;
                    tmp80_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp80_AST);
                    match(SQL_NE);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case BETWEEN: {
                    AST tmp81_AST = null;
                    tmp81_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp81_AST);
                    match(BETWEEN);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case BAND: {
                    AST tmp82_AST = null;
                    tmp82_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp82_AST);
                    match(BAND);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case NBAND: {
                    AST tmp83_AST = null;
                    tmp83_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp83_AST);
                    match(NBAND);
                    comparisonOperator_AST = (AST) currentAST.root;
                    break;
                }
                case MATCH: {
                    AST tmp84_AST = null;
                    tmp84_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp84_AST);
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

        try {      // for error handling
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
                    AST tmp85_AST = null;
                    tmp85_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp85_AST);
                    match(TRUE);
                    constant_AST = (AST) currentAST.root;
                    break;
                }
                case FALSE: {
                    AST tmp86_AST = null;
                    tmp86_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp86_AST);
                    match(FALSE);
                    constant_AST = (AST) currentAST.root;
                    break;
                }
                case NULL: {
                    AST tmp87_AST = null;
                    tmp87_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp87_AST);
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

        try {      // for error handling
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
                    case LPAREN:
                    case QUOTED_STRING:
                    case IDENT:
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
                        AST tmp88_AST = null;
                        tmp88_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp88_AST);
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
                                case LPAREN:
                                case QUOTED_STRING:
                                case IDENT:
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
                        AST tmp89_AST = null;
                        tmp89_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp89_AST);
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
                            AST tmp90_AST = null;
                            tmp90_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp90_AST);
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

        try {      // for error handling
            switch (LA(1)) {
                case PLUS: {
                    AST tmp91_AST = null;
                    tmp91_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp91_AST);
                    match(PLUS);
                    binaryOperator_AST = (AST) currentAST.root;
                    break;
                }
                case MINUS: {
                    AST tmp92_AST = null;
                    tmp92_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp92_AST);
                    match(MINUS);
                    binaryOperator_AST = (AST) currentAST.root;
                    break;
                }
                case STAR: {
                    AST tmp93_AST = null;
                    tmp93_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp93_AST);
                    match(STAR);
                    binaryOperator_AST = (AST) currentAST.root;
                    break;
                }
                case DIVIDE: {
                    AST tmp94_AST = null;
                    tmp94_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp94_AST);
                    match(DIVIDE);
                    binaryOperator_AST = (AST) currentAST.root;
                    break;
                }
                case MOD: {
                    AST tmp95_AST = null;
                    tmp95_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp95_AST);
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

        try {      // for error handling
            switch (LA(1)) {
                case PLUS: {
                    AST tmp96_AST = null;
                    tmp96_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp96_AST);
                    match(PLUS);
                    unaryOperator_AST = (AST) currentAST.root;
                    break;
                }
                case MINUS: {
                    AST tmp97_AST = null;
                    tmp97_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp97_AST);
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


    public static final String[] _tokenNames = {
            "<0>",
            "EOF",
            "<2>",
            "NULL_TREE_LOOKAHEAD",
            "\"select\"",
            "\"distinct\"",
            "\"from\"",
            "\"where\"",
            "\"and\"",
            "\"or\"",
            "\"order\"",
            "\"asc\"",
            "\"desc\"",
            "\"group\"",
            "\"by\"",
            "\"having\"",
            "\"with\"",
            "\"rollup\"",
            "\"max\"",
            "\"min\"",
            "\"avg\"",
            "\"sum\"",
            "\"count\"",
            "\"date_format\"",
            "\"year\"",
            "\"quarter\"",
            "\"month\"",
            "\"week\"",
            "\"concat\"",
            "\"is\"",
            "\"not\"",
            "\"null\"",
            "\"in\"",
            "\"like\"",
            "\"exists\"",
            "\"between\"",
            "\"true\"",
            "\"false\"",
            "\"match\"",
            "\"group_concat\"",
            "\"separator\"",
            "COMMA",
            "LPAREN",
            "QUOTED_STRING",
            "RPAREN",
            "IDENT",
            "STAR",
            "LITERAL",
            "NAMED_PARAM",
            "QUESTION_MARK",
            "PLUS",
            "MINUS",
            "DIVIDE",
            "MOD",
            "EQ",
            "LT",
            "GT",
            "LE",
            "GE",
            "SQL_NE",
            "BAND",
            "NBAND",
            "DOT",
            "COLON",
            "IDENT_START",
            "IDENT_LETTER",
            "DIGIT",
            "INT",
            "NUM",
            "ESCqs",
            "WS"
    };

    protected void buildTokenTypeASTClassMap() {
        tokenTypeToASTClassMap = null;
    }

    ;

    private static final long[] mk_tokenSet_0() {
        long[] data = {2L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
        long[] data = {17592186044418L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
        long[] data = {17592186045442L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3() {
        long[] data = {64L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

    private static final long[] mk_tokenSet_4() {
        long[] data = {17592186053762L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

    private static final long[] mk_tokenSet_5() {
        long[] data = {17592186053634L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

    private static final long[] mk_tokenSet_6() {
        long[] data = {17592186143746L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());

    private static final long[] mk_tokenSet_7() {
        long[] data = {17592186110978L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());

    private static final long[] mk_tokenSet_8() {
        long[] data = {4610651701718925250L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());

    private static final long[] mk_tokenSet_9() {
        long[] data = {17592186119170L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

    private static final long[] mk_tokenSet_10() {
        long[] data = {4593692834372099138L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());

    private static final long[] mk_tokenSet_11() {
        long[] data = {4411448956551168L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());

    private static final long[] mk_tokenSet_12() {
        long[] data = {4611685450954939730L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());

    private static final long[] mk_tokenSet_13() {
        long[] data = {16958867346817024L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());

    private static final long[] mk_tokenSet_14() {
        long[] data = {4593692834372108098L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());

    private static final long[] mk_tokenSet_15() {
        long[] data = {4610651701718925122L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());

    private static final long[] mk_tokenSet_16() {
        long[] data = {4411999248973824L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());

    private static final long[] mk_tokenSet_17() {
        long[] data = {18012407792140560L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());

    private static final long[] mk_tokenSet_18() {
        long[] data = {4412017502584832L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());

    private static final long[] mk_tokenSet_19() {
        long[] data = {4611682719892317970L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());

    private static final long[] mk_tokenSet_20() {
        long[] data = {4611682169599895314L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());

    private static final long[] mk_tokenSet_21() {
        long[] data = {17592186119938L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());

    private static final long[] mk_tokenSet_22() {
        long[] data = {18010208768960274L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());

    private static final long[] mk_tokenSet_23() {
        long[] data = {4611664560233906448L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());

    private static final long[] mk_tokenSet_24() {
        long[] data = {994166817423360L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());

    private static final long[] mk_tokenSet_25() {
        long[] data = {993958511509504L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());

    private static final long[] mk_tokenSet_26() {
        long[] data = {1033749236023296L, 0L};
        return data;
    }

    public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

}
