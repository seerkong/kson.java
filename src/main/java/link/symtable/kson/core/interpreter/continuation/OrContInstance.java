package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class OrContInstance extends KsContinuation {
    private LinkedList<KsNode> pendingNodes;

    public OrContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        pendingNodes = new LinkedList<>();

        KsListNode iter = expr;
        while (iter != KsListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(KsBoolean.FALSE)
                    .build();
        } else {
            KsNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (lastValue.toBoolean()) {
            return prepareNextRun(state, currentNodeToRun);
        } else {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(KsBoolean.TRUE)
                    .build();
        }
    }
}