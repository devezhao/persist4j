package cn.devezhao.persist4j.record;

import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.NullValue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: RecordCreator.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 * @since 0.1, Feb 14, 2009
 */
public interface RecordCreator {

    Log LOG = LogFactory.getLog(RecordCreator.class);

	/**
	 * 创建 Record
	 *
	 * @return
	 */
	Record create();

	/**
	 * #setFieldValue 产生警告时调用
	 *
	 * @param field
	 * @param value
	 * @return
	 */
	default boolean onSetFieldValueWarn(Field field, String value, Record record) {
		return false;
	}

	/**
	 * 设置字段值
	 *
	 * @param field
	 * @param value
	 * @param record
	 * @return
	 */
    default boolean setFieldValue(Field field, String value, Record record) {
        final boolean isNew = record.getPrimary() == null;

        // 忽略更新
        if (!isNew && !field.isUpdatable()) {
        	if (!onSetFieldValueWarn(field, value, record)) {
				LOG.warn("Could not put value to un-updatable field : " + field);
				return false;
			}
        }

        // 忽略新建
        if (isNew && !field.isCreatable()) {
			if (!onSetFieldValueWarn(field, value, record)) {
            	LOG.warn("Could not put value to un-creatable field : " + field);
				return false;
			}
        }

        boolean noValue = value == null || StringUtils.isEmpty(value);

        // 无值
        if (noValue) {
            if (isNew && !field.isNullable() && !field.isAutoValue()) {
                throw new FieldValueException("Field [ " + field + " ] must not be null");
            }
        }

        if (noValue) {
            record.setNull(field.getName());
        } else {
            RecordVisitor.setValueByLiteral(field, value, record);
        }
        return true;
    }

	/**
	 * 验证字段约束
	 *
	 * @param record
	 */
	default void verify(Record record) {
		if (record.getPrimary() == null) return;

		List<String> notNullable = new ArrayList<>();
		for (Field field : record.getEntity().getFields()) {
			if (FieldType.PRIMARY.equals(field.getType())) continue;

			Object val = record.getObjectValue(field.getName());
			if (!field.isNullable() && !field.isAutoValue() && (val == null || NullValue.is(val))) {
				notNullable.add(field.getName());
			}
		}

		if (!notNullable.isEmpty()) {
			throw new FieldValueException("Must not be null. Entity [ " + record.getEntity().getName()
					+ " ], Fields [ " + StringUtils.join(notNullable.toArray(new String[0]), ", ") + " ]");
		}
	}
}
