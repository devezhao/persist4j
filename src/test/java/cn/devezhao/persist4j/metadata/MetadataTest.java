package cn.devezhao.persist4j.metadata;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;

/**
 * @author zhaofang123@gmail.com
 * @since 12/28/2018
 */
public class MetadataTest {

	@Test
	public void testAnyReference() throws Exception {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		
		Entity allTypes = metadataFactory.getEntity(100);
		Field anyReference1 = allTypes.getField("tanyreference");
		System.out.println(anyReference1.getType() + " : " + StringUtils.join(anyReference1.getReferenceEntities(), " | "));
		
		Field anyReference2 = allTypes.getField("tanyreference2");
		System.out.println(anyReference2.getType() + " : " + StringUtils.join(anyReference2.getReferenceEntities(), " | "));
	}
	
	@Test
	public void testReferenceToFields() throws Exception {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		
		Entity test = metadataFactory.getEntity(102);
		for (Field field : test.getReferenceToFields()) {
			System.out.println(field);
		}
	}
}
