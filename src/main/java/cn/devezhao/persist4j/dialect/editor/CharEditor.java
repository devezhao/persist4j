package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 单字符
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: CharEditor.java 121 2016-01-08 04:07:07Z zhaofang123@gmail.com $
 */
public class CharEditor extends StringEditor {

	private static final long serialVersionUID = 8651192300226873094L;

	@Override
	public int getType() {
		return FieldType.CHAR.getMask();
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		String v = rs.getString(index);
		return (v == null) ? null : v.charAt(0);
	}
}
