package cn.devezhao.persist4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Map;

import org.junit.Test;

/**
 * @author zhaofang123@gmail.com
 * @since 12/21/2018
 */
public class CaseInsensitiveMapTest {

	@Test
	public void testMap() throws Exception {
		Map<String, String> map = new CaseInsensitiveMap<>();

		map.put("abc", "1");
		map.put("Abc", "11");
		map.put("AbC", "111");
		map.put("abcd", "2");
		map.remove("ABCd");
		
		System.out.println(map.keySet());
		System.out.println(map.entrySet());
		
		assertEquals(map.entrySet().size(), map.size());
		assertFalse(map.containsKey("abcd"));
		assertEquals(map.get("abc"), "111");
	}
}
