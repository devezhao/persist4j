package cn.devezhao.persist4j.dialect.editor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.commons.ObjectUtils;
import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 双精度数字
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: DoubleEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class DoubleEditor extends AbstractFieldEditor {
	
	private static final long serialVersionUID = 3370417172932258927L;

	@Override
    public int getType() {
		return FieldType.DOUBLE.getMask();
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		set(pstmt, index, value, FieldType.DEFAULT_DECIMAL_SCALE);
	}
	
	public void set(PreparedStatement pstmt, int index, Object value, int scale)
			throws SQLException {
		BigDecimal decimalValue;
		if (value instanceof BigDecimal) {
			decimalValue = (BigDecimal) value;
		} else {
			decimalValue = BigDecimal.valueOf((Double) value);
		}
		
		double doubleValue = decimalValue
				.setScale(scale < 0 ? FieldType.DEFAULT_DECIMAL_SCALE : scale, RoundingMode.HALF_UP)
				.doubleValue();
		pstmt.setDouble(index, doubleValue);
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Object hasVal = rs.getObject(index);
		return hasVal == null ? null : ObjectUtils.toDouble(hasVal);
	}
}
