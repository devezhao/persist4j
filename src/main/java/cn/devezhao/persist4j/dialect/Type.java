package cn.devezhao.persist4j.dialect;

/**
 * 字段类型
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: Type.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public interface Type {

	Integer getMask();

	String getName();

	Editor getFieldEditor();
}
