package cn.devezhao.persist4j.dialect.editor;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 数字
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: DecimalEditor.java 52 2009-05-25 09:54:02Z
 *          zhaofang123@gmail.com $
 */
public class DecimalEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = 3815050607620002543L;

	public int getType() {
		return FieldType.DECIMAL.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		set(pstmt, index, value, FieldType.DEFAULT_DECIMAL_SCALE);
	}
	
	public void set(PreparedStatement pstmt, int index, Object value, int scale)
			throws SQLException {
		if (value.getClass() == Double.class) {
			value = new BigDecimal((Double) value);
		}
		
		((BigDecimal) value).setScale(scale < 0 ? FieldType.DEFAULT_DECIMAL_SCALE : scale, BigDecimal.ROUND_HALF_UP);
		pstmt.setBigDecimal(index, (BigDecimal) value);
	}

	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getBigDecimal(index);
	}
}
