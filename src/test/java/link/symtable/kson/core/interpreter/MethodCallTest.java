package link.symtable.kson.core.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsHostObject;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsString;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MethodCallTest {
    public static Interpreter interp = new Interpreter();

    public static class A implements SupportMethodCall {
        private String name = "Alice";

        @Override
        public KsNode callMethod(ExecState state, String methodName, KsNode[] args) {
            if ("get_name".equals(methodName)) {
                return new KsString(name);
            } else {
                throw new NotImplementedException("methodName " + methodName + " is not supported");
            }
        }
    }

    @Test
    public void testHostObj() {
        String source =
                "(. a (get_name)) ";
        KsNode node = Kson.parse(source);

        A a = new A();
        KsHostObject hostObject = new KsHostObject(a);
        Env env = Env.makeRootEnv();
        env.define("a", hostObject);
        ExecResult r = interp.run(node, new ExecState(), env);

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsString(a.name)));
    }

    @Test
    public void testMapMethods() {
        String source =
                "(. {m:2} (put \"a\" 1) (keys))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        List<KsNode> keys = new ArrayList<>();
        keys.add(new KsString("m"));
        keys.add(new KsString("a"));
        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsArray(keys)));
    }
}
