package cn.devezhao.persist4j;

import java.io.Serializable;

import cn.devezhao.persist4j.engine.ID;

/**
 * 持久化管理器
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: PersistManager.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public interface PersistManager extends Serializable {

	/**
	 * 获取持久化管理器工场
	 * 
	 * @return
	 */
	PersistManagerFactory getPersistManagerFactory();
	
	/**
	 * 保存记录
	 * 
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	Record save(Record record) throws DataAccessException;
	
	/**
	 * 更新记录
	 * 
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	Record update(Record record) throws DataAccessException;
	
	/**
	 * 保存或更新记录（根据主键值）
	 * 
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	Record saveOrUpdate(Record record) throws DataAccessException;
	
	/**
	 * 删除记录（包含级联删除）
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	int delete(ID id) throws DataAccessException;
	
	/**
	 * 删除多调记录（包含级联删除）
	 * 
	 * @param ids
	 * @return
	 * @throws DataAccessException
	 */
	int[] delete(ID[] ids) throws DataAccessException;
}
