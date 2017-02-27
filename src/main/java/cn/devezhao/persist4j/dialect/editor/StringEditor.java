package cn.devezhao.persist4j.dialect.editor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 字符串
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 12, 2009
 * @version $Id: StringEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class StringEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = 1570486266187499134L;

	public int getType() {
		return FieldType.STRING.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		pstmt.setString(index, value.toString());
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getString(index);
	}
}
