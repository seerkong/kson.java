package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;

public class BlockContInstance extends KsContinuation {
    private LinkedList<KsNode> pendingNodes;

    public BlockContInstance(KsContinuation nextCont, KsListNode nodeToRun) {
        this(nextCont, nodeToRun, nextCont.getEnv());
    }

    public BlockContInstance(KsContinuation nextCont, KsListNode nodeToRun, Env env) {
        super(nextCont, env);
        pendingNodes = new LinkedList<>();

        KsListNode iter = nodeToRun;
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
                    .newLastValue(KsNull.NULL)
                    .isSafePoint(true)
                    .build();
        }
        KsNode nextToRun = pendingNodes.pollFirst();
        ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
        return nextCont.prepareNextRun(state, nextToRun);
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .isSafePoint(true)
                    .build();
        }
        return prepareNextRun(state, currentNodeToRun);
    }
}
