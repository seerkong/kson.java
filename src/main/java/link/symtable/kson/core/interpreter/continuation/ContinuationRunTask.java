package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecState;

@FunctionalInterface
public interface ContinuationRunTask {
    ContRunResult run(ExecState state, KsNode lastValue, KsNode currentNodeToRun, KsContinuation currentCont);
}
