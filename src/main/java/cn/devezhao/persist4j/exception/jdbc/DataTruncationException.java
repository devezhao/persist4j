package cn.devezhao.persist4j.exception.jdbc;

import cn.devezhao.persist4j.exception.JdbcException;

import java.sql.SQLException;

/**
 *
 * @author <a href="mailto:devezhao@126.com">FANGFANG ZHAO</a>
 * @since 1.7.3, 10/20/2023
 */
public class DataTruncationException extends JdbcException {

    public DataTruncationException(String message, SQLException root) {
        super(message, root);
    }

    public DataTruncationException(String message, SQLException root, String sql) {
        super(message, root, sql);
    }
}
