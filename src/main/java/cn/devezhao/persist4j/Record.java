package cn.devezhao.persist4j;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import cn.devezhao.persist4j.engine.ID;

/**
 * 记录
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: Record.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public interface Record extends Cloneable, Serializable {

	Entity getEntity();

	ID getEditor();

	ID getPrimary();

	Record setID(String key, ID value);

	ID getID(String key);

	Record setIDArray(String key, ID[] values);

	ID[] getIDArray(String key);

	Record setChar(String key, Character value);

	Character getChar(String key);

	Record setString(String key, String value);

	String getString(String key);

	Record setInt(String key, Integer value);

	Integer getInt(String key);

	Record setDouble(String key, Double value);

	Double getDouble(String key);

	Record setDecimal(String key, BigDecimal value);

	BigDecimal getDecimal(String key);
	
	Record setLong(String key, Long value);
	
	Long getLong(String key);

	Record setDate(String key, Date value);

	Date getDate(String key);

	Record setBoolean(String key, Boolean value);

	Boolean getBoolean(String key);

	Record setBinary(String key, InputStream stream);

	InputStream getBinary(String key);

	Record setReader(String key, Reader value);

	Record setReader(String key, String value);

	Reader getReader(String key);

	Record setObjectValue(String key, Object value);

	Object getObjectValue(String key);

	boolean hasValue(String key);
	
	Object removeValue(String key);
	
	Object setNull(String key);

	Iterator<String> getAvailableFieldIterator();

	Record clone();
}
