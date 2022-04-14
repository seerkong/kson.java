package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public class KsBoolean extends KsValueNode {
    public static KsBoolean TRUE = new KsBoolean(true);
    public static KsBoolean FALSE = new KsBoolean(false);
    private boolean value;

    public KsBoolean(boolean value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean getValue() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsBoolean)) {
            return false;
        }
        KsBoolean other = (KsBoolean) obj;
        return value == other.getValue();
    }

    public int hashCode() {
        return Boolean.hashCode(value);
    }

    public boolean toBoolean() {
        return value;
    }

    public boolean isBoolean() {
        return true;
    }

    public KsBoolean asBoolean() {
        return this;
    }
}
