package link.symtable.kson.core;

import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;
import link.symtable.kson.core.parser.KsonParser;

public class Kson {
    public static boolean isNull(KsNode n) {
        return (n == KsNull.NULL) || n instanceof KsNull;
    }

    public static boolean isListNode(KsNode n) {
        return (n == KsListNode.NIL) || n.isListNode();
    }

    public static KsNode parse(String source) {
        return KsonParser.parse(source);
    }
}
