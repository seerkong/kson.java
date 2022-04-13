package link.symtable.kson.core.node;

public class KsonUndefined extends KsonValueNode {
    public static KsonUndefined UNDEFINED = new KsonUndefined();

    public String toString() {
        return "undefined";
    }

    public Object toPlainObject() {
        return null;
    }

    public boolean equals(Object obj) {
        return obj instanceof KsonUndefined;
    }

    public int hashCode() {
        return 0;
    }
}
