package cn.devezhao.persist4j.metadata;

import java.io.Serializable;

/**
 * Base meta object
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 20, 2009
 * @version $Id: BaseMeta.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $ 
 */
public interface BaseMeta extends Serializable {

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 获取物理名称
	 * 
	 * @return
	 */
	String getPhysicalName();

	/**
	 * 获取描述
	 * 
	 * @return
	 */
	String getDescription();
}
