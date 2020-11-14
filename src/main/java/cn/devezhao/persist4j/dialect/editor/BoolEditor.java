package cn.devezhao.persist4j.dialect.editor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 布尔
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: BoolEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class BoolEditor extends CharEditor {

	private static final long serialVersionUID = -2740602793905099065L;
	
	public static final char TRUE 	= 'T';
	public static final char FALSE 	= 'F';
	
	@Override
    public int getType() {
		return FieldType.BOOL.getMask();
	}

	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		char ch = ((Boolean) value) ? TRUE : FALSE;
		super.set(pstmt, index, ch);
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Object v = super.get(rs, index);
		if (v == null) {
			return null;
		}
		
		char ch = v.toString().toUpperCase().charAt(0);
		switch (ch) {
		case TRUE:
			return true;
		case FALSE:
			return false;
		}
		return null;
	}
}
