package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.Record;

import java.io.Serializable;
import java.util.List;

/**
 * 查询结果集
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: Result.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public interface Result extends Serializable {

	Object[] unique();
	
	Record record();
	
	List<Record> list();
	
	Object[][] array();
	
	Result reset();
}
