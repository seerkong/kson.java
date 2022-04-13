package link.symtable.kson.core.interpreter.continuation;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.Registry;
import link.symtable.kson.core.node.KsonArray;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonMap;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.interpreter.ContRunResult;
import link.symtable.kson.core.interpreter.ExecAction;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.util.NodeTypeHelper;
import link.symtable.kson.core.node.KsonWord;

public class ExecNodeContInstance extends Continuation {
    public ExecNodeContInstance(Continuation currentCont) {
        super(currentCont);
    }

    @Override
    public ContRunResult initNextRun(ExecState state, KsonNode lastValue, KsonNode currentNodeToRun) {
        KsonNode newLastVal = lastValue;
        if (NodeTypeHelper.isSelfEvaluatedNode(currentNodeToRun)) {
            newLastVal = currentNodeToRun;
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(newLastVal)
                    .build();
        } else if (currentNodeToRun instanceof KsonWord) {
            KsonWord w = (KsonWord) currentNodeToRun;
            newLastVal = getEnv().lookup(w.getValue());
            // TODO CHECK newLastVal
            return ContRunResult.builder()
                    .nextAction(ExecAction.RUN_CONT)
                    .nextCont(getNext())
                    .nextNodeToRun(currentNodeToRun)
                    .newLastValue(newLastVal)
                    .build();
        } else if (currentNodeToRun instanceof KsonArray) {
            KsonArray arr = (KsonArray) currentNodeToRun;
            ArrayContInstance newCont = new ArrayContInstance(getNext(), arr);
            return newCont.initNextRun(state, lastValue, currentNodeToRun);
        } else if (currentNodeToRun instanceof KsonMap) {
            KsonMap map = (KsonMap) currentNodeToRun;
            MapContInstance newCont = new MapContInstance(getNext(), map);
            return newCont.initNextRun(state, lastValue, currentNodeToRun);
        } else if (currentNodeToRun instanceof KsonListNode) {
            KsonListNode list = (KsonListNode) currentNodeToRun;
            KsonNode firstNode = list.getValue();
            if (firstNode.isWord() && Registry.keywords.containsKey(firstNode.asWord().getValue())) {
                String keyword = firstNode.asWord().getValue();
                Continuation newCont = Registry.keywords.get(keyword).apply(getNext(), list);
                return newCont.initNextRun(state, lastValue, currentNodeToRun);
            } else {
                FuncCallContInstance newCont = new FuncCallContInstance(getNext(), list);
                return newCont.initNextRun(state, lastValue, currentNodeToRun);
            }
        }
        throw new NotImplementedException("ExecNodeContInstance#run " + newLastVal);
    }
}
