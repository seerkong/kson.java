package link.symtable.kson.core.node;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportSubscript;

public abstract class KsContainerNode extends KsNodeBase implements SupportSubscript {
    public boolean subscriptAcceptKey(ExecState state, String fieldName) {
        throw new NotImplementedException("subscriptAcceptKey not implemented, node:" + this.toString());
    }

    public KsNode subscriptByString(ExecState state, String fieldName) {
        throw new NotImplementedException("subscriptByString not implemented, node:" + this.toString());
    }

    public KsNode subscriptByIndex(ExecState state, int index) {
        throw new NotImplementedException("subscriptByIndex not implemented, node:" + this.toString());
    }
}
