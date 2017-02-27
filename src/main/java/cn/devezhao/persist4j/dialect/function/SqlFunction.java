package cn.devezhao.persist4j.dialect.function;

import cn.devezhao.persist4j.dialect.Type;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version 0.1, Apr 23, 2009
 */
public interface SqlFunction {

	String getToken();
	
	Type getReturnType();

	boolean hasArguments();

	String render(Object[] arguments);
}
