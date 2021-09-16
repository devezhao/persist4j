package cn.devezhao.persist4j.engine;

import cn.devezhao.commons.identifier.UUIDHexGenerator;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * ID生成器
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/13/08
 * @version $Id: IDGenerator.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class IDGenerator extends UUIDHexGenerator {

	static char SEP = '-';

	public IDGenerator() {
		super(SEP);
	}

	/**
	 * ENTITY_CODE - IP - JVM - MO_TIME - LO_TIME - COUNT
	 * 
	 * @param entityCode
	 * @return
	 */
	public Serializable generate(Integer entityCode) {
		return new StringBuilder(getLength()).append(
				StringUtils.leftPad(entityCode + "", 3, '0')).append(SEP)
				.append(super.generate());
	}
	
	@Override
	public Serializable generate() {
		return generate(0);
	}
	
	@Override
	public int getLength() {
		return 40;
	}
}
