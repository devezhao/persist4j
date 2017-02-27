package cn.devezhao.persist4j.engine;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Field;
import cn.devezhao.persist4j.dialect.Dialect;
import cn.devezhao.persist4j.metadata.MetadataFactory;

/**
 * 系统自带元数据帮助类
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-9-1
 * @version $Id: SystemMetadataHelper.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class SystemMetadataHelper {

	/**
	 * @param field
	 * @param recordId
	 * @param referenceId
	 * @return
	 */
	public static String insertSqlReferenceListMapping(Field field, ID recordId, ID referenceId) {
		return String.format(RLM_INSERT_,
				ID.newId(RLM.getEntityCode()).toLiteral(),
				field.getOwnEntity().getEntityCode(),
				field.getName(),
				recordId,
				referenceId);
	}
	
	/**
	 * @param field
	 * @param recordId
	 * @return
	 */
	public static String deleteSqlReferenceListMapping(Field field, ID recordId) {
		return String.format(RLM_DELETE_,
				field.getOwnEntity().getEntityCode(),
				field.getName(),
				recordId);
	}
	
	/**
	 * @param field
	 * @param recordId
	 * @param referenceIds
	 * @param deleteBefore
	 * @return
	 */
	public static String[] updateSqlReferenceListMapping(Field field, ID recordId, ID[] referenceIds, boolean deleteBefore) {
		List<String> sqls = new LinkedList<String>();
		if (deleteBefore)
			sqls.add(deleteSqlReferenceListMapping(field, recordId));
		
		for (ID rid : referenceIds) {
			sqls.add(insertSqlReferenceListMapping(field, recordId, rid));
		}
		return sqls.toArray(new String[sqls.size()]);
	}
	
	/**
	 * @param field
	 * @param recordId
	 * @return
	 */
	public static String selectSqlReferenceListMapping(Field field, ID recordId) {
		return String.format(RLM_SELECT_,
				field.getOwnEntity().getEntityCode(),
				field.getName(),
				recordId);
	}
	
	/**
	 * @param field
	 * @param recordIds
	 * @return
	 */
	public static String selectAjqlReferenceListMapping(Field field, ID[] recordIds) {
		StringBuilder ql = new StringBuilder("select recordId, referenceId from ReferenceListMapping where")
				.append( " entity = " )
				.append( field.getOwnEntity().getEntityCode() )
				.append( " and field ='" )
				.append( field.getName() )
				.append("' and recordId in ( '").append(StringUtils.join(recordIds, "', '")).append("' )");
		return ql.toString();
	}
	
	/**
	 */
	static class ReferenceListMapping {
		public static final String NAME 		= "ReferenceListMapping";
		public static final String uniqueId 	= "uniqueId";
		public static final String entity 		= "entity";
		public static final String field 		= "field";
		public static final String recordId 	= "recordId";
		public static final String referenceId 	= "referenceId";
	}
	
	/**
	 * 暂未用!!!
	 */
	static class LobsRepository {
		public static final String NAME 		= "LobHash";
		public static final String uniqueId 	= "uniqueId";
		public static final String entity 		= "entity";
		public static final String field 		= "field";
		public static final String recordId 	= "recordId";
		public static final String shaHex		= "shaHex";
		public static final String filePath		= "filePath";
	}
	
	// -----------------------------------------------------------------
	
	private static Entity RLM;
	private static String RLM_INSERT_;
	private static String RLM_DELETE_;
	private static String RLM_SELECT_;
	
	protected static void install(MetadataFactory factory, Dialect dialect) {
		RLM = factory.getEntity(ReferenceListMapping.NAME);
		StringBuilder sql = new StringBuilder("insert into ").append(dialect.quote(RLM.getPhysicalName())).append(" ( ");
		sql.append( column(ReferenceListMapping.uniqueId, RLM, dialect)).append(", ")
				.append(column(ReferenceListMapping.entity, RLM, dialect)).append(", ")
				.append(column(ReferenceListMapping.field, RLM, dialect)).append(", ")
				.append(column(ReferenceListMapping.recordId, RLM, dialect)).append(", ")
				.append(column(ReferenceListMapping.referenceId, RLM, dialect));
		RLM_INSERT_ = sql.append(" ) values ( '%s', %d, '%s', '%s', '%s' )").toString();
		sql = null;

		sql = new StringBuilder("delete from ").append(dialect.quote(RLM.getPhysicalName()));
		sql.append(" where ( ")
				.append(column(ReferenceListMapping.entity, RLM, dialect)).append(" = %d and ")
				.append(column(ReferenceListMapping.field, RLM, dialect)).append(" = '%s' and ")
				.append(column(ReferenceListMapping.recordId, RLM, dialect)).append(" = '%s' )");
		RLM_DELETE_ = sql.toString();
		sql = null;
		
		sql = new StringBuilder("select ").append(ReferenceListMapping.recordId);
		sql.append(" from ").append(RLM.getName()).append(" where ( ")
				.append(ReferenceListMapping.entity).append(" = %d and ")
				.append(ReferenceListMapping.field).append(" = '%s' and ")
				.append(ReferenceListMapping.recordId).append(" = '%s' )");
		RLM_SELECT_ = sql.toString();
		sql = null;
	}
	
	private static String column(String fName, Entity entity, Dialect dialect) {
		return dialect.quote(entity.getField(fName).getPhysicalName());
	}
}
