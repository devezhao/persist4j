package cn.devezhao.persist4j.dialect.editor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.devezhao.persist4j.PersistException;
import cn.devezhao.persist4j.dialect.FieldType;

/**
 * 二进制
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 14, 2009
 * @version $Id: BinaryEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class BinaryEditor extends AbstractFieldEditor {

	private static final long serialVersionUID = 3962501236650435747L;

	@Override
    public int getType() {
		return FieldType.BINARY.getMask();
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		InputStream stream = (InputStream) value;
		long size;
		try {
//			size = ByteUtils.size(stream);  // NOTE does not read
			size = stream.available();
		} catch (IOException e) {
			throw new PersistException("can't read size of stream", e);
		}
		if (size == 0) {
			System.out.println("## STREAM IS EMPTY");
		}
		pstmt.setBinaryStream(index, stream, (int) size);
	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getBinaryStream(index);
	}
}
