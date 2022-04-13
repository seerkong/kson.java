package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportSubscript;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;

public class SubscriptContInstance extends Continuation {
    private LinkedList<KsonNode> pendingNodes;
    private LinkedList<KsonNode> evaledNodes;

    public SubscriptContInstance(Continuation currentCont, KsonListNode initExpr) {
        super(currentCont);
        pendingNodes = new LinkedList<>();

        KsonListNode iter = initExpr.getNext();
        while (iter != KsonListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
        evaledNodes = new LinkedList<>();
    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            if (evaledNodes.size() == 0) {
                throw new RuntimeException("cannot eval empty expr");
            }
            KsonNode targetNode = evaledNodes.pollFirst();
            while (evaledNodes.size() > 0) {
                KsonNode subscript = evaledNodes.pollFirst();
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

            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(targetNode)
                    .build();
        } else {
            KsonNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(nextCont)
                    .nextNodeToRun(nextToRun)
                    .newLastValue(lastValue)
                    .build();
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
