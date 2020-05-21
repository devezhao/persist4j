package cn.devezhao.persist4j.util.support;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;

/**
 * 
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 12/04/08
 * @version $Id: SecurityPreferencesConfigurer.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class SecurityPreferencesConfigurer extends PreferencesPlaceholderConfigurer {
	
	@Override
	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
		this.afterLoad(props);
	}
	
	/*-
	 */
	protected void afterLoad(Properties props) {
		String dbUrl = props.getProperty("db.url");
		
		if (dbUrl.startsWith("jdbc:mysql://")) {
			String append = "";
			// Fix MySQL Connector/J 3.1 or higher Bugs
			if (!dbUrl.contains("zeroDateTimeBehavior")) {
				append = "&zeroDateTimeBehavior=convertToNull";
			}
			
			// MySQL using PreparedStatement. MUST BE MYSQL5 OR HIGHER
			if (!dbUrl.contains("useServerPrepStmts")) {
				append += "&useServerPrepStmts=true";
			}
			
			if (append.length() > 1) {
                props.put("db.url", dbUrl + append);
            }
		}
	}
}
