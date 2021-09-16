package cn.devezhao.persist4j.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 慢查询记录
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 2011-4-11
 * @version $Id: SlowLogger.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class SlowLogger {
	
	private static final Log LOG = LogFactory.getLog(SlowLogger.class);
	
	private static final ThreadLocal<Long> QUERY_START = new ThreadLocal<>();
	
	/**
	 */
	public static void start() {
		QUERY_START.set(System.currentTimeMillis());
	}
	
	/**
	 * @param loggerTime
	 * @param resultSize
	 */
	public static void stop(int loggerTime, int resultSize) {
		stop(loggerTime, resultSize, null);
	}
	
	/**
	 * @param loggerTime
	 * @param resultSize
	 * @param sql
	 */
	public static void stop(int loggerTime, int resultSize, String sql) {
		if (!LOG.isInfoEnabled() || loggerTime <= 0) {
			return;
		}
		
		Long start = QUERY_START.get();
		if (start == null) {
			start = 0L;
		}
		QUERY_START.remove();

		int time = start == 0 ? loggerTime : (int) (System.currentTimeMillis() - start);
		if (time <= loggerTime && resultSize <= 10000) {
			return;
		}
		
		StringBuilder logs = new StringBuilder()
				.append("\n** SQL Slower/Many ***********************************************************************")
				.append("\nSQL:        ").append(sql == null ? "Unknow" : sql);
		if (time > loggerTime) {
			logs.append("\nTime:       ").append(time).append("ms");
		}
		if (resultSize > 10000) {
			logs.append("\nResult(s):  ").append(resultSize);
		}
		LOG.warn(logs);
	}
}
