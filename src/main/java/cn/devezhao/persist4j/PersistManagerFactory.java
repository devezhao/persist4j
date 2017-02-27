package cn.devezhao.persist4j;

import java.io.Serializable;

import javax.sql.DataSource;

import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.engine.SqlExecutorContext;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.query.NativeQuery;

/**
 * 持久化管理器工厂
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: PersistManagerFactory.java 51 2009-05-06 07:45:48Z
 *          zhaofang123@gmail.com $
 */
public interface PersistManagerFactory extends Serializable {

	DataSource getDataSource();

	Dialect getDialect();

	MetadataFactory getMetadataFactory();

	SqlExecutorContext getSQLExecutorContext();

	PersistManager createPersistManager();

	Query createQuery(String ajql);

	Query createQuery(String ajql, Filter filter);
	
	NativeQuery createNativeQuery(String sql);
}
