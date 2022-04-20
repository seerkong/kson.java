package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;

public class LetContInstance extends KsContinuation {
    private String varName;
    private KsNode varValueExpr = null;
    public LetContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        if (expr.getNext() == KsListNode.NIL) {
            throw new RuntimeException("illegal let expr");
        }
        KsNode varNameNode = expr.getNext().getValue();
        varName = varNameNode.asWord().getValue();
        if (expr.getNext().getNext() != KsListNode.NIL) {
            varValueExpr = expr.getNext().getNext().getValue();
        }
    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (varValueExpr == null) {
            getEnv().define(varName, KsNull.NULL);
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(KsNull.NULL)
                    .build();
        } else {
            KsNode nextToRun = varValueExpr;
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        getEnv().define(varName, lastValue);
        return ContRunState.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
