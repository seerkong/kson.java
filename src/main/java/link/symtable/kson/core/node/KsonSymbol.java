package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public class KsonSymbol extends KsonValueNode {
    private String value;

    public KsonSymbol(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("$%s", value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonWord)) {
            return false;
        }
        KsonWord other = (KsonWord) obj;
        return value.equals(other.getValue());
    }

    public int hashCode() {
        int num = 0;
        if (value != null) {
            num += 7 * value.hashCode();
        }
        return num;
    }

    public boolean isSymbol() {
        return true;
    }
    public boolean isSymbol(String inner) {
        return value.equals(inner);
    }
    public KsonSymbol asSymbol() {
        return this;
    }
}
