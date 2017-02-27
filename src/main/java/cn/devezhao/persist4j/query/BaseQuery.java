package cn.devezhao.persist4j.query;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @see 0.1, May 25, 2009
 * @version $Id: BaseQuery.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public abstract class BaseQuery<T> implements IQuery<T> {
	private static final long serialVersionUID = -5599409110546399955L;
	
	private int firstResult;
	private int maxResults;
	private int timeout;
	private int slowLoggerTime;

	protected int limit, offset;

	public int getFirstResult() {
		return firstResult;
	}

	@SuppressWarnings("unchecked")
	public T setFirstResult(int firstResult) {
		this.firstResult = firstResult;
		return (T) this;
	}

	public int getMaxResults() {
		return maxResults;
	}

	@SuppressWarnings("unchecked")
	public T setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return (T) this;
	}

	public int getTimeout() {
		return timeout;
	}

	@SuppressWarnings("unchecked")
	public T setTimeout(int timeout) {
		this.timeout = timeout;
		return (T) this;
	}
	
	public int getSlowLoggerTime() {
		return slowLoggerTime;
	}
	
	@SuppressWarnings("unchecked")
	public T setSlowLoggerTime(int loggerTime) {
		this.slowLoggerTime = loggerTime;
		return (T) this;
	}
	
	public T setLimit(int limit) {
		return setLimit(limit, 0);
	}

	@SuppressWarnings("unchecked")
	public T setLimit(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
		return (T) this;
	}

	public T setParameter(int position, Object value) {
		throw new UnsupportedOperationException();
	}
}
