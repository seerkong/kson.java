package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonHostSyncFunction;
import link.symtable.kson.core.node.KsonLambdaFunction;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;

public class FuncCallContInstance extends Continuation {
    private LinkedList<KsonNode> pendingNodes;
    private LinkedList<KsonNode> evaledNodes;

    public FuncCallContInstance(Continuation currentCont, KsonListNode nodeToRun) {
        super(currentCont);
        pendingNodes = new LinkedList<>();

        KsonListNode iter = nodeToRun;
        while (iter != KsonListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
        evaledNodes = new LinkedList<>();
    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            if (evaledNodes.size() == 0) {
                throw new RuntimeException("cannot eval empty expr");
            }
            KsonNode funcNode = evaledNodes.pollFirst();
            KsonNode[] args = evaledNodes.toArray(new KsonNode[evaledNodes.size()]);
            if (funcNode instanceof KsonHostSyncFunction) {
                KsonNode applyResult = ((KsonHostSyncFunction) funcNode).apply(state, args);
                return ContRunResult.builder()
                        .nextAction(ExecAction.RUN_CONT)
                        .nextCont(getNext())
                        .nextNodeToRun(currentNodeToRun)
                        .newLastValue(applyResult)
                        .build();
            } else {
                KsonLambdaFunction func = (KsonLambdaFunction) funcNode;
                // bind args
                Env childEnv = Env.makeChildEnv(getEnv());
                List<String> funcArgs = func.getParamNames();
                for (int i = 0; i < funcArgs.size(); i++) {
                    String paramName = funcArgs.get(i);
                    KsonNode arg = args[i];
                    childEnv.define(paramName, arg);
                }
                BlockContInstance nextCont = new BlockContInstance(getNext(), func.getBlock(), childEnv);
                return nextCont.initNextRun(state, lastValue, func.getBlock());
            }
        } else {
            KsonNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(nextCont)
                    .nextNodeToRun(nextToRun)
                    .newLastValue(lastValue)
                    .build();
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
