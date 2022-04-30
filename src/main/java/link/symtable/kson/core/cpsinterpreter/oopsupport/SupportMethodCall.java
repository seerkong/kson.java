package link.symtable.kson.core.cpsinterpreter.oopsupport;

import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsNode;

@FunctionalInterface
public interface SupportMethodCall {
    KsNode callMethod(ExecState state, String methodName, KsNode[] args);
}
