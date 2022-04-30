package link.symtable.kson.core.cpsinterpreter.continuation;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecState;

public class ContinuationHelpers {
    public static ContinuationRunTask EntryContHandler = new ContinuationRunTask() {
        @Override
        public ContRunState run(ExecState state, KsNode lastValue, KsNode currentNodeToRun, KsContinuation currentCont) {
            ExecNodeContInstance nextCont = new ExecNodeContInstance(currentCont);
            return nextCont.prepareNextRun(state, currentNodeToRun);
        }
    };
}
