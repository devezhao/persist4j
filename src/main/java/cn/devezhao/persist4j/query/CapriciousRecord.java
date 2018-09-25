package cn.devezhao.persist4j.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.SqlExecutorContext;
import cn.devezhao.persist4j.engine.StandardRecord;
import cn.devezhao.persist4j.query.compiler.QueryCompiler;
import cn.devezhao.persist4j.query.compiler.SelectItem;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Mar 8, 2009
 * @version $Id: CapriciousRecord.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class CapriciousRecord extends StandardRecord {

	private static final long serialVersionUID = 5113799283331109208L;
	
	private final Map<String, Object> idLabel = new HashMap<String, Object>();
	private SelectItem[] selectItems;

	/**
	 * Serializable
	 */
	protected CapriciousRecord() {
		super();
	}
	
	protected CapriciousRecord(Entity entity, SqlExecutorContext context) {
		super(entity, null);
	}
	
	public void setSelectItems(SelectItem[] selectItems) {
		this.selectItems = selectItems.clone();
	}
	
	public SelectItem[] getSelectItems() {
		return selectItems.clone();
	}

	@Override
	protected void setObject(String key, Object value) {
		Entity e = getEntity();
		if (e.containsField(key)) {
			super.setObject(key, value);
			return;
		}
		
		if (key.charAt(0) == QueryCompiler.NAME_FIELD_PREFIX) {
			key = key.substring(1);
			ID id = getID(key);
			if (id != null) {
				id.setLabel(value.toString());
			} else {
				idLabel.put(key, value);
			}
		} else {
			recordMap.put(key, value);
		}
	}

	@Override
	protected Object getObject(String key, Class<?> clazz) {
		Entity e = getEntity();
		if (e.containsField(key))
			return super.getObject(key, clazz);

		return recordMap.get(key);
	}

	/**
	 * 
	 */
	protected void complete() {
		if (idLabel.isEmpty())
			return;
		for (Iterator<Map.Entry<String, Object>> iter = idLabel.entrySet()
				.iterator(); iter.hasNext();) {
			Map.Entry<String, Object> e = iter.next();
			ID id = getID(e.getKey());
			if (id != null)
				id.setLabel(e.getValue().toString());
		}
	}
}
