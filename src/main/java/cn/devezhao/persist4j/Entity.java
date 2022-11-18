package cn.devezhao.persist4j;

import cn.devezhao.persist4j.metadata.BaseMeta;

/**
 * 实体
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: Entity.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public interface Entity extends BaseMeta {
	
	/**
	 * 获取实体编号
	 * 
	 * @return
	 */
	Integer getEntityCode();
	
	/**
	 * 获取主键字段
	 * 
	 * @return
	 */
	Field getPrimaryField();
	
	/**
	 * 获取名称字段
	 * 
	 * @return
	 */
	Field getNameField();
	
	/**
	 * 获取字段
	 * 
	 * @param aName
	 * @return
	 */
	Field getField(String aName);
	
	/**
	 * 是否包含指定名称字段
	 * 
	 * @param aName
	 * @return
	 */
	boolean containsField(String aName);
	
	/**
	 * 获取全部字段
	 * 
	 * @return
	 */
	Field[] getFields();
	
	/**
	 * 获取引用到此实体的所有字段
	 * 
	 * @return
	 * @see #getReferenceToFields(boolean, boolean)
	 */
	Field[] getReferenceToFields();
	
	/**
	 * 获取引用到此实体的所有字段
	 * 
	 * @param excludeNReference 是否排除多引用
	 * @param excludeAnyReference 是否排除任意引用
	 * @return
	 */
	Field[] getReferenceToFields(boolean excludeNReference, boolean excludeAnyReference);
	
	/**
	 * 获取主实体（如有）
	 * 
	 * @return
	 */
	Entity getMainEntity();
	
	/**
	 * 获取明细实体（如有）
	 * 
	 * @return
	 */
	Entity getDetailEntity();
	
	/**
	 * 获取字段名称
	 * 
	 * @return
	 * @see #getFields()
	 */
	String[] getFieldNames();

	/**
	 * 是否可删除
	 *
	 * @return
	 */
	boolean isDeletable();
}
