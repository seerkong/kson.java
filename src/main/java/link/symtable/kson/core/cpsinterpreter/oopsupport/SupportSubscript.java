package link.symtable.kson.core.cpsinterpreter.oopsupport;

import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsNode;

public interface SupportSubscript {
    boolean subscriptAcceptKey(ExecState state, String fieldName);
    KsNode subscriptByString(ExecState state, String fieldName);
    KsNode subscriptByIndex(ExecState state, int index);
}
