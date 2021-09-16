package cn.devezhao.persist4j.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 级联删除模式
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 15, 2009
 * @version $Id: CascadeModel.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public enum CascadeModel {

	/**
	 * 级联删除
	 */
	Delete,
	/**
	 * 移除链接
	 */
	RemoveLinks,
	/**
	 * 忽略，啥都不干
	 */
	Ignore;
	
	private static final Map<String, CascadeModel> NAMED = new HashMap<>();
	static {
		NAMED.put("delete", Delete);
		NAMED.put("remove-links", RemoveLinks);
		NAMED.put("removelinks", RemoveLinks);
		NAMED.put("ignore", Ignore);
	}
	
	/**
	 * @param model
	 * @return
	 */
	public static CascadeModel parse(String model) {
		CascadeModel cm = NAMED.get(model.toLowerCase());
		if (cm != null) {
			return cm;
		}
		throw new MetadataException("No CascadeModel found: " + model);
	}
}
