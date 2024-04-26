package cn.devezhao.persist4j.util;

import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: StringHelper.java 119 2011-05-15 16:56:18Z
 * zhaofang123@gmail.com $
 * @since 0.1, Feb 17, 2009
 */
public class StringHelper {

    private StringHelper() {
    }

    /**
     * 验证字符有效
     * 规则：['a-zA-Z', '_', '#'], can contains ['a-zA-Z', '_', '#', '0-9']
     *
     * @param ident
     * @return
     */
    public static boolean isIdentifier(String ident) {
        if (StringUtils.isBlank(ident)) {
            return false;
        }

        char start = ident.charAt(0);
        if (!(CharSet.ASCII_ALPHA.contains(start) || start == '_' || start == '#')) {
            return false;
        }

        for (char ch : ident.toCharArray()) {
            if (!(CharSet.ASCII_ALPHA.contains(ch) || CharSet.ASCII_NUMERIC.contains(ch) || ch == '_' || ch == '#')) {
                return false;
            }
        }

        return true;
    }

    /**
     * 根据驼峰加入下划线并大些，例如：userName &gt; USER_NAME
     *
     * @param s
     * @return
     */
    public static String hyphenate(final String s) {
        char[] chars = s.toCharArray();
        int lenth = s.length();

        StringBuilder sb = new StringBuilder();
        boolean lastAZ = false;
        boolean lastInsert = false;
        for (int i = 0; i < lenth; i++) {
            char ch = chars[i];
            if (ch >= 'A' && ch <= 'Z') {
                if (i > 0) {
                    if (!lastAZ) {
                        sb.append('_');
                        lastInsert = true;
                    } else {
                        lastInsert = false;
                    }
                }

                lastAZ = true;
            } else {
                if (lastAZ) {
                    if (!lastInsert && i > 1) {
                        if (!(ch >= '0' && ch <= '9')) {
                            sb.insert(sb.length() - 1, '_');
                        }
                    }
                }

                lastAZ = false;
                lastInsert = false;
            }
            sb.append(ch);
        }
        return sb.toString().replaceAll("__", "_").toLowerCase();
    }

}
