package cn.devezhao.persist4j.metadata;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 20, 2009
 * @version $Id: BaseMetaObject.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public abstract class BaseMetaObject implements BaseMeta {
	private static final long serialVersionUID = -439042640413431557L;
	
	final private String name;
	final private String physicalName;
	final private String description;

	public BaseMetaObject(String name, String physicalName, String description) {
		this.name = name;
		this.physicalName = physicalName;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getPhysicalName() {
		return physicalName;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return String.format("<%s, %s>", name, physicalName);
	}
}
