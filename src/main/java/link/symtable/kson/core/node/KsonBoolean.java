package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public class KsonBoolean extends KsonValueNode {
    public static KsonBoolean TRUE = new KsonBoolean(true);
    public static KsonBoolean FALSE = new KsonBoolean(false);
    private boolean value;

    public KsonBoolean(boolean value) {
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
        if (!(obj instanceof KsonBoolean)) {
            return false;
        }
        KsonBoolean other = (KsonBoolean) obj;
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

    public KsonBoolean asBoolean() {
        return this;
    }
}
