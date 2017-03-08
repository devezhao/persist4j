package cn.devezhao.persist4j.dialect;

import java.sql.Types;

import cn.devezhao.persist4j.engine.ID;

/**
 * MySql方言
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 5, 2009
 * @version $Id: MySQL5Dialect.java 20 2009-02-10 03:35:10Z
 *          zhaofang123@gmail.com $
 */
public class MySQL5Dialect extends Dialect {
	private static final long serialVersionUID = 9022968517851901342L;

	public MySQL5Dialect() {
		super();
		final String idt = String.format("char(%d)", ID.getIDGenerator().getLength());
		registerColumnType(FieldType.PRIMARY.getMask(), idt, Types.CHAR);
		registerColumnType(FieldType.REFERENCE.getMask(), idt, Types.CHAR);
		registerColumnType(FieldType.INT.getMask(), "int(11)", Types.INTEGER);
		registerColumnType(FieldType.SMALL_INT.getMask(), "smallint(6)", Types.SMALLINT);
		registerColumnType(FieldType.DOUBLE.getMask(), "double(19, %d)", Types.DOUBLE);
		registerColumnType(FieldType.DECIMAL.getMask(), "decimal(19, 4)", Types.DECIMAL);
		registerColumnType(FieldType.LONG.getMask(), "bigint(20)", Types.BIGINT);
		registerColumnType(FieldType.CHAR.getMask(), "char(1)", Types.CHAR);
		registerColumnType(FieldType.STRING.getMask(), "varchar(%d)", Types.VARCHAR);
		registerColumnType(FieldType.TEXT.getMask(), "text(%d)", Types.VARCHAR);
		registerColumnType(FieldType.DATE.getMask(), "date", Types.DATE);
		registerColumnType(FieldType.TIMESTAMP.getMask(), "timestamp", Types.TIMESTAMP);
		registerColumnType(FieldType.BOOL.getMask(), "char(1)", Types.CHAR);
		registerColumnType(FieldType.NTEXT.getMask(), "longtext", Types.LONGVARCHAR);
		registerColumnType(FieldType.BINARY.getMask(), "longblob", Types.BLOB);
	}

	@Override
	public String getDialectName() {
		return "mysql5";
	}

	@Override
	public char getClosedQuote() {
		return QUOTED[0];
	}

	@Override
	public char getStartQuote() {
		return QUOTED[1];
	}

	@Override
	public String insertLimit(String query, int limit, int offset) {
		return new StringBuilder(query)
				.append(" limit ").append(limit)
				.append(offset > 0 ? (" offset " + offset) : "")
				.toString();
	}

	@Override
	public boolean supportsLimitOffset() {
		return true;
	}
}
