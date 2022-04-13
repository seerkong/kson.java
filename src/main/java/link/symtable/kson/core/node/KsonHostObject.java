package link.symtable.kson.core.node;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;

import lombok.Getter;


@Getter
public class KsonHostObject extends KsonNodeBase implements SupportMethodCall {
    private Object hostObj;
    public KsonHostObject(Object hostObj) {
        this.hostObj = hostObj;
    }

    @Override
    public KsonNode callMethod(ExecState state, String methodName, KsonNode[] args) {
        if (hostObj instanceof SupportMethodCall) {
            return ((SupportMethodCall) hostObj).callMethod(state, methodName, args);
        } else {
            throw new RuntimeException("KsonHostObject");
        }
    }
}
