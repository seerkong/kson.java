package link.symtable.kson.core.interpreter;

import link.symtable.kson.core.interpreter.functions.HostIOFunctions;
import link.symtable.kson.core.interpreter.functions.HostMathFuctions;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonNull;
import link.symtable.kson.core.interpreter.continuation.Continuation;
import link.symtable.kson.core.interpreter.continuation.ReturnContInstance;
import link.symtable.kson.core.interpreter.continuation.ContinuationHelpers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Interpreter {
    private void exportDefaultToEnv(Env env) {
        HostIOFunctions.exportToEnv(env);
        HostMathFuctions.exportToEnv(env);
    }

    public ExecResult run(KsonNode ast, ExecState state, Env env) {
        exportDefaultToEnv(env);

        ExecAction nextAction;
        KsonNode lastValue = KsonNull.NULL;
        KsonNode currentNodeToRun = ast;
        Continuation currentContInstance = new ReturnContInstance(null, env);

        ContRunResult r = ContinuationHelpers.EntryContHandler.run(state, lastValue, currentNodeToRun, currentContInstance);


        while (r.nextAction != ExecAction.FINISH) {
            nextAction = r.nextAction;
            lastValue = r.newLastValue;
            currentNodeToRun = r.nextNodeToRun;
            currentContInstance = r.nextCont;
            log.info("lastValue {}", lastValue);
            switch (nextAction) {
                case RUN_CONT:
                    r = currentContInstance.run(state, lastValue, currentNodeToRun);
                    break;
                default:
                    // 不应该执行到这里
                    r.nextAction = ExecAction.FINISH;
                    break;
            }
            log.info("");
        }

        return new ExecResult(lastValue);
    }
}