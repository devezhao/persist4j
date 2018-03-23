package cn.devezhao.persist4j.query.compiler;

import org.junit.Test;

import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;
import cn.devezhao.persist4j.engine.PersistManagerFactoryImpl;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 03/05/2018
 */
public class SqlKeywordTest {

	@Test
	public void testDistinct() {
		String ajql = "select distinct tString from TestAllType where tString is null";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	String compile(String ajql) {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		PersistManagerFactory persistManagerFactory = new PersistManagerFactoryImpl(null, dialect, metadataFactory);
		QueryCompiler queryCompiler = new QueryCompiler(ajql);
		String rawSql = queryCompiler.compile(persistManagerFactory.getSQLExecutorContext());
		return rawSql;
	}
}
