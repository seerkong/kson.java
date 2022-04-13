package link.symtable.kson.core.node;

import org.apache.commons.lang3.NotImplementedException;


public abstract class KsonNodeBase implements KsonNode {
    public Object toPlainObject() {
        throw new NotImplementedException("NotImplementedException");
    }

    public boolean toBoolean() {
        return true;
    }

    public boolean isBoolean() {
        return false;
    }

    public KsonBoolean asBoolean() {
        throw new RuntimeException("cannot converted to KsonBoolean");
    }

    public boolean isWord() {
        return false;
    }
    public boolean isWord(String inner) {
        return false;
    }

    public KsonWord asWord() {
        throw new RuntimeException("cannot converted to KsonWord");
    }

    public boolean isSymbol() {
        return false;
    }
    public boolean isSymbol(String inner) {
        return false;
    }
    public KsonSymbol asSymbol() {
        throw new RuntimeException("cannot converted to KsonWord");
    }

    public boolean isString() {
        return false;
    }
    public KsonString asString() {
        throw new RuntimeException("cannot converted to KsonString");
    }

    public boolean isInt64() {
        return false;
    }
    public KsonInt64 asInt64() {
        throw new RuntimeException("cannot converted to KsonInt64");
    }

    public boolean isDouble() {
        return false;
    }
    public KsonDouble asDouble() {
        throw new RuntimeException("cannot converted to KsonDouble");
    }

    public boolean isListNode() {
        return false;
    }
    public KsonListNode asListNode() {
        throw new RuntimeException("cannot converted to ListNode");
    }

    public boolean isArray() {
        return false;
    }
    public KsonArray asArray() {
        throw new RuntimeException("cannot converted to KsonArray");
    }

    public boolean isMap() {
        return false;
    }
    public KsonMap asMap() {
        throw new RuntimeException("cannot converted to KsonMap");
    }
}
