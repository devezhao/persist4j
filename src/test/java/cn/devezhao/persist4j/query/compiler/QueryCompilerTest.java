package cn.devezhao.persist4j.query.compiler;

import org.junit.Test;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 09/25/2018
 */
public class QueryCompilerTest extends Compiler {
	
	@Test
	public void testWithRollup() throws Exception {
		String ajql = "select tString, count(tString) from TestAllType group by tString with rollup";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
		ajql = "select tString, count(tString) from TestAllType group by tString having(count(tString) > 1) with rollup";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
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
	
//	@Test
//	public void testIdLabelField() {
//		String ajql = "select #tReference.tReference from TestAllType where 1=1";
//		String sql = compile(ajql);
//		System.out.println(ajql + "\n>>\n" + sql);
//	}
	
	@Test
	public void testJoin() {
		String ajql = "select t2Primary from Test2 where t2Reference.tInt > 99";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testIn() {
		String ajql = "select tPrimary from TestAllType where tReference in (select t2Primary from Test2 where t2Int > 10)";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testExists() {
		String ajql = "select tPrimary from TestAllType where exists (select t2Primary from Test2 where ^tReference = t2Reference and t2Int = 999)";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
		ajql = "select tPrimary from TestAllType where exists (select t2Primary from Test2 where t2Reference = ^tReference and t2Int = 999)";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testBetween() {
		String ajql = "select tPrimary from TestAllType where tInt between 1 and 2";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
		ajql = "select tPrimary from TestAllType where tInt between '2010-09-01' and '2010-10-01'";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
		ajql = "select tPrimary from TestAllType where tInt between ? and ?";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testDateFormat() {
		String ajql = "select date_format(tDate,'%Y') from TestAllType";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
		ajql = "select date_format(tDate,'%Y') from TestAllType group by date_format(tDate,'%Y')";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
	}
}
