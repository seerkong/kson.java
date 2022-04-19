package link.symtable.kson.core.interpreter.continuation;

import java.util.LinkedList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;


public class ConditionContInstance extends KsContinuation {
    private LinkedList<Pair<KsNode, KsListNode>> pendingPairs;
    private KsListNode currentBranchBlock;
    private KsListNode fallbackBlock = KsListNode.NIL;
    private boolean needExecFallback = false;
    public ConditionContInstance(KsContinuation currentCont, KsListNode expr) {
        super(currentCont);
        pendingPairs = new LinkedList<>();
        KsListNode conditionsIter = expr.getNext();
        while (conditionsIter != KsListNode.NIL) {
            KsNode pairNode = conditionsIter.getValue();
            if (!(pairNode instanceof KsListNode)) {
                throw new RuntimeException("condition pair should be a list");
            }
            KsListNode pair = pairNode.asListNode();
            KsNode condExpr = pair.getValue();
            if (condExpr.isWord("else")) {
                fallbackBlock = pair.getNext();
                if (!fallbackBlock.isListNode()) {
                    throw new RuntimeException("condition else branch should not be empty");
                }
                needExecFallback = true;
                break;
            }

            KsListNode condBlock = pair.getNext();
            if (!condBlock.isListNode()) {
                throw new RuntimeException("condition block should not be empty");
            }
            pendingPairs.add(new ImmutablePair<>(condExpr, condBlock));
            conditionsIter = conditionsIter.getNext();
        }

    }

    public ContRunResult prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        if (pendingPairs.size() == 0) {
            if (needExecFallback) {
                needExecFallback = false;
                BlockContInstance nextCont = new BlockContInstance(getNext(), fallbackBlock);
                return nextCont.prepareNextRun(state, fallbackBlock);
            } else {
                return ContRunResult.builder()
                        .nextAction(ExecAction.RUN_CONT)
                        .nextCont(getNext())
                        .nextNodeToRun(currentNodeToRun)
                        .newLastValue(KsBoolean.FALSE)
                        .build();
            }
        } else {
            Pair<KsNode, KsListNode> condPair = pendingPairs.pollFirst();
            KsNode nextToRun = condPair.getLeft();
            currentBranchBlock = condPair.getRight();
            ExecNodeContInstance nextCont = new ExecNodeContInstance(this);
            return nextCont.prepareNextRun(state, nextToRun);
        }
    }

    @Override
    public ContRunResult runWithValue(ExecState state, KsNode lastValue, KsNode currentNodeToRun) {
        boolean lastCondExprResult = lastValue.toBoolean();
        if (lastCondExprResult) {
            BlockContInstance nextCont = new BlockContInstance(getNext(), currentBranchBlock);
            return nextCont.prepareNextRun(state, currentBranchBlock);
        }
        return prepareNextRun(state, currentNodeToRun);
    }
}
