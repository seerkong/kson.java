package link.symtable.kson.core.interpreter.functions;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonBoolean;
import link.symtable.kson.core.node.KsonHostSyncFunction;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonString;
import link.symtable.kson.core.util.ArgsCountNotMatchException;

public class HostIOFunctions {
    public static void exportToEnv(Env env) {
        env.define("write_line", new KsonHostSyncFunction("write_line", 1, true, HostIOFunctions::writeLine));
    }

    public static KsonNode writeLine(ExecState state, KsonNode[] args) {
        if (args.length < 1) {
            throw new ArgsCountNotMatchException("expect at least 1 arg");
        }
        // TODO support more args
        KsonString str = (KsonString) (args[0]);
        System.out.println(str.getValue());
        return KsonBoolean.TRUE;
    }
}
