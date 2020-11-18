package cn.devezhao.persist4j.query.compiler;

import cn.devezhao.persist4j.Field;

import java.io.Serializable;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: SelectItem.java 120 2016-01-07 11:49:05Z zhaoff@wisecrm.com $
 */
public interface SelectItem extends Serializable {

	Field getField();
	
	int getIndex();
	
	String getName();
	
	String getAlias();
	
	String getFieldPath();
	
	SelectItemType getType();
}
