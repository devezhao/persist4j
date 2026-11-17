package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.engine.ID;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * 查询占位符参数值转换。仅将字符串转换为字段要求的类型，非字符串原样返回。
 *
 * @author devezhao
 * @since 2026/11/17
 */
final class QueryParameterConverter {

	private static final String[] DATE_PATTERNS = {
			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss",
			"yyyy-MM-dd'T'HH:mm:ss"
	};

	private QueryParameterConverter() {
	}

	/**
	 * @param type
	 * @param value
	 * @return
	 */
	static Object convert(Type type, Object value) {
		// 非字符串即已是编辑器要求的类型，无需转换
		if (type == null || !(value instanceof String)) {
			return value;
		}

		String text = ((String) value).trim();
		if (FieldType.PRIMARY.equals(type)
				|| FieldType.REFERENCE.equals(type)
				|| FieldType.ANY_REFERENCE.equals(type)) {
			return ID.valueOf(text);
		} else if (FieldType.DATE.equals(type)
				|| FieldType.DATETIME.equals(type)
				|| FieldType.TIMESTAMP.equals(type)) {
			// 解析为 java.util.Date，由对应 Editor 决定最终的 JDBC 类型
			return parseDate(text);
		} else if (FieldType.TIME.equals(type)) {
			try {
				return LocalTime.parse(text);
			} catch (DateTimeParseException ex) {
				throw new IllegalArgumentException("Cannot parse time value: " + value, ex);
			}
		} else if (FieldType.BOOL.equals(type)) {
			Boolean bool = BooleanUtils.toBooleanObject(text);
			if (bool == null && "1".equals(text)) {
				bool = Boolean.TRUE;
			} else if (bool == null && "0".equals(text)) {
				bool = Boolean.FALSE;
			}
			if (bool == null) {
				throw new IllegalArgumentException("Cannot parse boolean value: " + value);
			}
			return bool;
		}
		return value;
	}

	/**
	 * @param text
	 * @return
	 */
	private static java.util.Date parseDate(String text) {
		try {
			return DateUtils.parseDateStrictly(text, DATE_PATTERNS);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Cannot parse date value: " + text, ex);
		}
	}
}
