package link.symtable.kson.core.node;

public class KsUndefined extends KsValueNode {
    public static KsUndefined UNDEFINED = new KsUndefined();

    public String toString() {
        return "undefined";
    }

    public Object toPlainObject() {
        return null;
    }

    public boolean equals(Object obj) {
        return obj instanceof KsUndefined;
    }

    public int hashCode() {
        return 0;
    }
}
