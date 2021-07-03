package cn.devezhao.persist4j.dialect.function;

import cn.devezhao.persist4j.dialect.Type;

/**
 * SQL 函数
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version 0.1, Apr 23, 2009
 */
public interface SqlFunction {

	/**
	 * 函数名（不区分大小写）
	 *
	 * @return
	 */
	String getToken();

	/**
	 * 返回类型（兼容判断）
	 *
	 * @return
	 */
	Type getReturnType();

	/**
	 * 解析函数成为 SQL
	 *
	 * @param arguments
	 * @return
	 */
	String eval(Object[] arguments);
}
