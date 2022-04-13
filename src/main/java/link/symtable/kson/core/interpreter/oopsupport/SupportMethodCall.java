package link.symtable.kson.core.interpreter.oopsupport;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonNode;

@FunctionalInterface
public interface SupportMethodCall {
    KsonNode callMethod(ExecState state, String methodName, KsonNode[] args);
}
