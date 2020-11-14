package cn.devezhao.persist4j.dialect;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.function.GetDateFunction;

/**
 * 数据库方言
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: Dialect.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public abstract class Dialect implements Serializable {
	private static final long serialVersionUID = 2651165738037766620L;

	protected static final Log LOG = LogFactory.getLog(Dialect.class);

	// mysql, sql server, and others
	protected static final char[] QUOTED = { '`', '`', '[', ']', '"'};

	// types of system
	final private Set<Type> fieldTypes = new HashSet<>();
	// types of raw
	final private Map<Integer, String> columnTypes = new HashMap<>();
	// system to raw mapping
	final private Map<Integer, Integer> field2columnMapping = new HashMap<>();
	// function of sql in ajql query
	final private Map<String, Class<?>> sqlFunctions = new HashMap<>();

	/**
	 * Create a new Dialect
	 */
	protected Dialect() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Using dialect provider: " + this.getClass());
		}
		
		registerFieldType(FieldType.PRIMARY);
		registerFieldType(FieldType.REFERENCE);
		registerFieldType(FieldType.ANY_REFERENCE);
		registerFieldType(FieldType.REFERENCE_LIST);
		registerFieldType(FieldType.INT);
		registerFieldType(FieldType.SMALL_INT);
		registerFieldType(FieldType.DOUBLE);
		registerFieldType(FieldType.DECIMAL);
		registerFieldType(FieldType.LONG);
		registerFieldType(FieldType.CHAR);
		registerFieldType(FieldType.STRING);
		registerFieldType(FieldType.TEXT);
		registerFieldType(FieldType.DATE);
		registerFieldType(FieldType.TIMESTAMP);
		registerFieldType(FieldType.BOOL);
		registerFieldType(FieldType.NTEXT);
		registerFieldType(FieldType.BINARY);
		
		registerSqlFunction("getdate", GetDateFunction.class);
	}
	
	protected void registerFieldType(Type type) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Register field type " + type);
		}
		fieldTypes.add(type);
	}
	
	protected void registerColumnType(int mask, String columnType, int sqlType) {
		if (LOG.isDebugEnabled()) {
			LOG.info("Register column(sql) type <" + mask + ':' + columnType + ">");
		}
		columnTypes.put(mask, columnType);
		field2columnMapping.put(mask, sqlType);
	}

	protected void registerSqlFunction(String name, Class<?> funcClazz) {
		String upName = name.toUpperCase();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Register sql function: $" + upName + " > " + funcClazz);
		}
		if (sqlFunctions.containsKey(upName)) {
			LOG.warn("Overlay function: " + upName);
		}
		sqlFunctions.put(upName, funcClazz);
	}
	
	public Type getFieldType(int mask) {
		for (Type tt : fieldTypes) {
			if (tt.getMask() == mask) {
				return tt;
			}
		}
		throw new DialectException("No field type defined by mask: " + mask);
	}
	
	public Type getFieldType(String shortName) {
		for (Type tt : fieldTypes) {
			if (tt.getName().equalsIgnoreCase(shortName)) {
				return tt;
			}
		}
		throw new DialectException("No field type defined by short name: " + shortName);
	}
	
	public String getColumnType(int mask) {
		String ct = columnTypes.get(mask);
		if (ct == null) {
			throw new DialectException("No column type defined: " + mask);
		}
		return ct;
	}
	
	public int getSQLType(int mask) {
		Integer sqlt = field2columnMapping.get(mask);
		if (sqlt == null) {
			throw new DialectException("No sql type mapping by mask: " + mask);
		}
		return sqlt;
	}
	
	public String generateColumnDDL(Field field, Integer length) {
		String cType = columnTypes.get(field.getType().getMask());
		if (cType.contains("%d")) {
			cType = String.format(cType, length);
		}
		
		String ddl = String.format("%s %s %s", 
				field.getPhysicalName(),
				cType,
				field.isNullable() ? "" : "not null");
		
		if (FieldType.PRIMARY == field.getType()) {
			return ddl + " primary key";
		}
		return ddl;
	}
	
	public String quote(String field) {
		return getStartQuote() + field.trim() + getClosedQuote();
	}
	
	public boolean supportsLimitOffset() {
		return false;
	}
	
	public boolean supportsFullText() {
		return false;
	}
	
	// ------------------------------------------------------------------------ Abstract Methods

	abstract public String getDialectName();
	
	abstract public char getStartQuote();

	abstract public char getClosedQuote();

	abstract public String insertLimit(String query, int limit, int offset);
	
	// ------------------------------------------------------------------------ Override Methods
	
	@Override
	public String toString() {
		return getDialectName();
	}
}
