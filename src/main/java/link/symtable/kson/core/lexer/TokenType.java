package link.symtable.kson.core.lexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TokenType {
    BeginMap(0),
    EndMap(1),
    BeginArray(2),
    EndArray(3),
    BeginList(4),
    EndList(5),
    PairSeperator(6),
    ElementSeperator(7),
    Boolean(8),
    Null(9),
    NewLine(10),
    SingleLineComment(11),
    Whitespace(12),
    Number(13),
    String(14),
    Word(15),
    Symbol(16),
    ;

    public static Set<TokenType> BlankTypes = new HashSet<>(Arrays.asList(new TokenType[] {
            TokenType.NewLine, TokenType.Whitespace, TokenType.SingleLineComment
    }));

    private int code;

    TokenType(int code) {
        this.code = code;
    }

    public static TokenType getByCode(int code) {
        TokenType[] values = TokenType.values();
        for (TokenType v : values) {
            if (v.code == code) {
                return v;
            }
        }
        return TokenType.Null;
    }
}
