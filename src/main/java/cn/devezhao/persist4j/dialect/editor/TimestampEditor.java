package cn.devezhao.persist4j.dialect.editor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 日期时间。兼容: <tt>Date, Long</tt>
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: TimestampEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class TimestampEditor extends DateEditor {

	private static final long serialVersionUID = -8487826805942935976L;

	@Override
	public int getType() {
		return FieldType.TIMESTAMP.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		Timestamp v = null;
		Class<?> vClazz = value.getClass();
		if (vClazz == java.util.Date.class || vClazz == java.sql.Date.class) {
			v = new Timestamp( ((java.util.Date) value).getTime() );
		} else if (vClazz == Long.class) {
			v = new Timestamp( (Long) value );
		} else {
			v = (Timestamp) value;
		}
		pstmt.setTimestamp(index, v);
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Timestamp v = rs.getTimestamp(index);
		return (v == null) ? null : new Date(v.getTime());
	}
}
