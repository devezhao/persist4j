package cn.devezhao.persist4j.dialect.editor;

import java.io.BufferedReader;
import java.io.IOException;
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

	@Override
    public int getType() {
		return FieldType.NTEXT.getMask();
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		Object val = super.get(rs, index);
		return read2String(val);
	}
	
	/**
	 * @param ntext
	 * @return
	 */
	public Object read2String(Object ntext) {
		if (ntext == null) {
			return null;
		} 
		else if (ntext instanceof Clob) {
			try (Reader reader = ((Clob) ntext).getCharacterStream()) {
				try (BufferedReader br = new BufferedReader(reader)) {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						line = br.readLine();
					}
					ntext = sb.toString();
				}
				
			} catch (Exception ex) {
				LOG.error("Reading Clob failed", ex);
			}
		}
		else if (ntext instanceof Reader) {
			try (BufferedReader br = ntext instanceof BufferedReader
					? ((BufferedReader) ntext) : new BufferedReader((Reader) ntext)) {
				StringBuilder sb = new StringBuilder();
				String line;
				try {
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					ntext = sb.toString();
					
				} catch (IOException ex) {
					LOG.error("Reading Reader failed", ex);
				}
				
			} catch (IOException ex) {
				LOG.error("Reading Reader failed", ex);
			}
		}
		
		return ntext;
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
