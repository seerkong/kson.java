package link.symtable.kson.core.interpreter.continuation;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;

public class NopContInstance extends KsContinuation {
    public NopContInstance(KsContinuation currentCont) {
        super(currentCont);
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("NopContInstance#prepareNextRun");
    }
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        // continue wait
        return ContRunState.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(this)
                .isSafePoint(true)
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
