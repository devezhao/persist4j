package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.SqlExecutorContext;
import cn.devezhao.persist4j.engine.StandardRecord;
import cn.devezhao.persist4j.query.compiler.QueryCompiler;
import cn.devezhao.persist4j.query.compiler.SelectItem;
import cn.devezhao.persist4j.util.CaseInsensitiveMap;

import java.util.Map;

/**
 * 查询出的记录
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Mar 8, 2009
 * @version $Id: CapriciousRecord.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class QueryedRecord extends StandardRecord {

	private static final long serialVersionUID = 5113799283331109208L;
	
	private final Map<String, Object> idLabel = new CaseInsensitiveMap<>();
	private SelectItem[] selectItems;

	/**
	 * for Serializable
	 */
	protected QueryedRecord() {
		super();
	}
	
	protected QueryedRecord(Entity entity, SqlExecutorContext context) {
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
				id.setLabel(value);
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
		if (e.containsField(key)) {
			return super.getObject(key, clazz);
		}
		return recordMap.get(key);
	}

	/**
	 * 完成后整理数据结构
	 */
	protected void completeAfter() {
		if (idLabel.isEmpty()) {
			return;
		}

		for (Map.Entry<String, Object> e : idLabel.entrySet()) {
			ID id = getID(e.getKey());
			if (id != null) {
				id.setLabel(e.getValue());
			}
		}
	}
}
