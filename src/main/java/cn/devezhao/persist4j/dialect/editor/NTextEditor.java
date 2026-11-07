package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.hutool.core.io.IoUtil;

import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 大文本
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: NTextEditor.java 121 2016-01-08 04:07:07Z zhaofang123@gmail.com $
 */
public class NTextEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = -6531990086997201844L;

	@Override
    public int getType() {
		return FieldType.NTEXT.getMask();
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Object val = super.get(rs, index);
		return read2String(val);
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		if (value == null) {
			pstmt.setNull(index, Types.CLOB);
			return;
		}
		value = value instanceof Reader ? read2String(value) : value.toString();
		pstmt.setString(index, String.valueOf(value));
	}

	/**
	 * @param ntext
	 * @return
	 */
	public static Object read2String(Object ntext) {
		if (ntext == null) {
			return null;
		}
		else if (ntext instanceof Clob) {
			try {
				ntext = ((Clob) ntext).getSubString(1, (int) ((Clob) ntext).length());
			} catch (Exception ex) {
				LOG.error("Reading Clob failed", ex);
			}
		}
		else if (ntext instanceof Reader) {
			try {
				ntext = IoUtil.read((Reader) ntext);
			} catch (Exception ex) {
				LOG.error("Reading Reader failed", ex);
			}
		}

		return ntext;
	}
}
