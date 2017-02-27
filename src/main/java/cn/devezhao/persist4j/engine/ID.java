package cn.devezhao.persist4j.engine;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devezhao.commons.ByteUtils;

/**
 * Id of record
 * <pre>
 * Using:
 *   # System.setProperty("org.qdss.persist.id", WeakIDGenerator.class.getName())
 * to set type of id
 * </pre>
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/12/08
 * @version $Id: ID.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class ID implements Serializable {
	private static final long serialVersionUID = 5861261456599575527L;
	
	private static final Log LOG = LogFactory.getLog(ID.class);

	private static IDGenerator idGenerator;
	private static int idLength;
	static {
		String idClazz = StringUtils.defaultIfEmpty(
				System.getProperty("org.qdss.persist.id"), WeakIDGenerator.class.getName());
		try {
			idGenerator = (IDGenerator) Class.forName(idClazz).newInstance();
		} catch (Throwable ex) {
			LOG.error("Could't instance ID clazz: " + idClazz);
		}
		if (idGenerator == null) {
			idGenerator = new IDGenerator();
		}
		idLength = idGenerator.getLength();
		if (LOG.isInfoEnabled()) {
			LOG.warn("Using ID provider: " + idGenerator.getClass() + " (eg: " + idGenerator.generate(0) + ')');
		}
	}
	
	public static final ID[] EMPTY_ID_ARRAY = new ID[0];

	/**
	 * @param typeCode
	 * @return
	 */
	public static ID newId(int typeCode) {
		Validate.isTrue((typeCode > -1), "entity code must be or 0-999");
		return new ID(idGenerator.generate(typeCode).toString());
	}

	/**
	 * @param id
	 * @return
	 */
	public static boolean isId(Object id) {
		if (id instanceof ID)
			return true;
		
		if (id == null || StringUtils.isEmpty(id.toString())
				|| id.toString().length() != idLength)
			return false;
		if (id.toString().charAt(3) != '-')
			return false;
		return true;
	}

	/**
	 * @param id
	 * @return
	 */
	public static ID valueOf(String id) {
		if (!isId(id))
			throw new IllegalArgumentException("Invaild id character: " + id);
		return (new ID(id));
	}
	
	/**
	 * @return
	 */
	public static IDGenerator getIDGenerator() {
		return idGenerator;
	}

	// ----------------------------------------------------------------------------
	final private int entityCode;
	final private String id;
	
	private String label;

	/**
	 * Create a new ID
	 * 
	 * @param id
	 */
	private ID(String id) {
		this.id = id;
		this.entityCode = Integer.valueOf(id.substring(0, 3));
	}

	/**
	 * @return
	 */
	public Integer getEntityCode() {
		return entityCode;
	}

	/**
	 * @return
	 */
	public String toLiteral() {
		return id;
	}
	
	/**
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return toLiteral();
	}

	@Override
	public int hashCode() {
		return ByteUtils.hash(id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj == this || obj instanceof ID))
			return false;
		return obj.hashCode() == hashCode();
	}
}
