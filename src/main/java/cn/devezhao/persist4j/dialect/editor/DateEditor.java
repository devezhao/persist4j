package cn.devezhao.persist4j.dialect.editor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 日期
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: DateEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class DateEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = -2499354201101231156L;

	@Override
    public int getType() {
		return FieldType.DATE.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		java.sql.Date v = null;
		if (value.getClass() == java.util.Date.class) {
			v = new java.sql.Date( ((java.util.Date) value).getTime() );
		} else {
			v = (java.sql.Date) value;
		}
		pstmt.setDate(index, v);
	}

	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		java.sql.Date v = rs.getDate(index);
		return (v == null) ? null : new java.util.Date(v.getTime());
	}
}
