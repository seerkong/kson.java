package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;

@FunctionalInterface
public interface ContInstanceFactory {
    KsContinuation create(KsContinuation currentCont, KsListNode nodeToRun);
}
