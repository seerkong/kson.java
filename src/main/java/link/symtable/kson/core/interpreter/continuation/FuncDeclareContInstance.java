package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class FuncDeclareContInstance extends KsContinuation {
    private String funcName = null;
    private KsListNode params;
    private KsListNode block;
    public FuncDeclareContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);

        if (expr.getNext().getValue().isWord()) {
            funcName = expr.getNext().getValue().asWord().getValue();
            expr = expr.getNext().getNext();
        } else {
            expr = expr.getNext();
        }
        params = expr.getValue().asListNode();
        block = expr.getNext();
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        KsLambdaFunction func = new KsLambdaFunction(funcName, params, block);
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
