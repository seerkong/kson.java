package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecState;

@FunctionalInterface
public interface ContinuationRunTask {
    ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun, Continuation currentCont);
}
