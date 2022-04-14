package link.symtable.kson.core.node;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;

import lombok.Getter;


@Getter
public class KsHostObject extends KsNodeBase implements SupportMethodCall {
    private Object hostObj;
    public KsHostObject(Object hostObj) {
        this.hostObj = hostObj;
    }

    @Override
    public KsNode callMethod(ExecState state, String methodName, KsNode[] args) {
        if (hostObj instanceof SupportMethodCall) {
            return ((SupportMethodCall) hostObj).callMethod(state, methodName, args);
        } else {
            throw new RuntimeException("KsonHostObject");
        }
    }
}
