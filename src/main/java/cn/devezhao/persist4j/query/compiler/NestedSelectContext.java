package cn.devezhao.persist4j.query.compiler;

import java.io.Serializable;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.query.compiler.JoinTree.JoinNode;

/**
 * @author zhaofang123@gmail.com
 * @since 10/27/2018
 */
public class NestedSelectContext implements Serializable {
	private static final long serialVersionUID = 7585161604688620934L;
	
	private Entity master;
	private JoinNode masterRoot;
	private int tableIncrease;

	protected NestedSelectContext(Entity master, JoinNode masterRoot, int tableIncrease) {
		this.master = master;
		this.masterRoot = masterRoot;
		this.tableIncrease = tableIncrease;
	}

	public Entity getMaster() {
		return master;
	}
	
	public JoinNode getMasterRoot() {
		return masterRoot;
	}

	public int getTableIncrease() {
		return tableIncrease;
	}
}
