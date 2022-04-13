package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonArray;
import link.symtable.kson.core.node.KsonNode;

public class ArrayContInstance extends Continuation {
    private LinkedList<KsonNode> pendingNodes;
    private List<KsonNode> evaledNodes;
    public ArrayContInstance(Continuation currentCont, KsonArray nodeToRun) {
        super(currentCont);
        pendingNodes = new LinkedList<>(nodeToRun.getItems());
        evaledNodes = new ArrayList<>();
    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(new KsonArray(evaledNodes))
                    .build();
        } else {
            KsonNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, nextToRun);
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
