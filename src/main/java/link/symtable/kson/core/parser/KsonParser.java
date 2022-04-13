package link.symtable.kson.core.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.lexer.KsonLexer;
import link.symtable.kson.core.lexer.Token;
import link.symtable.kson.core.lexer.TokenStream;
import link.symtable.kson.core.lexer.TokenType;
import link.symtable.kson.core.node.KsonArray;
import link.symtable.kson.core.node.KsonBoolean;
import link.symtable.kson.core.node.KsonDouble;
import link.symtable.kson.core.node.KsonInt64;
import link.symtable.kson.core.node.KsonListNode;
import link.symtable.kson.core.node.KsonMap;
import link.symtable.kson.core.node.KsonNode;
import link.symtable.kson.core.node.KsonNull;
import link.symtable.kson.core.node.KsonNumber;
import link.symtable.kson.core.node.KsonString;
import link.symtable.kson.core.node.KsonSymbol;
import link.symtable.kson.core.node.KsonWord;
import link.symtable.kson.core.util.LexException;
import link.symtable.kson.core.util.ParseException;


public class KsonParser {
    private static Pattern EmptyReg = Pattern.compile("^\\s*$");

    public static KsonNode parse(String input) throws LexException, ParseException {
        Matcher matcher = EmptyReg.matcher(input);
        if (matcher.find()) {
            return KsonNull.NULL;
        }
        return parse(KsonLexer.lex(input));
    }

    public static KsonNode parse(List<Token> input) throws ParseException {
        return parseValue(new TokenStream(input));
    }

    public static KsonNode parseValue(TokenStream s) throws ParseException {
        s.skipBlankTokens();
        Token currentToken = s.current();
        switch (currentToken.getType()) {
            case BeginMap:
                return parseContainer(s, TokenType.BeginMap, TokenType.EndMap, TokenType.ElementSeperator,
                        KsonParser::parsePair,
                        children -> new KsonMap(children));
            case BeginArray:
                return parseContainer(s, TokenType.BeginArray, TokenType.EndArray, TokenType.ElementSeperator,
                        KsonParser::parseValue,
                        children -> new KsonArray(children));
            case BeginList:
                return parseListContainer(s, TokenType.BeginList, TokenType.EndList, KsonParser::parseValue,
                        children -> KsonListNode.makeByList(children));
            case Number:
                return parseNumber(s);
            case String:
                return parseString(s);
            case Word:
                return parseWord(s);
            case Symbol:
                return parseSymbol(s);
            case Boolean:
                return parseBoolean(s);
            case Null:
                return parseNull(s);
            default:
                throw s.newParseException();
        }
    }


    public static String parseLiteralString(TokenStream s) throws ParseException {
        String string = s.consumeTypeAndSkipBlankTokens(TokenType.String).getValue();
        char[] subChars = string.substring(1).toCharArray();
        List<Character> charList = new ArrayList<>();
        for (char c : subChars) {
            charList.add(c);
        }

        return parseChars(new CharStream(charList));
    }

    public static KsonString parseString(TokenStream s) throws ParseException {
        return new KsonString(parseLiteralString(s));
    }

    public static KsonWord parseWord(TokenStream s) throws ParseException {
        Token t = s.consume(TokenType.Word);
        return new KsonWord(t.getValue());
    }

    public static KsonSymbol parseSymbol(TokenStream s) throws ParseException {
        Token t = s.consume(TokenType.Symbol);
        return new KsonSymbol(t.getValue().substring(1));
    }

    public static KsonNumber parseNumber(TokenStream s) throws ParseException {
        Token nextToken = s.consumeTypeAndSkipBlankTokens(TokenType.Number);
        if (nextToken.getValue().contains(".") || nextToken.getValue().contains("e") || nextToken.getValue()
                .contains("E")) {
            return new KsonDouble(Double.parseDouble(nextToken.getValue()));
        } else {
            return new KsonInt64(Long.parseLong(nextToken.getValue()));
        }
    }

    public static KsonBoolean parseBoolean(TokenStream s) throws ParseException {
        Token t = s.consumeTypeAndSkipBlankTokens(TokenType.Boolean);
        return new KsonBoolean(Boolean.parseBoolean(t.getValue()));
    }

    public static KsonNull parseNull(TokenStream s) throws ParseException {
        s.consumeTypeAndSkipBlankTokens(TokenType.Null);
        return KsonNull.NULL;
    }

    public static <T, C> T parseListContainer(TokenStream s, TokenType begin, TokenType end,
            Function<TokenStream, C> parser,
            Function<List<C>, T> factory) throws ParseException {
        s.consumeTypeAndSkipBlankTokens(begin);

        List<C> children = new ArrayList<C>();
        s.skipBlankTokens();
        while (s.current().getType() != end) {
            children.add(parser.apply(s));
            s.skipBlankTokens();
        }
        s.consumeTypeAndSkipBlankTokens(end);

        T container = factory.apply(children);
        return container;
    }

    public static <T, C> T parseContainer(TokenStream s, TokenType begin, TokenType end, TokenType seperator,
            Function<TokenStream, C> parser,
            Function<List<C>, T> factory) throws ParseException {
        s.consumeTypeAndSkipBlankTokens(begin);

        List<C> children = new ArrayList<C>();
        s.skipBlankTokens();
        while (s.current().getType() != end) {
            children.add(parser.apply(s));
            s.skipTokens(seperator);
            s.skipBlankTokens();
        }
        T container = factory.apply(children);


        s.consumeTypeAndSkipBlankTokens(end);

        return container;
    }

    public static String parseChars(CharStream s) throws ParseException {
        StringBuilder str = new StringBuilder();
        while (s.current() != '"') {
            str.append(parseChar(s));
        }
        return str.toString();
    }

    public static char parseChar(CharStream s) throws ParseException {
        char curChar = s.current();
        switch (s.current()) {
            case '"':
                throw s.newParseException();
            case '\\':
                s.consume();
                Character ch = s.consume();
                switch (ch) {
                    case 'n':
                        return '\n';
                    case 'r':
                        return '\r';
                    case 't':
                        return '\t';
                    case 'u':
                        return parseUnicodeSequence(s);
                    case 'b':
                        return '\b';
                    case 'f':
                        return '\f';
                    case '"':
                    case '/':
                    case '\\':
                        return ch;
                    default:
                        throw s.newParseException();
                }
            default:
                return s.consume();
        }
    }

    public static Pair<String, KsonNode> parsePair(TokenStream s) throws ParseException {
        String str;
        s.skipBlankTokens();
        if (s.current().getType() == TokenType.Word) {
            str = s.current().getValue();
            s.consume();
        } else {
            str = parseLiteralString(s);
        }
        s.consumeTypeAndSkipBlankTokens(TokenType.PairSeperator);
        return new ImmutablePair<>(str, parseValue(s));
    }

    public static char parseUnicodeSequence(CharStream s) throws ParseException {
        String t = "" + s.consume().charValue() + s.consume().charValue() + s.consume().charValue() + s.consume()
                .charValue();
        Integer num = Integer.parseInt(t, 16);
        char c = (char) ('\0' + num);
        String r = "" + c;
        return r.charAt(0);
    }


}
