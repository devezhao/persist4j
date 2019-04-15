package cn.devezhao.persist4j.query.compiler;

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
public class Compiler {

	/**
	 * @param ajql
	 * @return
	 */
	protected String compile(String ajql) {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		PersistManagerFactory persistManagerFactory = new PersistManagerFactoryImpl(null, dialect, metadataFactory);
		QueryCompiler queryCompiler = new QueryCompiler(ajql);
		String rawSql = queryCompiler.compile(persistManagerFactory.getSQLExecutorContext());
		queryCompiler.getSelectItems();
		return rawSql;
	}
	
	/**
	 * @param ajql
	 * @return
	 */
	protected QueryCompiler createCompiler(String ajql) {
		Dialect dialect = new MySQL5Dialect();
		MetadataFactory metadataFactory = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
		PersistManagerFactory persistManagerFactory = new PersistManagerFactoryImpl(null, dialect, metadataFactory);
		QueryCompiler queryCompiler = new QueryCompiler(ajql);
		queryCompiler.compile(persistManagerFactory.getSQLExecutorContext());
		return queryCompiler;
	}
}
