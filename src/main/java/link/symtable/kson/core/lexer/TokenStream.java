package link.symtable.kson.core.lexer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import link.symtable.kson.core.util.IndexedStream;
import link.symtable.kson.core.util.ParseException;

public class TokenStream extends IndexedStream<Token, TokenType> {
    public TokenStream(Collection<Token> input) {
        super(input, (token, tokenType) -> token.type == tokenType);
    }

    public void skipTokens(TokenType expect) {
        Set<TokenType> expectTypes = new HashSet<>();
        expectTypes.add(expect);
        skipBlankTokens();
        Token elem = current();
        if (inTypeSet(elem, expectTypes)) {
            consume();
        }
    }

    public Token consumeTypeAndSkipBlankTokens(TokenType expect) {
        Set<TokenType> expectTypes = new HashSet<>();
        expectTypes.add(expect);
        return consumeAndSkipBlankTokens(expectTypes);
    }


    public Token consumeAndSkipBlankTokens(Set<TokenType> expectedTypes) throws
            ParseException {
        skipBlankTokens();
        Token elem = current();
        if (inTypeSet(elem, expectedTypes)) {
            consume();
            return elem;
        }
        throw newParseException();
    }

    public void skipBlankTokens() {
        while (inTypeSet(current(), TokenType.BlankTypes)) {
            consume();
        }
    }

    public boolean inTypeSet(Token elem, Set<TokenType> ignoreTypes) {
        for (TokenType ignoreType : ignoreTypes) {
            if (constraintChecker.apply(elem, ignoreType)) {
                return true;
            }
        }
        return false;
    }
}
