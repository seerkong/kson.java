package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonNode;

public abstract class Continuation {
    private Continuation nextCont;
    private Env curEnv;

    public Continuation(Continuation currentCont) {
        this.curEnv = currentCont.curEnv;
        nextCont = currentCont;
    }

    public Continuation(Continuation currentCont, Env curEnv) {
        this.curEnv = curEnv;
        nextCont = currentCont;
    }

    public Continuation getNext() {
        return nextCont;
    }

    public Env getEnv() {
        return curEnv;
    }

    public abstract ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun);

    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
