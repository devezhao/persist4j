package cn.devezhao.persist4j.util.support;

import cn.devezhao.persist4j.Query;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.query.IQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Zhao Fangfang
 * @version $Id: QueryHelper.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 * @since 2.6, 2012-10-22
 */
public class QueryHelper {

	/**
	 * 分页读取
	 * 
	 * @param query
	 * @return
	 */
	public static Object[][] readArray(IQuery<?> query) {
		return readArray(query, 10000);
	}

	/**
	 * 分页读取
	 * 
	 * @param query
	 * @param pageSize
	 * @return
	 */
	public static Object[][] readArray(IQuery<?> query, int pageSize) {
		List<Object[]> tempList = new ArrayList<>();
		int pageNo = 1;
		while (true) {
			query.reset();
			query.setLimit(pageSize, pageNo++ * pageSize - pageSize);
			Object[][] temp = query.array();
			Collections.addAll(tempList, temp);

			if (temp.length < pageSize) {
				break;
			}
		}
		return tempList.toArray(new Object[tempList.size()][]);
	}

	/**
	 * 分页读取
	 * 
	 * @param query
	 * @return
	 */
	public static List<Record> readList(Query query) {
		return readList(query, 10000);
	}

	/**
	 * 分页读取
	 * 
	 * @param query
	 * @param pageSize
	 * @return
	 */
	public static List<Record> readList(Query query, int pageSize) {
		List<Record> tempList = new ArrayList<>();
		int pageNo = 1;
		while (true) {
			query.result().reset();
			query.setLimit(pageSize, pageNo++ * pageSize - pageSize);
			List<Record> temp = query.list();
			tempList.addAll(temp);
			
			if (temp.size() < pageSize) {
				break;
			}
		}
		return tempList;
	}
}
