package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

/**
 * 时间。兼容: <tt>Date, LocalTime, Long</tt>
 *
 * @author devezhao
 * @since 2022/3/9
 */
public class TimeEditor extends AbstractFieldEditor {
    private static final long serialVersionUID = -2499354201101781156L;

    @Override
    public int getType() {
        return FieldType.TIME.getMask();
    }

    @Override
    public void set(PreparedStatement pstmt, int index, Object value)
            throws SQLException {
        Time v;
        if (value.getClass() == Date.class) {
            v = new Time(((Date) value).getTime());
        } else if (value.getClass() == LocalTime.class) {
            v = Time.valueOf((LocalTime) value);
        } else if (value instanceof Number) {
            v = new Time(((Number) value).longValue());
        } else {
            v = (Time) value;
        }

        pstmt.setTime(index, v);
    }

    @Override
    public Object get(ResultSet rs, int index) throws SQLException {
        Time v = rs.getTime(index);
        return (v == null) ? null : v.toLocalTime();
    }
}
