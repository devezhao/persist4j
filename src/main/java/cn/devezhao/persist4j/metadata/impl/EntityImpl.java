package cn.devezhao.persist4j.metadata.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
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
	
	private Map<String, Field> fieldMap = new CaseInsensitiveMap<>();
	private Set<String> fieldSorted = new LinkedHashSet<>();
	private List<Field> referenceTo = new ArrayList<Field>();
	
	private Entity masterEntity;
	private Entity slaveEntity;
	
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

	@Override
	public boolean containsField(String aName) {
		return fieldMap.containsKey(aName);
	}

	@Override
	public Field getField(String aName) {
		if (!containsField(aName)) {
			throw new MetadataException("No such field [ " + aName + " ] in entity [ " + getName() + " ]");
		}
		return fieldMap.get(aName);
	}

	@Override
	public Field[] getFields() {
		return fieldMap.values().toArray(new Field[fieldMap.size()]);
	}
	
	@Override
	public Field getNameField() {
		if (StringUtils.isBlank(nameFieldName)) {
			nameFieldName = primaryFieldName;
		}
		if (containsField(nameFieldName)) {
			return getField(nameFieldName);
		}
		return null;
	}

	@Override
	public Field getPrimaryField() {
		return getField(primaryFieldName);
	}

	@Override
	public Integer getEntityCode() {
		return typeCode;
	}

	@Override
	public Field[] getReferenceToFields() {
		return getReferenceToFields(false);
	}
	
	@Override
	public Field[] getReferenceToFields(boolean excludeNReference) {
		if (excludeNReference) {
			List<Field> excluded = new ArrayList<Field>();
			for (Field rt : referenceTo) {
				if (rt.getType() == FieldType.REFERENCE) {
					excluded.add(rt);
				}
			}
			return excluded.toArray(new Field[excluded.size()]);
		} else {
			return referenceTo.toArray(new Field[referenceTo.size()]);
		}
	}
	
	@Override
	public Entity getMasterEntity() {
		return masterEntity;
	}
	
	@Override
	public Entity getSlaveEntity() {
		return slaveEntity;
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
		return "<" + getEntityCode() + ':' + getName() + ">@" + super.toString();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		EntityImpl clone = (EntityImpl) super.clone();
		clone.fieldMap = new CaseInsensitiveMap<>(this.fieldMap);
		clone.referenceTo = new ArrayList<Field>(this.referenceTo);
		return clone;
	}

	/**
	 * @param field
	 */
	protected void addField(Field field) {
		fieldMap.put(field.getName(), field);
		fieldSorted.add(field.getName());
		if (field.getType() == FieldType.PRIMARY) {
			primaryFieldName = field.getName();
			if (nameFieldName == null) {
				nameFieldName = field.getName();
			}
		}
	}
	
	/**
	 * @return
	 */
	public Set<String> getFieldSorted() {
		return fieldSorted;
	}
	
	/**
	 * @param field
	 */
	protected void addReferenceTo(Field field) {
		referenceTo.add(field);
	}
	
	/**
	 * @param master
	 */
	protected void setMasterEntity(Entity master) {
		this.masterEntity = master;
		((EntityImpl) master).slaveEntity = this;
	}
}
