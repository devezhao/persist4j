package cn.devezhao.persist4j.engine;

import org.junit.Test;

import cn.devezhao.persist4j.BaseTest;
import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.Record;

/**
 * @author devezhao zhaofang123@gmail.com
 * @since 2019年11月28日
 */
public class PersistManagerTest extends BaseTest {

	@Test
	public void testCLob() throws Exception {
		this.readySchemes();
		PersistManagerFactory factory = getPersistManagerFactory();
		
		Record record = new StandardRecord(factory.getMetadataFactory().getEntity("TestAllType"));
		record.setString("tText", "CLOB/Text");
		factory.createPersistManager().save(record);
		
		Object[][] array = factory.createQuery("select tText from TestAllType").setLimit(1).array();
		for (Object[] o : array) {
			System.out.println(o[0]);
		}
	}
}
