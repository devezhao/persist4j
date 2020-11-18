package cn.devezhao.persist4j.dialect;

import cn.devezhao.persist4j.dialect.editor.*;

import java.io.Serializable;

/**
 * 字段类型定义
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, May 25, 2009
 * @version $Id: FieldType.java 8 2015-06-08 09:09:03Z zhaoff@wisecrm.com $
 */
public class FieldType implements Type, Serializable {
	private static final long serialVersionUID = 1L;

	public static final int NO_NEED_LENGTH = -1;
	public static final int DEFAULT_TEXT_LENGTH = 21845; // TEXT类型默认长度
	public static final int DEFAULT_STRING_LENGTH = 255; // STRING类型默认长度
	public static final int DEFAULT_PRECISION = 19; 	 // 数字精度(整数位+小数位=精度)
	public static final int DEFAULT_DECIMAL_SCALE = 4; 	 // 默认小数精度

	public static final Type PRIMARY = new FieldType(10001, "primary", new PrimaryEditor());
	public static final Type REFERENCE = new FieldType(10002, "reference", new ReferenceEditor());
	public static final Type ANY_REFERENCE = new FieldType(10003, "any-reference", new AnyReferenceEditor());
	public static final Type REFERENCE_LIST = new FieldType(10004, "reference-list", new ReferenceListEditor());

	public static final Type INT = new FieldType(10011, "int", new IntEditor());
	public static final Type SMALL_INT = new FieldType(10012, "small-int", new SmallIntEditor());
	public static final Type DOUBLE = new FieldType(10014, "double", new DoubleEditor());
	public static final Type DECIMAL = new FieldType(10015, "decimal", new DecimalEditor());
	public static final Type LONG = new FieldType(10016, "long", new LongEditor());

	public static final Type DATE = new FieldType(10021, "date", new DateEditor());
	public static final Type TIMESTAMP = new FieldType(10023, "timestamp", new TimestampEditor());

	public static final Type CHAR = new FieldType(10031, "char", new CharEditor());
	public static final Type STRING = new FieldType(10032, "string", new StringEditor());
	public static final Type TEXT = new FieldType(10033, "text", new TextEditor());

	public static final Type BOOL = new FieldType(10091, "bool", new BoolEditor());
	public static final Type NTEXT = new FieldType(10092, "ntext", new NTextEditor());
	public static final Type BINARY = new FieldType(10093, "binary", new BinaryEditor());

	// ----

	final private Integer mask;
	final private String name;
	final private Editor editor;

	private FieldType(Integer mask, String name, Editor editor) {
		this.mask = mask;
		this.name = name;
		this.editor = editor;
	}

	@Override
    public Integer getMask() {
		return mask;
	}

	@Override
    public String getName() {
		return name;
	}

	@Override
    public Editor getFieldEditor() {
		return editor;
	}

	@Override
	public int hashCode() {
		return super.hashCode() >>> getMask();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Type)) {
			return false;
		}
		Type t2 = (Type) obj;
		return t2.getMask().equals(this.getMask());
	}

	@Override
	public String toString() {
		return "<" + mask + ':' + name + '>';
	}
}
