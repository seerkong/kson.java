package link.symtable.kson.core.lexer;

import java.util.List;

import org.junit.jupiter.api.Test;

import link.symtable.kson.core.util.LexException;


public class KsonLexerTest {
    @Test
    public void testTokens() throws LexException {
        String source = "{}[]():,true false null \n 123 \"abc\"";
        List<Token> tokens = KsonLexer.lex(source);
        System.out.println(tokens);
    }

    @Test
    public void mathTokens() throws LexException {
        String source = "+ - * / mod > >= < <=";
        List<Token> tokens = KsonLexer.lex(source);
        System.out.println(tokens);
    }

    @Test
    public void markTokens() throws LexException {
        String source = " . @ $symbol % %nodename ";
        List<Token> tokens = KsonLexer.lex(source);
        System.out.println(tokens);
    }

    @Test
    public void commentTokens() throws LexException {
        String source = "abc # comment1\n#comment2\n efg";
        List<Token> tokens = KsonLexer.lex(source);
        System.out.println(tokens);
    }
}
