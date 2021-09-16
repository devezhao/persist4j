package cn.devezhao.persist4j.query.compiler;

import cn.devezhao.persist4j.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Apr 3, 2009
 * @version $Id: ParameterItem.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(named, index);
    }
}
