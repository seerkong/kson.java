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
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class MethodCallContInstance extends KsContinuation {
    private KsNode targetNode;
    private KsNode targetEvaled = null;

    private LinkedList<Pair<String, KsArray>> pendingMethodAndArgsPairs = new LinkedList<>();
    ;
    private Pair<String, KsArray> currentMethodAndArgsPairs;
    private LinkedList<Pair<String, KsArray>> evaledMethodAndArgsPairs = new LinkedList<>();
    ;

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

    public ContRunResult initNextRun(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (targetEvaled == null) {
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, targetNode);
        } else if (pendingMethodAndArgsPairs.size() == 0) {
            KsNode targetNode = targetEvaled;
            while (evaledMethodAndArgsPairs.size() > 0) {
                Pair<String, KsArray> methodAndArgs = evaledMethodAndArgsPairs.pollFirst();
                if (!(targetNode instanceof SupportMethodCall)) {
                    throw new RuntimeException("get subscript not supported");
                }
                KsNode[] argsArr = methodAndArgs.getRight().getItems().toArray(new KsNode[0]);
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
    public ContRunResult run(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (currentMethodAndArgsPairs == null) {
            // eval method chain first target
            targetEvaled = lastValue;
        } else {
            evaledMethodAndArgsPairs.add(new ImmutablePair<>(currentMethodAndArgsPairs.getLeft(), lastValue.asArray()));
        }
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
