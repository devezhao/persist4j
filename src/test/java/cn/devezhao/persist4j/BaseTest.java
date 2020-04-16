package cn.devezhao.persist4j;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.h2.H2Dialect;
import cn.devezhao.persist4j.engine.PersistManagerFactoryImpl;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.metadata.impl.ConfigurationMetadataFactory;
import cn.devezhao.persist4j.util.support.SchemaExport;

/**
 * @author zhaofang123@gmail.com
 * @since 11/27/2019
 */
public class BaseTest {
	
	private DataSource ds;
	private PersistManagerFactory persistManagerFactory;
	
	/**
	 * 获取持久化工厂
	 * 
	 * @return
	 */
	protected PersistManagerFactory getPersistManagerFactory() {
		if (persistManagerFactory == null) {
			Dialect dialect = new H2Dialect();
			MetadataFactory metadata = new ConfigurationMetadataFactory("metadata-test.xml", dialect);
			this.persistManagerFactory = new PersistManagerFactoryImpl(getDataSource(), dialect, metadata);
		}
		return persistManagerFactory;
	}
	
	/**
	 * 获取 H2 数据源（内存表）。已知于 MySQL 兼容问题：
	 * 1. 不支持 double（不支持指定小数位）
	 * 2. 不支付全文索引，及对 CLOB/TEXT 字段建立索引
	 * 
	 * @return
	 */
	protected DataSource getDataSource() {
		if (ds == null) {
			Map<String, String> dbProps = new HashMap<>();
			dbProps.put("url", "jdbc:h2:mem:test;MODE=MYSQL;DATABASE_TO_LOWER=TRUE;IGNORECASE=TRUE");
			dbProps.put("testWhileIdle", "false");
			try {
				this.ds = DruidDataSourceFactory.createDataSource(dbProps);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return ds;
	}
	
	/**
	 * 建表
	 * 
	 * @throws Exception
	 */
	protected void readySchemes() throws Exception {
		PersistManagerFactory factory = getPersistManagerFactory();
		SchemaExport schemaExport = new SchemaExport(
				factory.getMetadataFactory(), factory.getDialect(), factory.getDataSource().getConnection());
		schemaExport.export(false, false);
	}
}
