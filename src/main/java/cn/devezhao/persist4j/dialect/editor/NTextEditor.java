package cn.devezhao.persist4j.dialect.editor;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 大文本
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: NTextEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class NTextEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = -6531990086997201844L;

	public int getType() {
		return FieldType.NTEXT.getMask();
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Object val = super.get(rs, index);
		
		if (val != null && val instanceof Clob) {
			try (Reader reader = ((Clob) val).getCharacterStream()) {
				try (BufferedReader br = new BufferedReader(reader)) {
					StringBuffer sb = new StringBuffer();
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						line = br.readLine();
					}
					val = sb.toString();
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return val;
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
//		Reader reader = null;
//		long size = 0;
//		if (Reader.class.isAssignableFrom(value.getClass())) {
//			reader = (Reader) value;
//			try {
//				size = ByteUtils.size(reader);
//			} catch (IOException e) {
//				throw new PersistException("can't read size of stream", e);
//			}
//		} else {
//			String v = value.toString();
//			reader = new StringReader(v);
//			size = v.length();
//		}
		// TASK can't put reader to column
//		pstmt.setCharacterStream(index, reader, (int) size);
		pstmt.setString(index, value.toString());
	}
}
