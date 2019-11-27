package cn.devezhao.persist4j;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * @author zhaofang123@gmail.com
 * @since 11/27/2019
 */
public class BaseTese {
	
	private DataSource ds;
	
	/**
	 * 获取 H2 数据源。已知于 MySQL 兼容问题：
	 * 1. 不支持 double（不支持指定小数位）
	 * 2. 不支付全文索引
	 * 
	 * @return
	 * @throws Exception 
	 */
	protected DataSource getDataSource() throws Exception {
		if (ds == null) {
			Map<String, String> dbProps = new HashMap<>();
			dbProps.put("url", "jdbc:h2:mem:test;MODE=MYSQL;DATABASE_TO_LOWER=TRUE;IGNORECASE=TRUE");
			dbProps.put("testWhileIdle", "false");
			ds = DruidDataSourceFactory.createDataSource(dbProps);
		}
		return ds;
	}
}
