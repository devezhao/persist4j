package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 文本
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: TextEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class TextEditor extends StringEditor {

	private static final long serialVersionUID = 7190173130419637496L;

	@Override
	public int getType() {
		return FieldType.TEXT.getMask();
	}
}
