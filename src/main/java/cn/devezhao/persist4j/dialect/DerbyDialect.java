package cn.devezhao.persist4j.dialect;

/**
 * Derby
 * 
 * @author zhaofang123@gmail.com
 * @since 02/27/2017
 */
public class DerbyDialect extends Dialect {
	private static final long serialVersionUID = 5546940510409644552L;

	@Override
	public String getDialectName() {
		return "derby";
	}

	@Override
	public char getStartQuote() {
		return QUOTED[0];
	}

	@Override
	public char getClosedQuote() {
		return QUOTED[1];
	}

	@Override
	public String insertLimit(String query, int limit, int offset) {
		// TODO Auto-generated method stub
		return null;
	}
}
