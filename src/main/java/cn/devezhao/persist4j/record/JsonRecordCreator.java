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

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.StandardRecord;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

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
	@Override
    public Record create() {
		return create(true);
	}

	/**
	 * @param ignoreNullValue 忽略空值（仅在新建时有效）
	 * @return
	 */
	public Record create(boolean ignoreNullValue) {
		Record record = new StandardRecord(entity, editor);

		JSONObject metadata = source.getJSONObject(META_FIELD);
		if (metadata == null) {
			throw new FieldValueException("`metadata` element must not be null");
		}

		String id = metadata.getString("id");
		if (ID.isId(id)) {
			record.setID(entity.getPrimaryField().getName(), ID.valueOf(id));
		}
		
		for (Map.Entry<String, Object> e : source.entrySet()) {
			String fileName = e.getKey();
			if (META_FIELD.equals(fileName)) continue;

			final Field field = entity.containsField(fileName) ? entity.getField(fileName) : null;
			if (field == null) {
				LOG.warn("Unable found field [ " + entity.getName() + '#' + fileName  + " ], will ignore");
				continue;
			}

			Object fieldValue = e.getValue();
			if (fieldValue != null) fieldValue = fieldValue.toString();

			if (ignoreNullValue && (fieldValue == null || StringUtils.isEmpty((String) fieldValue))) {
				continue;
			}

			setFieldValue(field, (String) fieldValue, record);
		}
		
		afterCreate(record);
		return record;
	}

	protected void afterCreate(Record record) {
		verify(record);
	}
}
