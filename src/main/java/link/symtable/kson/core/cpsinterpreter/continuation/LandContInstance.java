package link.symtable.kson.core.cpsinterpreter.continuation;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.cpsinterpreter.Env;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;

public class LandContInstance extends KsContinuation {

    public LandContInstance(KsContinuation currentCont, Env env) {
        super(currentCont, env);
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("ReturnContInstance#prepareNextRun");
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
