package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;

public class SetContInstance extends KsContinuation {
    private String varName;
    private KsNode varValueExpr = KsNull.NULL;
    public SetContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        if (expr.getNext() == KsListNode.NIL || expr.getNext().getNext() == KsListNode.NIL) {
            throw new RuntimeException("illegal set expr");
        }
        KsNode varNameNode = expr.getNext().getValue();
        varName = varNameNode.asWord().getValue();
        varValueExpr = expr.getNext().getNext().getValue();
    }

    public ContRunResult prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        KsNode nextToRun = varValueExpr;
        ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
        return nextCont.prepareNextRun(state, nextToRun);
    }

    @Override
    public ContRunResult runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        Env varDeclaredAtEnv = getEnv().lookupDeclareEnv(varName);
        varDeclaredAtEnv.define(varName, lastValue);
        return ContRunResult.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
