package link.symtable.kson.core.node;

import java.util.function.BiFunction;

import link.symtable.kson.core.interpreter.ExecState;

import lombok.Getter;

@Getter
public class KsonHostSyncFunction extends KsonFunction {
    BiFunction<ExecState, KsonNode[], KsonNode> handler;

    public KsonHostSyncFunction(String name, int requiredArgsCount, boolean hasVariableArgs , BiFunction<ExecState, KsonNode[], KsonNode> handler) {
        this.name = name;
        this.requiredArgsCount = requiredArgsCount;
        this.hasVariableArgs = hasVariableArgs;
        this.handler = handler;
    }

    public KsonNode apply(ExecState state, KsonNode[] args) {
        return handler.apply(state, args);
    }
}
