package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.node.KsonListNode;

@FunctionalInterface
public interface ContInstanceFactory {
    Continuation create(Continuation currentCont, KsonListNode nodeToRun);
}
