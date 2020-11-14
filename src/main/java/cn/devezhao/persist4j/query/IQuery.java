package cn.devezhao.persist4j.query;

import java.io.Serializable;

/**
 * 查询根接口
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, May 25, 2009
 * @version $Id: IQuery.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public interface IQuery<T> extends Serializable {

	T setFirstResult(int firstResult);

	int getFirstResult();

	T setMaxResults(int maxResults);

	int getMaxResults();

	T setTimeout(int timeout);

	int getTimeout();
	
	T setSlowLoggerTime(int loggerTime);
	
	int getSlowLoggerTime();

	T setLimit(int limit);
	
	T setLimit(int limit, int offset);

	T setParameter(int position, Object value);

	Object[] unique();

	Object[][] array();
	
	T reset();
}
