package cn.devezhao.persist4j.metadata.impl;

import cn.devezhao.commons.ByteUtils;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.metadata.BaseMetaObject;
import cn.devezhao.persist4j.metadata.MissingMetaExcetion;
import cn.devezhao.persist4j.util.CaseInsensitiveMap;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: EntityImpl.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class EntityImpl extends BaseMetaObject implements Entity, Cloneable {
	private static final long serialVersionUID = 4949038246043880357L;

	final private int typeCode;
	private String nameFieldName;
	final private boolean deletable;

	private String primaryFieldName;

	private Map<String, Field> fieldMap = new CaseInsensitiveMap<>();
	private Set<String> fieldSorted = new LinkedHashSet<>();
	private List<Field> referenceTo = new ArrayList<>();

	// for Detail
	private Entity mainEntity;
	// for Main
	private List<Entity> detailEntities = new ArrayList<>();

	public EntityImpl(String name, String physicalName, String description, JSONObject extraAttrs,
					  boolean creatable, boolean updatable, boolean queryable,
					  int typeCode, String nameFieldName, boolean deletable) {
		super(name, physicalName, description, extraAttrs, creatable, updatable, queryable);
		this.typeCode = typeCode;
		this.nameFieldName = nameFieldName;
		this.deletable = deletable;
	}

	@Override
	public boolean containsField(String aName) {
		return fieldMap.containsKey(aName);
	}

	@Override
	public Field getField(String aName) {
		if (!containsField(aName)) {
			throw new MissingMetaExcetion(aName, getName());
		}
		return fieldMap.get(aName);
	}

	@Override
	public Field[] getFields() {
		return fieldMap.values().toArray(new Field[0]);
	}
	
	@Override
	public Field getNameField() {
		if (StringUtils.isBlank(nameFieldName)) {
			nameFieldName = primaryFieldName;
		}
		if (containsField(nameFieldName)) {
			return getField(nameFieldName);
		} else {
			return getPrimaryField();
		}
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
		return getReferenceToFields(Boolean.FALSE, Boolean.FALSE);
	}
	
	@Override
	public Field[] getReferenceToFields(boolean excludeNReference, boolean excludeAnyReference) {
		List<Field> refs = new ArrayList<>();
		for (Field field : referenceTo) {
			if (field.getType() == FieldType.REFERENCE) {
				refs.add(field);
			}
			if (!excludeNReference && field.getType() == FieldType.REFERENCE_LIST) {
				refs.add(field);
			}
			if (!excludeAnyReference && field.getType() == FieldType.ANY_REFERENCE) {
				refs.add(field);
			}
		}

		return refs.toArray(new Field[0]);
	}
	
	@Override
	public Entity getMainEntity() {
		return mainEntity;
	}
	
	@Override
	public Entity getDetailEntity() {
		if (detailEntities.isEmpty()) return null;
		return getDetialEntities()[0];
	}

	@Override
	public Entity[] getDetialEntities() {
		if (detailEntities.isEmpty()) return new Entity[0];
		return detailEntities.toArray(new Entity[0]);
	}

	@Override
	public String[] getFieldNames() {
		return fieldMap.keySet().toArray(new String[0]);
	}

	@Override
	public boolean isDeletable() {
		return deletable;
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
		return obj.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return "<" + getEntityCode() + ':' + getName() + ">@" + super.toString();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		EntityImpl clone = (EntityImpl) super.clone();
		clone.fieldMap = new CaseInsensitiveMap<>(this.fieldMap);
		clone.fieldSorted = new LinkedHashSet<>(this.fieldSorted);
		clone.referenceTo = new ArrayList<>(this.referenceTo);
		clone.mainEntity = null;
		clone.detailEntities = new ArrayList<>();
		return clone;
	}

	/**
	 * 获取按顺序排列的字段
	 *
	 * @return
	 */
	public Set<String> getFieldSorted() {
		return new LinkedHashSet<>(fieldSorted);
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
	 * @param field
	 */
	protected void addReferenceTo(Field field) {
		referenceTo.add(field);
	}

	/**
	 * @param mainEntity
	 */
	protected void setMainEntity(Entity mainEntity) {
		Assert.isNull(this.mainEntity, "Cannot reset `mainEntity`");
		this.mainEntity = mainEntity;
		if (!BooleanUtils.toBoolean(System.getProperty("persist4j.detailEntities"))) {
			((EntityImpl) mainEntity).detailEntities.clear();
		}
		((EntityImpl) mainEntity).detailEntities.add(this);

		// MUST! sort by code
		((EntityImpl) mainEntity).detailEntities.sort((o1, o2) -> o2.getEntityCode().compareTo(o1.getEntityCode()));
	}
}
