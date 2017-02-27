package cn.devezhao.persist4j.support.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dom4j.Element;

import cn.devezhao.persist4j.Entity;
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
	final private Connection connect;
	
	public SchemaExport(MetadataFactory metadataFactory, Dialect dialect, Connection connect) {
		this.metadataFactory = metadataFactory;
		this.dialect = dialect;
		this.connect = connect;
	}

	@SuppressWarnings("deprecation")
	public void export(boolean drop, boolean createFK) {
		Entity[] entities = metadataFactory.getEntities();
		Element cfgRoot = ((ConfigurationMetadataFactory) metadataFactory).getConfigDocument().getRootElement();

		PreparedStatement pstmt = null;
		try {
			for (Entity entity : entities) {
				String[] sqls = new Table(entity, dialect, cfgRoot).generateDDL(drop, createFK);

				pstmt = connect.prepareStatement("/* !NULL */");
				for (String sql : sqls) {
					pstmt.addBatch(sql);
					System.out.println(sql + "\n");
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SqlHelper.close(pstmt);
			SqlHelper.close(connect);
		}
	}
}
