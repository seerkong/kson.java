package link.symtable.kson.core.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.TestBase;
import link.symtable.kson.core.node.KsArray;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgebraicEffectsTryHandleTest extends TestBase {
    public static Interpreter interp = new Interpreter();
    @Test
    public void withoutSetTimeout() {
        KsNode node = parseFile("/perform_resume_without_set_timeout.kson");
        System.out.println(node);
        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        System.out.println("result " + r.getData());
        Assertions.assertTrue(r.getData().equals(new KsInt64(6)));
    }

    @Test
    public void withSetTimeout() {
        KsNode node = parseFile("/perform_resume_with_set_timeout.kson");
        System.out.println(node);
        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        System.out.println("result " + r.getData());
        Assertions.assertTrue(r.getData().equals(new KsInt64(6)));
    }

    @Test
    public void performAll() {
        KsNode node = parseFile("/perform_all_with_set_timeout.kson");
        System.out.println(node);
        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        System.out.println("result " + r.getData());
        List<KsNode> inner = new ArrayList<>();
        inner.add(new KsInt64(36));
        KsNode arr = new KsArray(inner);
        Assertions.assertTrue(r.getData().equals(arr));
    }
}
