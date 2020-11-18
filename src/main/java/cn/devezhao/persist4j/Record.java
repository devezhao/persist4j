package cn.devezhao.persist4j;

import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.NullValue;
import com.alibaba.fastjson.JSON;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 记录
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: Record.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public interface Record extends Cloneable, Serializable {

	/**
	 * 实体
	 * 
	 * @return
	 */
	Entity getEntity();

	/**
	 * 编辑用户
	 * 
	 * @return
	 */
	ID getEditor();

	/**
	 * 主键
	 * 
	 * @return
	 */
	ID getPrimary();

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setID(String key, ID value);

	/**
	 * @param key
	 * @return
	 */
	ID getID(String key);

	/**
	 * @param key
	 * @param values
	 * @return
	 */
	Record setIDArray(String key, ID[] values);

	/**
	 * @param key
	 * @return
	 */
	ID[] getIDArray(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setChar(String key, Character value);

	/**
	 * @param key
	 * @return
	 */
	Character getChar(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setString(String key, String value);

	/**
	 * @param key
	 * @return
	 */
	String getString(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setInt(String key, Integer value);

	/**
	 * @param key
	 * @return
	 */
	Integer getInt(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setDouble(String key, Double value);

	/**
	 * @param key
	 * @return
	 */
	Double getDouble(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setDecimal(String key, BigDecimal value);

	/**
	 * @param key
	 * @return
	 */
	BigDecimal getDecimal(String key);
	
	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setLong(String key, Long value);
	
	/**
	 * @param key
	 * @return
	 */
	Long getLong(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setDate(String key, Date value);

	/**
	 * @param key
	 * @return
	 */
	Date getDate(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setBoolean(String key, Boolean value);

	/**
	 * @param key
	 * @return
	 */
	Boolean getBoolean(String key);

	/**
	 * @param key
	 * @param stream
	 * @return
	 */
	Record setBinary(String key, InputStream stream);

	/**
	 * @param key
	 * @return
	 */
	InputStream getBinary(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setReader(String key, Reader value);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setReader(String key, String value);

	/**
	 * @param key
	 * @return
	 */
	Reader getReader(String key);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	Record setObjectValue(String key, Object value);

	/**
	 * @param key
	 * @return
	 */
	Object getObjectValue(String key);

	/**
	 * @param key
	 * @return
	 */
	boolean hasValue(String key);
	
	/**
	 * @param key
	 * @param includeNullValue {@link NullValue}
	 * @return
	 * @see #setNull(String)
	 */
	boolean hasValue(String key, boolean includeNullValue);
	
	/**
	 * @param key
	 * @return
	 */
	Object removeValue(String key);
	
	/**
	 * @param key
	 * @return
	 */
	Object setNull(String key);

	/**
	 * @return
	 * @see #getAvailableFields()
	 */
	Iterator<String> getAvailableFieldIterator();
	
	/**
	 * @return
	 */
	Set<String> getAvailableFields();

	/**
	 * @return Anothers Record
	 */
	Record clone();
	
	/**
	 * @return JSON K/V
	 */
	JSON serialize();
}
