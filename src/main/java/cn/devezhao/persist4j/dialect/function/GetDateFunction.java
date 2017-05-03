package cn.devezhao.persist4j.dialect.function;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.Type;

/**
 * <tt>$GETDATE</tt>
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-9-2
 * @version $Id: GetDateFunction.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class GetDateFunction implements SqlFunction {

	public String getToken() {
		return "getdate";
	}
	
	public Type getReturnType() {
		return FieldType.DATE;
	}

	public boolean hasArguments() {
		return false;
	}

	public String render(Object[] arguments) {
		return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	}
}
