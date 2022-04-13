package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;


public class ConditionContInstance extends Continuation {
    private LinkedList<Pair<KsonNode, KsonListNode>> pendingPairs;
    private KsonListNode currentBranchBlock;
    private KsonListNode fallbackBlock = KsonListNode.NIL;
    private boolean needExecFallback = false;
    public ConditionContInstance(Continuation currentCont, KsonListNode expr) {
        super(currentCont);
        pendingPairs = new LinkedList<>();
        KsonListNode conditionsIter = expr.getNext();
        while (conditionsIter != KsonListNode.NIL) {
            KsonNode pairNode = conditionsIter.getValue();
            if (!(pairNode instanceof KsonListNode)) {
                throw new RuntimeException("condition pair should be a list");
            }
            KsonListNode pair = pairNode.asListNode();
            KsonNode condExpr = pair.getValue();
            if (condExpr.isWord("else")) {
                fallbackBlock = pair.getNext();
                if (!fallbackBlock.isListNode()) {
                    throw new RuntimeException("condition else branch should not be empty");
                }
                needExecFallback = true;
                break;
            }

            KsonListNode condBlock = pair.getNext();
            if (!condBlock.isListNode()) {
                throw new RuntimeException("condition block should not be empty");
            }
            pendingPairs.add(new ImmutablePair<>(condExpr, condBlock));
            conditionsIter = conditionsIter.getNext();
        }

    }

    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        if (pendingPairs.size() == 0) {
            if (needExecFallback) {
                needExecFallback = false;
                BlockContInstance nextCont = new BlockContInstance(getNext(), fallbackBlock);
                return nextCont.initNextRun(state, lastValue, fallbackBlock);
            } else {
                return ContRunResult.builder()
                        .nextAction(ExecAction.RUN_CONT)
                        .nextCont(getNext())
                        .nextNodeToRun(currentNodeToRun)
                        .newLastValue(lastValue)
                        .build();
            }
        } else {
            Pair<KsonNode, KsonListNode> condPair = pendingPairs.pollFirst();
            KsonNode nextToRun = condPair.getLeft();
            currentBranchBlock = condPair.getRight();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.initNextRun(state, lastValue, nextToRun);
        }
    }

    @Override
    public ContRunResult run(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        boolean lastCondExprResult = lastValue.toBoolean();
        if (lastCondExprResult) {
            BlockContInstance nextCont = new BlockContInstance(getNext(), currentBranchBlock);
            return nextCont.initNextRun(state, lastValue, currentBranchBlock);
        }
        return initNextRun(state, lastValue, currentNodeToRun);
    }
}
