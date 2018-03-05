package cn.devezhao.persist4j.query.compiler;

import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.query.compiler.JoinTree.JoinNode;

/**
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 10/11/08
 * @version $Id: JoinField.java 120 2016-01-07 11:49:05Z zhaoff@wisecrm.com $
 */
public class JoinField implements SelectItem {
	private static final long serialVersionUID = 6578159656188301525L;
	
	private final JoinNode tableNode;
	private final Field field;
	private final String fieldPath;
	private final SelectItemType type;

	private String fName;
	private String fAlias;
	private Integer index;
	
	private String full = null;
	
	private String aggre;

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

	public String getFieldPath() {
		return fieldPath;
	}

	public String getName() {
		return fName;
	}
	
	public String getAlias() {
		return fAlias;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Field getField() {
		return field;
	}

	public SelectItemType getType() {
		return type;
	}
	
	
	// --------------------------------------------------------------------- 
	
	protected void setAggregator(String aggre) {
		if (this.aggre != null)
			throw new IllegalStateException("Aggregator was set");
		this.aggre = aggre;
	}
	
	public String getAggregator() {
		return aggre;
	}
	
	protected String as(int increase, Dialect dialect) {
		if (full != null) {
			return full;
		}
		
		index = increase;
		fName = String.format("%s.%s", 
				tableNode.getAlias(), dialect.quote(field.getPhysicalName()));
		fAlias = JoinTree.COLUMN_ALIAS_PREFIX + index;
		return (full = fName + " as " + fAlias);
	}
	
	@Override
	public String toString() {
		return String.format("[%s->%s->%s]", fieldPath, fName, fAlias);
	}
}
