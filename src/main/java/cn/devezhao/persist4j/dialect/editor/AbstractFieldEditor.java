package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.Editor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 字段编辑者公用实现
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 12, 2009
 * @version $Id: AbstractFieldEditor.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public abstract class AbstractFieldEditor implements Editor {
	private static final long serialVersionUID = 4896806840146314960L;

	protected static final Log LOG = LogFactory.getLog(AbstractFieldEditor.class);
	
	protected AbstractFieldEditor() {
	}
	
	@Override
    public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getObject(index);
	}

	@Override
    public String getLiteral(ResultSet rs, int index) throws SQLException {
		return toLiteral( get(rs, index) );
	}

	@Override
    public void set(PreparedStatement pstmt, int index, Object value)
			throws SQLException {
		pstmt.setObject(index, value);
	}

	@Override
    public String toLiteral(Object value) {
		return (value == null) ? null : value.toString();
	}
}
