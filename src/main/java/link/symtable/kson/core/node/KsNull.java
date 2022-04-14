package link.symtable.kson.core.node;


public class KsNull extends KsValueNode {
//    public static KsonNull NULL = new KsonNull();
    public static KsNull NULL = null;

    public String toString() {
        return "null";
    }

    public Object toPlainObject() {
        return null;
    }

    public boolean equals(Object obj) {
        return obj instanceof KsNull;
    }

    public int hashCode() {
        return 0;
    }

    public boolean toBoolean() {
        return false;
    }
}
