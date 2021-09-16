package cn.devezhao.persist4j.query.compiler;

import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.query.compiler.JoinTree.JoinNode;

import java.util.Objects;

/**
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 10/11/08
 * @version $Id: JoinField.java 120 2016-01-07 11:49:05Z zhaofang123@gmail.com $
 */
public class JoinField implements SelectItem {
	private static final long serialVersionUID = 6578159656188301525L;
	
	private final JoinNode tableNode;
	private final Field field;
	private final String fieldPath;
	private final SelectItemType type;

	private String name;
	private String alias;
	private Integer index;
	
	private String fullPath = null;
	
	private String aggregator;
	private String aggregatorSibling;
	private String aggregatorMode;

	JoinField(JoinNode tableNode, Field field, String fieldPath, SelectItemType type) {
		this.tableNode = tableNode;
		this.field = field;
		this.fieldPath = fieldPath;
		this.type = type;
	}
	
	JoinField(JoinField joinField, SelectItemType type) {
		this.tableNode = joinField.tableNode;
		this.field = joinField.field;
		this.fieldPath = joinField.fieldPath;
		this.type = type;
	}
	
	// --------------------------------------------------------------------- Implements SelectItem

	@Override
    public String getFieldPath() {
		return fieldPath;
	}

	@Override
    public String getName() {
		return name;
	}
	
	@Override
    public String getAlias() {
		return alias;
	}
	
	@Override
    public int getIndex() {
		return index;
	}
	
	@Override
    public Field getField() {
		return field;
	}

	@Override
    public SelectItemType getType() {
		return type;
	}
	
	
	// --------------------------------------------------------------------- 
	
	protected void setAggregator(String aggregator, String aggregatorSibling) {
		this.aggregator = aggregator;
		this.aggregatorSibling = aggregatorSibling;
	}
	
	public String getAggregator() {
		return aggregator;
	}
	
	public String getAggregatorSibling() {
		return aggregatorSibling;
	}
	
	protected void setAggregatorMode(String mode) {
		this.aggregatorMode = mode;
	}
	
	public String getAggregatorMode() {
		return aggregatorMode;
	}
	
	protected String as(int increase, Dialect dialect) {
		if (fullPath != null) {
			return fullPath;
		}
		
		index = increase;
		if (tableNode == null) {
			name = "$";
		} else {
			name = String.format("%s.%s", 
					tableNode.getAlias(), dialect.quote(field.getPhysicalName()));
		}
		alias = JoinTree.COLUMN_ALIAS_PREFIX + index;
		return (fullPath = name + " as " + alias);
	}
	
	@Override
	public String toString() {
		if (getAggregator() != null) {
			return String.format("%s%s[%s->%s->%s]", 
					getAggregator(), (getAggregatorMode() == null ? "" : ("," + getAggregatorMode())),
					fieldPath, name, alias);
		} else {
			return String.format("[%s->%s->%s]", fieldPath, name, alias);
		}
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return o.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableNode, field, fieldPath, aggregator, aggregatorMode);
    }
}
