package link.symtable.kson.core.interpreter.continuation;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ExtensionRegistry;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsMap;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.interpreter.ContRunState;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.util.NodeTypeHelper;
import link.symtable.kson.core.node.KsWord;

public class ExecNodeContInstance extends KsContinuation {
    public ExecNodeContInstance(KsContinuation currentCont) {
        super(currentCont);
    }

    @Override
    public ContRunState prepareNextRun(ExecState state, KsNode currentNodeToRun) {
        KsNode newLastVal;
        if (NodeTypeHelper.isSelfEvaluatedNode(currentNodeToRun)) {
            newLastVal = currentNodeToRun;
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(newLastVal)
                    .build();
        } else if (currentNodeToRun instanceof KsWord) {
            KsWord w = (KsWord) currentNodeToRun;
            newLastVal = getEnv().lookup(w.getValue());
            // TODO CHECK newLastVal
            return ContRunState.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(newLastVal)
                    .build();
        } else if (currentNodeToRun instanceof KsArray) {
            KsArray arr = (KsArray) currentNodeToRun;
            ArrayContInstance newCont = new ArrayContInstance(getNext(), arr);
            return newCont.prepareNextRun(state, currentNodeToRun);
        } else if (currentNodeToRun instanceof KsMap) {
            KsMap map = (KsMap) currentNodeToRun;
            MapContInstance newCont = new MapContInstance(getNext(), map);
            return newCont.prepareNextRun(state, currentNodeToRun);
        } else if (currentNodeToRun instanceof KsListNode) {
            KsListNode list = (KsListNode) currentNodeToRun;
            KsNode firstNode = list.getValue();
            if (firstNode.isWord() && ExtensionRegistry.keywords.containsKey(firstNode.asWord().getValue())) {
                String keyword = firstNode.asWord().getValue();
                KsContinuation newCont = ExtensionRegistry.keywords.get(keyword).apply(getNext(), list);
                return newCont.prepareNextRun(state, currentNodeToRun);
            } else {
                FuncCallContInstance newCont = new FuncCallContInstance(getNext(), list);
                return newCont.prepareNextRun(state, currentNodeToRun);
            }
        }
        throw new NotImplementedException("ExecNodeContInstance#prepareNextRun currentNodeToRun " + currentNodeToRun);
    }
}
