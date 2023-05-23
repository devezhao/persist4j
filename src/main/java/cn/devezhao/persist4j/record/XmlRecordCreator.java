package cn.devezhao.persist4j.record;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.StandardRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: XMLRecordCreator.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
@Deprecated
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
		return record;
	}
	
	protected void afterCreate(Record record) {
		verify(record);
	}
}
