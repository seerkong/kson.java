package link.symtable.kson.core.node;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;

public abstract class KsContinuation extends KsNodeBase {
    private KsContinuation nextCont;
    private Env curEnv;

    public KsContinuation(KsContinuation currentCont) {
        this.curEnv = currentCont.curEnv;
        nextCont = currentCont;
    }

    public KsContinuation(KsContinuation currentCont, Env curEnv) {
        this.curEnv = curEnv;
        nextCont = currentCont;
    }

    public KsContinuation getNext() {
        return nextCont;
    }

    public Env getEnv() {
        return curEnv;
    }

    public abstract ContRunResult initNextRun(ExecState state, KsNode lastValue, KsNode currentNodeToRun);

    public ContRunResult run(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
