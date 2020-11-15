package cn.devezhao.persist4j.dialect.editor;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.ID;
import org.apache.commons.lang.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 多引用（任意实体）
 * 
 * @author zhaofang123@gmail.com
 * @since 12/28/2018
 */
public class ReferenceListEditor extends StringEditor {

	private static final long serialVersionUID = -7654860974427302836L;

	/**
	 * ID 分隔符
	 */
	public static final String VALUE_SEP = ",";
	
	@Override
    public int getType() {
		return FieldType.REFERENCE_LIST.getMask();
	}
	
	@Override
	public void set(PreparedStatement pstmt, int index, Object value) throws SQLException {
		String text = toLiteral(value);
		super.set(pstmt, index, text);

	}
	
	@Override
	public Object get(ResultSet rs, int index) throws SQLException {
		String v = rs.getString(index);
		if (StringUtils.isBlank(v)) {
			return null;
		}
		
		List<ID> ids = new ArrayList<>();
		for (String id : v.split(",")) {
			ids.add(ID.valueOf(id));
		}
		return ids.isEmpty() ? null : ids.toArray();
	}
	
	@Override
	public String toLiteral(Object value) {
		if (value == null) {
			return null;
		}
		ID[] ids = (ID[]) value;
		return ids.length == 0 ? null : StringUtils.join(ids, VALUE_SEP);
	}
}
