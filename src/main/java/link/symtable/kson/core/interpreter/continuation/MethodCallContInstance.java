package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;
import link.symtable.kson.core.node.KsonArray;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;

public class MethodCallContInstance extends Continuation {
    private KsonNode targetNode;
    private KsonNode targetEvaled = null;

    private LinkedList<Pair<String, KsonArray>> pendingMethodAndArgsPairs = new LinkedList<>();
    ;
    private Pair<String, KsonArray> currentMethodAndArgsPairs;
    private LinkedList<Pair<String, KsonArray>> evaledMethodAndArgsPairs = new LinkedList<>();
    ;

    public MethodCallContInstance(Continuation currentCont, KsonListNode initExpr) {
        super(currentCont);
        targetNode = initExpr.getNext().getValue();

        KsonListNode iter = initExpr.getNext().getNext();
        while (iter != KsonListNode.NIL) {
            if (!iter.getValue().isListNode()) {
                throw new RuntimeException("method and args should be a list");
            }
            KsonListNode methodAndArgsList = iter.getValue().asListNode();
            String methodName = methodAndArgsList.getValue().asWord().getValue();
            KsonListNode methodArgIter = methodAndArgsList.getNext();
            List<KsonNode> methodArgs = new ArrayList<>();
            while (methodArgIter != KsonListNode.NIL) {
                methodArgs.add(methodArgIter.getValue());

                methodArgIter = methodArgIter.getNext();
            }
            pendingMethodAndArgsPairs.add(new ImmutablePair<>(methodName, new KsonArray(methodArgs)));
            iter = iter.getNext();
        }
    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (targetEvaled == null) {
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, targetNode);
        } else if (pendingMethodAndArgsPairs.size() == 0) {
            KsonNode targetNode = targetEvaled;
            while (evaledMethodAndArgsPairs.size() > 0) {
                Pair<String, KsonArray> methodAndArgs = evaledMethodAndArgsPairs.pollFirst();
                if (!(targetNode instanceof SupportMethodCall)) {
                    throw new RuntimeException("get subscript not supported");
                }
                KsonNode[] argsArr = methodAndArgs.getRight().getItems().toArray(new KsonNode[0]);
                targetNode = ((SupportMethodCall) targetNode).callMethod(state, methodAndArgs.getLeft(), argsArr);
            }

            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(targetNode)
                    .build();
        } else {
            currentMethodAndArgsPairs = pendingMethodAndArgsPairs.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, currentMethodAndArgsPairs.getRight());
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (currentMethodAndArgsPairs == null) {
            // eval method chain first target
            targetEvaled = lastValue;
        } else {
            evaledMethodAndArgsPairs.add(new ImmutablePair<>(currentMethodAndArgsPairs.getLeft(), lastValue.asArray()));
        }
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
