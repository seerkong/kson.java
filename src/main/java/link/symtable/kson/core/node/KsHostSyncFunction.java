package link.symtable.kson.core.node;

import java.util.function.BiFunction;

import link.symtable.kson.core.cpsinterpreter.ExecState;

import lombok.Getter;

@Getter
public class KsHostSyncFunction extends KsFunction {
    BiFunction<ExecState, KsNode[], KsNode> handler;

    public KsHostSyncFunction(String name, int requiredArgsCount, boolean hasVariableArgs , BiFunction<ExecState, KsNode[], KsNode> handler) {
        this.name = name;
        this.requiredArgsCount = requiredArgsCount;
        this.hasVariableArgs = hasVariableArgs;
        this.handler = handler;
    }

    public KsNode apply(ExecState state, KsNode[] args) {
        return handler.apply(state, args);
    }

    public boolean isFunction() {
        return true;
    }
    public KsFunction asFunction() {
        return this;
    }
}
