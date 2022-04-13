package link.symtable.kson.core.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import link.symtable.kson.core.util.LexException;


public class KsonLexer {
    public static Pattern reg = Pattern.compile(
            "(?<BeginMap>\\{)|(?<EndMap>\\})|(?<BeginArray>\\[)|(?<EndArray>\\])|(?<BeginList>\\()|(?<EndList>\\))"
                    + "|(?<PairSeperator>:)|(?<ElementSeperator>,)"
                    + "|(?<Boolean>true|false)|(?<Null>null)|(?<NewLine>\\n)"
                    + "|(?<SingleLineComment>#.*\\n)"
                    + "|(?<Whitespace>(?:\t| |\r)+)"
                    + "|(?<Number>-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[Ee][+-]?\\d+)?)"
                    + "|(?<String>\"(?:[^\\\\\"]|\\\\(?:[\"\\/bfnrt\\\\]|u[0-9A-Fa-f]{4}))*\")"
                    + "|(?<Word>(?:(?:[a-zA-Z_])(?:[a-zA-Z1-9_])*)|\\+|\\-|\\*|\\/|>[=]?|<[=]?|\\.|@|%)"
                    + "|(?<Symbol>(?:\\$[a-zA-Z_])(?:[a-zA-Z1-9_])*)"
    );

    public static List<Token> lex(String input) throws LexException {
        List<Token> tokenList = new ArrayList<Token>();
        int startat = 0;
        int row = 1;
        int column = 1;
        Matcher matcher = reg.matcher(input);
        while (startat < input.length()) {
            if (!matcher.find(startat)) {
                throw new LexException("Invalid Token", row, column);
            }
            for (TokenType tokenType : TokenType.values()) {
                String value = matcher.group(tokenType.name());
                if (Objects.nonNull(value)) {
                    tokenList.add(new Token(tokenType, value, row, column));
                    switch (tokenType) {
                        case NewLine:
                        case SingleLineComment:
                            row++;
                            column = 1;
                            startat += value.length();
                            break;
                        case Whitespace:
                            startat += value.length();
                            column += value.length();
                            break;
                        default:
                            startat += value.length();
                            column += value.length();
                            break;
                    }
                    break;
                }
            }
        }
        return tokenList;
    }
}
