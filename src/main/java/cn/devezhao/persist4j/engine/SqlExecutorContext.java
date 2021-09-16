package cn.devezhao.persist4j.engine;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;

/**
 * Sql执行上下文信息
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Mar 8, 2009
 * @version $Id: SqlExecutorContext.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class SqlExecutorContext implements Serializable {
	private static final long serialVersionUID = 661188666290260734L;
	
	final private DataSource dataSource;
	final private MetadataFactory metadataFactory;
	final private Dialect dialect;

	public SqlExecutorContext(MetadataFactory metadataFactory, Dialect dialect,
			DataSource dataSource) {
		this.metadataFactory = metadataFactory;
		this.dialect = dialect;
		this.dataSource = dataSource;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public Entity getEntity(String tname) {
		return metadataFactory.getEntity(tname);
	}

	public Entity getEntity(int tcode) {
		return metadataFactory.getEntity(tcode);
	}

	public Connection getConnection() {
		return DataSourceUtils.getConnection(getDataSource());
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}
