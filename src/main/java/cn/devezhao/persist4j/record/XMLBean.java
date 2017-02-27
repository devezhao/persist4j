package cn.devezhao.persist4j.record;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.engine.ID;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: XMLBean.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class XMLBean {
	
	public XMLBean(Entity entity) {
	}
	
	public XMLBean(Entity entity, ID recordId) {
	}
	
	public XMLBean addValue(String fieldName, String value) {
		return this;
	}
}
