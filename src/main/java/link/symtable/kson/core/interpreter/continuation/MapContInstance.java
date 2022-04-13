package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonMap;
import link.symtable.kson.core.node.KsonNode;

public class MapContInstance extends Continuation {
    private LinkedList<Pair<String, KsonNode>> pendingPairs;
    private Pair<String, KsonNode> currentPair;
    private List<Pair<String, KsonNode>> evaledPairs;
    public MapContInstance(Continuation currentCont, KsonMap nodeToRun) {
        super(currentCont);
        pendingPairs = new LinkedList<>();
        evaledPairs = new ArrayList<>();
        Map<String, KsonNode> map = nodeToRun.getValue();

        for (Map.Entry<String, KsonNode> entry : map.entrySet()) {
            pendingPairs.push(new ImmutablePair<>(entry.getKey(), entry.getValue()));
        }

    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (pendingPairs.size() == 0) {
            KsonMap map = new KsonMap(evaledPairs);
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(map)
                    .build();
        } else {
            currentPair = pendingPairs.pollFirst();
            KsonNode nextToRun = currentPair.getValue();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, nextToRun);
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        evaledPairs.add(new ImmutablePair<>(currentPair.getKey(), lastValue));
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
