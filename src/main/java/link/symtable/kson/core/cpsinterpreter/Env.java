package link.symtable.kson.core.cpsinterpreter;

import java.util.HashMap;
import java.util.Map;

import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsUndefined;
import link.symtable.kson.core.util.VariableNotDefinedException;

public class Env {
    public Env parent;
    public Map<String, KsNode> bindings = new HashMap<>();

    public Env() {
    }

    public static Env makeRootEnv() {
        Env result = new Env();
        result.parent = null;
        return result;
    }

    public static Env makeChildEnv(Env parentEnv) {
        Env result = new Env();
        result.parent = parentEnv;
        return result;
    }

    public void define(String key, KsNode value) {
        bindings.put(key, value);
    }

    public KsNode lookup(String key) {
        if (bindings.containsKey(key)) {
            return bindings.get(key);
        }
        else if (parent == null) {
            return KsUndefined.UNDEFINED;
        }
        else {
            return parent.lookup(key);
        }
    }

    public Env lookupDeclareEnv(String key) {
        Env iter = this;
        while (iter != null) {
            if (iter.bindings.containsKey(key)) {
                return iter;
            }
            iter = iter.parent;
        }
        throw new VariableNotDefinedException();
    }

    public static boolean isLookupFailedResult(KsNode valInEnv) {
        return (valInEnv == null || valInEnv == KsUndefined.UNDEFINED);
    }
}
