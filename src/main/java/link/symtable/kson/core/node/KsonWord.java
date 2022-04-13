package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public class KsonWord extends KsonValueNode {
    private String value;

    public KsonWord(String value) {
        this.value = value;
    }

    public String toString() {
        return String.format("%s", value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonWord)) {
            return false;
        }
        KsonWord other = (KsonWord) obj;
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

    public KsonWord asWord() {
        return this;
    }
}
