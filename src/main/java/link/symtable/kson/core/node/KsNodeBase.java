package link.symtable.kson.core.node;

import org.apache.commons.lang3.NotImplementedException;


public abstract class KsNodeBase implements KsNode {
    public Object toPlainObject() {
        throw new NotImplementedException("NotImplementedException");
    }

    public boolean toBoolean() {
        return true;
    }

    public boolean isBoolean() {
        return false;
    }

    public KsBoolean asBoolean() {
        throw new RuntimeException("cannot converted to KsonBoolean");
    }

    public boolean isWord() {
        return false;
    }
    public boolean isWord(String inner) {
        return false;
    }

    public KsWord asWord() {
        throw new RuntimeException("cannot converted to KsonWord");
    }

    public boolean isSymbol() {
        return false;
    }
    public boolean isSymbol(String inner) {
        return false;
    }
    public KsSymbol asSymbol() {
        throw new RuntimeException("cannot converted to KsonWord");
    }

    public boolean isString() {
        return false;
    }
    public KsString asString() {
        throw new RuntimeException("cannot converted to KsonString");
    }

    public boolean isInt64() {
        return false;
    }
    public KsInt64 asInt64() {
        throw new RuntimeException("cannot converted to KsonInt64");
    }

    public boolean isDouble() {
        return false;
    }
    public KsDouble asDouble() {
        throw new RuntimeException("cannot converted to KsonDouble");
    }

    public boolean isListNode() {
        return false;
    }
    public KsListNode asListNode() {
        throw new RuntimeException("cannot converted to ListNode");
    }

    public boolean isArray() {
        return false;
    }
    public KsArray asArray() {
        throw new RuntimeException("cannot converted to KsonArray");
    }

    public boolean isMap() {
        return false;
    }
    public KsMap asMap() {
        throw new RuntimeException("cannot converted to KsonMap");
    }


    public boolean isFunction() {
        return false;
    }
    public KsFunction asFunction() {
        throw new RuntimeException("cannot converted to KsonFunction");
    }
}
