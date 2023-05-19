package cn.devezhao.persist4j.metadata;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author zhaofang123@gmail.com
 * @since 12/28/2018
 */
public class MetadataTest {

	@Test
	public void testAnyReference() throws Exception {
		final MetadataFactory metadataFactory = createMetadataFactory();
		
		Entity allTypes = metadataFactory.getEntity(100);
		Field anyReference1 = allTypes.getField("tanyreference");
		System.out.println(anyReference1.getType() + " : " + StringUtils.join(anyReference1.getReferenceEntities(), " | "));
		
		Field anyReference2 = allTypes.getField("tanyreference2");
		System.out.println(anyReference2.getType() + " : " + StringUtils.join(anyReference2.getReferenceEntities(), " | "));
	}
	
	@Test
	public void testReferenceToFields() throws Exception {
		final MetadataFactory metadataFactory = createMetadataFactory();
		
		Entity test = metadataFactory.getEntity(102);
		for (Field field : test.getReferenceToFields()) {
			System.out.println(field);
		}
	}

	@Test
	public void testCommon() {
		final MetadataFactory metadataFactory = createMetadataFactory();

		Field commonReference = metadataFactory.getEntity("Test2").getField("commonReference");
		System.out.println(commonReference);
	}

	@Test
	public void testNDetails() {
		final MetadataFactory metadataFactory = createMetadataFactory();

		Entity main = metadataFactory.getEntity(200);
		System.out.println(main.getDetailEntity());
		System.out.println(Arrays.toString(main.getDetialEntities()));

		Entity detail1 = metadataFactory.getEntity(201);
		System.out.println(detail1.getMainEntity());

		Entity detail2 = metadataFactory.getEntity(202);
		System.out.println(detail2.getMainEntity());

		System.out.println(detail1.getMainEntity() == (detail2.getMainEntity()));
	}

	static MetadataFactory createMetadataFactory() {
		Dialect dialect = new MySQL5Dialect();
		return new ConfigurationMetadataFactory("metadata-test.xml", dialect);
	}
}
