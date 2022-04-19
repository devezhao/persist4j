package cn.devezhao.persist4j.record;

import cn.devezhao.commons.CalendarUtils;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.dialect.editor.BoolEditor;
import cn.devezhao.persist4j.dialect.editor.NTextEditor;
import cn.devezhao.persist4j.dialect.editor.ReferenceListEditor;
import cn.devezhao.persist4j.engine.ID;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: RecordVisitor.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class RecordVisitor {

	public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Sets a literal-value to specify field
	 * 
	 * @param fieldName
	 * @param value
	 * @param record
	 */
	public static void setValueByLiteral(String fieldName, String value, Record record) {
		setValueByLiteral(record.getEntity().getField(fieldName), value, record);
	}
	
	/**
	 * Sets a literal-value to specify field
	 * 
	 * @param field
	 * @param value
	 * @param record
	 */
	public static void setValueByLiteral(Field field, String value, Record record) {
		Type ft = field.getType();
		Object pVal;
		
		if (FieldType.PRIMARY.equals(ft) 
				|| FieldType.REFERENCE.equals(ft) 
				|| FieldType.ANY_REFERENCE.equals(ft)) {
			pVal = ID.valueOf(value);
			
		} else if (FieldType.REFERENCE_LIST.equals(ft)) {
			List<ID> ids = new ArrayList<>();
			for (String id : value.split(ReferenceListEditor.VALUE_SEP)) {
				if (ID.isId(id)) ids.add(ID.valueOf(id));
			}
			pVal = ids.toArray(ID.EMPTY_ID_ARRAY);

		} else if (FieldType.INT.equals(ft)
				|| FieldType.SMALL_INT.equals(ft)) {
			pVal = NumberUtils.toInt(clearNumber(value));
			
		} else if (FieldType.DOUBLE.equals(ft)) {
			pVal = NumberUtils.toDouble(clearNumber(value));
			
		} else if (FieldType.DECIMAL.equals(ft)) {
			pVal = new BigDecimal(clearNumber(value).toCharArray());
			
		} else if (FieldType.LONG.equals(ft)) {
			pVal = NumberUtils.toLong(clearNumber(value));
			
		} else if (FieldType.DATE.equals(ft)
				|| FieldType.TIMESTAMP.equals(ft)) {
			pVal = tryParseDate(value);
			
		} else if (FieldType.TIME.equals(ft)) {
			pVal = tryParseTime(value);

		} else if (FieldType.BOOL.equals(ft)) {
			char ch = value.toUpperCase().charAt(0);
			pVal = ch == BoolEditor.TRUE;
			
		} else if (FieldType.NTEXT.equals(ft)) {
			pVal = new StringReader(value);
			
		} else if (FieldType.BINARY.equals(ft)) {
			// TODO BINARY
			throw new UnsupportedOperationException("Unsupported Type: BINARY");
			
		} else {  // FieldTypeImpl.CHAR, FieldTypeImpl.STRING, FieldTypeImpl.TEXT
			pVal = value;
		}
		record.setObjectValue(field.getName(), pVal);
	}
	
	/**
	 * Gets literal-value by a object-value
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static String getLiteralByValue(Field field, Object value) {
		Type ft = field.getType();
		String literalValue;
		
		if (FieldType.PRIMARY.equals(ft) 
				|| FieldType.REFERENCE.equals(ft) 
				|| FieldType.ANY_REFERENCE.equals(ft)) {
			literalValue = ((ID) value).toLiteral();
			
		} else if (FieldType.REFERENCE_LIST.equals(ft)) {
			literalValue = StringUtils.join((ID[]) value, ReferenceListEditor.VALUE_SEP);

		} else if (FieldType.INT.equals(ft)
				|| FieldType.SMALL_INT.equals(ft)
				|| FieldType.DOUBLE.equals(ft) 
				|| FieldType.DECIMAL.equals(ft)
				|| FieldType.LONG.equals(ft)) {
			literalValue = String.valueOf(value);
			
		} else if (FieldType.DATE.equals(ft)) {
			literalValue = getDefaultDateFormat().format((Date) value);
			
		} else if (FieldType.TIMESTAMP.equals(ft)) {
			literalValue = getDefaultDateTimeFormat().format((Date) value);
			
		} else if (FieldType.TIME.equals(ft)) {
			LocalTime lt = value instanceof Time ? ((Time) value).toLocalTime() : (LocalTime) value;
			literalValue = String.format("%02d:%02d:%02d", lt.getHour(), lt.getMinute(), lt.getSecond());

		} else if (FieldType.BOOL.equals(ft)) {
			literalValue = (Boolean) value ? "T" : "F";
			
		} else if (FieldType.NTEXT.equals(ft)) {
			literalValue = new NTextEditor().read2String(value).toString();
			
		} else if (FieldType.BINARY.equals(ft)) {
			// TODO BINARY
			throw new UnsupportedOperationException("Unsupported Type: BINARY");
			
		} else {  // FieldTypeImpl.CHAR, FieldTypeImpl.STRING, FieldTypeImpl.TEXT
			literalValue = value.toString();
			
		}
		return literalValue;
	}

	/**
	 * @param num
	 * @return
	 */
	protected static String clearNumber(String num) {
		if (StringUtils.isBlank(num)) return "";
		return num.replace(",", "").trim();
	}

	/**
	 * @return
	 * @see #DATE_FORMAT_STRING
	 */
	public static DateFormat getDefaultDateFormat() {
		return new SimpleDateFormat(DATE_FORMAT_STRING);
	}
	
	/**
	 * @see #DATETIME_FORMAT_STRING
	 */
	public static DateFormat getDefaultDateTimeFormat() {
		return new SimpleDateFormat(DATETIME_FORMAT_STRING);
	}
	
	private static final String[] DATEPARSE_MODES = new String[] {
			"yyyy", "yyyy-MM", "yyyy-MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss"
	};
	/**
	 * @param source
	 * @return
	 */
	public static Date tryParseDate(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		
		source = source.trim();
		int len = source.length();
		for (String mode : DATEPARSE_MODES) {
			if (mode.length() == len) {
				return CalendarUtils.parse(source, CalendarUtils.getDateFormat(mode));
			}
		}
		throw new FieldValueException("Invalid value of date: " + source);
	}

	/**
	 * @param source
	 * @return
	 */
	public static LocalTime tryParseTime(String source) {
		if (NumberUtils.isNumber(source)) {
			long s = NumberUtils.toLong(source, 0);
			return LocalTime.ofSecondOfDay(s);
		}

		String[] ss = source.split(":");
		try {
			int h = Integer.parseInt(ss[0]);
			int m = ss.length > 1 ? Integer.parseInt(ss[1]) : 0;
			int s = ss.length > 2 ? Integer.parseInt(ss[2]) : 0;

			return LocalTime.of(h, m, s);
		} catch (Exception ex) {
			throw new FieldValueException("Invalid value of time: " + source);
		}
	}
}
