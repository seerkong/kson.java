package link.symtable.kson.core.interpreter.oopsupport;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonNode;

public interface SupportSubscript {
    KsonNode subscriptByString(ExecState state, String fieldName);
    KsonNode subscriptByIndex(ExecState state, int index);
}
