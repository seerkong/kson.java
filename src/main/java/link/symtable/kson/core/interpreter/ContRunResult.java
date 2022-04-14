package link.symtable.kson.core.interpreter;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsNode;

import lombok.Builder;

@Builder
public class ContRunResult {
    public ExecAction nextAction;
    public KsNode nextNodeToRun;
    public KsContinuation nextCont;
    public KsNode newLastValue;
}
