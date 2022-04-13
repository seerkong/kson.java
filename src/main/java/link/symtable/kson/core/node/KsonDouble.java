package link.symtable.kson.core.node;

import java.util.Objects;

import lombok.Getter;

@Getter
public class KsonDouble extends KsonNumber {
    private double value;

    public KsonDouble(double value) {
        this.value = value;
    }

    public long toInt64Val() {
        return (long) value;
    }

    public double toDoubleVal() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public Object toPlainObject() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonDouble)) {
            return false;
        }
        KsonDouble other = (KsonDouble) obj;
        return Objects.equals(this.value, other.toPlainObject());
    }

    public int hashCode() {
        return Double.hashCode(value);
    }

    public boolean isDouble() {
        return true;
    }
    public KsonDouble asDouble() {
        return this;
    }
}
