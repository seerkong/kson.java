package link.symtable.kson.core.interpreter.continuation;


import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;

public class QuoteContInstance extends KsContinuation {
    KsNode node;
    public QuoteContInstance(KsContinuation currentCont,  KsNode initExpr) {
        super(currentCont);
        this.node = initExpr;
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        return ContRunResult.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(node)
                .build();
    }
}
