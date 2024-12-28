package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 日期时间。兼容: <tt>Date, Long</tt>
 * 
 * @author FF
 * @since 12/28/2024
 * @see TimestampEditor
 */
public class DateTimeEditor extends TimestampEditor {
	private static final long serialVersionUID = -8487826805942935988L;

	@Override
	public int getType() {
		return FieldType.DATETIME.getMask();
	}
}
