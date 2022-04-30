package link.symtable.kson.core.cpsinterpreter.continuation;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class FuncDeclareContInstance extends KsContinuation {
    private KsListNode declareExpr;
    public FuncDeclareContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        this.declareExpr = expr;
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        KsLambdaFunction func = new KsLambdaFunction(declareExpr);
        if (func.getName() != null) {
            getEnv().define(func.getName(), func);
        }
        return ContRunState.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(func)
                .build();
    }
}
