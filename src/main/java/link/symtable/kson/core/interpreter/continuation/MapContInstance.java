package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsMap;
import link.symtable.kson.core.node.KsNode;

public class MapContInstance extends KsContinuation {
    private LinkedList<Pair<String, KsNode>> pendingPairs;
    private Pair<String, KsNode> currentPair;
    private List<Pair<String, KsNode>> evaledPairs;
    public MapContInstance(KsContinuation currentCont, KsMap nodeToRun) {
        super(currentCont);
        pendingPairs = new LinkedList<>();
        evaledPairs = new ArrayList<>();
        Map<String, KsNode> map = nodeToRun.getValue();

        for (Map.Entry<String, KsNode> entry : map.entrySet()) {
            pendingPairs.push(new ImmutablePair<>(entry.getKey(), entry.getValue()));
        }

    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingPairs.size() == 0) {
            KsMap map = new KsMap(evaledPairs);
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(map)
                    .build();
        } else {
            currentPair = pendingPairs.pollFirst();
            KsNode nextToRun = currentPair.getValue();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        evaledPairs.add(new ImmutablePair<>(currentPair.getKey(), lastValue));
        return prepareNextRun(state, currentNodeToRun);
    }
}
