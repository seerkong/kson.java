package link.symtable.kson.core;

import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonNull;
import link.symtable.kson.core.parser.KsonParser;

public class Kson {
    public static boolean isNull(KsonNode n) {
        return (n == KsonNull.NULL) || n instanceof KsonNull;
    }

    public static boolean isListNode(KsonNode n) {
        return (n == KsonListNode.NIL) || n.isListNode();
    }

    public static KsonNode parse(String source) {
        return KsonParser.parse(source);
    }
}
