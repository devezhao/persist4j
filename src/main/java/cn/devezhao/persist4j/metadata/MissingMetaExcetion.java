package cn.devezhao.persist4j.metadata;

/**
 * 实体或字段未找到（可能已经删除）
 *
 * @author devezhao
 * @since 2020/12/19
 */
public class MissingMetaExcetion extends MetadataException {

    public MissingMetaExcetion(String message) {
        super(message);
    }

    public MissingMetaExcetion(String field, String entity) {
        super("No such field [ " + field + " ] in entity [ " + entity + " ]");
    }

    public MissingMetaExcetion(String message, Throwable ex) {
        super(message, ex);
    }
}
