package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonNull;

public class SetContInstance extends Continuation {
    private String varName;
    private KsonNode varValueExpr = KsonNull.NULL;
    public SetContInstance(Continuation currentCont, KsonListNode expr) {
        super(currentCont);
        if (expr.getNext() == KsonListNode.NIL || expr.getNext().getNext() == KsonListNode.NIL) {
            throw new RuntimeException("illegal set expr");
        }
        KsonNode varNameNode = expr.getNext().getValue();
        varName = varNameNode.asWord().getValue();
        varValueExpr = expr.getNext().getNext().getValue();
    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (varValueExpr.equals(KsonNull.NULL)) {
            Env varDeclaredAtEnv = getEnv().lookupDeclareEnv(varName);
            varDeclaredAtEnv.define(varName, varValueExpr);
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(lastValue)
                    .build();
        } else {
            KsonNode nextToRun = varValueExpr;
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, nextToRun);
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        Env varDeclaredAtEnv = getEnv().lookupDeclareEnv(varName);
        varDeclaredAtEnv.define(varName, varValueExpr);
        return ContRunResult.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(lastValue)
                .build();
    }
}
