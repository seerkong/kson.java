package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsMap;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;


public class WaitSingleTaskContInstance extends WaitTaskContInstance {
    private String label;
    public WaitSingleTaskContInstance(KsContinuation currentCont, String label, boolean needReturnErrors) {
        super(currentCont, label, needReturnErrors);
        this.label = label;
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
            KsNode resolvedResult = Optional.ofNullable(resolvedTaskResultMap.get(label)).orElse(KsNull.NULL);
            KsNode rejectedError = Optional.ofNullable(rejectedTaskErrorMap.get(label)).orElse(KsNull.NULL);
            KsNode newLastValue;
            if (needReturnErrors) {
                List<KsNode> pairItems = new ArrayList<>();
                pairItems.add(resolvedResult);
                pairItems.add(rejectedError);
                newLastValue = new KsArray(pairItems);
            } else {
                newLastValue = resolvedResult;
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
