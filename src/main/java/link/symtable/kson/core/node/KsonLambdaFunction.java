package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class KsonLambdaFunction extends KsonFunction {
    private List<String> paramNames = new ArrayList<>();
    private KsonListNode block;

    public KsonLambdaFunction(String name, KsonListNode args, KsonListNode block) {
        this.name = name;

        KsonListNode iter = args;
        while (iter != KsonListNode.NIL) {
            paramNames.add(iter.getValue().asWord().getValue());
            iter = iter.getNext();
        }
        this.block = block;

        // TODO 变长参数支持
        this.requiredArgsCount = paramNames.size();
        this.hasVariableArgs = false;
    }
}
