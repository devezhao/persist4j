package cn.devezhao.persist4j.record;

import org.junit.Test;

public class RecordVisitorTest {

    @Test
    public void testClearNumber() {
        System.out.println(RecordVisitor.clearNumber("123 ", Boolean.TRUE));
        System.out.println(RecordVisitor.clearNumber("123d ", Boolean.TRUE));
        System.out.println(RecordVisitor.clearNumber("1,123 ", Boolean.TRUE));
        System.out.println(RecordVisitor.clearNumber("111,1,11,,123.00 ", Boolean.FALSE));
        System.out.println(RecordVisitor.clearNumber("NaN", Boolean.TRUE));
    }
}