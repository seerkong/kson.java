package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsString;
import link.symtable.kson.core.node.KsSymbol;

public class ResumeTaskContInstance extends KsContinuation {
    private String handlerName;
    private boolean isResolve = true;

    public ResumeTaskContInstance(KsContinuation currentCont, String handlerName, boolean isResolve) {
        super(currentCont);
        this.handlerName = handlerName;
        this.isResolve = isResolve;
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        throw new NotImplementedException("ResumeTaskContInstance#prepareNextRun");
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        KsBoolean isSuccess = new KsBoolean(isResolve);
        List<KsNode> arrInner = new ArrayList<>();
        arrInner.add(new KsSymbol(handlerName));
        arrInner.add(isSuccess);
        arrInner.add(lastValue);
        KsNode newLastVal = new KsArray(arrInner);

        return ContRunState.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(newLastVal)
                .build();
    }
}
