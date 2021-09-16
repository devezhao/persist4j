package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.commons.ObjectUtils;
import cn.devezhao.persist4j.dialect.FieldType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 长整数
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-12-4
 * @version $Id: LongEditor.java 121 2016-01-08 04:07:07Z zhaofang123@gmail.com $
 */
public class LongEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = 9216600570875846158L;

	@Override
    public int getType() {
		return FieldType.LONG.getMask();
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		Long longValue;
		if (value instanceof Integer) {
			longValue = ((Integer) value).longValue();
		} else {
			longValue = (Long) value;
		}
		pstmt.setLong(index, longValue);
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Object hasVal = rs.getObject(index);
		return hasVal == null ? null : ObjectUtils.toLong(hasVal);
	}
}
