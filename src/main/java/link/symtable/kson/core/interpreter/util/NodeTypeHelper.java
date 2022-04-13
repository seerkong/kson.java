package link.symtable.kson.core.interpreter.util;

import link.symtable.kson.core.node.KsonBoolean;
import link.symtable.kson.core.node.KsonDouble;
import link.symtable.kson.core.node.KsonFunction;
import link.symtable.kson.core.node.KsonHostObject;
import link.symtable.kson.core.node.KsonInt64;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonNull;
import link.symtable.kson.core.node.KsonString;
import link.symtable.kson.core.node.KsonSymbol;
import link.symtable.kson.core.node.KsonUndefined;

public class NodeTypeHelper {
    public static boolean isSelfEvaluatedNode(KsonNode node) {
        return node instanceof KsonBoolean
                || node instanceof KsonInt64
                || node instanceof KsonDouble
                || node instanceof KsonString
                || node instanceof KsonSymbol
                || node instanceof KsonNull
                || node instanceof KsonUndefined
                || node instanceof KsonFunction
                || node instanceof KsonHostObject
                || (node == KsonListNode.NIL)
                ;
    }
}
