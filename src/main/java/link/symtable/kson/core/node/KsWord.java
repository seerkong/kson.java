package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public class KsWord extends KsValueNode {
    private String value;

    public KsWord(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("%s", value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsWord)) {
            return false;
        }
        KsWord other = (KsWord) obj;
        return value.equals(other.value);
    }

    public int hashCode() {
        int num = 0;
        if (value != null) {
            num += 7 * value.hashCode();
        }
        return num;
    }

    public boolean isWord() {
        return true;
    }
    public boolean isWord(String inner) {
        return value.equals(inner);
    }

    public KsWord asWord() {
        return this;
    }
}
