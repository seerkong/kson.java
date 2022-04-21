package link.symtable.kson.core.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import link.symtable.kson.core.interpreter.continuation.AndContInstance;
import link.symtable.kson.core.interpreter.continuation.BlockContInstance;
import link.symtable.kson.core.interpreter.continuation.CallccContInstance;
import link.symtable.kson.core.interpreter.continuation.ConditionContInstance;
import link.symtable.kson.core.interpreter.continuation.FuncDeclareContInstance;
import link.symtable.kson.core.interpreter.continuation.VarContInstance;
import link.symtable.kson.core.interpreter.continuation.MethodCallContInstance;
import link.symtable.kson.core.interpreter.continuation.OrContInstance;
import link.symtable.kson.core.interpreter.continuation.PerformAllContInstance;
import link.symtable.kson.core.interpreter.continuation.PerformContInstance;
import link.symtable.kson.core.interpreter.continuation.QuoteContInstance;
import link.symtable.kson.core.interpreter.continuation.SetContInstance;
import link.symtable.kson.core.interpreter.continuation.SetTimeoutContInstance;
import link.symtable.kson.core.interpreter.continuation.SubscriptContInstance;
import link.symtable.kson.core.interpreter.continuation.TryContInstance;
import link.symtable.kson.core.node.KsContinuation;
import link.symtable.kson.core.node.KsListNode;


public class ExtensionRegistry {
    public static Map<String, BiFunction<KsContinuation, KsListNode, KsContinuation>> keywords = new HashMap<>();

    static {
        keywords.put("var", VarContInstance::new);
        keywords.put("set", SetContInstance::new);
        keywords.put("cond", ConditionContInstance::new);
        keywords.put("begin", (nextCont, expr) -> {
            Env childEnv = Env.makeChildEnv(nextCont.getEnv());
            return new BlockContInstance(nextCont, expr.getNext(), childEnv);
        });
        keywords.put("func", FuncDeclareContInstance::new);
        keywords.put("@", SubscriptContInstance::new);
        keywords.put(".", MethodCallContInstance::new);
        keywords.put("%", QuoteContInstance::new);
        keywords.put("and", AndContInstance::new);
        keywords.put("or", OrContInstance::new);
        keywords.put("call_cc", CallccContInstance::new);
        keywords.put("try", TryContInstance::new);
        keywords.put("perform", (nextCont, expr) -> {
            return new PerformContInstance(nextCont, expr, false);
        });
        keywords.put("perform_all", (nextCont, expr) -> {
            return new PerformAllContInstance(nextCont, expr, false);
        });
        keywords.put("set_timeout", SetTimeoutContInstance::new);
    }
}
