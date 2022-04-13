package link.symtable.kson.core.interpreter.continuation;


import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonNode;

public class QuoteContInstance extends Continuation {
    KsonNode node;
    public QuoteContInstance(Continuation currentCont,  KsonNode initExpr) {
        super(currentCont);
        this.node = initExpr;
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        return ContRunResult.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(node)
                .build();
    }
}
