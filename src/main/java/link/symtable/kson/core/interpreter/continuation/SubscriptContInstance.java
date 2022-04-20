package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportSubscript;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class SubscriptContInstance extends KsContinuation {
    private LinkedList<KsNode> pendingNodes;
    private LinkedList<KsNode> evaledNodes;

    public SubscriptContInstance(KsContinuation currentCont, KsListNode initExpr) {
        super(currentCont);
        pendingNodes = new LinkedList<>();

        KsListNode iter = initExpr.getNext();
        while (iter != KsListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
        evaledNodes = new LinkedList<>();
    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            if (evaledNodes.size() == 0) {
                throw new RuntimeException("cannot eval empty expr");
            }
            KsNode targetNode = evaledNodes.pollFirst();
            while (evaledNodes.size() > 0) {
                KsNode subscript = evaledNodes.pollFirst();
                if (!(targetNode instanceof SupportSubscript)) {
                    throw new RuntimeException("get subscript not supported");
                }

                if (subscript.isString()) {
                    targetNode = ((SupportSubscript) targetNode).subscriptByString(state, subscript.asString().getValue());
                } else if (subscript.isSymbol()) {
                    targetNode = ((SupportSubscript) targetNode).subscriptByString(state, subscript.asSymbol().getValue());
                } else if (subscript.isInt64()) {
                    targetNode =
                            ((SupportSubscript) targetNode).subscriptByIndex(state, (int)subscript.asInt64().toInt64Val());
                } else {
                    throw new RuntimeException("get subscript not supported");
                }
            }

            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(targetNode)
                    .build();
        } else {
            KsNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return prepareNextRun(state, currentNodeToRun);
    }
}
