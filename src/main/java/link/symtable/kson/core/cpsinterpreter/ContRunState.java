package link.symtable.kson.core.cpsinterpreter;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;

import lombok.Builder;

@Builder
public class ContRunState {
    public ExecAction nextAction;
    public KsNode nextNodeToRun;
    public KsContinuation nextCont;
    public KsNode newLastValue;
    public boolean isSafePoint = false;
}
