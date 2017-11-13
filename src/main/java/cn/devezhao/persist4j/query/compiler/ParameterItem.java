package cn.devezhao.persist4j.query.compiler;

import java.io.Serializable;

import cn.devezhao.persist4j.Field;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Apr 3, 2009
 * @version $Id: ParameterItem.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class ParameterItem implements Serializable {
	private static final long serialVersionUID = 6919840306703366608L;
	
	private String named;
	private int index;
	private Field field;

	public ParameterItem(String named, int index, Field field) {
		this.named = named;
		this.index = index;
		this.field = field;
	}

	public String getNamed() {
		return named;
	}

	public int getIndex() {
		return index;
	}

	public Field getField() {
		return field;
	}
}
