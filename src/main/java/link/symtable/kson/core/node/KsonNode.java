package link.symtable.kson.core.node;

public interface KsonNode {
    Object toPlainObject();
    boolean toBoolean();

    boolean isBoolean();
    KsonBoolean asBoolean();

    boolean isWord();
    boolean isWord(String inner);
    KsonWord asWord();

    boolean isSymbol();
    boolean isSymbol(String inner);
    KsonSymbol asSymbol();

    boolean isString();
    KsonString asString();



    boolean isInt64();
    KsonInt64 asInt64();

    boolean isDouble();
    KsonDouble asDouble();

    boolean isListNode();
    KsonListNode asListNode();

    boolean isArray();
    KsonArray asArray();

    boolean isMap();
    KsonMap asMap();
}
