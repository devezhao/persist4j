package cn.devezhao.persist4j.query.compiler;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
		assertEquals(sql, "select _t0.`T_STRING` as _c0, count( _t0.`T_STRING` ) as _c1 from `test_all_type` as _t0 where ( 1 = 1 ) group by _t0.`T_STRING` with rollup");
		
		ajql = "select tString, count(tString) from TestAllType group by tString having(count(tString) > 1) with rollup";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_STRING` as _c0, count( _t0.`T_STRING` ) as _c1 from `test_all_type` as _t0 where ( 1 = 1 ) group by _t0.`T_STRING` having ( count( _t0.`T_STRING` ) > 1 ) with rollup");
	}
	
	@Test
	public void testDistinct() {
		String ajql = "select distinct tString from TestAllType where tString is null";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select distinct _t0.`T_STRING` as _c0 from `test_all_type` as _t0 where _t0.`T_STRING` is null");
	}

	@Test
	public void testNameField() {
		String ajql = "select &tReference from TestAllType where 1=1";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t1.`T_PRIMARY` as _c0 from `test_all_type` as _t0 left join `test_all_type` as _t1 on _t0.`T_REFERENCE` = _t1.`T_PRIMARY` where 1 = 1");
	}
	
	@Test
	public void testJoin() {
		String ajql = "select t2Primary from Test2 where t2Reference.tInt > 99";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T2_PRIMARY` as _c0 from `test2` as _t0 left join `test_all_type` as _t1 on _t0.`T2_REFERENCE` = _t1.`T_PRIMARY` where _t1.`T_INT` > 99");
	}
	
	@Test
	public void testIn() {
		String ajql = "select tPrimary from TestAllType where tReference in (select t2Primary from Test2 where t2Int > 10)";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where _t0.`T_REFERENCE` in ( select _t0.`T2_PRIMARY` as _c0 from `test2` as _t0 where _t0.`T2_INT` > 10 )");
	}
	
	@Test
	public void testExists() {
		String ajql = "select tPrimary from TestAllType where exists (select t2Primary from Test2 where ^tReference = t2Reference and t2Int = 999) and tInt = 100";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where exists ( select _t1.`T2_PRIMARY` as _c0 from `test2` as _t1 where _t0.`T_REFERENCE` = _t1.`T2_REFERENCE` and _t1.`T2_INT` = 999 ) and _t0.`T_INT` = 100");
		
		ajql = "select tPrimary from TestAllType where exists (select t2Primary from Test2 where t2Reference = ^tReference and t2Int = 999)";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where exists ( select _t1.`T2_PRIMARY` as _c0 from `test2` as _t1 where _t1.`T2_REFERENCE` = _t0.`T_REFERENCE` and _t1.`T2_INT` = 999 )");
	}
	
	@Test
	public void testBetween() {
		String ajql = "select tPrimary from TestAllType where tInt = 12.12 and tInt = -10 and tInt between -1 and 2";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where _t0.`T_INT` = 12.12 and _t0.`T_INT` = -10 and _t0.`T_INT` between -1 and 2");
		
		ajql = "select tPrimary from TestAllType where tInt between '2010-09-01' and '2010-10-01'";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where _t0.`T_INT` between '2010-09-01' and '2010-10-01'");
		
		ajql = "select tPrimary from TestAllType where tInt between ? and ?";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where _t0.`T_INT` between ? and ?");
	}
	
	@Test
	public void testDateFormat() {
		String ajql = "select date_format(tDate,'%Y') from TestAllType where tDate > '2019-04-01'";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		assertEquals(sql, "select date_format( _t0.`T_DATE`, '%Y' ) as _c0 from `test_all_type` as _t0 where _t0.`T_DATE` > '2019-04-01'");
		
		ajql = "select date_format(tDate,'%Y') from TestAllType group by date_format(tDate,'%Y')";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testKeywords() throws Exception {
		String ajql = "select 'sum' from Test3";
		String sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
		
		ajql = "select sum('count') from Test3";
		sql = compile(ajql);
		System.out.println(ajql + "\n>>\n" + sql);
	}
	
	@Test
	public void testDateFormatGroupMutil() {
		String ajql = "select tDate, DATE_FORMAT(tDate,'%Y'),DATE_FORMAT(tDate,'%Y-%M') from TestAllType group by DATE_FORMAT(tDate,'%Y'),DATE_FORMAT(tDate,'%Y-%M')";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>> SELECT ITEMS\n" + StringUtils.join(compiler.getSelectItems(), " | "));
		System.out.println(">> RAW SQL\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(),
				"select _t0.`T_DATE` as _c0, DATE_FORMAT( _t0.`T_DATE`, '%Y' ) as _c1, DATE_FORMAT( _t0.`T_DATE`, '%Y-%M' ) as _c2 from `test_all_type` as _t0 where ( 1 = 1 ) group by DATE_FORMAT( _t0.`T_DATE`, '%Y' ), DATE_FORMAT( _t0.`T_DATE`, '%Y-%M' )");
	}
	
	@Test
	public void testSelectItems() {
		String ajql = "select count(tPrimary) from TestAllType where tPrimary = '0000'";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>> SELECT ITEMS\n" + StringUtils.join(compiler.getSelectItems(), " | "));
		System.out.println(">> RAW SQL\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(),
				"select count( _t0.`T_PRIMARY` ) as _c0 from `test_all_type` as _t0 where _t0.`T_PRIMARY` = '0000'");
	}
	
	@Test
	public void testFulltextMatch() {
//		String ajql = "select tPrimary,tInt,tBool from TestAllType where tLong > 100 and match(tReference.tLong,tText) against ('123 abc NB' in boolean mode)";
//		QueryCompiler compiler = createCompiler(ajql);
//		System.out.println(ajql + "\n>> FULLTEXT MATCH\n" + compiler.getCompiledSql());
		
		String ajql2 = "select tPrimary from TestAllType where (tInt > 100 and tText match '123 abc NB')";
		QueryCompiler compiler2 = createCompiler(ajql2);
		System.out.println(ajql2 + "\n>> FULLTEXT MATCH\n" + compiler2.getCompiledSql());
	}
	
	@Test
	public void testBAnd() {
		String ajql = "select tPrimary from TestAllType where (tInt !& 1) and tInt && 1";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(), 
				"select _t0.`T_PRIMARY` as _c0 from `test_all_type` as _t0 where ( _t0.`T_INT` & 1 = 0 ) and _t0.`T_INT` & 1");
	}
	
	@Test
	public void testCount() {
		String ajql = "select count(tPrimary) from TestAllType";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(), 
				"select count( _t0.`T_PRIMARY` ) as _c0 from `test_all_type` as _t0 where ( 1 = 1 )");
		
		ajql = "select count(distinct tPrimary) from TestAllType";
		compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(),
				"select count( distinct _t0.`T_PRIMARY` ) as _c0 from `test_all_type` as _t0 where ( 1 = 1 )");
	}
	
	@Test
	public void testDateAggregator() {
		String ajql = "select year(tDate),quarter(tDate),month(tDate),week(tDate),date_format(tDate, '%Y') from TestAllType where year(tDate) = 2020";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(),
				"select year( _t0.`T_DATE` ) as _c0, quarter( _t0.`T_DATE` ) as _c1, month( _t0.`T_DATE` ) as _c2, week( _t0.`T_DATE` ) as _c3, date_format( _t0.`T_DATE`, '%Y' ) as _c4 from `test_all_type` as _t0 where year( _t0.`T_DATE` ) = 2020");
	}
	
	@Test
	public void testConcat() {
		String ajql = "select concat(tDate, '_', tText) from TestAllType group by concat(tInt, '_', tText) order by concat(tDouble, '_', tText) asc";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(),
				"select concat( _t0.`T_DATE`, '_', _t0.`T_TEXT` ) as _c0 from `test_all_type` as _t0 where ( 1 = 1 ) group by concat( _t0.`T_INT`, '_', _t0.`T_TEXT` ) order by concat( _t0.`T_DOUBLE`, '_', _t0.`T_TEXT` ) asc");
		
		// Unsupports `concat` in where
		ajql = "select tBool, concat('123', quarter(tDate), '_', tText, date_format(tDate, '%Y')), tPrimary from TestAllType where concat('123', quarter(tDate), '_', tText, date_format(tDate, '%Y')) = 123";
		compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		assertEquals(compiler.getCompiledSql(),
				"select _t0.`T_BOOL` as _c0, concat( '123', quarter( _t0.`T_DATE` ), '_', _t0.`T_TEXT`, date_format( _t0.`T_DATE`, '%Y' ) ) as _c1, _t0.`T_PRIMARY` as _c2 from `test_all_type` as _t0 where concat( '123', quarter( _t0.`T_DATE` ), '_', _t0.`T_TEXT`, date_format( _t0.`T_DATE`, '%Y' ) ) = 123");
		
		for (SelectItem item : compiler.getSelectItems()) {
			System.out.println(item);
		}
	}
	
	@Test
	public void testKws() {
		String ajql = "select count('year'), quarter('year') from Test4 where 'year' = '1' order by 'year'";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
		
		// Unsupport kw in where after
		assertEquals(compiler.getCompiledSql(),
				"select count( _t0.`YEAR` ) as _c0, quarter( _t0.`YEAR` ) as _c1 from `test4` as _t0 where 'year' = '1' order by 'year'");
		
	}

	// FIXME 不支持 order by 子句
	@Test
	public void testGroupConcat() {
		String ajql = "select group_concat(DISTINCT tText SEPARATOR ';;;') from TestAllType";
		QueryCompiler compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());

		ajql = "select group_concat(DISTINCT tText) from TestAllType";
		compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());

		ajql = "select group_concat(tText) from TestAllType";
		compiler = createCompiler(ajql);
		System.out.println(ajql + "\n>>\n" + compiler.getCompiledSql());
	}
}
