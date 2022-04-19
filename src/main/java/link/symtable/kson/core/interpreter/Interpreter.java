package link.symtable.kson.core.interpreter;

import link.symtable.kson.core.interpreter.functions.HostIOFunctions;
import link.symtable.kson.core.interpreter.functions.HostPrimitiveFuctions;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNull;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.interpreter.continuation.LandContInstance;
import link.symtable.kson.core.interpreter.continuation.ContinuationHelpers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Interpreter {
    private void exportDefaultToEnv(Env env) {
        HostIOFunctions.exportToEnv(env);
        HostPrimitiveFuctions.exportToEnv(env);
    }

    public ExecResult run(KsNode ast, ExecState state, Env env) {
        exportDefaultToEnv(env);

        ExecAction nextAction;
        KsNode lastValue = KsNull.NULL;
        KsNode currentNodeToRun = ast;
        KsContinuation currentContInstance = new LandContInstance(null, env);

        ContRunResult r = ContinuationHelpers.EntryContHandler.run(state, lastValue, currentNodeToRun, currentContInstance);


        while (r.nextAction != ExecAction.FINISH) {
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

        return new ExecResult(lastValue);
    }
}
