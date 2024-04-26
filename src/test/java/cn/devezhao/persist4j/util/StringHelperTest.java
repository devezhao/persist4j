package cn.devezhao.persist4j.util;

import cn.hutool.core.util.RandomUtil;
import junit.framework.TestCase;

public class StringHelperTest extends TestCase {

    public void testHyphenate() {
        System.out.println(StringHelper.hyphenate("ishttp_Req"));
        System.out.println(StringHelper.hyphenate("ishttp_req"));
        System.out.println(StringHelper.hyphenate("ISHTTP_REQ"));
        System.out.println(StringHelper.hyphenate("Bp0B2"));
        System.out.println(StringHelper.hyphenate("IshttpReq"));
        System.out.println(StringHelper.hyphenate("isHttpReq"));
        System.out.println(StringHelper.hyphenate("isHTTPReq"));
        System.out.println(StringHelper.hyphenate("ISHTTPREQ"));
        System.out.println(StringHelper.hyphenate("_123A"));

        for (int i = 0; i < 100; i++) {
            String rnd = RandomUtil.randomString(20);
            String h1 = StringHelper.hyphenate(rnd).toLowerCase();
            System.out.println(rnd + " > " + h1);
        }
    }
}