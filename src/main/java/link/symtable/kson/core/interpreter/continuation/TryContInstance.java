package link.symtable.kson.core.interpreter.continuation;

import java.util.ArrayList;
import java.util.List;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsWord;

public class TryContInstance extends KsContinuation  {
    private KsListNode block;


    public TryContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        block = expr.getNextValue().asListNode();
        KsListNode iter = expr.getNextNext();
        while (iter != KsListNode.NIL) {
            KsListNode handlerClause = iter.getValue().asListNode();
            KsWord handlerName = handlerClause.getNextValue().asWord();
            KsListNode originArgTable = handlerClause.getNextNextValue().asListNode();
            KsListNode modifiedArgTable = new KsListNode(new KsWord("resume"), originArgTable);
            KsListNode handlerBlock = handlerClause.getNextNextNext();
            // make a function cont instance
            KsLambdaFunction func = new KsLambdaFunction(null, modifiedArgTable, handlerBlock);
            String handlerVarName = String.format("__handler_%s", handlerName.getValue());
            List<KsNode> handlerAndNextCont = new ArrayList<>();
            handlerAndNextCont.add(func);
            handlerAndNextCont.add(currentCont);
            KsArray arr = new KsArray(handlerAndNextCont);
            getEnv().define(handlerVarName, arr);


            iter = iter.getNext();
        }
    }

    @Override
    public ContRunResult prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        Env childEnv = Env.makeChildEnv(getEnv());
        BlockContInstance nextCont = new BlockContInstance(getNext(), block, childEnv);
        return nextCont.prepareNextRun(state, block);
    }
}
