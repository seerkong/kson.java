package link.symtable.kson.core.interpreter.continuation;

import java.util.Timer;
import java.util.TimerTask;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
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

        System.out.println("create set timeout 当前时间戳："+System.currentTimeMillis());
        long timerId = state.getTimerTaskIdGen().getAndIncrement();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                System.out.println("exec set timeout callback 当前时间戳："+System.currentTimeMillis());
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