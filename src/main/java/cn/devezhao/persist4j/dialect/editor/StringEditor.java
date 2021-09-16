package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.NullValue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 字符串
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 12, 2009
 * @version $Id: StringEditor.java 121 2016-01-08 04:07:07Z zhaofang123@gmail.com $
 */
public class StringEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = 1570486266187499134L;

	@Override
    public int getType() {
		return FieldType.STRING.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		if (value == null || NullValue.is(value)) {
			pstmt.setNull(index, Types.VARCHAR);
		} else {
			pstmt.setString(index, value.toString());
		}
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getString(index);
	}
}
