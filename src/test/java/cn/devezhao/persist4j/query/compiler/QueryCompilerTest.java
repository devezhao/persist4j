package cn.devezhao.persist4j.query.compiler;

import org.junit.Test;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 09/25/2018
 */
public class QueryCompilerTest extends Compiler {
	
	@Test
	public void testDistinct() {
		String ajql = "select distinct tString from TestAllType where tString is null";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}

	@Test
	public void testNameField() {
		String ajql = "select &tReference from TestAllType where 1=1";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testIdLabelField() {
		String ajql = "select #tReference from TestAllType where 1=1";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
}
