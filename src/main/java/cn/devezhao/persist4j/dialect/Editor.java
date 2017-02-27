package cn.devezhao.persist4j.dialect;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 字段编辑者
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 12, 2009
 * @version $Id: Editor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public interface Editor extends Serializable {

	int getType();
	
	void set(PreparedStatement pstmt, int index, Object value) throws SQLException;
	
	Object get(ResultSet rs, int index) throws SQLException;
	
	String getLiteral(ResultSet rs, int index) throws SQLException;
	
	String toLiteral(Object value);
}
