// $ANTLR 2.7.7 (2006-11-01): "ajql.g" -> "AjQLLexer.java"$

package cn.devezhao.persist4j.query.compiler.antlr;

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

import antlr.ANTLRHashString;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class AjQLLexer extends antlr.CharScanner implements AjQLParserTokenTypes, TokenStream {
	public AjQLLexer(InputStream in) {
		this(new ByteBuffer(in));
	}

	public AjQLLexer(Reader in) {
		this(new CharBuffer(in));
	}

	public AjQLLexer(InputBuffer ib) {
		this(new LexerSharedInputState(ib));
	}

	public AjQLLexer(LexerSharedInputState state) {
		super(state);
		caseSensitiveLiterals = false;
		setCaseSensitive(false);
		literals = new Hashtable();
		literals.put(new ANTLRHashString("count", this), new Integer(22));
		literals.put(new ANTLRHashString("sum", this), new Integer(21));
		literals.put(new ANTLRHashString("min", this), new Integer(19));
		literals.put(new ANTLRHashString("month", this), new Integer(26));
		literals.put(new ANTLRHashString("false", this), new Integer(38));
		literals.put(new ANTLRHashString("true", this), new Integer(37));
		literals.put(new ANTLRHashString("rollup", this), new Integer(17));
		literals.put(new ANTLRHashString("and", this), new Integer(8));
		literals.put(new ANTLRHashString("concat", this), new Integer(29));
		literals.put(new ANTLRHashString("asc", this), new Integer(11));
		literals.put(new ANTLRHashString("desc", this), new Integer(12));
		literals.put(new ANTLRHashString("select", this), new Integer(4));
		literals.put(new ANTLRHashString("exists", this), new Integer(35));
		literals.put(new ANTLRHashString("distinct", this), new Integer(5));
		literals.put(new ANTLRHashString("group", this), new Integer(13));
		literals.put(new ANTLRHashString("where", this), new Integer(7));
		literals.put(new ANTLRHashString("year", this), new Integer(24));
		literals.put(new ANTLRHashString("match", this), new Integer(39));
		literals.put(new ANTLRHashString("avg", this), new Integer(20));
		literals.put(new ANTLRHashString("order", this), new Integer(10));
		literals.put(new ANTLRHashString("in", this), new Integer(33));
		literals.put(new ANTLRHashString("null", this), new Integer(32));
		literals.put(new ANTLRHashString("having", this), new Integer(15));
		literals.put(new ANTLRHashString("quarter", this), new Integer(25));
		literals.put(new ANTLRHashString("date_format", this), new Integer(23));
		literals.put(new ANTLRHashString("or", this), new Integer(9));
		literals.put(new ANTLRHashString("between", this), new Integer(36));
		literals.put(new ANTLRHashString("max", this), new Integer(18));
		literals.put(new ANTLRHashString("from", this), new Integer(6));
		literals.put(new ANTLRHashString("is", this), new Integer(30));
		literals.put(new ANTLRHashString("like", this), new Integer(34));
		literals.put(new ANTLRHashString("date", this), new Integer(28));
		literals.put(new ANTLRHashString("week", this), new Integer(27));
		literals.put(new ANTLRHashString("with", this), new Integer(16));
		literals.put(new ANTLRHashString("not", this), new Integer(31));
		literals.put(new ANTLRHashString("by", this), new Integer(14));
	}

	public Token nextToken() throws TokenStreamException {
		Token theRetToken = null;
		tryAgain: for (;;) {
			Token _token = null;
			int _ttype = Token.INVALID_TYPE;
			resetText();
			try { // for char stream error handling
				try { // for lexical error handling
					switch (LA(1)) {
					case '/': {
						mDIVIDE(true);
						theRetToken = _returnToken;
						break;
					}
					case '+': {
						mPLUS(true);
						theRetToken = _returnToken;
						break;
					}
					case '-': {
						mMINUS(true);
						theRetToken = _returnToken;
						break;
					}
					case '*': {
						mSTAR(true);
						theRetToken = _returnToken;
						break;
					}
					case '%': {
						mMOD(true);
						theRetToken = _returnToken;
						break;
					}
					case '=': {
						mEQ(true);
						theRetToken = _returnToken;
						break;
					}
					case '!': {
						mNBAND(true);
						theRetToken = _returnToken;
						break;
					}
					case ',': {
						mCOMMA(true);
						theRetToken = _returnToken;
						break;
					}
					case '(': {
						mLPAREN(true);
						theRetToken = _returnToken;
						break;
					}
					case ')': {
						mRPAREN(true);
						theRetToken = _returnToken;
						break;
					}
					case '?': {
						mQUESTION_MARK(true);
						theRetToken = _returnToken;
						break;
					}
					case '\t':
					case '\n':
					case '\u000c':
					case '\r':
					case ' ': {
						mWS(true);
						theRetToken = _returnToken;
						break;
					}
					default:
						if ((LA(1) == '<') && (LA(2) == '=')) {
							mLE(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '>') && (LA(2) == '=')) {
							mGE(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '<') && (LA(2) == '>')) {
							mSQL_NE(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '&') && (LA(2) == '&')) {
							mBAND(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == ':') && (_tokenSet_0.member(LA(2)))) {
							mNAMED_PARAM(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '\'') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe'))) {
							mQUOTED_STRING(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '\'') && (LA(2) == '\'')) {
							mESCqs(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '<') && (true)) {
							mLT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '>') && (true)) {
							mGT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '.') && (true)) {
							mDOT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == ':') && (true)) {
							mCOLON(true);
							theRetToken = _returnToken;
						} else if ((_tokenSet_1.member(LA(1))) && (true)) {
							mIDENT(true);
							theRetToken = _returnToken;
						} else if ((_tokenSet_2.member(LA(1))) && (true)) {
							mLITERAL(true);
							theRetToken = _returnToken;
						} else {
							if (LA(1) == EOF_CHAR) {
								uponEOF();
								_returnToken = makeToken(Token.EOF_TYPE);
							} else {
								throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(),
										getColumn());
							}
						}
					}
					if (_returnToken == null)
						continue tryAgain; // found SKIP token
					_ttype = _returnToken.getType();
					_returnToken.setType(_ttype);
					return _returnToken;
				} catch (RecognitionException e) {
					throw new TokenStreamRecognitionException(e);
				}
			} catch (CharStreamException cse) {
				if (cse instanceof CharStreamIOException) {
					throw new TokenStreamIOException(((CharStreamIOException) cse).io);
				} else {
					throw new TokenStreamException(cse.getMessage());
				}
			}
		}
	}

	public final void mDIVIDE(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DIVIDE;
		int _saveIndex;

		match('/');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mPLUS(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = PLUS;
		int _saveIndex;

		match('+');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mMINUS(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = MINUS;
		int _saveIndex;

		match('-');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mSTAR(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = STAR;
		int _saveIndex;

		match('*');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mMOD(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = MOD;
		int _saveIndex;

		match('%');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mEQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = EQ;
		int _saveIndex;

		match('=');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mLT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LT;
		int _saveIndex;

		match('<');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mGT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = GT;
		int _saveIndex;

		match('>');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LE;
		int _saveIndex;

		match("<=");
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mGE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = GE;
		int _saveIndex;

		match(">=");
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mSQL_NE(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SQL_NE;
		int _saveIndex;

		match("<>");
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mBAND(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BAND;
		int _saveIndex;

		match("&&");
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mNBAND(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = NBAND;
		int _saveIndex;

		match("!&");
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mDOT(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DOT;
		int _saveIndex;

		match('.');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mCOMMA(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = COMMA;
		int _saveIndex;

		match(',');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mLPAREN(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LPAREN;
		int _saveIndex;

		match('(');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mRPAREN(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = RPAREN;
		int _saveIndex;

		match(')');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mCOLON(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = COLON;
		int _saveIndex;

		match(':');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mQUESTION_MARK(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = QUESTION_MARK;
		int _saveIndex;

		match('?');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mNAMED_PARAM(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = NAMED_PARAM;
		int _saveIndex;

		{
			mCOLON(false);
			{
				int _cnt100 = 0;
				_loop100: do {
					if ((_tokenSet_0.member(LA(1)))) {
						mIDENT_LETTER(false);
					} else {
						if (_cnt100 >= 1) {
							break _loop100;
						} else {
							throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
						}
					}

					_cnt100++;
				} while (true);
			}
		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	protected final void mIDENT_LETTER(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = IDENT_LETTER;
		int _saveIndex;

		switch (LA(1)) {
		case '#':
		case '$':
		case '&':
		case '^':
		case '_':
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z': {
			mIDENT_START(false);
			break;
		}
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9': {
			mDIGIT(false);
			break;
		}
		case '.': {
			mDOT(false);
			break;
		}
		default: {
			throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mIDENT(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = IDENT;
		int _saveIndex;

		mIDENT_START(false);
		{
			_loop103: do {
				if ((_tokenSet_0.member(LA(1)))) {
					mIDENT_LETTER(false);
				} else {
					break _loop103;
				}

			} while (true);
		}
		_ttype = testLiteralsTable(_ttype);
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	protected final void mIDENT_START(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = IDENT_START;
		int _saveIndex;

		switch (LA(1)) {
		case '_': {
			match('_');
			break;
		}
		case '$': {
			match('$');
			break;
		}
		case '&': {
			match('&');
			break;
		}
		case '#': {
			match('#');
			break;
		}
		case '^': {
			match('^');
			break;
		}
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z': {
			matchRange('a', 'z');
			break;
		}
		default: {
			throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
		}
		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	protected final void mDIGIT(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DIGIT;
		int _saveIndex;

		matchRange('0', '9');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	protected final void mINT(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = INT;
		int _saveIndex;

		{
			int _cnt109 = 0;
			_loop109: do {
				if (((LA(1) >= '0' && LA(1) <= '9'))) {
					mDIGIT(false);
				} else {
					if (_cnt109 >= 1) {
						break _loop109;
					} else {
						throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
					}
				}

				_cnt109++;
			} while (true);
		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	protected final void mNUM(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = NUM;
		int _saveIndex;

		mINT(false);
		{
			if ((LA(1) == '.')) {
				mDOT(false);
				{
					int _cnt113 = 0;
					_loop113: do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							mDIGIT(false);
						} else {
							if (_cnt113 >= 1) {
								break _loop113;
							} else {
								throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(),
										getColumn());
							}
						}

						_cnt113++;
					} while (true);
				}
			} else {
			}

		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mLITERAL(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LITERAL;
		int _saveIndex;

		{
			int _cnt116 = 0;
			_loop116: do {
				if ((_tokenSet_0.member(LA(1)))) {
					mIDENT_LETTER(false);
				} else if (((LA(1) >= '\u0080' && LA(1) <= '\ufffe'))) {
					matchRange('\u0080', '\uFFFE');
				} else {
					if (_cnt116 >= 1) {
						break _loop116;
					} else {
						throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
					}
				}

				_cnt116++;
			} while (true);
		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mQUOTED_STRING(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = QUOTED_STRING;
		int _saveIndex;

		_saveIndex = text.length();
		match('\'');
		text.setLength(_saveIndex);
		{
			_loop121: do {
				boolean synPredMatched120 = false;
				if (((LA(1) == '\'') && (LA(2) == '\''))) {
					int _m120 = mark();
					synPredMatched120 = true;
					inputState.guessing++;
					try {
						{
							mESCqs(false);
						}
					} catch (RecognitionException pe) {
						synPredMatched120 = false;
					}
					rewind(_m120);
					inputState.guessing--;
				}
				if (synPredMatched120) {
					mESCqs(false);
				} else if ((_tokenSet_3.member(LA(1)))) {
					matchNot('\'');
				} else {
					break _loop121;
				}

			} while (true);
		}
		_saveIndex = text.length();
		match('\'');
		text.setLength(_saveIndex);
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mESCqs(boolean _createToken)
			throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = ESCqs;
		int _saveIndex;

		match('\'');
		match('\'');
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = WS;
		int _saveIndex;

		{
			switch (LA(1)) {
			case ' ': {
				match(' ');
				break;
			}
			case '\t': {
				match('\t');
				break;
			}
			case '\u000c': {
				match('\f');
				break;
			}
			case '\n': {
				match('\n');
				if (inputState.guessing == 0) {
					newline();
				}
				break;
			}
			case '\r': {
				match('\r');
				break;
			}
			default: {
				throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine(), getColumn());
			}
			}
		}
		if (inputState.guessing == 0) {
			_ttype = Token.SKIP;
		}
		if (_createToken && _token == null && _ttype != Token.SKIP) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
		}
		_returnToken = _token;
	}

	private static final long[] mk_tokenSet_0() {
		long[] data = new long[1025];
		data[0] = 288019647876300800L;
		data[1] = 576460746934714368L;
		return data;
	}

	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

	private static final long[] mk_tokenSet_1() {
		long[] data = new long[1025];
		data[0] = 377957122048L;
		data[1] = 576460746934714368L;
		return data;
	}

	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

	private static final long[] mk_tokenSet_2() {
		long[] data = new long[3072];
		data[0] = 288019647876300800L;
		data[1] = 576460746934714368L;
		for (int i = 2; i <= 1022; i++) {
			data[i] = -1L;
		}
		data[1023] = 9223372036854775807L;
		return data;
	}

	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2048];
		data[0] = -549755813889L;
		for (int i = 1; i <= 1022; i++) {
			data[i] = -1L;
		}
		data[1023] = 9223372036854775807L;
		return data;
	}

	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

}
