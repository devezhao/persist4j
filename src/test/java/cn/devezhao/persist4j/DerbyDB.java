package cn.devezhao.persist4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.derby.jdbc.EmbeddedDriver;

import cn.devezhao.persist4j.util.SqlHelper;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 02/27/2017
 */
public class DerbyDB {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName(EmbeddedDriver.class.getCanonicalName());
		Connection conn = DriverManager.getConnection("jdbc:derby:TESTDB;create=true");
		System.out.println(conn.getClientInfo());
		SqlHelper.close(conn);
	}
}
