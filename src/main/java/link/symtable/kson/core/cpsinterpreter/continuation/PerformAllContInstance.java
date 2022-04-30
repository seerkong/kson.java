package link.symtable.kson.core.cpsinterpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.cpsinterpreter.InternalConstants;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;

public class PerformAllContInstance extends KsContinuation {
    private boolean needReturnErrors = false;

    private LinkedList<Pair<String, KsArray>> pendingHandlerAndArgsPairs = new LinkedList<>();
    private Pair<String, KsArray> currentEvalArgsTask;
    private List<Pair<String, KsArray>> evaledHandlerAndArgsPairs = new ArrayList<>();
    private int currentInvokeHanderIdx = -1;
    private WaitMultiTaskContInstance waitCont;
    private PerformAllStage currentStage = PerformAllStage.EVAL_HANDLER_ARGS;

    private static enum PerformAllStage {
        EVAL_HANDLER_ARGS,
        INVOKE_HANDLERS
    }



    public PerformAllContInstance(KsContinuation currentCont, KsListNode initExpr, boolean needReturnErrors) {
        super(currentCont);
        this.needReturnErrors = needReturnErrors;

        KsListNode iter = initExpr.getNext();
        while (iter != KsListNode.NIL) {
            if (!iter.getValue().isListNode()) {
                throw new RuntimeException("should at least has one task");
            }
            KsListNode handlerAndArgsList = iter.getValue().asListNode();
            String handlerName = handlerAndArgsList.getValue().asWord().getValue();
            KsListNode handlerArgIter = handlerAndArgsList.getNext();
            List<KsNode> methodArgs = new ArrayList<>();
            while (handlerArgIter != KsListNode.NIL) {
                methodArgs.add(handlerArgIter.getValue());

                handlerArgIter = handlerArgIter.getNext();
            }
            pendingHandlerAndArgsPairs.add(new ImmutablePair<>(handlerName, new KsArray(methodArgs)));
            iter = iter.getNext();
        }
        waitCont = new WaitMultiTaskContInstance(getNext(), pendingHandlerAndArgsPairs.size(), needReturnErrors);

    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        state.getWaitingResumeTasks().add(waitCont);

        if (pendingHandlerAndArgsPairs.size() > 0) {
            currentEvalArgsTask = pendingHandlerAndArgsPairs.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, currentEvalArgsTask.getRight());
        } else if (currentStage == PerformAllStage.INVOKE_HANDLERS && (currentInvokeHanderIdx + 1) < evaledHandlerAndArgsPairs.size()) {
            currentInvokeHanderIdx += 1;
            Pair<String, KsArray> currentInvokeHandlerTask = evaledHandlerAndArgsPairs.get(currentInvokeHanderIdx);
            String handlerName = currentInvokeHandlerTask.getLeft();

            String handlerVarName = String.format(InternalConstants.HANDLER_FORMART, handlerName);
            KsNode lookupResult = getEnv().lookup(handlerVarName);
            KsArray handlerAndNextContArr = lookupResult.asArray();
            KsLambdaFunction performHander = (KsLambdaFunction)handlerAndNextContArr.get(0);
            // the cont after try end
            KsContinuation tryNextCont = (KsContinuation)handlerAndNextContArr.get(1);

            ResumeTaskContInstance resolveCont = new ResumeTaskContInstance(waitCont, new KsInt64(currentInvokeHanderIdx), true);
            ResumeTaskContInstance rejectCont = new ResumeTaskContInstance(waitCont, new KsInt64(currentInvokeHanderIdx), false);

            List<KsNode> params = new ArrayList<>();
            params.add(resolveCont);    // resume
            params.add(resolveCont);    // resolve
            params.add(rejectCont);    // reject
            params.addAll(currentInvokeHandlerTask.getRight().getItems());
            FuncCallContInstance newCont = new FuncCallContInstance(this, performHander, params);
            return newCont.prepareNextRun(state, currentNodeToRun);
        } else {
            // all jobs finished
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(waitCont)
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(KsNull.NULL)
                    .build();
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (currentStage == PerformAllStage.EVAL_HANDLER_ARGS) {
            // step2 eval method args
            evaledHandlerAndArgsPairs.add(new ImmutablePair<>(currentEvalArgsTask.getLeft(), lastValue.asArray()));
            if (pendingHandlerAndArgsPairs.size() == 0) {
                currentStage = PerformAllStage.INVOKE_HANDLERS;
            }
        }
        return prepareNextRun(state, currentNodeToRun);
    }
}
