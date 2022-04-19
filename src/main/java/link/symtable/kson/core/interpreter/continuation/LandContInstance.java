package link.symtable.kson.core.interpreter.continuation;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;

public class LandContInstance extends KsContinuation {

    public LandContInstance(KsContinuation currentCont, Env env) {
        super(currentCont, env);
    }

    @Override
    public ContRunResult prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("ReturnContInstance#prepareNextRun");
    }

    public ContRunResult runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        return ContRunResult.builder()
                .nextAction(ExecAction.FINISH)
                .nextCont(null)
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
