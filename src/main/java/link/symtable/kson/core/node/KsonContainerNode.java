package link.symtable.kson.core.node;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportSubscript;

public abstract class KsonContainerNode extends KsonNodeBase implements SupportSubscript {

    public KsonNode subscriptByString(ExecState state, String fieldName) {
        throw new NotImplementedException("subscriptByString not implemented, node:" + this.toString());
    }

    public KsonNode subscriptByIndex(ExecState state, int index) {
        throw new NotImplementedException("subscriptByIndex not implemented, node:" + this.toString());
    }
}
