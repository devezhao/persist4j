package cn.devezhao.persist4j.record;

import org.junit.Test;

public class RecordVisitorTest {

    @Test
    public void testClearNumber() {
        System.out.println(RecordVisitor.clearNumber("123 "));
        System.out.println(RecordVisitor.clearNumber("123d "));
        System.out.println(RecordVisitor.clearNumber("1,123 "));
        System.out.println(RecordVisitor.clearNumber("111,1,11,,123.00 "));
    }
}