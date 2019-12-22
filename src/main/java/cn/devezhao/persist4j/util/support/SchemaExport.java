package cn.devezhao.persist4j.util.support;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dom4j.Element;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.PersistException;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;
import cn.devezhao.persist4j.util.SqlHelper;

/**
 * 生成建表语句
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/15/08
 * @version $Id: SchemaExport.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class SchemaExport {

	final private MetadataFactory metadataFactory;
	final private Dialect dialect;
	final private Connection connection;
	
	/**
	 * @param metadataFactory
	 * @param dialect
	 * @param connect
	 */
	public SchemaExport(MetadataFactory metadataFactory, Dialect dialect, Connection connect) {
		this.metadataFactory = metadataFactory;
		this.dialect = dialect;
		this.connection = connect;
	}

	/**
	 * @param dropExists
	 * @param createFK
	 */
	public void export(boolean dropExists, boolean createFK) {
		Entity[] entities = metadataFactory.getEntities();
		Element cfgRoot = ((ConfigurationMetadataFactory) metadataFactory).getConfigDocument().getRootElement();

		try {
			for (Entity entity : entities) {
				Element entityElement = (Element) cfgRoot.selectSingleNode("//entity[@name='" + entity.getName() + "']");
				List<?> ix = entityElement.selectNodes("index");
				String[] sqls = new Table(entity, dialect, ix).generateDDL(dropExists, createFK);
				
				for (String sql : sqls) {
					try (Statement stmt = connection.createStatement()) {
						stmt.execute(sql);
					}
				}
			}
		} catch (SQLException e) {
			throw new PersistException(null, e);
		} finally {
			SqlHelper.close(connection);
		}
	}
}
