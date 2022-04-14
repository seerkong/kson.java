package link.symtable.kson.core.interpreter.oopsupport;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsNode;

public interface SupportSubscript {
    KsNode subscriptByString(ExecState state, String fieldName);
    KsNode subscriptByIndex(ExecState state, int index);
}
