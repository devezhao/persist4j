package cn.devezhao.persist4j.engine;

import cn.devezhao.commons.CalendarUtils;
import cn.devezhao.persist4j.BaseTest;
import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.Record;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.time.LocalTime;

/**
 * @author devezhao zhaofang123@gmail.com
 * @since 2019年11月28日
 */
public class PersistManagerTest extends BaseTest {

	@Test
	public void testComplicated() throws Exception {
		this.readySchemes();
		PersistManagerFactory factory = getPersistManagerFactory();
		
		Record record = new StandardRecord(factory.getMetadataFactory().getEntity("TestAllType"));
		record.setString("tText", "CLOB/Text");
		record.setIDArray("tReferenceList", new ID[] { ID.newId(100), ID.newId(100) });
		record.setObjectValue("tTime", LocalTime.now());
		record.setDate("tTime", CalendarUtils.now());
		factory.createPersistManager().save(record);
		
		Object[][] array = factory.createQuery("select tText,tReferenceList,tTime from TestAllType").setLimit(1).array();
		for (Object[] o : array) {
			System.out.println(o[0]);
			System.out.println(StringUtils.join((ID[]) o[1], ","));
			System.out.println(o[2]);
		}
	}

}
