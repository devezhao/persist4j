package cn.devezhao.persist4j.dialect.function;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Usage: <tt>$getdate()</tt>
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-9-2
 * @version $Id: GetDateFunction.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class GetDateFunction implements SqlFunction {

	@Override
    public String getToken() {
		return "getdate";
	}
	
	@Override
    public Type getReturnType() {
		return FieldType.DATE;
	}

	@Override
    public String eval(Object[] arguments) {
		return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	}
}
