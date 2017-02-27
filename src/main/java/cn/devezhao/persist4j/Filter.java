package cn.devezhao.persist4j;

import java.io.Serializable;

/**
 * 查询过滤器
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 2, 2009
 * @version $Id: Filter.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $ 
 */
public interface Filter extends Serializable {
	
	String evaluate(Entity entity);
	
}
