package link.symtable.kson.core.cpsinterpreter.continuation;


import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;

public class QuoteContInstance extends KsContinuation {
    KsNode node;
    public QuoteContInstance(KsContinuation currentCont,  KsNode initExpr) {
        super(currentCont);
        this.node = initExpr;
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        return ContRunState.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(node)
                .build();
    }
}
