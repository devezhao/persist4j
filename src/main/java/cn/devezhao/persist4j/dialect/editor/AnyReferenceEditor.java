package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 任意引用
 * 
 * @author zhaofang123@gmail.com
 * @since 12/28/2018
 */
public class AnyReferenceEditor extends ReferenceEditor {

	private static final long serialVersionUID = -7654860974427302836L;
	
	public int getType() {
		return FieldType.ANY_REFERENCE.getMask();
	}
}
