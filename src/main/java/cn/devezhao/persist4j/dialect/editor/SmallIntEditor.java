package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 小整数
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: SmallIntEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class SmallIntEditor extends IntEditor {
	
	private static final long serialVersionUID = 942326510267027292L;

	@Override
	public int getType() {
		return FieldType.SMALL_INT.getMask();
	}
}
