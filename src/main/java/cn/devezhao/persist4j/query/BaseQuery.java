package cn.devezhao.persist4j.query;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, May 25, 2009
 * @version $Id: BaseQuery.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public abstract class BaseQuery<T> implements IQuery<T> {
	private static final long serialVersionUID = -5599409110546399955L;
	
	private int firstResult;
	private int maxResults;
	private int timeout;
	private int slowLoggerTime;

	protected int limit, offset;

	@Override
    public int getFirstResult() {
		return firstResult;
	}

	@Override
    @SuppressWarnings("unchecked")
	public T setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return (T) this;
	}

	@Override
    public int getMaxResults() {
		return maxResults;
	}

	@Override
    @SuppressWarnings("unchecked")
	public T setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return (T) this;
	}

	@Override
    public int getTimeout() {
		return timeout;
	}

	@Override
    @SuppressWarnings("unchecked")
	public T setTimeout(int timeout) {
		this.timeout = timeout;
		return (T) this;
	}
	
	@Override
    public int getSlowLoggerTime() {
		return slowLoggerTime;
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public T setSlowLoggerTime(int loggerTime) {
		this.slowLoggerTime = loggerTime;
		return (T) this;
	}
	
	@Override
    public T setLimit(int limit) {
		return setLimit(limit, 0);
	}

	@Override
    @SuppressWarnings("unchecked")
	public T setLimit(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
		return (T) this;
	}

	@Override
    public T setParameter(int position, Object value) {
		throw new UnsupportedOperationException();
	}
}
