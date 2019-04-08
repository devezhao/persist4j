package cn.devezhao.persist4j.dialect;

import java.sql.Types;
import java.util.Locale;

import cn.devezhao.persist4j.engine.ID;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 12/10/2018
 */
public class OracleDialect extends Dialect {
	private static final long serialVersionUID = 5056057920212451174L;

	public OracleDialect() {
		super();
		final String idt = String.format("CHAR(%d)", ID.getIDGenerator().getLength());
		registerColumnType(FieldType.PRIMARY.getMask(), idt, Types.CHAR);
		registerColumnType(FieldType.REFERENCE.getMask(), idt, Types.CHAR);
		registerColumnType(FieldType.ANY_REFERENCE.getMask(), idt, Types.CHAR);
		registerColumnType(FieldType.REFERENCE_LIST.getMask(), "VARCHAR2(420)", Types.CHAR);
		registerColumnType(FieldType.INT.getMask(), "INT", Types.INTEGER);
		registerColumnType(FieldType.SMALL_INT.getMask(), "SMALLINT", Types.SMALLINT);
		registerColumnType(FieldType.DOUBLE.getMask(), "NUMBER(19, 8)", Types.NUMERIC);
		registerColumnType(FieldType.DECIMAL.getMask(), "NUMBER(19, 8)", Types.NUMERIC);
		registerColumnType(FieldType.LONG.getMask(), "LONG", Types.BIGINT);
		registerColumnType(FieldType.CHAR.getMask(), "CHAR(1)", Types.CHAR);
		registerColumnType(FieldType.STRING.getMask(), "VARCHAR2(%d)", Types.VARCHAR);
		registerColumnType(FieldType.TEXT.getMask(), "LONG VARCHAR", Types.LONGVARCHAR);
		registerColumnType(FieldType.DATE.getMask(), "DATE", Types.DATE);
		registerColumnType(FieldType.TIMESTAMP.getMask(), "TIMESTAMP", Types.TIMESTAMP);
		registerColumnType(FieldType.BOOL.getMask(), "CHAR(1)", Types.CHAR);
		registerColumnType(FieldType.NTEXT.getMask(), "CLOB", Types.CLOB);
		registerColumnType(FieldType.BINARY.getMask(), "BLOB", Types.BLOB);
	}
	
	@Override
	public String getDialectName() {
		return "oracle";
	}

	@Override
	public char getStartQuote() {
		return QUOTED[4];
	}

	@Override
	public char getClosedQuote() {
		return QUOTED[4];
	}

	@Override
	public String insertLimit(String query, int limit, int offset) {
		query = query.trim();
		boolean isForUpdate = false;
		if (query.toLowerCase(Locale.ROOT).endsWith(" for update")) {
			query = query.substring(0, query.length() - 11);
			isForUpdate = true;
		}

		String pagingSelect = null;
		if (offset > 0) {
			pagingSelect = "select * from ( select row_.*, ROWNUM rownum_ from ( %s ) where rownum_ <= ? and rownum_ > ?";
			pagingSelect = String.format(pagingSelect, query, limit + offset, offset);
		} else {
			pagingSelect = "select * from ( %s ) where ROWNUM <= %d";
			pagingSelect = String.format(pagingSelect, query, limit);
		}

		if (isForUpdate) {
			pagingSelect += " for update";
		}
		
		return pagingSelect;
	}
}
