package cn.devezhao.persist4j.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devezhao.commons.ByteUtils;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.PersistException;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.metadata.MetadataException;
import cn.devezhao.persist4j.metadata.impl.AnyEntity;
import cn.devezhao.persist4j.record.FieldValueException;

/**
 * 记录对象
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: StandardRecord.java 20 2009-02-10 03:35:10Z
 *          zhaofang123@gmail.com $
 */
public class StandardRecord implements Record {
	private static final long serialVersionUID = -3714155230415024491L;

	private static final Log LOG = LogFactory.getLog(StandardRecord.class);

	/**
	 * A empty id array
	 */
	public static final ID[] EMPTY_ID_ARRAY = new ID[] {};

	final private Entity entity;
	final private ID editor;

	protected Map<String, Object> recordMap = new CaseInsensitiveMap<>();

	/**
	 * Serializable
	 */
	protected StandardRecord() {
		this.entity = null;
		this.editor = null;
	}
	
	/**
	 * Create a new StandardRecordImpl
	 * 
	 * @param entity
	 */
	public StandardRecord(Entity entity) {
		this(entity, null);
	}
	
	/**
	 * Create a new StandardRecordImpl
	 * 
	 * @param entity
	 * @param editor
	 */
	public StandardRecord(Entity entity, ID editor) {
		Validate.notNull(entity, "entity must not be null");
		this.entity = entity;
		this.editor = editor;
	}

	public Entity getEntity() {
		return entity;
	}

	public ID getEditor() {
		return editor;
	}

	public ID getPrimary() {
		return getID(entity.getPrimaryField().getName());
	}

	public ID getID(String key) {
		return (ID) getObject(key, ID.class);
	}

	public Record setID(String key, ID value) {
		if (entity.getPrimaryField().getName().equalsIgnoreCase(key) && getPrimary() != null) {
			throw new IllegalStateException(
					"primary field value already exists, can not be re-settings");
		}
		
		setObject(key, value);
		return this;
	}

	public ID[] getIDArray(String key) {
		Object v = recordMap.get(key);
		if (v == null) {
			return EMPTY_ID_ARRAY;
		}
		return (ID[]) v;
	}

	public Record setIDArray(String key, ID[] value) {
		setObject(key, value);
		return this;
	}

	public Character getChar(String key) {
		return (Character) getObject(key, Character.class);
	}

	public Record setChar(String key, Character value) {
		setObject(key, value);
		return this;
	}

	public String getString(String key) {
		return (String) getObject(key, String.class);
	}

	public Record setString(String key, String value) {
		setObject(key, value);
		return this;
	}

	public Integer getInt(String key) {
		return (Integer) getObject(key, Integer.class);
	}

	public Record setInt(String key, Integer value) {
		setObject(key, value);
		return this;
	}

	public Double getDouble(String key) {
		return (Double) getObject(key, Double.class);
	}

	public Record setDouble(String key, Double value) {
		setObject(key, value);
		return this;
	}

	public BigDecimal getDecimal(String key) {
		return (BigDecimal) getObject(key, BigDecimal.class);
	}

	public Record setDecimal(String key, BigDecimal value) {
		setObject(key, value);
		return this;
	}
	
	public Long getLong(String key) {
		return (Long) getObject(key, Long.class);
	}
	
	public Record setLong(String key, Long value) {
		setObject(key, value);
		return this;
	}

	public Date getDate(String key) {
		return (Date) getObject(key, Date.class);
	}

	public Record setDate(String key, Date value) {
		setObject(key, value);
		return this;
	}

	public Boolean getBoolean(String key) {
		return (Boolean) getObject(key, Boolean.class);
	}

	public Record setBoolean(String key, Boolean value) {
		setObject(key, value);
		return this;
	}

	public Reader getReader(String key) {
		return null;
	}

	public Record setReader(String key, Reader value) {
		if (value == null)
			return this;
		
		String content = null;
		try {
			content = ByteUtils.read(value);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return setReader(key, content);
	}

	public Record setReader(String key, String value) {
		if (value == null) {
			return this;
		}
		setObject(key, value);
		return this;
	}

	public InputStream getBinary(String key) {
		throw new UnsupportedOperationException();
	}

	public Record setBinary(String key, InputStream value) {
		setObject(key, value);
		return this;
	}

	public Record getRecord(String key) {
		throw new UnsupportedOperationException();
	}

	public Record setRecord(String key, Record value) {
		throw new UnsupportedOperationException();
	}

	public Object getObjectValue(String key) {
		return recordMap.get(key);
	}

	public Record setObjectValue(String key, Object value) {
		if (key == null || value == null)
			return this;
		recordMap.put(key, value);
		return this;
	}

	public boolean hasValue(String key) {
		return recordMap.containsKey(key);
	}
	
	public Object removeValue(String key) {
		return recordMap.remove(key);
	}
	
	public Object setNull(String key) {
		Object old = recordMap.get(key);
		recordMap.put(key, ObjectUtils.NULL);
		return old;
	}

	public Iterator<String> getAvailableFieldIterator() {
		return recordMap.keySet().iterator();
	}

	public Record clone() {
		StandardRecord o = null;
		try {
			o = (StandardRecord) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		o.recordMap = new HashMap<String, Object>(this.recordMap);
		return o;
	}
	
	// -------------------------------------------------------------------------------------

	protected void setObject(String key, Object value) {
		if (!entity.containsField(key))
			throw new MetadataException("No such field " + key + " in entity " + entity.getName());
		if (value == null)
			return;
		
		Field field = entity.getField(key);
		if (field.getType() == FieldType.REFERENCE) {
			checkReferenceValue(field, value);
		}

		if (LOG.isDebugEnabled()) {
			if (recordMap.containsKey(key)) {
				LOG.warn("update field [" + key + "] value. " +
						"OLD: " + recordMap.get(key) + ", NEW: " + value);
			}
		}
		recordMap.put(key, value);
	}

	protected Object getObject(String key, Class<?> clazz) {
		if (!entity.containsField(key)) {
			throw new MetadataException(
					"No such field [ " + key + " ] in entity [ " + entity.getName() + " ]");
		}

		Object v = recordMap.get(key);
		if (v == null) {
			return null;
		}

		if (clazz.isAssignableFrom(v.getClass())) {
			return v;
		}
		if (v == ObjectUtils.NULL) {
			return null;
		}
		throw new PersistException(
				"can't cast field [ " + key + " ] value type " + v.getClass() + " to " + clazz);
	}
	
	protected void checkReferenceValue(Field field, Object value) {
		Entity[] eList = field.getReferenceEntities();
		if (eList[0] instanceof AnyEntity)
			return;
		
		ID[] refIds = null;
		if (value.getClass() == ID[].class) {
			refIds = (ID[]) value;
		} else {
			refIds = new ID[] { (ID) value };
		}

		for (ID refId : refIds) {
			boolean badId = true;
			for (Entity e : eList) {
				if (e.getEntityCode().intValue() == refId.getEntityCode().intValue()) {
					badId = false;
					break;
				}
			}
			
			if (badId) throw new FieldValueException("Field " + field + " value [ " + refId + " ] is wrong. can't reference non-specify record of entity");
		}
	}
}
