package link.symtable.kson.core.node;

import java.util.Objects;

import lombok.Getter;

@Getter
public class KsDouble extends KsNumber {
    private double value;

    public KsDouble(double value) {
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
        if (!(obj instanceof KsDouble)) {
            return false;
        }
        KsDouble other = (KsDouble) obj;
        return Objects.equals(this.value, other.toPlainObject());
    }

    public int hashCode() {
        return Double.hashCode(value);
    }

    public boolean isDouble() {
        return true;
    }
    public KsDouble asDouble() {
        return this;
    }
}
