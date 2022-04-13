package link.symtable.kson.core.interpreter;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import link.symtable.kson.core.Kson;
import link.symtable.kson.core.node.KsonBoolean;
import link.symtable.kson.core.node.KsonInt64;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonString;
import link.symtable.kson.core.node.KsonWord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterpreterTest {
    public static Interpreter interp = new Interpreter();
    @Test
    public void testBooleanNode() {
        KsonNode ast = KsonBoolean.TRUE;

        ExecResult r = interp.run(ast, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testArrNode() {
        String source = "[ 1 , 2 , 3 ]  ";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testMapNode() {
        String source = "{ m: 1, \"n\": [1,2,3] }";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testEnvLookup() {
        String source = "a";
        KsonNode node = Kson.parse(source);

        Env env = Env.makeRootEnv();
        env.define("a", new KsonString("a string"));

        ExecResult r = interp.run(node, new ExecState(), env);
        log.info("result {}", r.getData());
    }

    @Test
    public void testFuncCall() {
        String source = "(write_line \"hello world\\n\")";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testMath() {
        String source = "(+ (- (* 3.1 2) 0.3) (/ 6 3))";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testBlock() {
        String source =
                "(blk (write_line \"line1\") (write_line \"line2\") (blk (write_line \"nested\")))";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testCondition1() {
        String source =
                "(cond (false (write_line \"should skip\")) (else 123 (write_line \"should enter\")  ) ))";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testCondition2() {
        String source =
                "(cond (false (write_line \"should skip\"))  ( (> 2 1) \"node in block\" (write_line \"should enter\"))  (else (write_line \"should skip\")) )";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        log.info("result {}", r.getData());
    }

    @Test
    public void testFunc() {
        String source =
                "(blk (func add3 (x y z) (+ (+ x y) z)) (add3 1 2 3))";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(new KsonInt64(6)));
    }

    @Test
    public void testQuote() {
        String source =
                "(% (+ 1 2))";
        KsonNode node = Kson.parse(source);

        ExecResult r = interp.run(node, new ExecState(), Env.makeRootEnv());
        List<KsonNode> innerExprNodes = new ArrayList<>();
        innerExprNodes.add(new KsonWord("+"));
        innerExprNodes.add(new KsonInt64(1));
        innerExprNodes.add(new KsonInt64(2));
        KsonListNode expr = KsonListNode.makeByList(innerExprNodes);
        List<KsonNode> outerNodes = new ArrayList<>();
        outerNodes.add(new KsonWord("%"));
        outerNodes.add(expr);
        KsonListNode expected = KsonListNode.makeByList(outerNodes);

        log.info("result {}", r.getData());
        Assertions.assertTrue(r.getData().equals(expected));
    }
}
