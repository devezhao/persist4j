package cn.devezhao.persist4j.query;

import cn.devezhao.persist4j.dialect.Type;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, May 25, 2009
 * @version $Id: NativeQuery.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public interface NativeQuery extends IQuery<NativeQuery> {

	NativeQuery setInParameterType(Type... fTypes);
	
	NativeQuery setReturnType(Type... fTypes);
	
	@Override
    NativeQuery reset();
}
