package cn.devezhao.persist4j.metadata;

/**
 * 元数据基础
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 20, 2009
 * @version $Id: BaseMetaObject.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public abstract class BaseMetaObject implements BaseMeta {
	private static final long serialVersionUID = -439042640413431557L;
	
	final private String name;
	final private String physicalName;
	private String description;
	private String extraAttrs;

	/**
	 * @param name
	 * @param physicalName
	 * @param description
	 */
	public BaseMetaObject(String name, String physicalName, String description, String extraAttrs) {
		this.name = name;
		this.physicalName = physicalName;
		this.description = description;
		this.extraAttrs = extraAttrs;
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
	public String getExtraAttrs() {
		return extraAttrs;
	}

	@Override
	public String toString() {
		return String.format("<%s,%s>@%s", name, physicalName, Integer.toHexString(super.hashCode()));
	}
}
