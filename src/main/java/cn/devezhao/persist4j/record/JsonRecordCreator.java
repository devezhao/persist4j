/*
Copyright 2018 DEVEZHAO(zhaofang123@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.devezhao.persist4j.record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.StandardRecord;

/**
 * JSON 记录解析
 * 
 * @author zhaofang123@gmail.com
 * @since 08/01/2018
 */
public class JsonRecordCreator implements RecordCreator {

	private static final Log LOG = LogFactory.getLog(JsonRecordCreator.class);

	public static final String META_FIELD = "metadata";
	
	final protected Entity entity;
	final protected ID editor;
	final protected JSONObject source;

	/**
	 * Create a new JsonRecordCreator. the format of document like
	 * <pre>
	 * {
	 *   metadata: { entity: 'User', [id='xx'] },
	 *   loginName: 'xx',
	 *   email: 'xx'
	 * }
	 * </pre>
	 * 
	 * @param entity
	 * @param editor
	 * @param source
	 */
	public JsonRecordCreator(Entity entity, JSONObject source) {
		this(entity, source, null);
	}
	
	/**
	 * 
	 * @param entity
	 * @param source
	 * @param editor
	 */
	public JsonRecordCreator(Entity entity, JSONObject source, ID editor) {
		Validate.notNull(entity);
		Validate.notNull(source);
		this.entity = entity;
		this.source = source;
		this.editor = editor;
	}
	
	/**
	 * @return
	 */
	public Record create() {
		return create(true);
	}

	/**
	 * @param ignoreNullValue 忽略空值（仅在新建时有效）
	 * @return
	 */
	public Record create(boolean ignoreNullValue) {
		Record record = new StandardRecord(entity, editor);
		boolean isNew = true;
		
		JSONObject metadata = source.getJSONObject(META_FIELD);
		if (metadata == null) {
			throw new FieldValueException("`metadata` element must not be null");
		}
		
		String id = metadata.getString("id");
		if (ID.isId(id)) {
			record.setID(entity.getPrimaryField().getName(), ID.valueOf(id));
			isNew = false;
		}
		
		for (Map.Entry<String, Object> e : source.entrySet()) {
			String fileName = e.getKey();
			if (META_FIELD.equals(fileName)) {
				continue;
			}
			
			Field field = entity.getField(fileName);
			if (field == null) {
				LOG.warn("Unable found field [ " + entity.getName() + '#' + fileName  + " ], will ignore");
				continue;
			}
			
			if (isNew == false && !field.isUpdatable()) {  // 忽略更新
				if (LOG.isDebugEnabled()) {
					LOG.warn("Could not put value to un-update field");
				}
				continue;
			}
			
			Object value = e.getValue();
			if (value == null || StringUtils.isEmpty(value.toString())) {
				if (isNew) {
					if (!field.isNullable() && !field.isAutoValue()) {
						throw new FieldValueException(entity.getName() + '#' + field.getName() + " must not be null");
					}
					
					// 不忽略空值
					if (!ignoreNullValue) {
						record.setNull(fileName);
					}
					continue;
				}
				value = null;
			}
			setValue(field, value == null ? null : value.toString(), record);
		}
		
		afterCreate(record, isNew);
		verify(record, isNew);
		return record;
	}
	
	/**
	 * @param field
	 * @param value
	 * @param record
	 */
	protected void setValue(Field field, String value, Record record) {
		if (value == null) {
			record.setNull(field.getName());
			return;
		}
		RecordVisitor.setValueByLiteral(field, value, record);
	}
	
	/**
	 * @param record
	 * @param isNew
	 */
	protected void afterCreate(Record record, boolean isNew) {
		// Biz to here
	}
	
	/**
	 * @param record
	 * @param isNew
	 */
	protected void verify(Record record, boolean isNew) {
		if (!isNew) {
			return;
		}
		
		List<String> notNulls = new ArrayList<String>();
		for (Field field : entity.getFields()) {
			if (FieldType.PRIMARY.equals(field.getType())) {
				continue;
			}
			
			Object val = record.getObjectValue(field.getName());
			if (!field.isNullable() && !field.isAutoValue() && val == null) {
				notNulls.add(field.getName());
			}
		}
		
		if (notNulls.isEmpty()) {
			return;
		}
		throw new FieldValueException("Must not been null. Entity [ " + entity.getName() 
				+ " ], Fields [ " + StringUtils.join(notNulls.toArray(new String[notNulls.size()]), ", ") + " ]");
	}
}
