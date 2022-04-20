package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecState;

@FunctionalInterface
public interface ContinuationRunTask {
    ContRunState run(ExecState state, KsNode lastValue, KsNode currentNodeToRun, KsContinuation currentCont);
}
