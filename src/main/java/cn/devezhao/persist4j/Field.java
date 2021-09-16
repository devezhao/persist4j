package cn.devezhao.persist4j;

import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.metadata.BaseMeta;
import cn.devezhao.persist4j.metadata.CascadeModel;

/**
 * 字段
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: Field.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public interface Field extends BaseMeta {

	/**
	 * 获取字段类型
	 * 
	 * @return
	 */
	Type getType();

	/**
	 * 获取此字段的所属实体
	 * 
	 * @return
	 */
	Entity getOwnEntity();

	/**
	 * 用于多引用字段。
	 * 如果是引用型字段，获取到其引用的实体。如果 `ref-entity=*` 会返回所有实体
	 * 
	 * @return
	 */
	Entity[] getReferenceEntities();
	
	/**
	 * 如果是引用型字段，获取到其引用的实体
	 * 
	 * @return
	 */
	Entity getReferenceEntity();
	
	/**
	 * 如果是引用型字段，获取其级联方式
	 * @return
	 */
	CascadeModel getCascadeModel();
	
	/**
	 * 获取此字段的最大允许长度
	 * 
	 * @return
	 */
	int getMaxLength();
	
	/**
	 * 是否可以为空
	 * 
	 * @return
	 */
	boolean isNullable();
	
	/**
	 * 是否可重复。注意：保留字段，系统不会针对此属性做处理
	 * 
	 * @return
	 */
	boolean isRepeatable();

	/**
	 * 是否为数据库自动填充值
	 * 
	 * @return
	 */
	boolean isAutoValue();
	
	/**
	 * 默认值。注意：默认值由数据库进行填充，框架不会做任何处理，只在在生成SCHEMA的时候会生成默认值
	 * 
	 * @return
	 */
	Object getDefaultValue();

	/**
	 * 小数位精度
	 *
	 * @return
	 * @see cn.devezhao.persist4j.dialect.editor.DoubleEditor
	 * @see cn.devezhao.persist4j.dialect.editor.DecimalEditor
	 */
	int getDecimalScale();
}
