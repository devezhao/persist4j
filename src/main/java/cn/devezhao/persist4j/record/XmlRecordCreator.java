package cn.devezhao.persist4j.record;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.NullValue;
import cn.devezhao.persist4j.engine.StandardRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: XMLRecordCreator.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class XmlRecordCreator implements RecordCreator {
	
	private static final Log LOG = LogFactory.getLog(XmlRecordCreator.class);

	final protected Entity entity;
	final protected ID editor;
	final protected Element source;

	/**
	 * Create a new XmlRecordCreator. the format of document like
	 * <pre>
	 * &lt;entity name=&quot;...&quot; [id=&quot;...&quot;]&gt;
	 *    &lt;userName value=&quot;...&quot; [label&quot;...&quot;]/&gt;
	 *    &lt;description&gt;
	 *       ...
	 *    &lt;/description&gt;
	 * &lt;/entity&gt;
	 * </pre>
	 * 
	 * @param entity
	 * @param source
	 */
	public XmlRecordCreator(Entity entity, Element source) {
		this(entity, source, null);
	}
	
	/**
	 * 
	 * @param entity
	 * @param source
	 * @param editor
	 */
	public XmlRecordCreator(Entity entity, Element source, ID editor) {
		Validate.notNull(entity);
		Validate.notNull(source);
		this.entity = entity;
		this.source = source;
		this.editor = editor;
	}

	@Override
    public Record create() {
		Record record = new StandardRecord(entity, editor);

		String id = source.valueOf("@id");
		if (!StringUtils.isBlank(id)) {
			record.setID(entity.getPrimaryField().getName(), ID.valueOf(id));
		}

		for (Object o : source.elements()) {
			Element el = (Element) o;
			String fieldName = el.getName();
			String fieldValue = el.attributeValue("value");
			if (fieldValue == null) {
				fieldValue = el.getText();
			}

			Field field = entity.containsField(fieldName) ? entity.getField(fieldName) : null;
			if (field == null) {
				LOG.warn("Unable found field [ " + entity.getName() + '#' + fieldName  + " ], will ignore");
				continue;
			}

			setFieldValue(field, fieldValue, record);
		}

		afterCreate(record);
		afterVerify(record);
		return record;
	}
	
	protected void afterCreate(Record record) {
	}

	protected void afterVerify(Record record) {
		verify(record);
	}

	/**
	 * 设置字段值
	 *
	 * @param field
	 * @param value
	 * @param record
	 * @return
	 */
	public static boolean setFieldValue(Field field, String value, Record record) {
		final boolean isNew = record.getPrimary() == null;

		// 忽略更新
		if (!isNew && !field.isUpdatable()) {
			LOG.warn("Could not put value to un-updatable field : " + field);
			return false;
		}

		// 忽略新建
		if (isNew && !field.isCreatable()) {
			LOG.warn("Could not put value to un-creatable field : " + field);
			return false;
		}

		// 无值
		if (value == null || StringUtils.isEmpty(value)) {
			if (isNew && !field.isNullable() && !field.isAutoValue()) {
				throw new FieldValueException("Field [ " + field + " ] must not be null");
			}
		}

		if (value == null || NullValue.is(value)) {
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
	public static void verify(Record record) {
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
			throw new FieldValueException("Muse not been null. Entity [ " + record.getEntity().getName()
					+ " ], Fields [ " + StringUtils.join(notNullable.toArray(new String[0]), ", ") + " ]");
		}
	}

}
