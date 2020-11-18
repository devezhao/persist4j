package cn.devezhao.persist4j.dialect.h2;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;

import java.sql.Types;

/**
 * H2 with MySQL
 * 
 * @author devezhao zhaofang123@gmail.com
 * @since 2019年11月28日
 */
public class H2Dialect extends MySQL5Dialect {
	private static final long serialVersionUID = 1212960786237010687L;

	public H2Dialect() {
		super();
		registerColumnType(FieldType.DOUBLE.getMask(), "double(19)", Types.DOUBLE);
	}
	
	@Override
	public String getDialectName() {
		return "H2";
	}
	
	@Override
	public boolean supportsFullText() {
		return Boolean.FALSE;
	}
}
