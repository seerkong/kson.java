package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;

public class ReturnContInstance extends KsContinuation {

    public ReturnContInstance(KsContinuation currentCont, Env env) {
        super(currentCont, env);
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        return ContRunResult.builder()
                .nextAction(ExecAction.FINISH)
                .nextCont(null)
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
