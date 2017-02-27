package cn.devezhao.persist4j.dialect.editor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 长整数
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-12-4
 * @version $Id: LongEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class LongEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = 9216600570875846158L;

	public int getType() {
		return FieldType.LONG.getMask();
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		pstmt.setLong(index, (Long) value);
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getLong(index);
	}
}
