package cn.devezhao.persist4j.metadata.impl;

import java.util.HashSet;
import java.util.Set;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.dialect.editor.DecimalEditor;
import cn.devezhao.persist4j.dialect.editor.DoubleEditor;
import cn.devezhao.persist4j.metadata.BaseMetaObject;
import cn.devezhao.persist4j.metadata.CascadeModel;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: FieldImpl.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class FieldImpl extends BaseMetaObject implements Field, Cloneable {
	private static final long serialVersionUID = 1702329731097027085L;

	private Entity ownEntity;
	private Type type;
	private int maxLength;
	private CascadeModel cascadeModel;

	private Set<Entity> referenceSet = new HashSet<Entity>();

	private boolean nullable;
	private boolean creatable;
	private boolean updatable;
	private boolean repeatable;
	private boolean autoValue;

	private int decimalScale = FieldType.DEFAULT_DECIMAL_SCALE;
	private Object defaultValue;

	public FieldImpl(String name, String physicalName, String description, Entity ownEntity, Type type,
			CascadeModel cascade, int maxLength, boolean nullable, boolean creatable, boolean updatable,
			boolean repeatable, int scale, Object defaultValue, boolean autoValue) {
		this(name, physicalName, description, ownEntity, type, cascade, maxLength, nullable, creatable, updatable,
				repeatable, scale, defaultValue, autoValue, null);
	}

	public FieldImpl(String name, String physicalName, String description, Entity ownEntity, Type type,
			CascadeModel cascade, int maxLength, boolean nullable, boolean creatable, boolean updatable,
			boolean repeatable, int scale, Object defaultValue, boolean autoValue, String extraAttrs) {
		super(name, physicalName, description, extraAttrs);

		this.ownEntity = ownEntity;
		this.type = type;
		this.maxLength = maxLength;
		this.cascadeModel = cascade;

		this.nullable = nullable;
		this.creatable = creatable;
		this.updatable = updatable;

		this.decimalScale = scale;
		this.defaultValue = defaultValue;
		this.autoValue = autoValue;
	}

	@Override
	public Entity[] getReferenceEntities() {
		if (referenceSet.isEmpty()) {
			return EntityImpl.EMPTY_ENTITY_ARRAY;
		}
		return referenceSet.toArray(new Entity[referenceSet.size()]);
	}

	@Override
	public Entity getReferenceEntity() {
		Entity[] e = getReferenceEntities();
		return e.length == 0 ? null : e[0];
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
	public CascadeModel getCascadeModel() {
		return cascadeModel;
	}

	@Override
	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}

	@Override
	public boolean isCreatable() {
		return creatable;
	}

	@Override
	public boolean isUpdatable() {
		return updatable;
	}

	@Override
	public boolean isRepeatable() {
		return repeatable;
	}

	@Override
	public boolean isAutoValue() {
		return autoValue;
	}

	/**
	 * @see DoubleEditor
	 * @see DecimalEditor
	 */
	@Override
	public int getDecimalScale() {
		return decimalScale;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
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
		return ((FieldImpl) obj).hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder().append("<").append(
				ownEntity.getName()).append("#").append(getName()).append(
				"(").append(getType()).append(",").append(getMaxLength()).append(")>@").append(super.toString()).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		FieldImpl clone = (FieldImpl) super.clone();
		clone.referenceSet = new HashSet<Entity>(this.referenceSet);
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
