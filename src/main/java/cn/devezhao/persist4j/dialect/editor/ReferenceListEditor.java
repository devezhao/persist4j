package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 多引用
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: ReferenceListEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class ReferenceListEditor extends ReferenceEditor {

	private static final long serialVersionUID = -8641225920984044088L;

	public int getType() {
		return FieldType.REFERENCE_LIST.getMask();
	}
}
