package cn.devezhao.persist4j.metadata.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

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

/**
 * 元数据配置
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: ConfigurationMetadataFactory.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class ConfigurationMetadataFactory implements MetadataFactory {
	private static final long serialVersionUID = 496898585196420839L;

	private static final Log LOG = LogFactory.getLog(ConfigurationMetadataFactory.class);
	
	final private XmlHelper XML_HELPER = new XmlHelper();
	
	volatile
	private boolean refreshLocked = false;
	
	volatile private Map<String, Integer> name2TypeMap = new CaseInsensitiveMap<>();
	volatile private Map<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
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
	
	public Entity getEntity(String name) {
		waitForRefreshLocked();
		return getEntityNoLock(name);
	}
	
	private Entity getEntityNoLock(String name) {
		Integer aType = name2TypeMap.get(name);
		if (aType == null) {
			throw new MetadataException("entity [ " + name + " ] dose not exists");
		}
		return entityMap.get(aType);
	}

	public Entity getEntity(int type) {
		waitForRefreshLocked();
		Entity e = entityMap.get(type);
		if (e == null) {
			throw new MetadataException("entity [ " + type + " ] dose not exists");
		}
		return e;
	}
	
	public boolean containsEntity(int aType) {
		waitForRefreshLocked();
		return name2TypeMap.containsValue(aType);
	}
	
	public Entity[] getEntities() {
		waitForRefreshLocked();
		return entityMap.values().toArray(new Entity[entityMap.size()]);
	}
	
	public Document getConfigDocument() {
		waitForRefreshLocked();
		return configDocument;
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
	
	synchronized
	public void refresh(boolean initState) {
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
	 * @param url
	 * @return
	 */
	protected Document readConfiguration(boolean initState) {
		URL url = getClass().getClassLoader().getResource(configLocation);
		List<String> errors = new ArrayList<String>();
		
		Document document = null;
		try {
			document = XML_HELPER.createSAXReader("",
					errors, XmlHelper.DEFAULT_DTD_RESOLVER).read( url.openStream() );
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
	 * @param eNode
	 * @param parent
	 * @return
	 */
	private Entity buildEntity(Node eNode, Entity parent) {
		String tCode = eNode.valueOf("@type-code");
		Validate.notEmpty(tCode);
		boolean fieldSchemaNameOptimize = BooleanUtils.toBoolean(eNode.valueOf("@schema-name-optimize"));
		fieldSchemaNameOptimize = fieldSchemaNameOptimize || (schemaNameOptimize && !fieldSchemaNameOptimize);
		
		String name = eNode.valueOf("@name");
		namingPolicy(name, "entity");
		String pName = eNode.valueOf("@physical-name");
		if (StringUtils.isEmpty(pName)) {
			pName = name;
			if (fieldSchemaNameOptimize) {
				pName = StringHelper.hyphenate(pName).toLowerCase();
			}
		}
		namingPolicy(pName, "entity physical");
		
		String nameField = eNode.valueOf("@name-field");
		String description = eNode.valueOf("@description");
		String extraAttrs = eNode.valueOf("@extra-attrs");
		
		String master = eNode.valueOf("@master");
		if (StringUtils.isNotBlank(master)) {
			SM_MAPPING.put(name, master);
		}
		
		EntityImpl entity = new EntityImpl(
				name, pName, description, Integer.parseInt(tCode), nameField, extraAttrs);
		
		Map<String, Field> fieldMap = new CaseInsensitiveMap<>();
		if (parent != null && !"false".equals(eNode.valueOf("@parent"))) {
			for (Field field : parent.getFields()) {
				FieldImpl fieldImpl = new FieldImpl(
						field.getName(), field.getPhysicalName(), field.getDescription(), 
						entity, field.getType(), field.getCascadeModel(), field.getMaxLength(),
						field.isNullable(), field.isCreatable(), field.isUpdatable(), field.isRepeatable(),
						field.getDecimalScale(), field.getDefaultValue(), field.isAutoValue());
				
				fieldMap.put(field.getName(), fieldImpl);
				
				// 复制父级的字段
				if (field.getType() == FieldType.REFERENCE 
						|| field.getType() == FieldType.ANY_REFERENCE || field.getType() == FieldType.REFERENCE_LIST) {
					REFFIELD_REFS.put(fieldImpl, REFFIELD_REFS.get(field));
				}
			}
		}
		
		for (Object obj : eNode.selectNodes("field")) {
			Field field = buildField((Node) obj, entity, fieldSchemaNameOptimize);
			if (entity.containsField(field.getName())) {
				throw new MetadataException("Field [ " + field + " ] already exists");
			}
			entity.addField(field);
		}
		
		for (Iterator<Map.Entry<String, Field>> iter = fieldMap.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<String, Field> e = iter.next();
			if (entity.containsField(e.getKey())) {
				continue;
			}
			entity.addField(e.getValue());
		}
		return entity;
	}
	
	/**
	 * @param node
	 * @param ownEntity
	 * @param fieldSchemaNameOptimize
	 * @return
	 */
	private Field buildField(Node node, Entity ownEntity, boolean fieldSchemaNameOptimize) {
		String name = node.valueOf("@name");
		namingPolicy(name, "field");
		String pName = node.valueOf("@physical-name");
		if (StringUtils.isEmpty(pName)) {
			pName = name;
			if (fieldSchemaNameOptimize) {
				pName = StringHelper.hyphenate(pName).toUpperCase();
			}
		}
		namingPolicy(pName, "field physical ");
		
		Type type = dialect.getFieldType(node.valueOf("@type"));
		Validate.notNull(type);
		
		boolean N = Boolean.parseBoolean(node.valueOf("@nullable"));
		boolean C = Boolean.parseBoolean(node.valueOf("@creatable"));
		boolean U = Boolean.parseBoolean(node.valueOf("@updatable"));
		boolean R = Boolean.parseBoolean(node.valueOf("@repeatable"));
		int maxLength = FieldType.NO_NEED_LENGTH;
		if (StringUtils.isBlank( node.valueOf("@max-length") )) {
			if (type == FieldType.STRING) {
				maxLength = FieldType.DEFAULT_STRING_LENGTH;
			} else if (type == FieldType.TEXT) {
				maxLength = FieldType.DEFAULT_TEXT_LENGTH;
			}
		} else {
			maxLength = Integer.parseInt( node.valueOf("@max-length") );
		}
		
		if (type == FieldType.PRIMARY) {
			N = C = U = R = false;
			maxLength = ID.getIDGenerator().getLength();
		}
		
		CascadeModel cascade = null;
		if (type == FieldType.REFERENCE || type == FieldType.ANY_REFERENCE) {
			cascade = CascadeModel.parse(node.valueOf("@cascade"));
			maxLength = ID.getIDGenerator().getLength();
		}
		
		int decimalScale = 0;
		if (type == FieldType.DECIMAL || type == FieldType.DOUBLE) {
			decimalScale = Integer.parseInt( 
					StringUtils.defaultIfEmpty(node.valueOf("@decimal-scale"), FieldType.DEFAULT_DECIMAL_SCALE + "") );
		} else {
			decimalScale = 0;
		}
		
		String desc = node.valueOf("@description");
		String defaultValue = node.valueOf("@default-value");
		boolean auto = Boolean.parseBoolean(node.valueOf("@auto-value"));
		if (auto) {
			N = C = U = R = false;
			type = FieldType.LONG;
		}
		
		String extraAttrs = node.valueOf("@extra-attrs");
		
		Field field = new FieldImpl(
				name, pName, desc, ownEntity, type, cascade, maxLength, N, C, U, R,
				decimalScale, defaultValue, auto, extraAttrs);
		
		String refs = node.valueOf("@ref-entity");
		if (type == FieldType.REFERENCE) {
			Validate.notEmpty(refs,
					"reference field [ " + field + " ] must have attribute ref-entity");
			REFFIELD_REFS.put(field, new String[] { refs });
		} else if (type == FieldType.ANY_REFERENCE || type == FieldType.REFERENCE_LIST) {
			REFFIELD_REFS.put(field,
					StringUtils.isBlank(refs) ? new String[] { AnyEntity.FLAG } : refs.split("\\,"));
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
		for (Iterator<Map.Entry<Field, String[]>> iter = REFFIELD_REFS.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<Field, String[]> e = iter.next();
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
		for (Iterator<Map.Entry<String, String>> iter = SM_MAPPING.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<String, String> e = iter.next();
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
