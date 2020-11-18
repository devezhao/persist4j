package cn.devezhao.persist4j.query.compiler;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.query.compiler.JoinTree.JoinNode;

import java.io.Serializable;

/**
 * @author zhaofang123@gmail.com
 * @since 10/27/2018
 */
public class NestedSelectContext implements Serializable {
	private static final long serialVersionUID = 7585161604688620934L;
	
	private Entity root;
	private JoinNode rootNode;
	private int tableIncrease;

	protected NestedSelectContext(Entity root, JoinNode rootNode, int tableIncrease) {
		this.root = root;
		this.rootNode = rootNode;
		this.tableIncrease = tableIncrease;
	}

	public Entity getRoot() {
		return root;
	}
	
	public JoinNode getRootNode() {
		return rootNode;
	}

	public int getTableIncrease() {
		return tableIncrease;
	}
}
