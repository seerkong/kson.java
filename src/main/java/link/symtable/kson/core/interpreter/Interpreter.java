package link.symtable.kson.core.interpreter;

import java.util.concurrent.TimeUnit;

import link.symtable.kson.core.interpreter.continuation.ExecNodeContInstance;
import link.symtable.kson.core.interpreter.continuation.LandCallbackContInstance;
import link.symtable.kson.core.interpreter.continuation.WaitTaskContInstance;
import link.symtable.kson.core.interpreter.functions.HostSystemFunctions;
import link.symtable.kson.core.interpreter.functions.HostPrimitiveFuctions;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.interpreter.continuation.LandContInstance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Interpreter {
    private void exportDefaultToEnv(Env env) {
        HostSystemFunctions.exportToEnv(env);
        HostPrimitiveFuctions.exportToEnv(env);
    }

    public ExecResult run(KsNode ast, ExecState state, Env env) {
        exportDefaultToEnv(env);
        ExecNodeContInstance startCont = new ExecNodeContInstance(new LandContInstance(null, env));
        ContRunState r =  startLoop(state, ast, startCont);
        return new ExecResult(r.newLastValue);
    }



    public ContRunState startLoop(ExecState state, KsNode startNode, KsContinuation startCont) {
        ExecAction nextAction = ExecAction.RUN_CONT;
        KsNode lastValue = KsNull.NULL;
        KsNode currentNodeToRun = startNode;
        KsContinuation currentContInstance = startCont;

        ContRunState r = startCont.prepareNextRun(state, currentNodeToRun);


        while (r.nextAction != ExecAction.FINISH) {
            ContRunState contRunStateBeforeCheck = r;
            if (isSafePoint(contRunStateBeforeCheck)) {
                r = runCallbackOrRestore(state, contRunStateBeforeCheck);
                if (r != contRunStateBeforeCheck) {
                    log.info("callback loaded or old cont restored");
                }
            }

            nextAction = r.nextAction;
            lastValue = r.newLastValue;
            currentNodeToRun = r.nextNodeToRun;
            currentContInstance = r.nextCont;
            log.info("lastValue {}", lastValue);

            switch (nextAction) {
                case RUN_CONT:
                    r = currentContInstance.runWithValue(state, lastValue, currentNodeToRun);
                    break;
                default:
                    // 不应该执行到这里
                    r.nextAction = ExecAction.FINISH;
                    break;
            }
            log.info("");
        }

        return r;
    }

    private boolean isSafePoint(ContRunState r) {
        return r.isSafePoint || r.nextCont instanceof LandContInstance;
    }

    private boolean isCallbackFinished(ContRunState r) {
        return r.nextCont instanceof LandCallbackContInstance;
    }

    private boolean isWaitTaskContPoint(ExecState state, ContRunState r) {
        return (state.getWaitingResumeTasks().size() > 0 || state.getTimerTasks().size() > 0)
                && (r.nextCont instanceof LandContInstance || r.nextCont instanceof WaitTaskContInstance);
    }

    public ContRunState runCallbackOrRestore(ExecState state, ContRunState contRunStateBeforeCheck) {
        if (state.hasStoredContRun() && isCallbackFinished(contRunStateBeforeCheck)) {
            return state.restoreContRunState();
        }
        if (state.getCallbackQueue().size() > 0) {
            try {
                ContRunState callbackState = state.getCallbackQueue().take();
                state.setStoredContRun(contRunStateBeforeCheck);
                return callbackState;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isWaitTaskContPoint(state, contRunStateBeforeCheck)) {
            try {
                // TODO 修改为等待resume task queue变化了，或者timer task map 变化了
                ContRunState callbackState = take(state, contRunStateBeforeCheck);
                state.setStoredContRun(contRunStateBeforeCheck);
                return callbackState;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return contRunStateBeforeCheck;
    }

    public ContRunState take(ExecState state, ContRunState r) throws InterruptedException {
//        state.getLock().lock();
        try {
//            while (isWaitTaskContPoint(state, r)) {
//                state.getNotEmpty().await();
//            }
            ContRunState callbackState = state.getCallbackQueue().poll(1, TimeUnit.HOURS);
            return callbackState;
        } finally {
//            System.out.println("unlocking");
//            state.getLock().unlock();
        }
    }
}
