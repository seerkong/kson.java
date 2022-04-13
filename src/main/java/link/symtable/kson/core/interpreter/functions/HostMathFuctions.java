package link.symtable.kson.core.interpreter.functions;

import link.symtable.kson.core.interpreter.Env;
import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.node.KsonBoolean;
import link.symtable.kson.core.node.KsonDouble;
import link.symtable.kson.core.node.KsonHostSyncFunction;
import link.symtable.kson.core.node.KsonInt64;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonNumber;
import link.symtable.kson.core.util.ArgsCountNotMatchException;

public class HostMathFuctions {
    public static void exportToEnv(Env env) {
        env.define("+", new KsonHostSyncFunction("+", 1, true,
                HostMathFuctions::add));
        env.define("-", new KsonHostSyncFunction("-", 1, true,
                HostMathFuctions::minus));
        env.define("*", new KsonHostSyncFunction("*", 1, true,
                HostMathFuctions::multiply));
        env.define("/", new KsonHostSyncFunction("/", 1, true,
                HostMathFuctions::divide));
        env.define("mod", new KsonHostSyncFunction("mod", 1, true,
                HostMathFuctions::add));

        env.define(">", new KsonHostSyncFunction(">", 1, true,
                HostMathFuctions::add));
        env.define(">=", new KsonHostSyncFunction(">=", 1, true,
                HostMathFuctions::add));
        env.define("<", new KsonHostSyncFunction("<", 1, true,
                HostMathFuctions::add));
        env.define("<=", new KsonHostSyncFunction("<=", 1, true,
                HostMathFuctions::add));
    }

    public static KsonNode add(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        if (first.isInt64()) {
            return new KsonInt64(first.toInt64Val() + second.toInt64Val());
        } else {
            return new KsonDouble(first.toDoubleVal() + second.toDoubleVal());
        }
    }

    public static KsonNode minus(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        if (first.isInt64()) {
            return new KsonInt64(first.toInt64Val() - second.toInt64Val());
        } else {
            return new KsonDouble(first.toDoubleVal() - second.toDoubleVal());
        }
    }

    public static KsonNode multiply(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        if (first.isInt64()) {
            return new KsonInt64(first.toInt64Val() * second.toInt64Val());
        } else {
            return new KsonDouble(first.toDoubleVal() * second.toDoubleVal());
        }
    }

    public static KsonNode divide(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        if (first.isInt64()) {
            return new KsonInt64(first.toInt64Val() / second.toInt64Val());
        } else {
            return new KsonDouble(first.toDoubleVal() / second.toDoubleVal());
        }
    }

    public static KsonNode mod(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        long resultVal = first.toInt64Val() % second.toInt64Val();
        return new KsonInt64(resultVal);
    }

    public static KsonNode greater(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() > second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() > second.toDoubleVal();
        }
        return checkRes ? KsonBoolean.TRUE : KsonBoolean.FALSE;
    }

    public static KsonNode greaterOrEqual(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() >= second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() >= second.toDoubleVal();
        }
        return checkRes ? KsonBoolean.TRUE : KsonBoolean.FALSE;
    }

    public static KsonNode lower(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() < second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() < second.toDoubleVal();
        }
        return checkRes ? KsonBoolean.TRUE : KsonBoolean.FALSE;
    }

    public static KsonNode lowerOrEqual(ExecState state, KsonNode[] args) {
        if (args.length != 2) {
            throw new ArgsCountNotMatchException("expect 2 arg");
        }
        KsonNumber first = (KsonNumber) (args[0]);
        KsonNumber second = (KsonNumber) (args[1]);
        boolean checkRes;
        if (first.isInt64()) {
            checkRes = first.toInt64Val() <= second.toInt64Val();
        } else {
            checkRes = first.toDoubleVal() <= second.toDoubleVal();
        }
        return checkRes ? KsonBoolean.TRUE : KsonBoolean.FALSE;
    }
}
