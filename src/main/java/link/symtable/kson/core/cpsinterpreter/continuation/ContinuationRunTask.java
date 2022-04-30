package link.symtable.kson.core.cpsinterpreter.continuation;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecState;

@FunctionalInterface
public interface ContinuationRunTask {
    ContRunState run(ExecState state, KsNode lastValue, KsNode currentNodeToRun, KsContinuation currentCont);
}
