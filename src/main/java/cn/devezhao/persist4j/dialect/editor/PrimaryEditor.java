package cn.devezhao.persist4j.dialect.editor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.ID;

/**
 * 主键
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 12, 2009
 * @version $Id: PrimaryEditor.java 21 2009-02-12 15:44:18Z
 *          zhaofang123@gmail.com $
 */
public class PrimaryEditor extends StringEditor {

	private static final long serialVersionUID = -1490384633712480716L;

	public int getType() {
		return FieldType.PRIMARY.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		super.set(pstmt, index, ((ID) value).toLiteral());
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		String v = rs.getString(index);
		return (StringUtils.isEmpty(v)) ? null : ID.valueOf(v);
	}

	@Override
	public String toLiteral(Object value) {
		return (value == null) ? null : ((ID) value).toLiteral();
	}
}
