package link.symtable.kson.core.node;

public interface KsNode {
    Object toPlainObject();
    boolean toBoolean();

    boolean isBoolean();
    KsBoolean asBoolean();

    boolean isWord();
    boolean isWord(String inner);
    KsWord asWord();

    boolean isSymbol();
    boolean isSymbol(String inner);
    KsSymbol asSymbol();

    boolean isString();
    KsString asString();



    boolean isInt64();
    KsInt64 asInt64();

    boolean isDouble();
    KsDouble asDouble();

    boolean isListNode();
    KsListNode asListNode();

    boolean isArray();
    KsArray asArray();

    boolean isMap();
    KsMap asMap();

    boolean isFunction();
    KsFunction asFunction();
}
