package link.symtable.kson.core.cpsinterpreter.functions;

import link.symtable.kson.core.cpsinterpreter.Env;
import link.symtable.kson.core.cpsinterpreter.ExecState;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsHostSyncFunction;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.util.ArgsCountNotMatchException;

public class HostSystemFunctions {
    public static void exportToEnv(Env env) {
        env.define("write_line", new KsHostSyncFunction("write_line", 1, true, HostSystemFunctions::writeLine));
        env.define("sleep", new KsHostSyncFunction("sleep", 1, false, HostSystemFunctions::sleep));
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

    public static KsNode sleep(ExecState state, KsNode[] args) {
        if (args.length < 1) {
            throw new ArgsCountNotMatchException("expect at least 1 arg");
        }
        KsInt64 sleepTimeInMillis = args[0].asInt64();
        try {
            Thread.sleep(sleepTimeInMillis.getValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return KsBoolean.TRUE;
    }
}
