package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonLambdaFunction;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;

public class FuncDeclareContInstance extends Continuation {
    private String funcName = null;
    private KsonListNode params;
    private KsonListNode block;
    public FuncDeclareContInstance(Continuation currentCont, KsonListNode expr) {
        super(currentCont);

        if (expr.getNext().getValue().isWord()) {
            funcName = expr.getNext().getValue().asWord().getValue();
            expr = expr.getNext().getNext();
        }
        params = expr.getValue().asListNode();
        block = expr.getNext();
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        KsonLambdaFunction func = new KsonLambdaFunction(funcName, params, block);
        if (funcName != null) {
            getEnv().define(funcName, func);
        }
        return ContRunResult.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(func)
                .build();
    }
}
