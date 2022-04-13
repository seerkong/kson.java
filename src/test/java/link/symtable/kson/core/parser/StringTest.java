package link.symtable.kson.core.parser;

import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.node.KsonNode;

public class StringTest {
    @Test
    public void test() {
        String source = "\"String 中文\\\" \\\\ \\/ \\b \\f \\n \\r \\t \\uAAAA \\u1337 \"";

        KsonNode node = Kson.parse(source);
        String s = node.toString();
        System.out.println(s);
    }
}
