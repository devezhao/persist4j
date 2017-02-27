package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 日期时间
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: DateTimeEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class DateTimeEditor extends DateEditor {

	private static final long serialVersionUID = -4114451723433792163L;

	@Override
	public int getType() {
		return FieldType.DATETIME.getMask();
	}
}
