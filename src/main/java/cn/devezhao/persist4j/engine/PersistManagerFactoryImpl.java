package cn.devezhao.persist4j.engine;

import javax.sql.DataSource;

import cn.devezhao.persist4j.Filter;
import cn.devezhao.persist4j.PersistManager;
import cn.devezhao.persist4j.PersistManagerFactory;
import cn.devezhao.persist4j.Query;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.metadata.MetadataFactory;
import cn.devezhao.persist4j.query.AjqlQuery;
import cn.devezhao.persist4j.query.NativeQuery;
import cn.devezhao.persist4j.query.NativeQueryImpl;

/**
 * 持久化对象工厂
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: PersistManagerFactoryImpl.java 20 2009-02-10 03:35:10Z
 *          zhaofang123@gmail.com $
 */
public class PersistManagerFactoryImpl implements PersistManagerFactory {
	private static final long serialVersionUID = -3863550450762257322L;

	final private DataSource dataSource;

	final private Dialect dialect;
	final private MetadataFactory metadataFactory;
	final private SqlExecutorContext executorContext;

	public PersistManagerFactoryImpl(DataSource dataSource, Dialect dialect,
			MetadataFactory metadataFactory) {
		this.dataSource = dataSource;
		this.dialect = dialect;
		
		this.metadataFactory = metadataFactory;
		this.executorContext = new SqlExecutorContext(metadataFactory, dialect, dataSource);
	}

	@Override
    public DataSource getDataSource() {
		return dataSource;
	}

	@Override
    public Dialect getDialect() {
		return dialect;
	}

	@Override
    public MetadataFactory getMetadataFactory() {
		return metadataFactory;
	}

	@Override
    public SqlExecutorContext getSQLExecutorContext() {
		return executorContext;
	}

	@Override
    public PersistManager createPersistManager() {
		return (new PersistManagerImpl(this));
	}

	@Override
    public Query createQuery(String ajql) {
		return createQuery(ajql, null);
	}

	@Override
    public Query createQuery(String ajql, Filter filter) {
		return new AjqlQuery(ajql, this, filter);
	}

	@Override
    public NativeQuery createNativeQuery(String sql) {
		return (new NativeQueryImpl(sql, this));
	}
}
