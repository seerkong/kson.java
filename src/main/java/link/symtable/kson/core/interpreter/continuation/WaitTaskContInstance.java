package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsMap;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;
import link.symtable.kson.core.node.KsSymbol;

public class WaitTaskContInstance extends KsContinuation {
    protected boolean needReturnErrors = false;
    protected Map<String, Boolean> isTaskEndMap = new HashMap<>();
    protected Map<String, KsNode> resolvedTaskResultMap = new HashMap<>();
    protected Map<String, KsNode> rejectedTaskErrorMap = new HashMap<>();

    public WaitTaskContInstance(KsContinuation currentCont, String label, boolean needReturnErrors) {
        super(currentCont);
        isTaskEndMap.put(label, false);
        this.needReturnErrors = needReturnErrors;
    }

    public WaitTaskContInstance(KsContinuation currentCont, Collection<String> labels, boolean needReturnErrors) {
        super(currentCont);
        for (String label : labels) {
            isTaskEndMap.put(label, false);
        }
        this.needReturnErrors = needReturnErrors;
    }

    public void resolve(String label, KsNode result) {
        isTaskEndMap.put(label, true);
        resolvedTaskResultMap.put(label, result);
    }

    public void reject(String label, KsNode error) {
        isTaskEndMap.put(label, true);
        rejectedTaskErrorMap.put(label, error);
    }

    public void handleLastValue(KsNode lastValue) {
        KsArray resultTuple = lastValue.asArray();
        KsSymbol handler = resultTuple.get(0).asSymbol();
        String handlerName = handler.getValue();
        KsBoolean isResolve = resultTuple.get(1).asBoolean();
        KsNode result = resultTuple.get(2);
        if (isResolve.getValue()) {
            reject(handlerName, result);
        } else {
            resolve(handlerName, result);
        }
    }

    protected boolean isAllTasksFinished() {
        Set<String> allLables = new HashSet<>(isTaskEndMap.keySet());

        allLables.removeAll(resolvedTaskResultMap.keySet());
        allLables.removeAll(rejectedTaskErrorMap.keySet());
        return allLables.isEmpty();
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("WaitTaskContInstance#prepareNextRun");
    }

    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        handleLastValue(lastValue);
        // 任务没有全部执行完毕
        if (!isAllTasksFinished()) {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(this)
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        } else {
            KsMap resolvedMap = new KsMap(resolvedTaskResultMap);
            KsMap rejectedMap = new KsMap(rejectedTaskErrorMap);
            KsNode newLastValue;
            if (needReturnErrors) {
                List<KsNode> pairItems = new ArrayList<>();
                pairItems.add(resolvedMap);
                pairItems.add(rejectedMap);
                newLastValue = new KsArray(pairItems);
            } else {
                newLastValue = resolvedMap;
            }

            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(newLastValue)
                    .build();
        }
    }
}
