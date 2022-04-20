package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class KsLambdaFunction extends KsFunction {
    private List<String> paramNames = new ArrayList<>();
    private KsListNode block;

    public KsLambdaFunction(KsListNode declareExpr) {
        if (declareExpr.getNext().getValue().isWord()) {
            this.name = declareExpr.getNext().getValue().asWord().getValue();
            declareExpr = declareExpr.getNext().getNext();
        } else {
            declareExpr = declareExpr.getNext();
        }
        KsArray params = declareExpr.getValue().asArray();
        for (int i = 0; i < params.size(); i++) {
            paramNames.add(params.get(i).asWord().getValue());
        }

        block = declareExpr.getNext();
    }

    public KsLambdaFunction(String name, KsArray params, KsListNode block) {
        this.name = name;

        for (int i = 0; i < params.size(); i++) {
            paramNames.add(params.get(i).asWord().getValue());
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
