package cn.devezhao.persist4j.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

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
import cn.devezhao.persist4j.record.FieldValueException;
import cn.devezhao.persist4j.util.CaseInsensitiveMap;

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
	 * for Serializable
	 */
	protected StandardRecord() {
		this.entity = null;
		this.editor = null;
	}
	
	/**
	 * @param entity
	 */
	public StandardRecord(Entity entity) {
		this(entity, null);
	}
	
	/**
	 * @param entity
	 * @param editor
	 */
	public StandardRecord(Entity entity, ID editor) {
		Validate.notNull(entity, "'entity' must not be null");
		this.entity = entity;
		this.editor = editor;
	}
	
	@Override
	public Entity getEntity() {
		return entity;
	}
	
	@Override
	public ID getEditor() {
		return editor;
	}
	
	@Override
	public ID getPrimary() {
		return getID(entity.getPrimaryField().getName());
	}
	
	@Override
	public ID getID(String key) {
		return (ID) getObject(key, ID.class);
	}
	
	@Override
	public Record setID(String key, ID value) {
		if (entity.getPrimaryField().getName().equalsIgnoreCase(key) && getPrimary() != null) {
			throw new IllegalStateException(
					"Primary field value already exists, can not be re-settings");
		}
		
		setObject(key, value);
		return this;
	}
	
	@Override
	public ID[] getIDArray(String key) {
		Object v = recordMap.get(key);
		if (v == null) {
			return EMPTY_ID_ARRAY;
		}
		return (ID[]) v;
	}
	
	@Override
	public Record setIDArray(String key, ID[] value) {
		setObject(key, value);
		return this;
	}
	
	@Override
	public Character getChar(String key) {
		return (Character) getObject(key, Character.class);
	}
	
	@Override
	public Record setChar(String key, Character value) {
		setObject(key, value);
		return this;
	}
	
	@Override
	public String getString(String key) {
		return (String) getObject(key, String.class);
	}
	
	@Override
	public Record setString(String key, String value) {
		setObject(key, value);
		return this;
	}
	
	@Override
	public Integer getInt(String key) {
		return (Integer) getObject(key, Integer.class);
	}
	
	@Override
	public Record setInt(String key, Integer value) {
		setObject(key, value);
		return this;
	}
	
	@Override
	public Double getDouble(String key) {
		return (Double) getObject(key, Double.class);
	}
	
	@Override
	public Record setDouble(String key, Double value) {
		setObject(key, value);
		return this;
	}
	
	@Override
	public BigDecimal getDecimal(String key) {
		return (BigDecimal) getObject(key, BigDecimal.class);
	}
	
	@Override
	public Record setDecimal(String key, BigDecimal value) {
		setObject(key, value);
		return this;
	}
	
	@Override
	public Long getLong(String key) {
		return (Long) getObject(key, Long.class);
	}
	
	@Override
	public Record setLong(String key, Long value) {
		setObject(key, value);
		return this;
	}

	@Override
	public Date getDate(String key) {
		return (Date) getObject(key, Date.class);
	}

	@Override
	public Record setDate(String key, Date value) {
		setObject(key, value);
		return this;
	}

	@Override
	public Boolean getBoolean(String key) {
		return (Boolean) getObject(key, Boolean.class);
	}

	@Override
	public Record setBoolean(String key, Boolean value) {
		setObject(key, value);
		return this;
	}

	@Override
	public Reader getReader(String key) {
		return null;
	}

	@Override
	public Record setReader(String key, Reader value) {
		if (value == null) {
			return this;
		}
		
		String content = null;
		try {
			content = ByteUtils.read(value);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return setReader(key, content);
	}

	@Override
	public Record setReader(String key, String value) {
		if (value == null) {
			return this;
		}
		setObject(key, value);
		return this;
	}

	@Override
	public InputStream getBinary(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Record setBinary(String key, InputStream value) {
		setObject(key, value);
		return this;
	}

	@Override
	public Object getObjectValue(String key) {
		return recordMap.get(key);
	}

	@Override
	public Record setObjectValue(String key, Object value) {
		if (key == null || value == null) {
			return this;
		}
		recordMap.put(key, value);
		return this;
	}

	@Override
	public boolean hasValue(String key) {
		return hasValue(key, true);
	}
	
	@Override
	public boolean hasValue(String key, boolean includeNullValue) {
		boolean has = recordMap.containsKey(key);
		if (!has) {
			return false;
		}
		return includeNullValue ? true : !NullValue.is(recordMap.get(key));
	}
	
	@Override
	public Object removeValue(String key) {
		return recordMap.remove(key);
	}
	
	@Override
	public Object setNull(String key) {
		Object old = recordMap.get(key);
		recordMap.put(key, new NullValue());
		return old;
	}

	@Override
	public Iterator<String> getAvailableFieldIterator() {
		return recordMap.keySet().iterator();
	}

	@Override
	public Record clone() {
		StandardRecord o = null;
		try {
			o = (StandardRecord) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		o.recordMap = new CaseInsensitiveMap<>(this.recordMap);
		return o;
	}
	
	// -------------------------------------------------------------------------------------

	protected void setObject(String key, Object value) {
		if (!entity.containsField(key)) {
			throw new MetadataException("No such field " + key + " in entity " + entity.getName());
		}
		if (value == null) {
			return;
		}
		
		Field field = entity.getField(key);
		if (field.getType() == FieldType.REFERENCE ||
				field.getType() == FieldType.ANY_REFERENCE || field.getType() == FieldType.REFERENCE_LIST) {
			checkReferenceValue(field, value);
		}

		if (LOG.isDebugEnabled()) {
			if (recordMap.containsKey(key)) {
				LOG.warn("Update field [" + key + "] value. OLD: " + recordMap.get(key) + ", NEW: " + value);
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
		if (v == null || NullValue.is(v)) {
			return null;
		} else if (clazz.isAssignableFrom(v.getClass())) {
			return v;
		}
		
		throw new PersistException(
				"Can't cast field [ " + key + " ] value type " + v.getClass() + " to " + clazz);
	}
	
	protected void checkReferenceValue(Field field, Object value) {
		ID[] idList = null;
		if (value.getClass() == ID[].class) {
			idList = (ID[]) value;
		} else {
			idList = new ID[] { (ID) value };
		}

		Entity[] referenceEntities = field.getReferenceEntities();
		for (ID id : idList) {
			int idType = id.getEntityCode().intValue();
			boolean idInvalid = true;
			for (Entity e : referenceEntities) {
				if (e.getEntityCode().intValue() == idType) {
					idInvalid = false;
					break;
				}
			}
			
			if (idInvalid) {
				throw new FieldValueException("Field " + field + " value [ " + id + " ] is wrong. can't reference non-specify record of entity");
			}
		}
	}
}
