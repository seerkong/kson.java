package link.symtable.kson.core.cpsinterpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;

public class ArrayContInstance extends KsContinuation {
    private LinkedList<KsNode> pendingNodes;
    private List<KsNode> evaledNodes;
    public ArrayContInstance(KsContinuation currentCont, KsArray nodeToRun) {
        super(currentCont);
        pendingNodes = new LinkedList<>(nodeToRun.getItems());
        evaledNodes = new ArrayList<>();
    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(new KsArray(evaledNodes))
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
