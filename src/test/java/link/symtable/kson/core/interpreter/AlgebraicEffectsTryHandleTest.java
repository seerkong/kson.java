package link.symtable.kson.core.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.TestBase;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgebraicEffectsTryHandleTest extends TestBase {
    public static Interpreter interp = new Interpreter();
    @Test
    public void withoutSetTimeout() {
        KsNode node = parseFile("/try_catch_without_set_timeout.kson");
        System.out.println(node);
        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsInt64(6)));
    }
}
