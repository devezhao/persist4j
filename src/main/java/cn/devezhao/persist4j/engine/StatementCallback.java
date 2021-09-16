package cn.devezhao.persist4j.engine;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 语句回调
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: StatementCallback.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public interface StatementCallback {

	String getSql();
	
	Object doInParameters(PreparedStatement pstmt) throws SQLException;
}
