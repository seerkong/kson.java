package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public class KsSymbol extends KsValueNode {
    private String value;

    public KsSymbol(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("$%s", value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsWord)) {
            return false;
        }
        KsWord other = (KsWord) obj;
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
    public KsSymbol asSymbol() {
        return this;
    }
}
