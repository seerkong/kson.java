package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.interpreter.ContRunState;
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
    private boolean needReturnErrors = false;

    public PerformContInstance(KsContinuation currentCont, KsListNode expr, boolean needReturnErrors) {
        super(currentCont);
        this.needReturnErrors = needReturnErrors;
        KsWord handlerWord = expr.getNextValue().asWord();
        handlerName = handlerWord.getValue();

        KsArray params = expr.getNextNextValue().asArray();
        for (int i = 0; i < params.size(); i++) {
            pendingNodes.add(params.get(i));
        }
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            String handlerVarName = String.format("__handler_%s", handlerName);
            KsNode lookupResult = getEnv().lookup(handlerVarName);
            KsArray handlerAndNextContArr = lookupResult.asArray();
            KsLambdaFunction performHander = (KsLambdaFunction)handlerAndNextContArr.get(0);
            KsContinuation tryNextCont = (KsContinuation)handlerAndNextContArr.get(1);

            WaitTaskContInstance waitCont = new WaitSingleTaskContInstance(getNext(), handlerName, needReturnErrors);
            ResumeTaskContInstance resolveCont = new ResumeTaskContInstance(waitCont, handlerName, true);
            ResumeTaskContInstance rejectCont = new ResumeTaskContInstance(waitCont, handlerName, false);

            List<KsNode> params = new ArrayList<>();
            params.add(resolveCont);    // resume
            params.add(resolveCont);    // resolve
            params.add(rejectCont);    // reject
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
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return prepareNextRun(state, currentNodeToRun);
    }
}
