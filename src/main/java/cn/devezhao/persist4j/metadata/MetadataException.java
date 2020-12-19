package cn.devezhao.persist4j.metadata;

import cn.devezhao.persist4j.PersistException;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: MetadataException.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class MetadataException extends PersistException {
	private static final long serialVersionUID = -2308505098712378613L;

	public MetadataException(String message) {
		super(message);
	}

	public MetadataException(String message, Throwable ex) {
		super(message, ex);
	}
}
