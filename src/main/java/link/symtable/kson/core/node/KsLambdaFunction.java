package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class KsLambdaFunction extends KsFunction {
    private List<String> paramNames = new ArrayList<>();
    private KsListNode block;

    public KsLambdaFunction(String name, KsListNode args, KsListNode block) {
        this.name = name;

        KsListNode iter = args;
        while (iter != KsListNode.NIL) {
            paramNames.add(iter.getValue().asWord().getValue());
            iter = iter.getNext();
        }
        this.block = block;

        // TODO 变长参数支持
        this.requiredArgsCount = paramNames.size();
        this.hasVariableArgs = false;
    }

    public boolean isFunction() {
        return true;
    }
    public KsFunction asFunction() {
        return this;
    }
}
