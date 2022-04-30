package link.symtable.kson.core.cpsinterpreter.continuation;

import java.util.Timer;
import java.util.TimerTask;

import link.symtable.kson.core.cpsinterpreter.ContRunState;
import link.symtable.kson.core.cpsinterpreter.Env;
import link.symtable.kson.core.cpsinterpreter.ExecAction;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class SetTimeoutContInstance extends KsContinuation {
    private KsListNode funcDeclareExpr;
    private long waitTimeInMillis = 0;
    public SetTimeoutContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        this.funcDeclareExpr = expr.getNextValue().asListNode();
        this.waitTimeInMillis = expr.getNextNextValue().asInt64().getValue();

    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        Env currentEnv = getEnv();
        LandCallbackContInstance landCont = new LandCallbackContInstance(null, currentEnv);
        KsLambdaFunction func = new KsLambdaFunction(funcDeclareExpr);
        KsListNode funcCallExpr = new KsListNode(func);
        FuncCallContInstance contUsedForFuture = new FuncCallContInstance(landCont, funcCallExpr);
        ContRunState callThisCallbackState = contUsedForFuture.prepareNextRun(state, funcCallExpr);

        long timerId = state.getTimerTaskIdGen().getAndIncrement();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                state.getTimerTasks().remove(timerId);
                state.getCallbackQueue().offer(callThisCallbackState);
//                synchronized (state.getLock()) {
//                    state.getNotEmpty().signal();
//                }

            }
        };
        Timer t = state.getTimer();
        t.schedule(tt, waitTimeInMillis);
        KsInt64 newLastVal = new KsInt64(timerId);
        state.getTimerTasks().put(timerId, tt);
        return ContRunState.builder()
                .nextAction(ExecAction.RUN_CONT)
                .nextCont(getNext())
                .nextNodeToRun(currentNodeToRun)
                .newLastValue(newLastVal)
                .build();
    }
}
