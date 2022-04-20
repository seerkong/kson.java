package link.symtable.kson.core.interpreter.continuation;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;


public class LandCallbackContInstance extends KsContinuation {
    public LandCallbackContInstance(KsContinuation currentCont, Env env) {
        super(currentCont, env);
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("LandCallbackContInstance#prepareNextRun");
    }

    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        return ContRunState.builder()
                .nextAction(ExecAction.FINISH)
                .nextCont(null)
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
