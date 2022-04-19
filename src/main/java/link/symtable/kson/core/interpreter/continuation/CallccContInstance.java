package link.symtable.kson.core.interpreter.continuation;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;


public class CallccContInstance extends KsContinuation {
    private KsFunction func;
    public CallccContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        String funcName = expr.getNextValue().asWord().getValue();
        KsNode funcNode = getEnv().lookup(funcName);
        func = funcNode.asFunction();
    }

    @Override
    public ContRunResult prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        // 将上个continuation作为函数参数，执行函数
        KsListNode params = new KsListNode(getNext());
        FuncCallContInstance newCont = new FuncCallContInstance(getNext(), func, params);
        return newCont.prepareNextRun(state, currentNodeToRun);
    }
}
