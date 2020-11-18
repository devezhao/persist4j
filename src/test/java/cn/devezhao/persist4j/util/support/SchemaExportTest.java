package cn.devezhao.persist4j.util.support;

import cn.devezhao.persist4j.BaseTest;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.junit.Test;

import java.util.List;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 02/27/2017
 */
public class SchemaExportTest extends BaseTest {

	@Test
	public void testExportDDL() throws Exception {
		PersistManagerFactory factory = getPersistManagerFactory();
		
		SchemaExport schemaExport = new SchemaExport(
				factory.getMetadataFactory(), factory.getDialect(), factory.getDataSource().getConnection());
		schemaExport.export(true, false);
	}
	
	@Test
	public void testGenDDL() throws Exception {
		PersistManagerFactory factory = getPersistManagerFactory();
		
		Element cfgRoot = ((ConfigurationMetadataFactory) factory.getMetadataFactory()).getConfigDocument().getRootElement();
		Entity entity = factory.getMetadataFactory().getEntity("TestAllType");
		List<?> ix = cfgRoot.selectSingleNode("//entity[@name='" + entity.getName() + "']").selectNodes("index");
		
		String[] sqls = new Table(entity, factory.getDialect(), ix).generateDDL(false, false);
		System.out.println(StringUtils.join(sqls, "\n"));
	}
}
