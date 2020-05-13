package cn.devezhao.persist4j.metadata.impl;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.metadata.CascadeModel;
import cn.devezhao.persist4j.metadata.MetadataException;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.util.CaseInsensitiveMap;
import cn.devezhao.persist4j.util.StringHelper;
import cn.devezhao.persist4j.util.XmlHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 元数据配置
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: ConfigurationMetadataFactory.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
@SuppressWarnings("SpellCheckingInspection")
public class ConfigurationMetadataFactory implements MetadataFactory {
	private static final long serialVersionUID = 496898585196420839L;

	private static final Log LOG = LogFactory.getLog(ConfigurationMetadataFactory.class);
	
	final private XmlHelper XML_HELPER = new XmlHelper();
	
	volatile private boolean refreshLocked = false;
	
	volatile private Map<String, Integer> name2TypeMap = new CaseInsensitiveMap<>();
	volatile private Map<Integer, Entity> entityMap = new HashMap<>();
	volatile private Document configDocument = null;
	
	private String configLocation;
	private Dialect dialect;
	
	private String commonEntityName = null;
	private boolean schemaNameOptimize = false;

	/**
	 * @param configLocation
	 * @param dialect
	 */
	public ConfigurationMetadataFactory(String configLocation, Dialect dialect) {
		this.configLocation = configLocation;
		this.dialect = dialect;
		this.refresh(true);
	}
	
	@Override
	public Entity getEntity(String name) {
		waitForRefreshLocked();
		return getEntityNoLock(name);
	}
	
	@Override
	public Entity getEntity(int type) {
		waitForRefreshLocked();
		Entity e = entityMap.get(type);
		if (e == null) {
			throw new MetadataException("entity [ " + type + " ] dose not exists");
		}
		return e;
	}

	private Entity getEntityNoLock(String name) {
		Integer aType = name2TypeMap.get(name);
		if (aType == null) {
			throw new MetadataException("entity [ " + name + " ] dose not exists");
		}
		return entityMap.get(aType);
	}
	
	@Override
	public boolean containsEntity(int aType) {
		waitForRefreshLocked();
		return name2TypeMap.containsValue(aType);
	}
	
	@Override
	public Entity[] getEntities() {
		waitForRefreshLocked();
		return entityMap.values().toArray(new Entity[0]);
	}

	/**
	 * @return
	 */
	public Document getConfigDocument() {
		waitForRefreshLocked();
		return (Document) configDocument.clone();
	}
	
