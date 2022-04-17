package link.symtable.kson.core.interpreter.oopsupport;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsNode;

public interface SupportSubscript {
    boolean subscriptAcceptKey(ExecState state, String fieldName);
    KsNode subscriptByString(ExecState state, String fieldName);
    KsNode subscriptByIndex(ExecState state, int index);
}
