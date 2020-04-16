package cn.devezhao.persist4j.query;

import org.junit.Test;

import cn.devezhao.persist4j.BaseTest;
import cn.devezhao.persist4j.Query;
import cn.devezhao.persist4j.engine.ID;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @see 04/16/2020
 */
public class QueryTest extends BaseTest {

	@Test
	public void testParameterInNested() throws Exception {
		readySchemes();
		createQuery("select tPrimary from TestAllType where tDouble > ? and"
				+ " tInt in (select tInt from TestAllType where tPrimary = ?)")
				.setParameter(1, 1d)
				.setParameter(2, ID.newId(0))
				.unique();
	}
	
	@Test
	public void testParameterExistsNested() throws Exception {
		readySchemes();
		createQuery("select tPrimary from TestAllType where"
				+ " exists (select t2Primary from Test2 where ^tReference = t2Reference and t2Int = ?) and"
				+ " tText = ?")
				.setParameter(1, 1)
				.setParameter(2, "123")
				.unique();
	}
	
	@Test
	public void testParameterInAndExistsNested() throws Exception {
		readySchemes();
		createQuery("select tPrimary from TestAllType where"
				+ " exists (select t2Primary from Test2 where ^tReference = t2Reference and t2Int = ?) and"
				+ " tText = ? and"
				+ " tInt in (select tInt from TestAllType where tPrimary = ?)")
				.setParameter(1, 1)
				.setParameter(2, "123")
				.setParameter(3, ID.newId(0))
				.unique();
	}

	/**
	 * 创建查询
	 * 
	 * @param ajql
	 * @return
	 */
	private Query createQuery(String ajql) {
		return getPersistManagerFactory().createQuery(ajql);
	}
}
