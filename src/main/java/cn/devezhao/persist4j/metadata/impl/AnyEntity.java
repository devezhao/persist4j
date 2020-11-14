package cn.devezhao.persist4j.metadata.impl;

/**
 * 任意引用字段指向的实体
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 2010-6-28
 * @version $Id: AnyEntity.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public final class AnyEntity extends EntityImpl {
	private static final long serialVersionUID = 1666508270744579952L;

	/**
	 * 任意引用标识
	 */
	public static final String FLAG = "*";

	protected AnyEntity() {
		super(null, null, null, null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
				0, null, Boolean.TRUE);
	}
}