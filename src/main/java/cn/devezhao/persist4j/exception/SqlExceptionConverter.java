package cn.devezhao.persist4j.exception;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import cn.devezhao.persist4j.exception.jdbc.ConstraintViolationException;
import cn.devezhao.persist4j.exception.jdbc.GenericJdbcException;
import cn.devezhao.persist4j.exception.jdbc.LockAcquisitionException;
import cn.devezhao.persist4j.exception.jdbc.SqlSyntaxException;

/**
 * 将Sql异常转换至清晰的异常类型
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 03/19/08
 * @version $Id: SqlExceptionConverter.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class SqlExceptionConverter {

	private static final Set<String> SQL_GRAMMAR_CATEGORIES = new HashSet<String>();
	private static final Set<String> DATA_CATEGORIES = new HashSet<String>();
	private static final Set<String> INTEGRITY_VIOLATION_CATEGORIES = new HashSet<String>();
	private static final Set<String> CONNECTION_CATEGORIES = new HashSet<String>();
	
	// error code
	static {
		SQL_GRAMMAR_CATEGORIES.add("07");
		SQL_GRAMMAR_CATEGORIES.add("37");
		SQL_GRAMMAR_CATEGORIES.add("42");
		SQL_GRAMMAR_CATEGORIES.add("65");
		SQL_GRAMMAR_CATEGORIES.add("S0");
		SQL_GRAMMAR_CATEGORIES.add("20");

		DATA_CATEGORIES.add("22");
		DATA_CATEGORIES.add("21");
		DATA_CATEGORIES.add("02");

		INTEGRITY_VIOLATION_CATEGORIES.add("23");
		INTEGRITY_VIOLATION_CATEGORIES.add("27");
		INTEGRITY_VIOLATION_CATEGORIES.add("44");

		CONNECTION_CATEGORIES.add("08");
	}
	
	/**
	 */
	private SqlExceptionConverter() { }

	/**
	 * Converter jdbc exception
	 * 
	 * @param sqlex
	 * @param message
	 * @param sql
	 * @return
	 */
	public static JdbcException convert(SQLException sqlex, String message, String sql) {
		String stateCode = sqlex.getSQLState();

		if (stateCode != null && stateCode.length() >= 2) {
			String classCode = stateCode.substring(0, 2);
			
			if (SQL_GRAMMAR_CATEGORIES.contains(classCode)) {
				return new SqlSyntaxException(message, sqlex, sql);
			} else if (INTEGRITY_VIOLATION_CATEGORIES.contains(classCode)
					|| sqlex instanceof SQLIntegrityConstraintViolationException) {
				return new ConstraintViolationException(message, sqlex, sql);
			}
		}

		if ("40001".equals(stateCode)) {
			return new LockAcquisitionException(message, sqlex, sql);
		}

		if ("61000".equals(stateCode)) {
			// oracle sql-state code for deadlock
			return new LockAcquisitionException(message, sqlex, sql);
		}
		
		return handledNonSpecificException(sqlex, message, sql);
	}

	/**
	 * non-specific sql exception
	 * 
	 * @param sqlex
	 * @param message
	 * @param sql
	 * @return
	 */
	private static JdbcException handledNonSpecificException(SQLException sqlex,
			String message, String sql) {
		return new GenericJdbcException(message, sqlex, sql);
	}
}
