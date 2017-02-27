package cn.devezhao.persist4j.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: CascadeModel.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public enum CascadeModel {

	Delete, RemoveLinks, Ignore;
	
	
	static Map<String, CascadeModel> cmMap = new HashMap<String, CascadeModel>();
	static {
		cmMap.put("delete", Delete);
		cmMap.put("remove-links", RemoveLinks);
		cmMap.put("ignore", Ignore);
	}
	
	public static CascadeModel parse(String model) {
		CascadeModel cm = cmMap.get(model);
		if (cm != null) {
			return cm;
		}
		throw new MetadataException("Nu CascadeModel found: " + model);
	}
}
