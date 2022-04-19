package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsWord;

public class PerformContInstance extends KsContinuation {
    private String handlerName;
    private LinkedList<KsNode> pendingNodes = new LinkedList<>();
    private LinkedList<KsNode> evaledNodes = new LinkedList<>();
    private boolean waitingResume = false;

    public PerformContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        KsWord handlerWord = expr.getNextValue().asWord();
        handlerName = handlerWord.getValue();
        KsListNode iter = expr.getNextNextValue().asListNode();
        while (iter != KsListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
    }

    @Override
    public ContRunResult prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            waitingResume = true;
            String handlerVarName = String.format("__handler_%s", handlerName);
            KsNode lookupResult = getEnv().lookup(handlerVarName);
            KsArray handlerAndNextContArr = lookupResult.asArray();
            KsLambdaFunction performHander = (KsLambdaFunction)handlerAndNextContArr.get(0);
            KsContinuation tryNextCont = (KsContinuation)handlerAndNextContArr.get(1);
            List<KsNode> params = new ArrayList<>();
            params.add(this);    // resume
            params.addAll(evaledNodes);
            FuncCallContInstance newCont = new FuncCallContInstance(tryNextCont, performHander, params);
            return newCont.prepareNextRun(state, currentNodeToRun);
        } else {
            KsNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunResult runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        if (waitingResume) {
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        } else {
            evaledNodes.add(lastValue);
            return prepareNextRun(state, currentNodeToRun);
        }
    }
}
