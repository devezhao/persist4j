package cn.devezhao.persist4j.query.compiler;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.devezhao.persist4j.dialect.Dialect;


/**
 * 
 * 
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 0.1, 11/22/2008
 * @version $Id: JoinTree.java 121 2016-01-08 04:07:07Z zhaoff@wisecrm.com $
 */
public class JoinTree implements Serializable {
	
	private static final long serialVersionUID = -868044162738823519L;
	
	static final String TABLE_ALIAS_PREFIX = "_t";
	static final String COLUMN_ALIAS_PREFIX = "_c";
	
	private JoinNode rootNode;
	private int tableIncrease = -1;
	
	private String tableAliasPrefix = TABLE_ALIAS_PREFIX;
	
	/**
	 * Create a new JoinTree
	 * 
	 * @param table
	 */
	public JoinTree(String table) {
		this.rootNode = new JoinNode(table);
	}
	
	/**
	 * @param table
	 */
	public JoinTree(String table, String tableAliasPrefix) {
		this.rootNode = new JoinNode(table);
		this.tableAliasPrefix = tableAliasPrefix;
	}
	
	/**
	 * Gets root node
	 * 
	 * @return
	 */
	public JoinNode getRootJoinNode() {
		return rootNode;
	}
	
	/**
	 * Adds a node to root node
	 * 
	 * @param node
	 */
	public void addChildJoin(JoinNode node) {
		addChildJoin(node, rootNode);
	}
	
	/**
	 * Adds a node to root node
	 * 
	 * @param table
	 * @param joinOnLeft
	 * @param joinOnRight
	 * @return the added node
	 */
	public JoinNode addChildJoin(String table, String joinOnLeft, String joinOnRight) {
		return addChildJoin(table, joinOnLeft, joinOnRight, rootNode);
	}
	
	/**
	 * Adds a node to specify parent node
	 * 
	 * @param node
	 * @param parentNode
	 */
	public void addChildJoin(JoinNode node, JoinNode parentNode) {
		parentNode.addChildJoin(node);
	}
	
	/**
	 * Adds a node to specify parent node
	 * 
	 * @param table
	 * @param joinOnLeft
	 * @param joinOnRight
	 * @param parentNode
	 * @return the added node
	 */
	public JoinNode addChildJoin(String table, String joinOnLeft, String joinOnRight, JoinNode parentNode) {
		JoinNode node = hasEquallyNode(table, joinOnLeft, joinOnRight, parentNode);
		if (node == null) {
			node = new JoinNode(table, joinOnLeft, joinOnRight, parentNode);
			addChildJoin(node, parentNode);
		}
		return node;
	}
	
	/**
	 * to join sql
	 * 
	 * @return
	 */
	public String toJoinsSQL(Dialect dialect) {
		StringBuilder sql = new StringBuilder();
		sql.append(rootNode.as(dialect));
		
		for (JoinNode node : rootNode.getChildJoins()) {
			sql.append(asJoinString(node, dialect));
		}
		return sql.toString();
	}
	
	/*
	 * join call-back
	 */
	private String asJoinString(JoinNode node, Dialect dialect) {
		StringBuilder joinSql = new StringBuilder();
		if (node.parentJoin.getAlias() == null)
			joinSql.append(node.parentJoin.as(dialect));
		joinSql.append(" left join ");
		
		if (node.getChildJoins().length > 0) {  // has child join, using () quote
			joinSql.append('(');
			for (JoinNode jNode : node.getChildJoins()) {
				joinSql.append(asJoinString(jNode, dialect));
			}
			joinSql.append(')');
		}
		
		if (node.getAlias() == null)
			joinSql.append(node.as(dialect));
		
		joinSql.append(" on ")
			.append((node.parentJoin.getAlias() + '.' + dialect.quote(node.joinOnLeft)))
			.append(" = ")
			.append((node.getAlias() + '.' + dialect.quote(node.joinOnRight)));
		return joinSql.toString();
	}
	
	/**
	 * has equals node?
	 */
	private JoinNode hasEquallyNode(String table, String joinOnLeft, String joinOnRight, JoinNode parentNode) {
		if (parentNode == null)
			return null;
		
		for (JoinNode node : parentNode.getChildJoins()) {
			if (node.table.equals(table)
					&& node.joinOnLeft.equals(joinOnLeft)
					/*&& node.joinOnRight.equals(joinOnRight)*/
					&& parentNode.table.equals(node.parentJoin.table))
			return node;
		}
		return null;
	}
	
	private int getTableIncrease() {
		tableIncrease++;
		return tableIncrease;
	}
	
	
	/**
	 * Double-ended JoinNode
	 */
	public class JoinNode implements Serializable {
		
		private static final long serialVersionUID = -9134659400293558001L;
		
		String table;
		String joinOnLeft;
		String joinOnRight;

		JoinNode parentJoin;
		Set<JoinNode> childJoins;

		String alias;

		JoinNode(String table) {
			this.childJoins = new LinkedHashSet<JoinNode>();
			this.table = table;
		}
		
		JoinNode(String table, String joinOnLeft, String joinOnRight, JoinNode parentJoin) {
			this(table);
			this.joinOnLeft = joinOnLeft;
			this.joinOnRight = joinOnRight;
			this.parentJoin = parentJoin;
			
			parentJoin.addChildJoin(this);
		}

		void addChildJoin(JoinNode childJoin) {
			childJoins.add(childJoin);
		}

		String as(Dialect dialect) {
//			String pn = (parentJoin == null ? null : parentJoin.getAlias());
//			if (pn != null)
//				alias = pn + "_" + getTableIncrease();
//			else
			alias = tableAliasPrefix + getTableIncrease();
			
			return String.format(
					"%s as %s",
					dialect.quote(table), alias);
		}

		public String getAlias() {
			return this.alias;
		}

		JoinNode[] getChildJoins() {
			return childJoins.toArray(new JoinNode[] {});
		}
		
		@Override
		public String toString() {
			if (this.parentJoin == null)
				return String.format("<%s>", this.table);
			
			return String.format(
					"<%s, %s = %s, %s>",
					table, joinOnLeft, joinOnRight, parentJoin.table);
		}
	}
}