package cn.devezhao.persist4j.metadata.impl;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.metadata.BaseMetaObject;
import cn.devezhao.persist4j.metadata.CascadeModel;
import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: FieldImpl.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class FieldImpl extends BaseMetaObject implements Field, Cloneable {
	private static final long serialVersionUID = 1702329731097027085L;

	final private static Entity[] EMPTY_ENTITY_ARRAY = new Entity[0];

	final private Entity ownEntity;
	final private Type type;
	final private int maxLength;
	final private CascadeModel cascadeModel;
	final private boolean nullable;
	final private boolean repeatable;
	final private boolean autoValue;
	final private int decimalScale;
	final private Object defaultValue;

	private Set<Entity> referenceSet = new HashSet<>();

	public FieldImpl(String name, String physicalName, String description, JSONObject extraAttrs,
					 boolean creatable, boolean updatable, boolean queryable,
					 Entity ownEntity, Type type, int maxLength, CascadeModel cascadeModel,
					 boolean nullable, boolean repeatable, boolean autoValue, int decimalScale, Object defaultValue) {
		super(name, physicalName, description, extraAttrs, creatable, updatable, queryable);
		this.ownEntity = ownEntity;
		this.type = type;
		this.maxLength = maxLength;
		this.cascadeModel = cascadeModel;
		this.nullable = nullable;
		this.repeatable = repeatable;
		this.autoValue = autoValue;
		this.decimalScale = decimalScale;
		this.defaultValue = defaultValue;
	}

	protected FieldImpl(Field c, Entity ownEntity) {
		this(c.getName(), c.getPhysicalName(), c.getDescription(), c.getExtraAttrs(),
				c.isCreatable(), c.isUpdatable(), c.isQueryable(),
				ownEntity, c.getType(), c.getMaxLength(), c.getCascadeModel(),
				c.isNullable(), c.isRepeatable(), c.isAutoValue(), c.getDecimalScale(), c.getDefaultValue());
	}

	@Override
	public Entity getOwnEntity() {
		return ownEntity;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public CascadeModel getCascadeModel() {
		return cascadeModel;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}

	@Override
	public boolean isRepeatable() {
		return repeatable;
	}

	@Override
	public boolean isAutoValue() {
		return autoValue;
	}

	@Override
	public int getDecimalScale() {
		return decimalScale;
	}


	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public Entity[] getReferenceEntities() {
		if (referenceSet.isEmpty()) {
			return EMPTY_ENTITY_ARRAY;
		}
		return referenceSet.toArray(new Entity[0]);
	}

	@Override
	public Entity getReferenceEntity() {
		Entity[] e = getReferenceEntities();
		return e.length == 0 ? null : e[0];
	}

	@Override
	public int hashCode() {
		return (ownEntity.getName() + "#" + getName()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof FieldImpl)) {
			return false;
		}
		return obj.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return (ownEntity.getName() + "#" + getName()).toUpperCase();
//		return new StringBuilder().append("<").append(
//				ownEntity.getName()).append("#").append(getName()).append(
//				"(").append(getType()).append(",").append(getMaxLength()).append(")>@").append(super.toString()).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		FieldImpl clone = (FieldImpl) super.clone();
		clone.referenceSet = new HashSet<>(this.referenceSet);
		return clone;
	}

	/**
	 * @param entity
	 */
	protected void addReference(Entity entity) {
		referenceSet.add(entity);
		((EntityImpl) entity).addReferenceTo(this);
	}
}
