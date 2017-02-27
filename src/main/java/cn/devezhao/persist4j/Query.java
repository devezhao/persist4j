package cn.devezhao.persist4j;

import cn.devezhao.persist4j.query.IQuery;
import cn.devezhao.persist4j.query.Result;
import cn.devezhao.persist4j.query.compiler.SelectItem;

/**
 * 查询
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 2, 2009
 * @version $Id: Query.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public interface Query extends IQuery<Query>, Result {

	// ---------------------------------------------------
	
	Query setParameter(String name, Object value);

	Query setFilter(Filter filter);
	
	Entity getRootEntity();

	SelectItem[] getSelectItems();

	Result result();
}
