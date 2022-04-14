package link.symtable.kson.core.interpreter.oopsupport;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsNode;

@FunctionalInterface
public interface SupportMethodCall {
    KsNode callMethod(ExecState state, String methodName, KsNode[] args);
}
