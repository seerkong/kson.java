package link.symtable.kson.core.interpreter;

import link.symtable.kson.core.interpreter.continuation.Continuation;
import link.symtable.kson.core.node.KsonNode;

import lombok.Builder;

@Builder
public class ContRunResult {
    public ExecAction nextAction;
    public KsonNode nextNodeToRun;
    public Continuation nextCont;
    public KsonNode newLastValue;
}
