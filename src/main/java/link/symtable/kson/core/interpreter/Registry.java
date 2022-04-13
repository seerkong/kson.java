package link.symtable.kson.core.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import link.symtable.kson.core.interpreter.continuation.BlockContInstance;
import link.symtable.kson.core.interpreter.continuation.ConditionContInstance;
import link.symtable.kson.core.interpreter.continuation.Continuation;
import link.symtable.kson.core.interpreter.continuation.FuncDeclareContInstance;
import link.symtable.kson.core.interpreter.continuation.LetContInstance;
import link.symtable.kson.core.interpreter.continuation.MethodCallContInstance;
import link.symtable.kson.core.interpreter.continuation.QuoteContInstance;
import link.symtable.kson.core.interpreter.continuation.SetContInstance;
import link.symtable.kson.core.interpreter.continuation.SubscriptContInstance;
import link.symtable.kson.core.node.KsonListNode;


public class Registry {
    public static Map<String, BiFunction<Continuation, KsonListNode, Continuation>> keywords = new HashMap<>();

    static {
        keywords.put("let", LetContInstance::new);
        keywords.put("set", SetContInstance::new);
        keywords.put("cond", ConditionContInstance::new);
        keywords.put("blk", (nextCont, expr) -> new BlockContInstance(nextCont, expr.getNext()));
        keywords.put("func", FuncDeclareContInstance::new);
        keywords.put("@", SubscriptContInstance::new);
        keywords.put(".", MethodCallContInstance::new);
        keywords.put("%", QuoteContInstance::new);
    }
}
