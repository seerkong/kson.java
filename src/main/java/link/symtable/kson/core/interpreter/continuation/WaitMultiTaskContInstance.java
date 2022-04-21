package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;

public class WaitMultiTaskContInstance extends KsContinuation {
    protected boolean needReturnErrors = false;
    protected boolean[] isTaskEndMarks;
    protected KsNode[] resolvedTaskResultArr;
    protected KsNode[] rejectedTaskErrorArr;
    private NopContInstance waitLoopCont;

    public WaitMultiTaskContInstance(KsContinuation currentCont, int taskSize, boolean needReturnErrors) {
        super(currentCont);
        this.needReturnErrors = needReturnErrors;
        isTaskEndMarks = new boolean[taskSize];
        Arrays.fill(isTaskEndMarks, false);
        resolvedTaskResultArr = new KsNode[taskSize];
        rejectedTaskErrorArr = new KsNode[taskSize];
        waitLoopCont = new NopContInstance(currentCont);
    }

    public void resolve(int index, KsNode result) {
        isTaskEndMarks[index] = true;
        resolvedTaskResultArr[index] = result;
    }

    public void reject(int index, KsNode error) {
        isTaskEndMarks[index] = true;
        rejectedTaskErrorArr[index] = error;
    }

    // resultTuple (index, isResolved, val)
    public void handleLastValue(KsArray resultTuple) {
        KsInt64 indexLabel = resultTuple.get(0).asInt64();
        int idx = (int) indexLabel.getValue();
        KsBoolean isResolve = resultTuple.get(1).asBoolean();
        KsNode result = resultTuple.get(2);
        if (isResolve.getValue()) {
            resolve(idx, result);
        } else {
            reject(idx, result);
        }
    }

    protected boolean isAllTasksFinished() {
        boolean r = true;
        for (int i = 0; i < isTaskEndMarks.length; i++) {
            if (!isTaskEndMarks[i]) {
                r = false;
            }
        }
        return r;
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("WaitTaskContInstance#prepareNextRun");
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
        handleLastValue(lastValue.asArray());
        if (!isAllTasksFinished()) {
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(waitLoopCont)
                    .isSafePoint(true)
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        } else {
            List<KsNode> resultInner = new ArrayList<>();
            for (int i = 0; i < isTaskEndMarks.length; i++) {
                KsNode resultInnerNode;
                if (needReturnErrors) {
                    List<KsNode> pairItems = new ArrayList<>();
                    pairItems.add(Optional.ofNullable(resolvedTaskResultArr[i]).orElse(KsNull.NULL));
                    pairItems.add(Optional.ofNullable(rejectedTaskErrorArr[i]).orElse(KsNull.NULL));
                    resultInnerNode = new KsArray(pairItems);
                } else {
                    resultInnerNode = Optional.ofNullable(resolvedTaskResultArr[i]).orElse(KsNull.NULL);
                }
                resultInner.add(resultInnerNode);
            }

            KsNode newLastValue = new KsArray(resultInner);

            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(newLastValue)
                    .build();
        }
    }
}
