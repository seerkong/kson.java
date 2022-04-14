package link.symtable.kson.core.interpreter.functions;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsDouble;
import link.symtable.kson.core.node.KsHostSyncFunction;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsNumber;
import link.symtable.kson.core.util.ArgsCountNotMatchException;

public class HostPrimitiveFuctions {
    public static void exportToEnv(Env env) {
        env.define("not", new KsHostSyncFunction("not", 1, true,
                HostPrimitiveFuctions::not));
        env.define("+", new KsHostSyncFunction("+", 1, true,
                HostPrimitiveFuctions::add));
        env.define("-", new KsHostSyncFunction("-", 1, true,
                HostPrimitiveFuctions::minus));
        env.define("*", new KsHostSyncFunction("*", 1, true,
                HostPrimitiveFuctions::multiply));
        env.define("/", new KsHostSyncFunction("/", 1, true,
                HostPrimitiveFuctions::divide));
        env.define("mod", new KsHostSyncFunction("mod", 1, true,
                HostPrimitiveFuctions::add));

        env.define(">", new KsHostSyncFunction(">", 1, true,
                HostPrimitiveFuctions::greater));
        env.define(">=", new KsHostSyncFunction(">=", 1, true,
                HostPrimitiveFuctions::greaterOrEqual));
        env.define("<", new KsHostSyncFunction("<", 1, true,
                HostPrimitiveFuctions::lower));
        env.define("<=", new KsHostSyncFunction("<=", 1, true,
                HostPrimitiveFuctions::lowerOrEqual));
    }

    public static KsNode not(ExecState state, KsNode[] args) {
        if (args.length != 1) {
            throw new ArgsCountNotMatchException("expect 1 arg");
        }
        boolean first = args[0].toBoolean();
        return first ? KsBoolean.FALSE : KsBoolean.TRUE;
    }

    public static KsNode add(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        if (first.isInt64()) {
            return new KsInt64(first.toInt64Val() + second.toInt64Val());
        } else {
            return new KsDouble(first.toDoubleVal() + second.toDoubleVal());
        }
    }

    public static KsNode minus(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        if (first.isInt64()) {
            return new KsInt64(first.toInt64Val() - second.toInt64Val());
        } else {
            return new KsDouble(first.toDoubleVal() - second.toDoubleVal());
        }
    }

    public static KsNode multiply(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        if (first.isInt64()) {
            return new KsInt64(first.toInt64Val() * second.toInt64Val());
        } else {
            return new KsDouble(first.toDoubleVal() * second.toDoubleVal());
        }
    }

    public static KsNode divide(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        if (first.isInt64()) {
            return new KsInt64(first.toInt64Val() / second.toInt64Val());
        } else {
            return new KsDouble(first.toDoubleVal() / second.toDoubleVal());
        }
    }

    public static KsNode mod(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        long resultVal = first.toInt64Val() % second.toInt64Val();
        return new KsInt64(resultVal);
    }

    public static KsNode greater(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() > second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() > second.toDoubleVal();
        }
        return checkRes ? KsBoolean.TRUE : KsBoolean.FALSE;
    }

    public static KsNode greaterOrEqual(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() >= second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() >= second.toDoubleVal();
        }
        return checkRes ? KsBoolean.TRUE : KsBoolean.FALSE;
    }

    public static KsNode lower(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() < second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() < second.toDoubleVal();
        }
        return checkRes ? KsBoolean.TRUE : KsBoolean.FALSE;
    }

    public static KsNode lowerOrEqual(ExecState state, KsNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsNumber first = (KsNumber) (args[0]);
        KsNumber second = (KsNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() <= second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() <= second.toDoubleVal();
        }
        return checkRes ? KsBoolean.TRUE : KsBoolean.FALSE;
    }
}
