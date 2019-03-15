package cn.devezhao.persist4j.util.support;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.dialect.FieldType;
import cn.devezhao.persist4j.dialect.MySQL5Dialect;

/**
 * 生成建表语句
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, 06/23/08
 * @version $Id: Table.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class Table {

	private Entity entity;
	private Dialect dialect;
	private List<?> indexList;
	
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
		return generateDDL(dropExists, createFk, false);
	}

	/**
	 * @param dropExists
	 * @param createFk
	 * @param noZeroDate MySQL5.7+
	 * @return
	 */
	public String[] generateDDL(boolean dropExists, boolean createFk, boolean noZeroDate) {
		List<String> sqls = new ArrayList<String>();
		
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
		List<Object[]> FKs = new ArrayList<Object[]>();
		
		Field[] fields = entity.getFields();
		for (int i = 0, j = fields.length; i < j; i++) {
			Field field = fields[i];
			String column = field.getPhysicalName();
			if (field.getType() == FieldType.PRIMARY) {
				PK = column;
			} else if (field.getType() == FieldType.REFERENCE && createFk) {
				FKs.add(new Object[] {
						column,
						field.getReferenceEntities()[0] });
			}
			
			sql.append("\n  ");
			generateFieldDDL(field, sql, noZeroDate);
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
		
		// 唯一索引
		String[] uix = genIndexDDL(true);
		if (uix.length > 0) {
			for (String u : uix) {
				sql.append(",\n  ").append(u);
			}
		}
		
		sql.append("\n)");
		if (dialect.getClass() == MySQL5Dialect.class) {
			sql.append("Engine=InnoDB;");  // ENGINE=INNODB, MyISAM?
		}
		sqls.add(sql.toString());
		
		// 索引
		String ux[] = genIndexDDL(false);
		if (ux.length > 0) {
			sql = new StringBuilder("alter table ").append(dialect.quote(entity.getPhysicalName()));
			for (String u : ux) {
				sql.append("\n  add ").append(u).append(",");
			}
			sql.deleteCharAt(sql.length() - 1).append(";");
			sqls.add(sql.toString());
		}
		
		return sqls.toArray(new String[0]);
	}
	
	/**
	 * @param field
	 * @param sql
	 * @param noZeroDate
	 */
	public void generateFieldDDL(Field field, StringBuilder sql, boolean noZeroDate) {
		String column = field.getPhysicalName();
		String type = dialect.getColumnType(field.getType().getMask());
		if (field.getType() == FieldType.DOUBLE) {
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

		String zeroDate = noZeroDate ? "1000-01-01 00:00:00" : "0000-00-00 00:00:00";
		
		String canNull = "";  // "DEFAULT NULL";
		if (!field.isNullable()) {
			canNull = " not null";
			if (field.getType() == FieldType.TIMESTAMP) {
				canNull += String.format(" default '%s'", zeroDate);
			} else if (field.getType() == FieldType.DATE) {
				canNull += String.format(" default '%s'", zeroDate.split(" ")[0]);
			}
		} else if (field.getType() == FieldType.TIMESTAMP) {
			canNull += String.format(" default '%s'", zeroDate);
		} else if (field.getType() == FieldType.DATE) {
			canNull += String.format(" default '%s'", zeroDate.split(" ")[0]);
		}
		
		// 自增值
		if (field.isAutoValue()) {
			canNull += " auto_increment";  // MS-SQL !?
		} else if (StringUtils.isNotBlank((String) field.getDefaultValue())) {  // 默认值
			if (canNull.contains("default")) {
				canNull = canNull.split("default")[0];
				canNull += " " + field.getDefaultValue().toString().replaceAll("'", "") + "'";
			} else {
				canNull += " default '" + field.getDefaultValue().toString().replaceAll("'", "") + "'";
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
		sql.append(ddl);
		
		if (field.isAutoValue()) {
			sql.append(',');
			String aix = String.format("unique index `AIX0_%s` (`%s`)", entity.getPhysicalName(), column);
			sql.append("\n  " + aix);
		}
	}
	
	/**
	 * @param isUIX
	 * @return
	 */
	protected String[] genIndexDDL(boolean isUIX) {
		if (indexList == null || indexList.isEmpty()) {
			return new String[0];
		}
		
		List<String> uix = new ArrayList<String>();
		int idxIndex = 0;
		for (Object o : indexList) {
			Element el = (Element) o;
			String type = el.attributeValue("type");
			if (isUIX && !"unique".equals(type)) {
				continue;
			} else if (!isUIX && "unique".equals(type)) {
				continue;
			}
			
			String fieldList = el.attributeValue("field-list");
			String indexName = ("unique".equals(type) ? "UIX" : "IX") + (++idxIndex) + '_' + entity.getPhysicalName();
			
			List<String> fpNames = new LinkedList<String>();
			for (String f : fieldList.split("\\,")) {
				fpNames.add( dialect.quote(entity.getField(f.trim()).getPhysicalName()) );
			}
			String colNames = StringUtils.join(fpNames.iterator(), ", ");
			
			uix.add(("unique".equals(type) ? "unique " : "") + "index "
					+ dialect.quote(indexName) + " (" + colNames + ")");
		}
		return uix.toArray(new String[0]);
	}
	
	static final String COLUMN_ 	= "%s %s";
	static final String PK_			= "primary key  ({0})";
	static final String FK_ 		= "index        ({0}),\n  foreign key  ({0})  references {1}({2})";
}
