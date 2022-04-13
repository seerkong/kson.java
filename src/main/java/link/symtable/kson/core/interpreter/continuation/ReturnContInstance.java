package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;

public class ReturnContInstance extends Continuation {

    public ReturnContInstance(Continuation currentCont, Env env) {
        super(currentCont, env);
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        return ContRunResult.builder()
                .nextAction(ExecAction.FINISH)
                .nextCont(null)
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
