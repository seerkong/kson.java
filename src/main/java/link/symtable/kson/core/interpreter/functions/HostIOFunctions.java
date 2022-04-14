package link.symtable.kson.core.interpreter.functions;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsHostSyncFunction;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.util.ArgsCountNotMatchException;

public class HostIOFunctions {
    public static void exportToEnv(Env env) {
        env.define("write_line", new KsHostSyncFunction("write_line", 1, true, HostIOFunctions::writeLine));
    }

    public static KsNode writeLine(ExecState state, KsNode[] args) {
        if (args.length < 1) {
            throw new ArgsCountNotMatchException("expect at least 1 arg");
        }
        // TODO support more args
        if (args[0].isString()) {
            System.out.println(args[0].asString().getValue());
        } else {
            System.out.println(args[0].toString());
        }

        return KsBoolean.TRUE;
    }
}
