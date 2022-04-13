package link.symtable.kson.core.node;

import java.util.Objects;

import lombok.Getter;

@Getter
public class KsonInt64 extends KsonNumber {
    private long value;

    public KsonInt64(long value) {
        this.value = value;
    }


    public long toInt64Val() {
        return value;
    }

    public double toDoubleVal() {
        return (double) value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonInt64)) {
            return false;
        }
        KsonInt64 other = (KsonInt64) obj;
        return Objects.equals(this.value, other.toPlainObject());
    }

    public int hashCode() {
        return Long.hashCode(value);
    }

    public boolean isInt64() {
        return true;
    }
    public KsonInt64 asInt64() {
        return this;
    }
}
