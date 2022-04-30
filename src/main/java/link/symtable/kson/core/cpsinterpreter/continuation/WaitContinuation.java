package link.symtable.kson.core.cpsinterpreter.continuation;

import link.symtable.kson.core.cpsinterpreter.Env;
import link.symtable.kson.core.node.KsContinuation;

public abstract class WaitContinuation extends KsContinuation {
    public WaitContinuation(KsContinuation currentCont) {
        super(currentCont);
    }

    public WaitContinuation(KsContinuation currentCont, Env curEnv) {
        super(currentCont, curEnv);
    }
}
