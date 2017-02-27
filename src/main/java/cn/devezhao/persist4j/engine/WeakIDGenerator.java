package cn.devezhao.persist4j.engine;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 弱ID生成器，只有20位长度
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-8-11
 * @version $Id: WeakIDGenerator.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class WeakIDGenerator extends IDGenerator {

	/**
	 * ENTITY_CODE - MO_TIME LO_TIME COUNT
	 */
	@Override
	public Serializable generate(Integer entityCode) {
		return new StringBuilder(getLength()).append(
				StringUtils.leftPad(entityCode + StringUtils.EMPTY, 3, '0')).append(SEP).append(
				format(getMoTime())).append(format(getLoTime())).append(format(getCount()))
				.toString();
	}
	
	@Override
	public int getLength() {
		return 20;
	}
}
