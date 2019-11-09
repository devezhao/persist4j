package cn.devezhao.persist4j.util.support;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Driver;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 02/27/2017
 */
public class SchemaExportTest {

	@Test
	public void testExportDDL() throws Exception {
		Dialect dialect = new MySQL5Dialect();
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:mysql://localhost:4653/persist4j_01?useUnicode=true&amp;characterEncoding=UTF-8");
		dataSource.setUser("root");
		dataSource.setPassword("a");
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		
		SchemaExport schemaExport = new SchemaExport(metadataFactory, dialect, dataSource.getConnection());
		schemaExport.export(true, false);
	}
	
	@Test
	public void testGenDDL() throws Exception {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadata = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		Element cfgRoot = ((ConfigurationMetadataFactory) metadata).getConfigDocument().getRootElement();
		
		Entity entity = metadata.getEntity("TestAllType");
		List<?> ix = cfgRoot.selectSingleNode("//entity[@name='" + entity.getName() + "']").selectNodes("index");
		
		String[] sqls = new Table(entity, dialect, ix).generateDDL(false, false);
		System.out.println(StringUtils.join(sqls, "\n"));
	}
}
