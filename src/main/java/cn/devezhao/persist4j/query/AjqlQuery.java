package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.*;
import cn.devezhao.persist4j.query.compiler.QueryCompiler;
import cn.devezhao.persist4j.query.compiler.SelectItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ajql query
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 10, 2009
 * @version $Id: StandardQuery.java 23 2009-02-15 12:16:17Z
 *          zhaofang123@gmail.com $
 */
public class AjqlQuery extends BaseQuery<Query> implements Query {
	private static final long serialVersionUID = -2539834203845429816L;

	final private String ajql;
	transient private PersistManagerFactory managerFactory;
	private Filter filter;

	private Map<String, Object> inParameters = new HashMap<>();

	private QueryCompiler queryCompiler;
	private Result result;

	public AjqlQuery(String ajql, PersistManagerFactory managerFactory) {
		this(ajql, managerFactory, null);
	}

	public AjqlQuery(String ajql, PersistManagerFactory managerFactory, Filter filter) {
		this.ajql = ajql;
		this.managerFactory = managerFactory;
		this.filter = filter;
	}

	// COPY
	protected AjqlQuery(AjqlQuery query) {
		this(query.ajql, query.managerFactory, query.filter);
	}
	
	@Override
    public Query setParameter(int position, Object value) {
		return setParameter(position + "", value);
	}

	@Override
    public Query setParameter(String name, Object value) {
		inParameters.put(name, value);
		return this;
	}

	@Override
    public Query setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}

	@Override
    public Result result() {
		if (result == null) {
			result = new AjqlResultImpl(this);
		}
		return result;
	}

	@Override
    public Object[] unique() {
		return result().unique();
	}

	@Override
    public Object[][] array() {
		return result().array();
	}

	@Override
    public List<Record> list() {
		return result().list();
	}

	@Override
    public Record record() {
		return result().record();
	}
	
	@Override
    public Query reset() {
		result().reset();
		return this;
	}

	@Override
    public Entity getRootEntity() {
		compileQueryIfNeed();
		return queryCompiler.getRootEntity();
	}

	@Override
    public SelectItem[] getSelectItems() {
		compileQueryIfNeed();
		return queryCompiler.getSelectItems();
	}

	private void compileQueryIfNeed() {
		if (queryCompiler != null) {
			return;
		}
		queryCompiler = new QueryCompiler(this.ajql);
		queryCompiler.compile(managerFactory.getSQLExecutorContext(), this.filter);
	}

	// ---------------------------------------------------------------------

	protected PersistManagerFactory getPersistManagerFactory() {
		return managerFactory;
	}

	protected QueryCompiler getQueryCompiler() {
		return queryCompiler;
	}

	protected Map<String, Object> getInParameters() {
		return inParameters;
	}
}
