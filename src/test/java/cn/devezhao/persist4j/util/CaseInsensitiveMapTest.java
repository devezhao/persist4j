package cn.devezhao.persist4j.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.junit.Test;

/**
 * @author zhaofang123@gmail.com
 * @since 12/21/2018
 */
public class CaseInsensitiveMapTest {

	@Test
	public void testMap() throws Exception {
		Map<String, String> map1 = new HashMap<>();
		Map<String, String> map2 = new CaseInsensitiveMap<>();

		map1.put("Abc", "1");
		map2.put("Abc", "1");
		map2.put("aBc", "11");
		map1.put("Abcd", "2");
		map2.put("Abcd", "2");
		
		System.out.println(map1.containsKey("abc"));  // false
		System.out.println(map2.containsKey("abc"));  // true
		
		System.out.println(map1.get("abc"));  // null
		System.out.println(map2.get("abc"));  // 1
		
		System.out.println(map1);
		System.out.println(map2);
		
		System.out.println(map1.remove("abc"));  // null
		System.out.println(map2.remove("abc"));  // 1
		
		System.out.println(map1);
		System.out.println(map2);
	}
}