	synchronized
	private void waitForRefreshLocked() {
		if (refreshLocked) {
			try {
				this.wait(1000 * 10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new MetadataException("Wait for refresh lock fail");
			}
		}
	}

	/**
	 * @param initState
	 */
	synchronized public void refresh(boolean initState) {
		final Document newlyDocument = readConfiguration(initState);
		
		this.refreshLocked = true;
		this.name2TypeMap.clear();
		this.entityMap.clear();
		this.configDocument = null;
		try {
			build(newlyDocument);
			this.configDocument = newlyDocument;
		} finally {
			this.refreshLocked = false;
			this.notifyAll();
		}
	}

	/**
	 * @param initState
	 * @return
	 */
	protected Document readConfiguration(boolean initState) {
		URL url = getClass().getClassLoader().getResource(configLocation);
		List<String> errors = new ArrayList<>();
		
		Document document;
		try {
			document = XML_HELPER.createSAXReader("",
					errors, XmlHelper.DEFAULT_DTD_RESOLVER).read(Objects.requireNonNull(url).openStream());
		} catch (IOException e) {
			throw new MetadataException("could not load metadata config [ " + url + " ]", e);
		} catch (DocumentException e) {
			throw new MetadataException("could not parse metadata config [ " + url + " ]", e);
		}
		return document;
	}

	/**
	 * @param ident
	 * @param type
	 */
	protected void namingPolicy(String ident, String type) {
		if (!StringHelper.isIdentifier(ident)) {
			throw new MetadataException(type + " name [ " + ident + " ] is wrong! must start with ['a-zA-Z'|'_'|'#'] and contains ['a-zA-Z'|'_'|'#'|'0-9'] only");
		}
	}

	/**
	 * @param document
	 */
	private void build(Document document) {
		Element root = document.getRootElement();
		schemaNameOptimize = BooleanUtils.toBoolean(root.valueOf("@schema-name-optimize"));
		
		Entity globalCommon = null;
		String allParent = root.valueOf("@default-parent");
		if (!StringUtils.isBlank(allParent)) {
			globalCommon = buildEntity(root.selectSingleNode(String.format("entity[@name='%s']", allParent)), null);
			if (LOG.isInfoEnabled()) {
				LOG.info("Default entity [ " + globalCommon + " ] will merge into all entities");
			}
			commonEntityName = globalCommon.getName();
		}
		
		for (Object o : root.selectNodes("//entity")) {
			Node e = (Node) o;
			if (globalCommon != null && globalCommon.getName().equals(e.valueOf("@name"))) {
				continue;
			}
			
			Entity entity = buildEntity(e, globalCommon);
			registerEntity(entity);
		}
		
		buildCompleted();
	}

	/**
	 * @param entityNode
	 * @param parent
	 * @return
	 */
	private Entity buildEntity(Node entityNode, Entity parent) {
		String tCode = entityNode.valueOf("@type-code");
		Validate.notEmpty(tCode);
		boolean fieldSchemaNameOptimize = BooleanUtils.toBoolean(entityNode.valueOf("@schema-name-optimize"));
		fieldSchemaNameOptimize = fieldSchemaNameOptimize || schemaNameOptimize;
		
		String name = entityNode.valueOf("@name");
		namingPolicy(name, "entity");
		String pName = entityNode.valueOf("@physical-name");
		if (StringUtils.isEmpty(pName)) {
			pName = name;
			if (fieldSchemaNameOptimize) {
				pName = StringHelper.hyphenate(pName).toLowerCase();
			}
		}
		namingPolicy(pName, "entity physical");
		
		String nameField = entityNode.valueOf("@name-field");
		String description = entityNode.valueOf("@description");

		String extraAttrs = entityNode.valueOf("@extra-attrs");
		JSONObject extraAttrsJson = JSON.parseObject(StringUtils.defaultIfBlank(extraAttrs, "{}"));

		String master = entityNode.valueOf("@master");
		if (StringUtils.isNotBlank(master)) {
			SM_MAPPING.put(name, master);
		}

		boolean C = Boolean.parseBoolean(entityNode.valueOf("@creatable"));
		boolean U = Boolean.parseBoolean(entityNode.valueOf("@updatable"));
		boolean Q = Boolean.parseBoolean(entityNode.valueOf("@queryable"));
		boolean D = Boolean.parseBoolean(entityNode.valueOf("@deletable"));

		EntityImpl entity = new EntityImpl(
				name, pName, description, extraAttrsJson, C, U, Q, Integer.parseInt(tCode), nameField, D);
		
		Map<String, Field> fieldMap = new CaseInsensitiveMap<>();
		if (parent != null && !"false".equals(entityNode.valueOf("@parent"))) {
			for (Field field : parent.getFields()) {
				FieldImpl fieldClone = new FieldImpl(field);
				fieldMap.put(field.getName(), fieldClone);

				// 复制父级的字段
				if (field.getType() == FieldType.REFERENCE 
						|| field.getType() == FieldType.ANY_REFERENCE || field.getType() == FieldType.REFERENCE_LIST) {
					REFFIELD_REFS.put(fieldClone, REFFIELD_REFS.get(field));
				}
			}
		}
		
		for (Object obj : entityNode.selectNodes("field")) {
			Field field = buildField((Node) obj, entity, fieldSchemaNameOptimize);
			if (entity.containsField(field.getName())) {
				throw new MetadataException("Field [ " + field + " ] already exists");
			}
			entity.addField(field);
		}

		for (Map.Entry<String, Field> e : fieldMap.entrySet()) {
			if (entity.containsField(e.getKey())) {
				continue;
			}
			entity.addField(e.getValue());
		}
		return entity;
	}
	
	/**
	 * @param fieldNode
	 * @param ownEntity
	 * @param fieldSchemaNameOptimize
	 * @return
	 */
	private Field buildField(Node fieldNode, Entity ownEntity, boolean fieldSchemaNameOptimize) {
		String name = fieldNode.valueOf("@name");
		namingPolicy(name, "field");
		String pName = fieldNode.valueOf("@physical-name");
		if (StringUtils.isEmpty(pName)) {
			pName = name;
			if (fieldSchemaNameOptimize) {
				pName = StringHelper.hyphenate(pName).toUpperCase();
			}
		}
		namingPolicy(pName, "field physical ");
		
		Type type = dialect.getFieldType(fieldNode.valueOf("@type"));
		Validate.notNull(type);

		boolean C = Boolean.parseBoolean(fieldNode.valueOf("@creatable"));
		boolean U = Boolean.parseBoolean(fieldNode.valueOf("@updatable"));
		boolean Q = Boolean.parseBoolean(fieldNode.valueOf("@queryable"));
		boolean N = Boolean.parseBoolean(fieldNode.valueOf("@nullable"));
		boolean R = Boolean.parseBoolean(fieldNode.valueOf("@repeatable"));

		int maxLength = FieldType.NO_NEED_LENGTH;
		if (StringUtils.isBlank( fieldNode.valueOf("@max-length") )) {
			if (type == FieldType.STRING) {
				maxLength = FieldType.DEFAULT_STRING_LENGTH;
			} else if (type == FieldType.TEXT) {
				maxLength = FieldType.DEFAULT_TEXT_LENGTH;
			}
		} else {
			maxLength = Integer.parseInt( fieldNode.valueOf("@max-length") );
		}
		
		if (type == FieldType.PRIMARY) {
			C = U = N = R = false;
			maxLength = ID.getIDGenerator().getLength();
		}
		
		CascadeModel cascade = null;
		if (type == FieldType.REFERENCE || type == FieldType.ANY_REFERENCE) {
			cascade = CascadeModel.parse(fieldNode.valueOf("@cascade"));
			maxLength = ID.getIDGenerator().getLength();
		}
		
		int decimalScale;
		if (type == FieldType.DECIMAL || type == FieldType.DOUBLE) {
			decimalScale = Integer.parseInt( 
					StringUtils.defaultIfEmpty(fieldNode.valueOf("@decimal-scale"), FieldType.DEFAULT_DECIMAL_SCALE + "") );
		} else {
			decimalScale = 0;
		}

		// 自增值
		boolean autoValue = Boolean.parseBoolean(fieldNode.valueOf("@auto-value"));
		if (autoValue) {
			C = U = N = R = false;
			type = FieldType.LONG;
		}

		String desc = fieldNode.valueOf("@description");
		String defaultValue = fieldNode.valueOf("@default-value");

		String extraAttrs = fieldNode.valueOf("@extra-attrs");
		JSONObject extraAttrsJson = JSON.parseObject(StringUtils.defaultIfBlank(extraAttrs, "{}"));

		Field field = new FieldImpl(name, pName, desc, extraAttrsJson, C, U, Q,
				ownEntity, type, maxLength, cascade, N, R, autoValue, decimalScale, defaultValue);

		String refs = fieldNode.valueOf("@ref-entity");
		if (type == FieldType.REFERENCE) {
			Validate.notEmpty(refs,
					"reference field [ " + field + " ] must have attribute ref-entity");
			REFFIELD_REFS.put(field, new String[] { refs });
		} else if (type == FieldType.ANY_REFERENCE || type == FieldType.REFERENCE_LIST) {
			REFFIELD_REFS.put(field,
					StringUtils.isBlank(refs) ? new String[] { AnyEntity.FLAG } : refs.split(","));
		}
		return field;
	}
	
	private void registerEntity(Entity entity) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("register entity " + entity);
		}
		if (name2TypeMap.get(entity.getName()) != null || entityMap.get(entity.getEntityCode()) != null) {
			throw new MetadataException("repeated entity : " + entity);
		}
		name2TypeMap.put(entity.getName(), entity.getEntityCode());
		entityMap.put(entity.getEntityCode(), entity);
	}
	
	final private Map<Field, String[]> REFFIELD_REFS = new ConcurrentHashMap<>();
	final private Map<String, String> SM_MAPPING = new ConcurrentHashMap<>();
	/**
	 * 最终的
	 */
	private void buildCompleted() {
		// 特殊字段处理
		for (Map.Entry<Field, String[]> e : REFFIELD_REFS.entrySet()) {
			FieldImpl field = (FieldImpl) e.getKey();
			if (field.getOwnEntity().getName().equals(commonEntityName)) {
				continue;
			}

			// 任意引用，引用所有实体
			if (AnyEntity.FLAG.equals(e.getValue()[0])) {
				for (Entity entity : entityMap.values()) {
					field.addReference(entity);
				}
			} else {
				for (String eName : e.getValue()) {
					field.addReference(getEntityNoLock(eName));
				}
			}
		}
		REFFIELD_REFS.clear();
		
		// SM 实体处理
		for (Map.Entry<String, String> e : SM_MAPPING.entrySet()) {
			Entity slaveEntity = getEntityNoLock(e.getKey());
			String master = e.getValue();
			if (name2TypeMap.containsKey(master)) {
				((EntityImpl) slaveEntity).setMasterEntity(getEntityNoLock(master));
			} else {
				throw new MetadataException("No master-entity found : " + master);
			}
		}
		SM_MAPPING.clear();
	}
}
