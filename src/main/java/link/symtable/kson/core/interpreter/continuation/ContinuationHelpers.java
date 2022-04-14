package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecState;

public class ContinuationHelpers {
    public static ContinuationRunTask EntryContHandler = new ContinuationRunTask() {
        @Override
        public ContRunResult run(ExecState state, KsNode lastValue, KsNode currentNodeToRun, KsContinuation currentCont) {
            ExecNodeContInstance nextCont = new ExecNodeContInstance(currentCont);
            return nextCont.initNextRun(state, lastValue, currentNodeToRun);
        }
    };
}
