package cn.devezhao.persist4j;

import java.util.Iterator;

import org.junit.Test;

import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.engine.StandardRecord;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 12/28/2018
 */
public class RecordTest {

	@Test
	public void testAnyReference() throws Exception {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		Entity allTypes = metadataFactory.getEntity(100);
		
		Record record = new StandardRecord(allTypes);
		record.setID("tAnyReference2", ID.newId(102));
		record.setID("tAnyReference2", ID.newId(103));
		
		for (Iterator<String> iter = record.getAvailableFieldIterator(); iter.hasNext(); ) {
			System.out.println(iter.next());
		}
	}
}
