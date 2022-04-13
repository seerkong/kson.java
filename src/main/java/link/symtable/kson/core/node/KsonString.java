package link.symtable.kson.core.node;

import link.symtable.kson.core.util.StringEscapeHelper;

import lombok.Getter;

@Getter
public class KsonString extends KsonValueNode {
    private String value;

    public KsonString(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("\"%s\"", StringEscapeHelper.escapeString(value));
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonString)) {
            return false;
        }
        KsonString other = (KsonString) obj;
        return value.equals(other.value);
    }

    public int hashCode() {
        int num = 0;
        if (value != null) {
            num += 7 * value.hashCode();
        }
        return num;
    }

    public boolean isString() {
        return true;
    }
    public KsonString asString() {
        return this;
    }
}
