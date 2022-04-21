package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;
import java.util.List;

import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsFunction;
import link.symtable.kson.core.node.KsHostSyncFunction;
import link.symtable.kson.core.node.KsLambdaFunction;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;

public class FuncCallContInstance extends KsContinuation {
    private LinkedList<KsNode> pendingNodes = new LinkedList<>();
    private LinkedList<KsNode> evaledNodes = new LinkedList<>();
    private KsFunction func; // for debug

    public FuncCallContInstance(KsContinuation currentCont, KsListNode nodeToRun) {
        super(currentCont);
        KsListNode iter = nodeToRun;
        while (iter != KsListNode.NIL) {
            pendingNodes.add(iter.getValue());
            iter = iter.getNext();
        }
    }

    // 直接初始化为参数已计算完毕
    public FuncCallContInstance(KsContinuation currentCont, KsFunction func, KsListNode evaledArgs) {
        super(currentCont);
        evaledNodes.add(func);
        KsListNode iter = evaledArgs;
        while (iter != KsListNode.NIL) {
            evaledNodes.add(iter.getValue());
            iter = iter.getNext();
        }
    }

    // 直接初始化为参数已计算完毕
    public FuncCallContInstance(KsContinuation currentCont, KsFunction func, List<KsNode> evaledArgs) {
        super(currentCont);
        evaledNodes.add(func);
        evaledNodes.addAll(evaledArgs);
    }

    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingNodes.size() == 0) {
            if (evaledNodes.size() == 0) {
                throw new RuntimeException("cannot eval empty expr");
            }
            KsNode funcNode = evaledNodes.pollFirst();
            KsNode[] args = evaledNodes.toArray(new KsNode[evaledNodes.size()]);
            if (funcNode instanceof KsHostSyncFunction) {
                KsNode applyResult = ((KsHostSyncFunction) funcNode).apply(state, args);
                return ContRunState.builder()
                        .nextAction(ExecAction.RUN_CONT)
                        .nextCont(getNext())
                        .nextNodeToRun(currentNodeToRun)
                        .newLastValue(applyResult)
                        .isSafePoint(true)
                        .build();
            } else if (funcNode instanceof KsContinuation) {
                // cont的参数计算完毕，可以恢复现场了
                KsNode newLastValToResumeCont = args[0];
                return ContRunState.builder()
                        .nextAction(ExecAction.RUN_CONT)
                        .nextCont((KsContinuation) funcNode)
                        .nextNodeToRun(currentNodeToRun)
                        .newLastValue(newLastValToResumeCont)
                        .build();
            } else {
                KsLambdaFunction func = (KsLambdaFunction) funcNode;
                // bind args
                Env childEnv = Env.makeChildEnv(getEnv());
                List<String> funcArgs = func.getParamNames();
                for (int i = 0; i < funcArgs.size(); i++) {
                    String paramName = funcArgs.get(i);
                    KsNode arg = args[i];
                    childEnv.define(paramName, arg);
                }
                // 创建buildin的 continuation变量
                childEnv.define("return", getNext());
                BlockContInstance nextCont = new BlockContInstance(getNext(), func.getBlock(), childEnv);
                return nextCont.prepareNextRun(state, func.getBlock());
            }
        } else {
            KsNode nextToRun = pendingNodes.pollFirst();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunState runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        evaledNodes.add(lastValue);
        return prepareNextRun(state, currentNodeToRun);
    }
}
