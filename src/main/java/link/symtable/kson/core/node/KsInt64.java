package link.symtable.kson.core.node;

import java.util.Objects;

import lombok.Getter;

@Getter
public class KsInt64 extends KsNumber {
    private long value;

    public KsInt64(long value) {
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
        if (!(obj instanceof KsInt64)) {
            return false;
        }
        KsInt64 other = (KsInt64) obj;
        return Objects.equals(this.value, other.toPlainObject());
    }

    public int hashCode() {
        return Long.hashCode(value);
    }

    public boolean isInt64() {
        return true;
    }
    public KsInt64 asInt64() {
        return this;
    }
}
