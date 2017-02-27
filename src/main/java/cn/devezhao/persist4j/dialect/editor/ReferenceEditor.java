package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 引用
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 12, 2009
 * @version $Id: ReferenceEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class ReferenceEditor extends PrimaryEditor {

	private static final long serialVersionUID = 6498270129310553155L;

	@Override
	public int getType() {
		return FieldType.REFERENCE.getMask();
	}
}
