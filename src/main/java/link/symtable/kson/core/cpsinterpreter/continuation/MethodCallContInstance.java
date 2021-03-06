package link.symtable.kson.core.cpsinterpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.cpsinterpreter.oopsupport.SupportMethodCall;
import link.symtable.kson.core.cpsinterpreter.oopsupport.SupportSubscript;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class MethodCallContInstance extends KsContinuation {
    private KsNode targetNode;
    private KsNode targetEvaled = null;

    private LinkedList<Pair<String, KsArray>> pendingMethodAndArgsPairs = new LinkedList<>();
    private Pair<String, KsArray> currentEvalArgsTask;
    private LinkedList<Pair<String, KsArray>> evaledMethodAndArgsPairs = new LinkedList<>();
    private Pair<String, KsArray> currentApplyMethodTask;


    public MethodCallContInstance(KsContinuation currentCont, KsListNode initExpr) {
        super(currentCont);
        targetNode = initExpr.getNext().getValue();

        KsListNode iter = initExpr.getNext().getNext();
        while (iter != KsListNode.NIL) {
            if (!iter.getValue().isListNode()) {
                throw new RuntimeException("method and args should be a list");
            }
            KsListNode methodAndArgsList = iter.getValue().asListNode();
            String methodName = methodAndArgsList.getValue().asWord().getValue();
            KsListNode methodArgIter = methodAndArgsList.getNext();
            List<KsNode> methodArgs = new ArrayList<>();
            while (methodArgIter != KsListNode.NIL) {
                methodArgs.add(methodArgIter.getValue());

                methodArgIter = methodArgIter.getNext();
            }
            pendingMethodAndArgsPairs.add(new ImmutablePair<>(methodName, new KsArray(methodArgs)));
            iter = iter.getNext();
        }
    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (targetEvaled == null) {
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, targetNode);
        } else if (pendingMethodAndArgsPairs.size() > 0) {
            currentEvalArgsTask = pendingMethodAndArgsPairs.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, currentEvalArgsTask.getRight());
        } else if (evaledMethodAndArgsPairs.size() > 0) {
            currentApplyMethodTask = evaledMethodAndArgsPairs.pollFirst();
            String methodName = currentApplyMethodTask.getLeft();
            KsNode targetNode = targetEvaled;
            if (targetNode instanceof SupportSubscript && ((SupportSubscript) targetNode).subscriptAcceptKey(state, methodName)) {
                KsNode fieldValue = ((SupportSubscript) targetNode).subscriptByString(state, methodName);
                if (fieldValue.isFunction()) {
                    List<KsNode> args = new ArrayList<>();
                    args.add(targetNode);
                    args.addAll(currentApplyMethodTask.getRight().getItems());
                    FuncCallContInstance newCont = new FuncCallContInstance(this, fieldValue.asFunction(), args);
                    return newCont.prepareNextRun(state, currentNodeToRun);
                } else {
                    throw new RuntimeException("field is not a function, call method not supported");
                }
            } else if (targetEvaled instanceof SupportMethodCall) {
                KsNode[] argsArr = currentApplyMethodTask.getRight().getItems().toArray(new KsNode[0]);

                targetNode = ((SupportMethodCall) targetNode).callMethod(state, methodName, argsArr);
                return ContRunState.builder()
                        .nextAction(ExecAction.RUN_CONT)
                        .nextCont(this)
                        .nextNodeToRun(currentNodeToRun)
                        .newLastValue(targetNode)
                        .build();
            } else {
                throw new RuntimeException("call method not supported");
            }

        } else {
            // all jobs finished
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(targetEvaled)
                    .build();
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (currentEvalArgsTask == null) {
            // step1 eval first target
            targetEvaled = lastValue;
        } else if (currentApplyMethodTask == null) {
            // step2 eval method args
            evaledMethodAndArgsPairs.add(new ImmutablePair<>(currentEvalArgsTask.getLeft(), lastValue.asArray()));
        } else {
            // step3 apply args to method
            targetEvaled = lastValue;
        }
        return prepareNextRun(state, currentNodeToRun);
    }
}
