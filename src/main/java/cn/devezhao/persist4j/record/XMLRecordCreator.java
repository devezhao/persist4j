package cn.devezhao.persist4j.record;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.StandardRecord;

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
	 * @param editor
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

	public Record create() {
		Record record = new StandardRecord(entity, editor);
		boolean isNew = true;
		
		String id = source.valueOf("@id");
		if (!StringUtils.isBlank(id)) {
			record.setID(entity.getPrimaryField().getName(), ID.valueOf(id));
			isNew = false;
		}

		for (Object o : source.elements()) {
			Element ele = (Element) o;
			String fn = ele.getName();
			Field field = entity.getField(fn);
			
			if (field == null) {
				LOG.warn("Unable found field [ " + entity.getName() + '#' + fn  + " ], will ignore");
				continue;
			}
			
			if (isNew == false && !field.isUpdatable()) {  // un update
				if (LOG.isDebugEnabled()) {
					LOG.warn("could not put value to un-update field");
				}
				continue;
			}
			
			String value = ele.attributeValue("value");
			if (value == null) {
				value = ele.getText();
			}

			if (StringUtils.isBlank(value)) {
				value = null;
				if (isNew) {
					if (!field.isNullable() && !field.isAutoValue()) {
						throw new FieldValueException(entity.getName() + '#' + field.getName() + " must not be null");
					}
					continue;
				}
			}
			
			setValue(field, value, record);
		}
		
		afterCreate(record, isNew);
		validate(record, isNew);
		return record;
	}
	
	protected void setValue(Field field, String value, Record record) {
		if (value == null) {
			record.setNull(field.getName());
			return;
		}
		RecordVisitor.setValueByLiteral(field, value, record);
	}
	
	protected void afterCreate(Record record, boolean isNew) {
	}
	
	protected void validate(Record record, boolean isNew) {
		if (!isNew) {
			return;
		}
		
		List<String> notNulls = new ArrayList<String>();
		for (Field field : entity.getFields()) {
			if (FieldType.PRIMARY.equals(field.getType())) {
				continue;
			}
			
			Object val = record.getObjectValue(field.getName());
			if (!field.isNullable() && !field.isAutoValue() && val == null) {
				notNulls.add(field.getName());
			}
		}
		
		if (notNulls.isEmpty()) {
			return;
		}
		throw new FieldValueException("Muse not been null. Entity [ " + entity.getName() 
				+ " ], Fields [ " + StringUtils.join(notNulls.toArray(new String[notNulls.size()]), ", ") + " ]");
	}
}
