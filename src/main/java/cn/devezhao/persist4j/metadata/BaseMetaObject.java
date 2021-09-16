package cn.devezhao.persist4j.metadata;

import com.alibaba.fastjson.JSONObject;

/**
 * 元数据基础
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 20, 2009
 * @version $Id: BaseMetaObject.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public abstract class BaseMetaObject implements BaseMeta {
	private static final long serialVersionUID = -439042640413431557L;
	
	final private String name;
	final private String physicalName;
	final private String description;
	final private JSONObject extraAttrs;

	final private boolean creatable;
	final private boolean updatable;
	final private boolean queryable;

	/**
	 * @param name
	 * @param physicalName
	 * @param description
	 * @param extraAttrs
	 * @param creatable
	 * @param updatable
	 * @param queryable
	 */
	public BaseMetaObject(String name, String physicalName, String description, JSONObject extraAttrs,
						  boolean creatable, boolean updatable, boolean queryable) {
		this.name = name;
		this.physicalName = physicalName;
		this.description = description;
		this.extraAttrs = extraAttrs;
		this.creatable = creatable;
		this.updatable = updatable;
		this.queryable = queryable;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPhysicalName() {
		return physicalName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public JSONObject getExtraAttrs() {
		return extraAttrs == null ? null : (JSONObject) extraAttrs.clone();
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
	public boolean isQueryable() {
		return queryable;
	}

	@Override
	public String toString() {
		return String.format("<%s,%s>@%s", name, physicalName, Integer.toHexString(super.hashCode()));
	}
}
