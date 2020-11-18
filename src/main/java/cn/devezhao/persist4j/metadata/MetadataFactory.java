package cn.devezhao.persist4j.metadata;

import cn.devezhao.persist4j.Entity;

import java.io.Serializable;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 2, 2009
 * @version $Id: MetadataFactory.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public interface MetadataFactory extends Serializable {
	
	Entity getEntity(String aName);
	
	Entity getEntity(int aType);
	
	boolean containsEntity(int aType);
	
	Entity[] getEntities();
}
