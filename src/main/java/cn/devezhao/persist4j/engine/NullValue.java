package cn.devezhao.persist4j.engine;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 空值
 * 
 * @author zhaofang123@gmail.com
 * @since 12/28/2018
 */
public class NullValue implements Serializable {
	private static final long serialVersionUID = -6198308278862795848L;
	
	/**
	 * @param value
	 * @return
	 */
	public static boolean is(Object value) {
		return value instanceof NullValue;
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isNull(Object value) {
		return value == null || is(value);
	}

	@Override
	public String toString() {
		return StringUtils.EMPTY;
	}
}
