package link.symtable.kson.core.cpsinterpreter.util;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsDouble;
import link.symtable.kson.core.node.KsFunction;
import link.symtable.kson.core.node.KsHostObject;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;
import link.symtable.kson.core.node.KsString;
import link.symtable.kson.core.node.KsSymbol;
import link.symtable.kson.core.node.KsUndefined;

public class NodeTypeHelper {
    public static boolean isSelfEvaluatedNode(KsNode node) {
        return node instanceof KsBoolean
                || node instanceof KsInt64
                || node instanceof KsDouble
                || node instanceof KsString
                || node instanceof KsSymbol
                || node instanceof KsNull
                || node instanceof KsUndefined
                || node instanceof KsFunction
                || node instanceof KsHostObject
                || node instanceof KsContinuation
                || (node == KsListNode.NIL)
                ;
    }
}
