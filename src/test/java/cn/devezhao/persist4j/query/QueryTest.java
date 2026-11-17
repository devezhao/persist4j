package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.BaseTest;
import cn.devezhao.persist4j.Query;
import cn.devezhao.persist4j.engine.ID;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertThrows;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 04/16/2020
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

    @Test
    public void testFnForDate() throws Exception {
        readySchemes();
        Object[] res = createQuery("select count(tPrimary) from TestAllType where ( DAYOFWEEK(tDate) = 5 )")
                .unique();
        System.out.println(Arrays.toString(res));
    }

    @Test
    public void testAvg() throws Exception {
        readySchemes();
        Object[] res = createQuery("select count(tPrimary),avg(tInt) from TestAllType")
                .unique();
        System.out.println(Arrays.toString(res));
    }

	@Test
	public void testParameterStringCompat() throws Exception {
		readySchemes();
		String id = ID.newId(0).toLiteral();

		createQuery("select tPrimary from TestAllType where tPrimary = ?")
				.setParameter(1, id).unique();
		createQuery("select tPrimary from TestAllType where tReference = ?")
				.setParameter(1, id).unique();
		createQuery("select tPrimary from TestAllType where tAnyReference = ?")
				.setParameter(1, id).unique();
		createQuery("select tPrimary from TestAllType where tDate > ?")
				.setParameter(1, "2019-04-01").unique();
		createQuery("select tPrimary from TestAllType where tTimestamp > ?")
				.setParameter(1, "2019-04-01 00:00:00").unique();
		createQuery("select tPrimary from TestAllType where tTime > ?")
				.setParameter(1, "10:00:00").unique();
		createQuery("select tPrimary from TestAllType where tBool = ?")
				.setParameter(1, "true").unique();

		assertThrows(IllegalArgumentException.class, () ->
				createQuery("select tPrimary from TestAllType where tPrimary = ?")
						.setParameter(1, "not-an-id").unique());
		assertThrows(IllegalArgumentException.class, () ->
				createQuery("select tPrimary from TestAllType where tDate > ?")
						.setParameter(1, "2019-13-01").unique());
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
