package cn.devezhao.persist4j.metadata.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.commons.ByteUtils;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.metadata.BaseMetaObject;
import cn.devezhao.persist4j.metadata.MetadataException;

/**
 * 实体
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: EntityImpl.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class EntityImpl extends BaseMetaObject implements Entity, Cloneable {
	private static final long serialVersionUID = 4949038246043880357L;
	
	final static Entity[] EMPTY_ENTITY_ARRAY = new Entity[0];
	final static Field[] EMPTY_FIELD_ARRAY = new Field[0];
	
	private int typeCode;
	private String primaryFieldName;
	private String nameFieldName;
	
	private Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();
	private List<Field> referenceTo = new ArrayList<Field>();
	
	/**
	 * @param name
	 * @param physicalName
	 * @param description
	 * @param typeCode
	 * @param nameField
	 */
	public EntityImpl(String name, String physicalName,
			String description, int typeCode, String nameField) {
		super(name, physicalName, description);

		this.typeCode = typeCode;
		this.nameFieldName = nameField;
	}

	public boolean containsField(String aName) {
		return fieldMap.containsKey(aName);
	}

	public Field getField(String aName) {
		if (!containsField(aName)) {
			throw new MetadataException("No such field [ " + aName + " ] in entity [ " + getName() + " ]");
		}
		return fieldMap.get(aName);
	}

	public Field[] getFields() {
		return fieldMap.values().toArray(new Field[fieldMap.size()]);
	}

	public Field getNameField() {
		if (StringUtils.isBlank(nameFieldName)) {
			nameFieldName = primaryFieldName;
		}
		
		return getField(nameFieldName);
	}

	public Field getPrimaryField() {
		return getField(primaryFieldName);
	}

	public Integer getEntityCode() {
		return typeCode;
	}

	public Field[] getReferenceToFields() {
		if (referenceTo.isEmpty()) {
			return EMPTY_FIELD_ARRAY;
		}
		return referenceTo.toArray(new Field[referenceTo.size()]);
	}

	@Override
	public int hashCode() {
		return ByteUtils.hash(getEntityCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EntityImpl)) {
			return false;
		}
		return ((EntityImpl) obj).hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return "<" + getEntityCode() + ':' + getName() + '>';
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		EntityImpl clone = (EntityImpl) super.clone();
		clone.fieldMap = new HashMap<String, Field>(this.fieldMap);
		clone.referenceTo = new ArrayList<Field>(this.referenceTo);
		return clone;
	}

	protected void addField(Field field) {
		fieldMap.put(field.getName(), field);
		
		if (field.getType() == FieldType.PRIMARY) {
			primaryFieldName = field.getName();
			if (nameFieldName == null) {
				nameFieldName = field.getName();
			}
		}
	}
	
	protected void addReferenceTo(Field field) {
		referenceTo.add(field);
	}
}
