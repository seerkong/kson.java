package link.symtable.kson.core.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;
import link.symtable.kson.core.node.KsonArray;
import link.symtable.kson.core.node.KsonHostObject;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonString;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MethodCallTest {
    public static Interpreter interp = new Interpreter();

    public static class A implements SupportMethodCall {
        private String name = "Alice";

        @Override
        public KsonNode callMethod(ExecState state, String methodName, KsonNode[] args) {
            if ("get_name".equals(methodName)) {
                return new KsonString(name);
            } else {
                throw new NotImplementedException("methodName " + methodName + " is not supported");
            }
        }
    }

    @Test
    public void testHostObj() {
        String source =
                "(. a (get_name)) ";
        KsonNode node = Kson.parse(source);

        A a = new A();
        KsonHostObject hostObject = new KsonHostObject(a);
        Env env = Env.makeRootEnv();
        env.define("a", hostObject);
        ExecResult r = interp.run(node, new ExecState(), env);

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsonString(a.name)));
    }

    @Test
    public void testMapMethods() {
        String source =
                "(. {m:2} (put \"a\" 1) (keys))";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        List<KsonNode> keys = new ArrayList<>();
        keys.add(new KsonString("m"));
        keys.add(new KsonString("a"));
        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsonArray(keys)));
    }
}
