package cn.devezhao.persist4j.util.support;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;
import cn.devezhao.persist4j.metadata.impl.EntityImpl;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 建表语句
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/23/08
 * @version $Id: Table.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class Table {

	private Entity entity;
	private Dialect dialect;
	
	private List<?> indexList;
	private List<String> autoValueIndex = new ArrayList<>();
	private int indexNo = 0;
	
	/**
	 * @param entity
	 * @param dialect
	 */
	public Table(Entity entity, Dialect dialect) {
		this(entity, dialect, null);
	}
	
	/**
	 * @param entity
	 * @param dialect
	 * @param indexList
	 */
	public Table(Entity entity, Dialect dialect, List<?> indexList) {
		this.entity = entity;
		this.dialect = dialect;
		this.indexList = indexList;
	}
	
	/**
	 * @param dropExists
	 * @param createFk
	 * @return
	 */
	public String[] generateDDL(boolean dropExists, boolean createFk) {
		return generateDDL(dropExists, createFk, true);
	}

	/**
	 * @param dropExists
	 * @param createFk
	 * @param allowZeroDate
	 * @return
	 */
	public String[] generateDDL(boolean dropExists, boolean createFk, boolean allowZeroDate) {
		List<String> sqls = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();

		String table = entity.getPhysicalName();
		if (dropExists) {
			sql.append("drop table if exists ").append(dialect.quote(table)).append(";");
			sqls.add(sql.toString());
			sql = new StringBuilder();
			
			sql.append("create table /*!32312 if not exists*/ ").append(dialect.quote(table)).append(" (");
		} else {
			sql.append("create table if not exists ").append(dialect.quote(table)).append(" (");
		}
		
		String PK = "";
		List<Object[]> FKs = new ArrayList<>();
		
		Set<String> fields = ((EntityImpl) entity).getFieldSorted();
		for (String fs : fields) {
			Field field = entity.getField(fs);
			String column = field.getPhysicalName();
			if (field.getType() == FieldType.PRIMARY) {
				PK = column;
			} else if (field.getType() == FieldType.REFERENCE && createFk) {
				FKs.add(new Object[] {
						column,
						field.getReferenceEntities()[0] });
			}
			
			sql.append("\n  ");
			generateFieldDDL(field, sql, allowZeroDate);
			sql.append(",");
		}
		
		// 主键
		String pk = MessageFormat.format(PK_, dialect.quote(PK));
		sql.append("\n  ").append(pk);
		
		// 外键
		if (!FKs.isEmpty() && createFk) {
			sql.append(",\n");
			
			for (int i = 0; i < FKs.size(); i++) {
				Object[] FK = FKs.get(i);
				Entity e = (Entity) FK[1];
				
				sql.append("\n  ");
				sql.append(MessageFormat.format(FK_,
						StringUtils.rightPad(dialect.quote(FK[0].toString()), 20),
						dialect.quote(e.getPhysicalName()),
						dialect.quote(e.getPrimaryField().getPhysicalName())));
				
				if (i + 1 < FKs.size()) {
					sql.append(',');
				}
			}
		}
		
		for (String aix : autoValueIndex) {
			sql.append(",\n  ").append(aix);
		}
		// 索引
		for (String ix : generateIndexDDL()) {
			sql.append(",\n  ").append(ix);
		}
		
		sql.append("\n)");
		if (dialect.getClass() == MySQL5Dialect.class) {
			sql.append("Engine=InnoDB;");  // ENGINE=INNODB, MyISAM?
		}
		sqls.add(sql.toString());
		
		return sqls.toArray(new String[0]);
	}
	
	/**
	 * @param field
	 * @param into
	 */
	public void generateFieldDDL(Field field, StringBuilder into) {
		generateFieldDDL(field, into, true);
	}
	
	/**
	 * @param field
	 * @param into
	 * @param allowZeroDate
	 */
	public void generateFieldDDL(Field field, StringBuilder into, boolean allowZeroDate) {
		String column = field.getPhysicalName();
		String type = dialect.getColumnType(field.getType().getMask());
		if (field.getType() == FieldType.DOUBLE
				|| field.getType() == FieldType.DECIMAL) {
			type = String.format(type, field.getDecimalScale());
		}
		
		if (type.contains("%d") && field.getMaxLength() > 0) {
			if (field.getType() == FieldType.TEXT 
					&& field.getMaxLength() > FieldType.DEFAULT_TEXT_LENGTH) {
				type = "mediumtext";
			} else {
				type = String.format(type, field.getMaxLength());
			}
		}

		String canNull = "";  // "DEFAULT NULL";
		if (!field.isNullable()) {
			canNull = " not null";
			if (field.getType() == FieldType.TIMESTAMP) {
				canNull += " default " + (allowZeroDate ? "'0000-00-00 00:00:00'" : "current_timestamp");
			} else if (field.getType() == FieldType.DATE) {
				canNull += " default " + (allowZeroDate ? "'0000-00-00'" : "current_date");
			}
		} else if (field.getType() == FieldType.TIMESTAMP || field.getType() == FieldType.DATE) {
			canNull += " null default null";
		}
		
		String defaultValue = field.getDefaultValue() != null ? field.getDefaultValue().toString() : null;
		
		// 自增值
		if (field.isAutoValue()) {
			canNull += " auto_increment";  // MS-SQL !?
		} else if (StringUtils.isNotBlank(defaultValue)) {  // 默认值
			if (canNull.contains("default")) {
				canNull = canNull.split("default")[0];
				canNull += " " + defaultValue.replaceAll("'", "") + "'";
			} else {
				canNull += " default '" + defaultValue.replaceAll("'", "") + "'";
			}
		}
		
		String ddl = String.format(COLUMN_,
				StringUtils.rightPad(dialect.quote(column), 20),
				type + canNull);
		// 注释
		if (StringUtils.isNotBlank(field.getDescription())) {
			if (dialect.getDialectName().startsWith("mysql")) {
				ddl += " comment '" + field.getDescription().replaceAll("'", "") + "'";
			} else {
				ddl += " /* " + field.getDescription() + " */";
			}
		}
		into.append(ddl);
		
		// 为自增列加唯一索引
		if (field.isAutoValue()) {
			String aix = String.format("unique index AIX%d_%s (`%s`)", this.indexNo++, entity.getPhysicalName(), column);
			autoValueIndex.add(aix);
		}
	}
	
	/**
	 * @return
	 */
	protected String[] generateIndexDDL() {
		if (indexList == null || indexList.isEmpty()) {
			return new String[0];
		}
		
		List<String> uix = new ArrayList<>();
		for (Object o : indexList) {
			Element el = (Element) o;
			String type = el.attributeValue("type");
			if ("fulltext".equals(type) && !dialect.supportsFullText()) {
				continue;
			}
			
			String fieldList = el.attributeValue("field-list");
			String indexName = ("unique".equals(type) ? "UIX" : ("fulltext".equals(type) ? "FIX" : "IX"))
					+ (this.indexNo++) + '_' + entity.getPhysicalName();
			
			List<String> fpNames = new LinkedList<>();
			for (String f : fieldList.split(",")) {
				fpNames.add( dialect.quote(entity.getField(f.trim()).getPhysicalName()) );
			}
			String colNames = StringUtils.join(fpNames.iterator(), ", ");
			
			uix.add(("unique".equals(type) ? "unique " : ("fulltext".equals(type) ? "fulltext " : ""))
					+ "index " + indexName + " (" + colNames + ")");
		}
		return uix.toArray(new String[0]);
	}
	
	static final String COLUMN_ 	= "%s %s";
	static final String PK_			= "primary key  ({0})";
	static final String FK_ 		= "index        ({0}),\n  foreign key  ({0})  references {1}({2})";
}
