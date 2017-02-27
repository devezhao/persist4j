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
	private boolean updatable;
	private boolean autoValue;
	
	private int decimalScale = FieldType.DEFAULT_DECIMAL_SCALE;
	private Object defaultValue;

	/**
	 * @param name
	 * @param physicalName
	 * @param description
	 * @param ownEntity
	 * @param type
	 * @param cascade
	 * @param maxLength
	 * @param nullable
	 * @param updatable
	 * @param scale
	 * @param defaultValue
	 * @param autoValue
	 */
	public FieldImpl(String name, String physicalName,
			String description, Entity ownEntity, Type type, CascadeModel cascade, int maxLength, boolean nullable, boolean updatable,
			int scale, Object defaultValue, boolean autoValue) {
		super(name, physicalName, description);

		this.ownEntity = ownEntity;
		this.type = type;
		this.maxLength = maxLength;
		this.cascadeModel = cascade;
		
		this.nullable = nullable;
		this.updatable = updatable;
		
		this.decimalScale = scale;
		this.defaultValue = defaultValue;
		this.autoValue = autoValue;
	}
	
	public Entity[] getReferenceEntities() {
		if (referenceSet.isEmpty())
			return EntityImpl.EMPTY_ENTITY_ARRAY;
		return referenceSet.toArray(new Entity[referenceSet.size()]);
	}
	
	public Entity getOwnEntity() {
		return ownEntity;
	}

	public Type getType() {
		return type;
	}
	
	public CascadeModel getCascadeModel() {
		return cascadeModel;
	}

	public int getMaxLength() {
		return maxLength;
	}
	
	public boolean isNullable() {
		return nullable;
	}

	public boolean isUpdatable() {
		return updatable;
	}
	
	public boolean isAutoValue() {
		return autoValue;
	}
	
	/**
	 * @see DoubleEditor
	 * @see DecimalEditor
	 */
	public int getDecimalScale() {
		return decimalScale;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public int hashCode() {
		return (ownEntity.getEntityCode() + getName()).hashCode() >> 13;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof FieldImpl))
			return false;
		return ((FieldImpl) obj).hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return new StringBuilder().append("<").append(
				ownEntity.getName()).append("#").append(getName()).append(
				"(").append(getType()).append(",").append(getMaxLength()).append(")>").toString();
		
//		'<' + ownEntity.getName() + '#' + getName() + ", "
//				+ getType().getMask() + '>';
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		FieldImpl clone = (FieldImpl) super.clone();
		clone.referenceSet = new HashSet<Entity>(this.referenceSet);
		return clone;
	}
	
	protected void addReference(Entity entity) {
		referenceSet.add(entity);
		((EntityImpl) entity).addReferenceTo(this);
	}
}
