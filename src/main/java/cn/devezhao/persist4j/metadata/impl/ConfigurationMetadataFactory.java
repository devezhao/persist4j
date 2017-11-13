package cn.devezhao.persist4j.metadata.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import cn.devezhao.persist4j.util.StringHelper;
import cn.devezhao.persist4j.util.XmlHelper;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: ConfigurationMetadataFactory.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class ConfigurationMetadataFactory implements MetadataFactory {
	private static final long serialVersionUID = 496898585196420839L;

	private static final Log LOG = LogFactory.getLog(ConfigurationMetadataFactory.class);
	
	transient
	final private XmlHelper XML_HELPER = new XmlHelper();
	
	final private Map<String, Integer> cnMap = new HashMap<String, Integer>();
	final private Map<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
	
	private String configLocation;
	transient private Dialect dialect;
	
	private String commonEntityName = null;
	private Document configDocument = null;
	private boolean schemaNameOptimize = false;
	
	/**
	 * @param configLocation
	 * @param dialect
	 */
	public ConfigurationMetadataFactory(String configLocation, Dialect dialect) {
		this.configLocation = configLocation;
		this.dialect = dialect;
		refresh();
	}

	public Entity getEntity(String name) {
		Integer aType = cnMap.get(name);
		if (aType == null) {
			throw new MetadataException("entity [ " + name + " ] dose not exists");
		}
		return getEntity(aType);
	}

	public Entity getEntity(int type) {
		Entity e = entityMap.get(type);
		if (e == null) {
			throw new MetadataException("entity [ " + type + " ] dose not exists");
		}
		return e;
	}
	
	public boolean containsEntity(int aType) {
		return cnMap.containsValue(aType);
	}
	
	public Entity[] getEntities() {
		return entityMap.values().toArray(new Entity[entityMap.size()]);
	}
	
	public void registerEntity(Entity entity) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("register entity " + entity);
		}
		if (cnMap.get(entity.getName()) != null || entityMap.get(entity.getEntityCode()) != null) {
			throw new MetadataException("repeated entity: " + entity);
		}
		cnMap.put(entity.getName(), entity.getEntityCode());
		entityMap.put(entity.getEntityCode(), entity);
	}
	
	public Document getConfigDocument() {
		return configDocument;
	}
	
	// -------------------------------------------------------------------------------
	
	/**
	 */
	private void refresh() {
		URL url = getClass().getClassLoader().getResource(configLocation);
		Document userDocument = read( url );
		bind(userDocument);
		configDocument = userDocument;
	}
	
	/**
	 * @param document
	 */
	private void bind(Document document) {
		Element root = document.getRootElement();
		schemaNameOptimize = BooleanUtils.toBoolean(root.valueOf("@schema-name-optimize"));
		
		Entity common = null;
		String allParent = root.valueOf("@default-parent");
		if (!StringUtils.isBlank(allParent)) {
			common = bindEntity(
					root.selectSingleNode(String.format("entity[@name='%s']", allParent)), null);
			if (LOG.isInfoEnabled())
				LOG.info("default entity [ " + common + " ] will injecting all entity");
			commonEntityName = common.getName();
		}
		
		for (Object obj : root.selectNodes("//entity")) {
			Node e = (Node) obj;
			if (common != null && common.getName().equals(e.valueOf("@name")))
				continue;
			
			Entity entity = bindEntity(e, common);
			registerEntity(entity);
		}
		
		refreshBefore();
		commonEntityName = null;
	}
	
	/**
	 * @param eNode
	 * @param parent
	 * @return
	 */
	private Entity bindEntity(Node eNode, Entity parent) {
		String tCode = eNode.valueOf("@type-code");
		Validate.notEmpty(tCode);
		boolean theSchemaNameOptimize = BooleanUtils.toBoolean(eNode.valueOf("@schema-name-optimize"));
		theSchemaNameOptimize = theSchemaNameOptimize || (schemaNameOptimize && !theSchemaNameOptimize);
		
		String name = eNode.valueOf("@name");
		namingPolicy(name, "entity");
		String pName = eNode.valueOf("@physical-name");
		if (StringUtils.isEmpty(pName)) {
			pName = name;
			if (theSchemaNameOptimize) {
				pName = StringHelper.hyphenate(pName).toLowerCase();
			}
		}
		namingPolicy(pName, "entity physical");
		
		String nameField = eNode.valueOf("@name-field");
		String desc = eNode.valueOf("@description");
		
		EntityImpl entity = new EntityImpl(
				name, pName, desc, Integer.parseInt(tCode), nameField);
		
		Map<String, Field> map = new HashMap<String, Field>();
		if (parent != null && !"false".equals(eNode.valueOf("@parent"))) {
			for (Field field : parent.getFields()) {
				FieldImpl fieldImpl = new FieldImpl(
						field.getName(), field.getPhysicalName(), field.getDescription(), 
						entity, field.getType(), field.getCascadeModel(), field.getMaxLength(),
						field.isNullable(), field.isUpdatable(),
						field.getDecimalScale(), field.getDefaultValue(), field.isAutoValue());
				
				map.put(field.getName(), fieldImpl);
				
				if (field.getType() == FieldType.REFERENCE) {
					completeMap.put(fieldImpl, completeMap.get(field));
				}
			}
		}
		
		for (Object obj : eNode.selectNodes("field")) {
			Field field = bindField((Node) obj, entity, theSchemaNameOptimize);
			if (entity.containsField(field.getName()))
				throw new MetadataException("Field [ " + field + " ] already exists");
			entity.addField(field);
		}
		
		for (Iterator<Map.Entry<String, Field>> iter = map.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<String, Field> e = iter.next();
			if (entity.containsField(e.getKey()))
				continue;
			entity.addField(e.getValue());
		}
		return entity;
	}
	
	/**
	 * @param fNode
	 * @param own
	 * @param theSchemaNameOptimize
	 * @return
	 */
	private Field bindField(Node fNode, Entity own, boolean theSchemaNameOptimize) {
		String name = fNode.valueOf("@name");
		namingPolicy(name, "field");
		String pName = fNode.valueOf("@physical-name");
		if (StringUtils.isEmpty(pName)) {
			pName = name;
			if (theSchemaNameOptimize) {
				pName = StringHelper.hyphenate(pName).toUpperCase();
			}
		}
		namingPolicy(pName, "field physical ");
		
		Type type = dialect.getFieldType(fNode.valueOf("@type"));
		Validate.notNull(type);
		
		boolean n = Boolean.parseBoolean(fNode.valueOf("@nullable"));
		boolean u = Boolean.parseBoolean(fNode.valueOf("@updatable"));
		int maxLength = FieldType.NO_NEED_LENGTH;
		if (StringUtils.isBlank( fNode.valueOf("@max-length") )) {
			if (type == FieldType.STRING)
				maxLength = FieldType.DEFAULT_STRING_LENGTH;
			else if (type == FieldType.TEXT)
				maxLength = FieldType.DEFAULT_TEXT_LENGTH;
		} else {
			maxLength = Integer.parseInt( fNode.valueOf("@max-length") );
		}
		
		if (type == FieldType.PRIMARY) {
			n = u = false;
			maxLength = ID.getIDGenerator().getLength();
		}
		
		CascadeModel cascade = null;
		if (type == FieldType.REFERENCE) {
			cascade = CascadeModel.parse(fNode.valueOf("@cascade"));
			maxLength = ID.getIDGenerator().getLength();
		}
		
		int decimalScale = 0;
		if (type == FieldType.DECIMAL || type == FieldType.DOUBLE) {
			decimalScale = Integer.parseInt( 
					StringUtils.defaultIfEmpty(fNode.valueOf("@decimal-scale"), FieldType.DEFAULT_DECIMAL_SCALE + "") );
		} else {
			decimalScale = 0;
		}
		
		String desc = fNode.valueOf("@description");
		String defaultValue = fNode.valueOf("@default-value");
		boolean a = Boolean.parseBoolean(fNode.valueOf("@auto-value"));
		if (a && type != FieldType.LONG) {
			type = FieldType.LONG;
		}
		
		Field field = new FieldImpl(
				name, pName, desc, own, type, cascade, maxLength, n, u,
				decimalScale, defaultValue, a);
		
		String refs = fNode.valueOf("@ref-entity");
		if (type == FieldType.REFERENCE) {
			Validate.notEmpty(refs, 
					"reference field [ " + field + " ] must have attribute ref-entity");
			completeMap.put(field, refs.split("\\,"));
		}
		return field;
	}
	
	/**
	 * @param url
	 * @return
	 */
	protected Document read(URL url) {
		List<String> errors = new ArrayList<String>();
		
		Document document = null;
		try {
			document = XML_HELPER.createSAXReader("",
					errors, XmlHelper.DEFAULT_DTD_RESOLVER).read( url.openStream() );
		} catch (IOException e) {
			throw new MetadataException("could not load metadata config [ " + url + " ]", e);
		} catch (DocumentException e) {
			throw new MetadataException("could not parse metadata config [ " + url + " ]", e);
		} finally {
		}
		return document;
	}
	
	/**
	 * @param ident
	 * @param type
	 */
	protected void namingPolicy(String ident, String type) {
		if (!StringHelper.isIdentifier(ident))
			throw new MetadataException(type + " name [ " + ident + " ] is wrong! must start with ['a-zA-Z'|'_'|'#'] and contains ['a-zA-Z'|'_'|'#'|'0-9'] only");
	}
	
	private static final Entity ANY_ENTITY = new AnyEntity();
	private Map<Field, String[]> completeMap = new HashMap<Field, String[]>();
	/**
	 */
	private void refreshBefore() {
		for (Iterator<Map.Entry<Field, String[]>> iter = completeMap.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<Field, String[]> e = iter.next();
			FieldImpl field = (FieldImpl) e.getKey();
			if (field.getOwnEntity().getName().equals(commonEntityName))
				continue;
			
			if (field.getType() == FieldType.REFERENCE) {
				String eName = e.getValue()[0];
				Entity entity = (AnyEntity.FLAG.equals(eName)) ? ANY_ENTITY : getEntity(eName);
				field.addReference(entity);
				continue;
			}
			
			for (String eName : e.getValue()) {
				Entity entity = getEntity(eName);
				field.addReference(entity);
			}
		}
		completeMap.clear();
	}
}
