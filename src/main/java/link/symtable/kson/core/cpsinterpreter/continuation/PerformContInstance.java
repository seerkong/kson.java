package link.symtable.kson.core.cpsinterpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.cpsinterpreter.InternalConstants;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsSymbol;
import link.symtable.kson.core.node.KsWord;

public class PerformContInstance extends KsContinuation {
    private String handlerName;
    private LinkedList<KsNode> pendingNodes = new LinkedList<>();
    private LinkedList<KsNode> evaledNodes = new LinkedList<>();
    private boolean needReturnErrors = false;
    private NopContInstance waitLoopCont;
    private WaitSingleTaskContInstance waitCont;

    public PerformContInstance(KsContinuation currentCont, KsListNode expr, boolean needReturnErrors) {
        super(currentCont);
        this.needReturnErrors = needReturnErrors;
        waitLoopCont = new NopContInstance(currentCont);
        KsWord handlerWord = expr.getNextValue().asWord();
        handlerName = handlerWord.getValue();

        KsListNode iter = expr.getNextNext().asListNode();
        while (iter != KsListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }

        waitCont = new WaitSingleTaskContInstance(getNext(), handlerName, needReturnErrors);

    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        state.getWaitingResumeTasks().add(waitCont);
        if (pendingNodes.size() == 0) {
            String handlerVarName = String.format(InternalConstants.HANDLER_FORMART, handlerName);
            KsNode lookupResult = getEnv().lookup(handlerVarName);
            KsArray handlerAndNextContArr = lookupResult.asArray();
            KsLambdaFunction performHander = (KsLambdaFunction)handlerAndNextContArr.get(0);
            KsContinuation tryNextCont = (KsContinuation)handlerAndNextContArr.get(1);

            ResumeTaskContInstance resolveCont = new ResumeTaskContInstance(waitCont, new KsSymbol(handlerName), true);
            ResumeTaskContInstance rejectCont = new ResumeTaskContInstance(waitCont, new KsSymbol(handlerName), false);

            List<KsNode> params = new ArrayList<>();
            params.add(resolveCont);    // resume
            params.add(resolveCont);    // resolve
            params.add(rejectCont);    // reject
            params.addAll(evaledNodes);
            FuncCallContInstance newCont = new FuncCallContInstance(waitLoopCont, performHander, params);
            return newCont.prepareNextRun(state, currentNodeToRun);
        } else {
            KsNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return prepareNextRun(state, currentNodeToRun);
    }
}
