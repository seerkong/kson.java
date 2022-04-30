package link.symtable.kson.core.cpsinterpreter.continuation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;
import link.symtable.kson.core.node.KsSymbol;


public class WaitSingleTaskContInstance extends WaitContinuation {
    private String label;
    protected boolean needReturnErrors = false;
    private boolean isTaskEnd = false;
    private KsNode resolvedTaskResult;
    private KsNode rejectedTaskError;
    private NopContInstance waitLoopCont;

    public WaitSingleTaskContInstance(KsContinuation currentCont, String label, boolean needReturnErrors) {
        super(currentCont);
        this.needReturnErrors = needReturnErrors;
        this.label = label;
        waitLoopCont = new NopContInstance(currentCont);
    }

    public void resolve(String label, KsNode result) {
        isTaskEnd = true;
        resolvedTaskResult = result;
    }

    public void reject(String label, KsNode error) {
        isTaskEnd = true;
        rejectedTaskError = error;
    }

    public void handleLastValue(KsNode lastValue) {
        KsArray resultTuple = lastValue.asArray();
        KsSymbol handler = resultTuple.get(0).asSymbol();
        String handlerName = handler.getValue();
        KsBoolean isResolve = resultTuple.get(1).asBoolean();
        KsNode result = resultTuple.get(2);
        if (isResolve.getValue()) {
            resolve(handlerName, result);
        } else {
            reject(handlerName, result);
        }
    }

    protected boolean isAllTasksFinished() {
        return isTaskEnd;
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("WaitSingleTaskContInstance#prepareNextRun");
    }

    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (!(lastValue instanceof KsArray)) {
            // continue wait
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(waitLoopCont)
                    .isSafePoint(true)
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        }
        handleLastValue(lastValue);
        // 任务没有全部执行完毕
        if (!isAllTasksFinished()) {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(waitLoopCont)
                    .isSafePoint(true)
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        } else {
            state.getWaitingResumeTasks().remove(this);

            KsNode resolvedResult = Optional.ofNullable(resolvedTaskResult).orElse(KsNull.NULL);
            KsNode rejectedError = Optional.ofNullable(rejectedTaskError).orElse(KsNull.NULL);
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
