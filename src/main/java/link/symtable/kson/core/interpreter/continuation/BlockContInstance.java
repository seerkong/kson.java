package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;

public class BlockContInstance extends Continuation {
    private LinkedList<KsonNode> pendingNodes;

    public BlockContInstance(Continuation nextCont, KsonListNode nodeToRun) {
        this(nextCont, nodeToRun, nextCont.getEnv());
    }

    public BlockContInstance(Continuation nextCont, KsonListNode nodeToRun, Env env) {
        super(nextCont, env);
        pendingNodes = new LinkedList<>();

        KsonListNode iter = nodeToRun;
        while (iter != KsonListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        } else {
            KsonNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, nextToRun);
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
