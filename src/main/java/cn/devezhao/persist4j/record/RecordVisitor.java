package cn.devezhao.persist4j.record;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;

import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.dialect.editor.BoolEditor;
import cn.devezhao.persist4j.engine.ID;


/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: RecordVisitor.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
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
		Object pVal = null;
		
		if (FieldType.PRIMARY.equals(ft) || FieldType.REFERENCE.equals(ft)) {
			pVal = ID.valueOf(value);
		}
		else if (FieldType.REFERENCE_LIST.equals(ft)) {
			String[] split = value.split("\\,");
			ID[] ids = new ID[split.length];
			for (int i = 0; i < split.length; i++)
				ids[i] = ID.valueOf(split[i].trim());
		}
		else if (FieldType.INT.equals(ft) || FieldType.TINY_INT.equals(ft) || FieldType.SMALL_INT.equals(ft)) {
			pVal = NumberUtils.toInt(value);
		}
		else if (FieldType.DOUBLE.equals(ft)) {
			pVal = NumberUtils.toDouble(value);
		}
		else if (FieldType.DECIMAL.equals(ft)) {
			pVal = new BigDecimal(value.toCharArray());
		}
		else if (FieldType.LONG.equals(ft)) {
			pVal = NumberUtils.toLong(value);
		}
		else if (FieldType.DATE.equals(ft)) {
			pVal = getDefaultDateFormat().parse(value, new ParsePosition(0));
		}
		else if (FieldType.DATETIME.equals(ft) || FieldType.TIMESTAMP.equals(ft)) {
			pVal = getDefaultDateTimeFormat().parse(value, new ParsePosition(0));
		}
		else if (FieldType.BOOL.equals(ft)) {
			char ch = value.toUpperCase().charAt(0);
			if (ch == BoolEditor.TRUE)
				pVal = true;
			else
				pVal = false;
		}
		else if (FieldType.NTEXT.equals(ft)) {
			pVal = new StringReader(value);
		}
		else if (FieldType.BINARY.equals(ft)) {
			return;  // TODO FieldTypeImpl.BINARY
		}
		else {  // FieldTypeImpl.CHAR, FieldTypeImpl.STRING, FieldTypeImpl.TEXT
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
		String pVal = null;
		
		if (FieldType.PRIMARY.equals(ft) 
				|| FieldType.REFERENCE.equals(ft)) {
			pVal = ((ID) value).toLiteral();
		}
		else if (FieldType.REFERENCE_LIST.equals(ft)) {
			ID[] ids = (ID[]) value;
			StringBuilder sb = new StringBuilder(ids.length * 41);
			for (int i = 0; i < ids.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(ids[i].toLiteral());
			}
		}
		else if (FieldType.INT.equals(ft) 
				|| FieldType.TINY_INT.equals(ft) 
				|| FieldType.SMALL_INT.equals(ft)
				|| FieldType.DOUBLE.equals(ft) 
				|| FieldType.DECIMAL.equals(ft)
				|| FieldType.LONG.equals(ft)) {
			pVal = String.valueOf( value );
		}
		else if (FieldType.DATE.equals(ft)) {
			pVal = getDefaultDateFormat().format((Date) value);
		}
		else if (FieldType.DATETIME.equals(ft) 
				|| FieldType.TIMESTAMP.equals(ft)) {
			pVal = getDefaultDateTimeFormat().format((Date) value);
		}
		else if (FieldType.BOOL.equals(ft)) {
			if ((Boolean) value)
				pVal = "T";
			else
				pVal = "F";
		}
		else if (FieldType.NTEXT.equals(ft)) {
			throw new UnsupportedOperationException();
		}
		else if (FieldType.BINARY.equals(ft)) {
			throw new UnsupportedOperationException();
		}
		else {  // FieldTypeImpl.CHAR, FieldTypeImpl.STRING, FieldTypeImpl.TEXT
			pVal = value.toString();
		}
		return pVal;
	}
	
	/**
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
}
