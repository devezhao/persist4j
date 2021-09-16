package cn.devezhao.persist4j.query.compiler;

import antlr.collections.AST;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.Filter;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.engine.SqlExecutorContext;
import cn.devezhao.persist4j.query.compiler.JoinTree.JoinNode;
import cn.devezhao.persist4j.query.compiler.antlr.AjQLParser;
import cn.devezhao.persist4j.query.compiler.antlr.AjQLParserTokenTypes;
import cn.devezhao.persist4j.query.compiler.antlr.ParserException;
import cn.devezhao.persist4j.query.compiler.antlr.ParserHelper;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;

/**
 * AJQL 编译器
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Jan 22, 2009
 * @version $Id: QueryCompiler.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class QueryCompiler implements Serializable {
	private static final long serialVersionUID = -47840212541257542L;

	private static final Log LOG = LogFactory.getLog(QueryCompiler.class);
	
	public static final char NAME_FIELD_PREFIX = '&';

	public static final char IDLABEL_FIELD_PREFIX = '#';  //  NOTE reserved

	public static final char CUSTOM_FUNC_PREFIX = '$';

	public static final char FORCE_JOIN_PREFIX 	= '^';
	
	public static final char NAMED_PARAM = ':';

	public static final char INDEX_PARAM = '?';
	
	private String ajql;
	private List<SelectItem> selectList = new LinkedList<>();
	private String compiledSql = null;
	private Entity rootEntity;
	
	final private Map<String, JoinField> joinFieldMap = new HashMap<>();
	
	final private Map<String, ParameterItem> inParameters = new HashMap<>();
	private int inParametersIndex = 0;
	
	private SqlExecutorContext sqlExecutorContext;
	
	private int nestedTableIncrease = 0;
	private NestedSelectContext nestedSelectContext;

	/**
	 * @param ajql
	 */
	public QueryCompiler(String ajql) {
		this.ajql = ajql;
	}
	
	/**
	 * @param context
	 * @return
	 * @throws CompileException
	 */
	public String compile(SqlExecutorContext context) throws CompileException {
		return compile(context, null);
	}
	
	/**
	 * 编译SQL
	 * 
	 * @param context
	 * @param filter
	 * @return
	 */
	public String compile(SqlExecutorContext context, Filter filter) throws CompileException {
		if (compiledSql != null) {
			return compiledSql;
		}
		
		this.sqlExecutorContext = context;
		
		AjQLParser parser = ParserHelper.createAjQLParser(ajql, true);
		try {
			parser.statement();
		} catch (Exception ex) {
			handleParseException(ex);
		}
		compiledSql = compileQuery( parser.getAST(), filter );
		return compiledSql;
	}
	
	/**
	 * 获取查询主实体
	 * 
	 * @return
	 */
	public Entity getRootEntity() {
		throwIfUncompile();
		return rootEntity;
	}
	
	/**
	 * 获取查询项（字段）
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public SelectItem[] getSelectItems() throws IllegalStateException {
		throwIfUncompile();
		return selectList.toArray(new SelectItem[0]);
	}
	
	/**
	 * 获取编译好的 SQL
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public String getCompiledSql() throws IllegalStateException {
		throwIfUncompile();
		return compiledSql;
	}
	
	/**
	 * 获取入参
	 * 
	 * @return
	 * @throws IllegalStateException
	 */
	public Map<String, ParameterItem> getInParameters() throws IllegalStateException {
		throwIfUncompile();
		return inParameters;
	}
	
	// ----------------------------------------------------------- Compile
	
	/**
	 * 主编译方法
	 * 
	 * @param select
	 * @param filter
	 * @return
	 */
	private String compileQuery(AST select, Filter filter) {
		AST from = select.getNextSibling();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Compiling select clause [ " + select.toStringList() + " ]");
		}
		
		Entity entity = sqlExecutorContext.getEntity(from.getFirstChild().getText());
		this.rootEntity = entity;
		final JoinTree aJTree = new JoinTree(entity.getPhysicalName(),
				nestedSelectContext == null ? -1 : nestedSelectContext.getTableIncrease() /* nested-sql */ );
		
		Set<JoinField> distinctFields = new HashSet<>();
		Map<JoinField, AST> mutliFieldsAggregators = new HashMap<>();
		
		List<JoinField> selectFields = new LinkedList<>();
		AST item = select.getFirstChild();
		do {
			JoinField aJF;
			
			if (item.getType() == AjQLParserTokenTypes.DISTINCT) {
				item = item.getNextSibling();
				aJF = bindJoinField(aJTree, entity, item, SelectItemType.Field);
				distinctFields.add(aJF);
				
			} else if (item.getType() == AjQLParserTokenTypes.CONCAT) {
				aJF = bindJoinField(aJTree, entity, item, SelectItemType.Aggregator);
				aJF.setAggregator(item.getText(), null);
				mutliFieldsAggregators.put(aJF, item);
				
			} else if (ParserHelper.isAggregator(item.getType())) {
				AST column = item.getFirstChild();
				boolean withDistinct = column.getType() == AjQLParserTokenTypes.DISTINCT;
				if (withDistinct) {
					column = column.getNextSibling();
				}
				
				aJF = bindJoinField(aJTree, entity, column, SelectItemType.Aggregator);
				aJF.setAggregator(item.getText(), withDistinct ? "distinct" : null);
				
				if (ParserHelper.isAggregatorWithMode(item.getType())) {
					String mode = column.getNextSibling().getNextSibling().getText();  // [, '%Y']
					aJF.setAggregatorMode(mode);
				}
				
			} else {
				aJF = bindJoinField(aJTree, entity, item, SelectItemType.Field);
			}
			
			selectFields.add(aJF);
		} while ((item = item.getNextSibling()) != null);
		selectList.clear();
		selectList.addAll(selectFields);
		
		// Fields in where and group-by and having and order-by 
		AST where = null, group = null, having = null, order = null;
		AST next = from.getNextSibling();
		while (next != null) {
			switch (next.getType()) {
			case AjQLParserTokenTypes.WHERE:
				where = next;
				break;
			case AjQLParserTokenTypes.GROUP:
				group = next;
				break;
			case AjQLParserTokenTypes.HAVING:
				having = next;
				break;
			case AjQLParserTokenTypes.ORDER:
				order = next;
				break;
			}
			
			findJoinFields(next, aJTree, entity);
			next = next.getNextSibling();
		}
		
		if (filter != null) {
			String fstrs = filter.evaluate(this.rootEntity);
			// Patch #whereClause()
			fstrs = "where " + ( (where == null) ? fstrs : "1=1 and " + fstrs );
			
			AjQLParser parser = ParserHelper.createAjQLParser(fstrs);
			try {
				parser.whereClause();
			} catch (Exception ex) {
				handleParseException(ex);
			}
			
			AST nood = parser.getAST();
			findJoinFields(nood, aJTree, entity);
			
			if (where == null) {
				where = nood;
			} else {
				// remove 1=1
				AST and = nood.getFirstChild().getNextSibling().getNextSibling().getNextSibling();  // 1 -> = -> 1 -> AND
				where.addChild(and);
			}
		}
		
		StringBuilder sql = new StringBuilder("select ");
		// Generate join clause and table alias
		String aJQL = aJTree.toJoinsSQL(sqlExecutorContext.getDialect());
		
		int columnIncrease = 0;
		Iterator<JoinField> iter = selectFields.iterator();
		for (;; columnIncrease++) {
			JoinField aJF = iter.next();
			String clause = aJF.as(columnIncrease, sqlExecutorContext.getDialect());
			
			if (aJF.getType() == SelectItemType.Aggregator) {
				if ("CONCAT".equalsIgnoreCase(aJF.getAggregator())) {
					AST node = mutliFieldsAggregators.get(aJF);
					StringBuilder concat = compileByClause(node, "concat");
					concat.insert(6, "( ").append(") as ");
					clause = concat.toString();
					
				} else {
					if (aJF.getAggregatorMode() != null) {
						clause = String.format("%s( %s, '%s' ) as ", aJF.getAggregator(), aJF.getName(), aJF.getAggregatorMode());
					} else if (aJF.getAggregatorSibling() != null) {
						clause = String.format("%s( %s %s ) as ", aJF.getAggregator(), aJF.getAggregatorSibling(), aJF.getName());
					} else {
						clause = String.format("%s( %s ) as ", aJF.getAggregator(), aJF.getName());
					}
				}
				
				clause += JoinTree.COLUMN_ALIAS_PREFIX + columnIncrease;
			}
			
			if (distinctFields.contains(aJF)) {
				sql.append("distinct ");
			}
			sql.append(clause);
			if (iter.hasNext()) {
				sql.append(", ");
			} else {
				break;
			}
		}
		sql.append(" from ").append(aJQL).append(" where ");
		
		StringBuilder clause = new StringBuilder();
		if (where == null) {
			sql.append("( 1 = 1 ) ");
		} else {
			JoinField aJF = null;
			
			next = where.getFirstChild();
			int prevType = 0;
			AST lastField = null;
			do {
				int type = next.getType();
				String text = next.getText();
				
				if (next.getNextSibling() != null
						&& next.getNextSibling().getType() == AjQLParserTokenTypes.MATCH) {
					lastField = next;
					continue;
				}
				
				switch (type) {
				case AjQLParserTokenTypes.IDENT:
					aJF = getJoinField(next, null, sqlExecutorContext.getDialect());
					clause.append(aJF.getName());
					break;
				case AjQLParserTokenTypes.QUOTED_STRING:
					clause.append('\'').append(text).append('\'');
					break;
				case AjQLParserTokenTypes.IN:
					clause.append(compileInClause(next));
					break;
				case AjQLParserTokenTypes.EXISTS:
					clause.append(compileExistsClause(next, aJTree.getRootJoinNode()));
					break;
				case AjQLParserTokenTypes.QUESTION_MARK:
				case AjQLParserTokenTypes.NAMED_PARAM:
					clause.append(INDEX_PARAM);
					inParametersIndex++;
					text = (text.charAt(0) == INDEX_PARAM) ? String.valueOf(inParametersIndex) : text;
					inParameters.put(text, new ParameterItem(text, inParametersIndex, aJF.getField()));
					break;
				case AjQLParserTokenTypes.MATCH:
					clause.append(compileMatchClause(next, lastField));
					lastField = null;
					next = next.getNextSibling();
					break;
				case AjQLParserTokenTypes.BAND: 
				case AjQLParserTokenTypes.NBAND: 
					clause.append('&');
					break;
				default:
					if (ParserHelper.isAggregator(type)) {
						clause.append(compileAggregator(next));
						
					} else if (ParserHelper.isInIgnore(type)) {
						clause.append(text);
						
						// 按位取反 a % b = 0
						if (prevType == AjQLParserTokenTypes.NBAND) {
							clause.append(" = 0");
						}
						
					} else {
						LOG.warn("Unknow token in `where` clause : <" + type + ", " + text + ">");
					}
				}
				
				clause.append(' ');
				prevType = type;
			} while (next != null && (next = next.getNextSibling()) != null);
			
			sql.append(clause);
		}
		
		if (group != null) {
			sql.append(compileGroupByClause(group, having));
		}
		
		if (order != null) {
			sql.append(compileOrderByClause(order));
		}
		
		return sql.toString().trim();
	}
	
	/**
	 * 编译 IN 子语句
	 * 
	 * @param in
	 * @return
	 */
	private String compileInClause(AST in) {
		AST next = in.getFirstChild();
		StringBuilder clause = new StringBuilder();
		JoinField aJF = getJoinField(next, null, sqlExecutorContext.getDialect());
		clause.append(aJF.getName());
		if (next.getNextSibling().getType() == AjQLParserTokenTypes.NOT) {
			clause.append(" not");
			next = next.getNextSibling();
		}
		clause.append(" in ( ");
		
		while ((next = next.getNextSibling()) != null) {
			String text = next.getText();
			int ttype = next.getType();
			switch (ttype) {
			case AjQLParserTokenTypes.QUOTED_STRING:
				clause.append('\'').append(text).append('\'');
				break;
			case AjQLParserTokenTypes.SELECT:
				
				String nestedSql = compileNestedSelect(null, next);
				// NOTE: nested select return now
				return clause.append(nestedSql).append(" )").toString();
				
			default:
				if (ParserHelper.isInIgnoreValue(ttype) || ParserHelper.isInIgnore(ttype)) {
					clause.append(text);
				} else {
					LOG.warn("Unknow token in `in` clause [ " + ttype + ":" + text + " ]");
				}
				break;
			}

			if (ttype == AjQLParserTokenTypes.COMMA) {
				clause.append(' ');
			}
		}
		return clause.append(" )").toString();
	}
	
	/**
	 * 编译 EXISTS 子语句
	 * @param exists
	 * @param root
	 * @return
	 */
	private String compileExistsClause(AST exists, JoinNode root) {
		StringBuilder clause = new StringBuilder();
		clause.append("exists ( ");
		
		AST next = exists.getFirstChild();
		do {
			String text = next.getText();
			int ttype = next.getType();
			switch (ttype) {
			case AjQLParserTokenTypes.QUOTED_STRING:
				clause.append('\'').append(text).append('\'');
				break;
			case AjQLParserTokenTypes.SELECT:
				
				String nestedSql = compileNestedSelect(root, next);
				// NOTE: nested select return now
				return clause.append(nestedSql).append(" )").toString();
				
			default:
				if (ParserHelper.isInIgnoreValue(ttype) || ParserHelper.isInIgnore(ttype)) {
					clause.append(text);
				} else {
					LOG.warn("Unknow token in `exists` clause [ " + ttype + ":" + text + " ]");
				}
				break;
			}
			
			if (ttype == AjQLParserTokenTypes.COMMA) {
				clause.append(' ');
			}
		} while ((next = next.getNextSibling()) != null);
		return clause.append(" )").toString();
	}
	
	private String compileNestedSelect(JoinNode root, AST select) {
		NestedSelectContext context = null;
		if (root != null) {
			context = new NestedSelectContext(rootEntity, root, nestedTableIncrease++);
		}
		QueryCompiler nested = new QueryCompiler(sqlExecutorContext, context, inParametersIndex);
		
		String nestedSql = nested.compileNestedSelect(select);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Compiled nested select clause [ " + nestedSql + " ]");
		}
		
		if (!nested.inParameters.isEmpty()) {
			this.inParameters.putAll(nested.inParameters);
			this.inParametersIndex = nested.inParametersIndex;
		}
		
		return nestedSql;
	}
	
	/**
	 * 编译 GROUP BY 子语句
	 * 
	 * @param by
	 * @param having
	 * @return
	 */
	private String compileGroupByClause(AST by, AST having) {
		StringBuilder clause = new StringBuilder();
		clause.append( compileByClause(by, "group by ") );
		AST with;
		if (having != null) {
			clause/*.append(' ')*/.append( compileByClause(having, "having ") );
			with = having.getNextSibling();
		} else {
			with = by.getNextSibling();
		}
		
		if (with != null && with.getType() == AjQLParserTokenTypes.WITH) {
			AST rollup = with.getFirstChild();
			if (rollup != null && rollup.getType() == AjQLParserTokenTypes.ROLLUP) {
				clause.append("with rollup ");
			}
		}
		
		return clause.toString();
	}
	
	/**
	 * 编译 ORDER BY 子语句
	 * 
	 * @param by
	 * @return
	 */
	private String compileOrderByClause(AST by) {
		return compileByClause(by, "order by ").toString();
	}
	
	/**
	 * 编译 [ORDER|GROYP] BY 子语句
	 * 
	 * @param ast
	 * @param who
	 * @return
	 */
	private StringBuilder compileByClause(AST ast, String who) {
		StringBuilder clause = new StringBuilder(who);
		
		AST next;
		if (who.toUpperCase().startsWith("HAVING")
				|| "CONCAT".equalsIgnoreCase(who)) {
			next = ast.getFirstChild();
		} else {
			next = ast.getFirstChild().getNextSibling();
		}
		
		do {
			int type = next.getType();
			JoinField aJF;
			
			switch (type) {
			case AjQLParserTokenTypes.COMMA:
				clause.insert(clause.length() - 1, ',');
				break;
			case AjQLParserTokenTypes.IDENT:
				aJF = getJoinField(next, null, sqlExecutorContext.getDialect());
				clause.append(aJF.getName()).append(' ');
				break;
			case AjQLParserTokenTypes.ASC:
			case AjQLParserTokenTypes.DESC:
				clause.append(next.getText()).append(' ');
				break;
			case AjQLParserTokenTypes.QUOTED_STRING:
				clause.append('\'').append(next.getText()).append("' ");
				break;
			default:
				if (ParserHelper.isAggregator(type)) {
					clause.append(compileAggregator(next));
				} else {
					// @see ParserLeader.isInIgnore(type)
					clause.append(next.getText());
				}
				clause.append(' ');
			}
		} while ((next = next.getNextSibling()) != null);
		return clause;
	}
	
	/**
	 * 编译 MATCH ... AGAINST
	 * 
	 * @param match
	 * @param field
	 * @return
	 */
	private String compileMatchClause(AST match, AST field) {
		JoinField aJF = getJoinField(field, null, sqlExecutorContext.getDialect());
		AST query = match.getNextSibling();
		return String.format("match (%s) against ('%s' in boolean mode)",
				aJF.getName(),
				query.getText());
	}
	
	/**
	 * @param next
	 * @return
	 */
	private String compileAggregator(AST next) {
		if (ParserHelper.isAggregatorWithNested(next.getType())) {
			StringBuilder concat = compileByClause(next, "concat");
			concat.insert(6, "( ").append(")");
			return concat.toString();
		}
		
		JoinField aJF = getJoinField(next.getFirstChild(), next, sqlExecutorContext.getDialect());
		StringBuilder clause = new StringBuilder(next.getText())
				.append("( ");
		if (aJF.getAggregatorMode() != null) {
			clause.append(aJF.getName()).append(", '").append(aJF.getAggregatorMode()).append('\'');
		} else {
			if (aJF.getAggregatorSibling() != null) {
				clause.append(aJF.getAggregatorSibling()).append(' ');
			}
			clause.append(aJF.getName());
		}
		clause.append(" )");
		return clause.toString();
	}
	
	/**
	 * 获取连接（join）字段
	 * 
	 * @param tree
	 * @param entity
	 * @param item
	 * @param type
	 * @return
	 */
	private JoinField bindJoinField(JoinTree tree, Entity entity, AST item, SelectItemType type) {
		final String itemName = item.getText();
		JoinField ifExists = joinFieldMap.get(itemName);
		if (ifExists != null) {
			return new JoinField(ifExists, type);
		}
		
		// 虚拟 JF
		if (ParserHelper.isAggregatorWithNested(item.getType())) {
			findJoinFields(item, tree, entity);

			return new JoinField(null, null, itemName, type);
		}
		
		String path = itemName;
		if (path.charAt(0) == NAME_FIELD_PREFIX) {  // eg. `&accountId`
			type = SelectItemType.NameField;
			path = path.substring(1);
			
			String[] joined = path.split("\\.");
			path += '.';
			Entity currentEntity = entity;
			for (int i = 0; i < joined.length; i++) {
				Field field = currentEntity.getField(joined[i]);
				currentEntity = getReferenceEntity(field);
				if (i + 1 == joined.length) {
					path += currentEntity.getNameField().getName();
					break;
				}
			}
		}
		
		String[] joined = path.split("\\.");
		
		if (path.charAt(0) == FORCE_JOIN_PREFIX) {  // eg. `^SalesOrder.totalAmount` `^t2Reference`
			if (joined.length == 2) {
				Entity joinEntity = sqlExecutorContext.getEntity( joined[0].substring(1) );
				Field referenceTo = null;
				for (Field to : rootEntity.getReferenceToFields()) {
					if ( joinEntity.equals(to.getOwnEntity()) ) {
						referenceTo = to;
						break;
					}
				}
				
				if (referenceTo == null) {
					throw new CompileException("Entity " + joinEntity.getName() + " no reference-to " + rootEntity.getName());
				}
				
				JoinNode jNode = tree.addChildJoin(
						joinEntity.getPhysicalName(), rootEntity.getPrimaryField().getPhysicalName(), referenceTo.getPhysicalName());
				JoinField aJF = new JoinField(jNode, joinEntity.getField(joined[1]), itemName, type);
				joinFieldMap.put(itemName, aJF);
				return aJF;
			} else if (joined.length == 1 && nestedSelectContext != null) {  // In nested-sql (exists)
				Entity root = nestedSelectContext.getRoot();
				JoinField aJF = new JoinField(
						nestedSelectContext.getRootNode(), root.getField(joined[0].substring(1)), itemName, type);
				joinFieldMap.put(itemName, aJF);
				return aJF;
			}
			throw new CompileException("Force join must has two nodes");
		}
		
		if (joined.length == 1) {  // No joins
			Field field = entity.getField(joined[0]);
			Validate.notNull(field, "Unknow field [ " + joined[0] + " ] in entity [ " + entity.getName() + " ]");
			JoinField aJF = new JoinField(tree.getRootJoinNode(), field, itemName, type);
			joinFieldMap.put(itemName, aJF);
			return aJF;
		}
		
		Field crtf;
		Entity crte = entity;
		JoinNode pjn = null;  // previous JoinNode
		for (int i = 0; i < joined.length; i++) {  // eg. `accountId.ownUser.fullName`
			String fn = joined[i];
			crtf = crte.getField(fn);
			Validate.notNull(crtf, "Unknow field [ " + fn + " ] in entity [ " + crte.getName() + " ]");
			crte = getReferenceEntity(crtf);
			
			if (pjn == null) {
				pjn = tree.addChildJoin(
						crte.getPhysicalName(), crtf.getPhysicalName(), crte.getPrimaryField().getPhysicalName());
			} else {
				pjn = tree.addChildJoin(
						crte.getPhysicalName(), crtf.getPhysicalName(), crte.getPrimaryField().getPhysicalName(), 
						pjn);
			}
			
			if (i + 2 == joined.length) {
				Field last = crte.getField(joined[i + 1]);
				Validate.notNull(last, "Unknow field [ " + fn + " ] in entity [ " + crte.getName() + " ]");
				
				JoinField aJF = new JoinField(pjn, last, itemName, type);
				joinFieldMap.put(itemName, aJF);
				return aJF;
			}
		}
		throw new CompileException("Unknow error on bind JoinField");
	}
	
	/**
	 * 获取引用字段的引用实体
	 * 
	 * @param field
	 * @return
	 */
	private Entity getReferenceEntity(Field field) {
		if (FieldType.REFERENCE != field.getType()) {
			throw new CompileException("Field [ " + field + " ] does not support joins");
		}
		return field.getReferenceEntity();
	}
	
	/**
	 * @param ast
	 * @param aJTree
	 * @param entity
	 */
	private void findJoinFields(AST ast, JoinTree aJTree, Entity entity) {
		ast = ast.getFirstChild();
		do {
			AST node = ast;
			if (node.getType() != AjQLParserTokenTypes.IDENT) {
				node = node.getFirstChild();
				if (node == null || node.getType() != AjQLParserTokenTypes.IDENT) {
					continue;
				}
			}
			
			bindJoinField(aJTree, entity, node, SelectItemType.Field);
			
		} while ((ast = ast.getNextSibling()) != null);
	}
	
	/**
	 * @param item
	 * @param aggregator
	 * @param dialect
	 * @return
	 */
	private JoinField getJoinField(AST item, AST aggregator, Dialect dialect) {
		JoinField aJF = joinFieldMap.get(item.getText());
		if (aJF == null) {
			throw new CompileException("Unknow JoinField in clause [ " + item.getType() + ", " + item.getText() + " ]");
		}
		
		// Use clone
		JoinField clone = new JoinField(aJF, null);
		if (aggregator != null) {
			clone.setAggregator(aggregator.getText(), null);
			if (ParserHelper.isAggregatorWithMode(aggregator.getType())) {
				String mode = item.getNextSibling().getNextSibling().getText();  // [, '%Y']
				clone.setAggregatorMode(mode);
			} else {
				clone.setAggregatorMode(null);
			}
		} else {
			clone.setAggregator(null, null);
			clone.setAggregatorMode(null);
		}
		clone.as(-1, dialect);  // compile
		return clone;
	}
	
	/**
	 */
	private void throwIfUncompile() {
		if (this.compiledSql == null) {
			throw new IllegalStateException("uncompile");
		}
	}
	
	/**
	 * @param ex
	 */
	private void handleParseException(Exception ex) {
		if (ex instanceof ParserException) {
			Throwable cause = ex.getCause();
			throw new CompileException(
					"ANTLR cannot parse AjQL stream, threw an exception [ "
					+ cause.getClass().getName() + ": " + ex.getCause() + " ]", ex.getCause());
		}
		throw new CompileException("Parse AjQL error", ex);
	}
	
	// ----------------------------------------------------------- Just for Nested SQL
	
	private QueryCompiler(SqlExecutorContext context, NestedSelectContext nestedSelectContext, int inParametersIndex) {
		this.sqlExecutorContext = context;
		this.nestedSelectContext = nestedSelectContext;
		this.inParametersIndex = inParametersIndex;
	}
	
	private String compileNestedSelect(AST nestedSelect) {
		return compileQuery(nestedSelect, null);
	}
}
