package link.symtable.kson.core.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.node.KsonInt64;
import link.symtable.kson.core.node.KsonNode;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SubscriptTest {
    public static Interpreter interp = new Interpreter();

    @Test
    public void testFunc() {
        String source =
                "(@ [[{m: [2 {t:2}], n:2}]] 0 0 \"m\" 1 $t)";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsonInt64(2)));
    }
}
