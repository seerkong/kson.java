package link.symtable.kson.core.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.TestBase;
import link.symtable.kson.core.node.KsBoolean;
import link.symtable.kson.core.node.KsInt64;
import link.symtable.kson.core.node.KsListNode;
import link.symtable.kson.core.node.KsNode;
import link.symtable.kson.core.node.KsString;
import link.symtable.kson.core.node.KsWord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterpreterTest extends TestBase {
    public static Interpreter interp = new Interpreter();
    @Test
    public void testBooleanNode() {
        KsNode ast = KsBoolean.TRUE;

        ExecResult r = interp.run(ast, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testArrNode() {
        String source = "[ 1 , 2 , 3 ]  ";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testMapNode() {
        String source = "{ m: 1, \"n\": [1,2,3] }";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testEnvLookup() {
        String source = "a";
        KsNode node = Kson.parse(source);

        Env env = Env.makeRootEnv();
        env.define("a", new KsString("a string"));

        ExecResult r = interp.run(node, new ExecState(), env);
        log.info("result {}", r.getData());
    }

    @Test
    public void testFuncCall() {
        String source = "(write_line \"hello world\\n\")";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testMath() {
        String source = "(+ (- (* 3.1 2) 0.3) (/ 6 3))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testBlock() {
        String source =
                "(begin (write_line \"line1\") (write_line \"line2\") (begin (write_line \"nested\")))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testCondition1() {
        String source =
                "(cond (false (write_line \"should skip\")) (else 123 (write_line \"should enter\")  ) ))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testCondition2() {
        String source =
                "(cond (false (write_line \"should skip\"))  ( (> 2 1) \"node in block\" (write_line \"should enter\"))  (else (write_line \"should skip\")) )";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testFunc() {
        String source =
                "(begin (func add3 (x y z) (+ (+ x y) z)) (add3 1 2 3))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsInt64(6)));
    }

    @Test
    public void testQuote() {
        String source =
                "(% (+ 1 2))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        List<KsNode> innerExprNodes = new ArrayList<>();
        innerExprNodes.add(new KsWord("+"));
        innerExprNodes.add(new KsInt64(1));
        innerExprNodes.add(new KsInt64(2));
        KsListNode expr = KsListNode.makeByList(innerExprNodes);
        List<KsNode> outerNodes = new ArrayList<>();
        outerNodes.add(new KsWord("%"));
        outerNodes.add(expr);
        KsListNode expected = KsListNode.makeByList(outerNodes);

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(expected));
    }

    @Test
    public void testAnd() {
        String source =
                "(and true (> 1 2))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(KsBoolean.FALSE));
    }

    @Test
    public void testOr() {
        String source =
                "(or false (+ 1 2))";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(KsBoolean.TRUE));
    }

    @Test
    public void testFuncWithoutName() {
        String source = "(func (x) x )";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        // should write 3
        log.info("result {}", r.getData());
    }

    @Test
    public void testWithoutCallcc() {
        String source =
                "(begin (func f (ret) (ret 2) 3 )  (write_line (f (func (x) x)) ) )";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        // should write 3
        log.info("result {}", r.getData());
    }

    @Test
    public void testWithCallcc() {
        String source =
                "(begin (func f (ret) (ret 2) 3 )  (write_line (call_cc f)) )";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        // should write 2
        log.info("result {}", r.getData());
    }

    @Test
    public void testReturn1() {
        String source =
                "(begin (func f (x) (return 2) x )  (write_line (f 4)) )";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        // should write 2
        log.info("result {}", r.getData());
    }

    @Test
    public void testReturn2() {
        String source =
                "(begin (func f (x) (return (+ 3 5)) x )  (write_line (f 4)) )";
        KsNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        // should write 8
        log.info("result {}", r.getData());
    }

    @Test
    public void mapFieldAsMethod() throws RuntimeException {
        KsNode node = parseFile("/map_field_as_method.kson");
        System.out.println(node);
        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsInt64(3)));
    }
}
