package link.symtable.kson.core.node;


public class KsonNull extends KsonValueNode {
//    public static KsonNull NULL = new KsonNull();
    public static KsonNull NULL = null;

    public String toString() {
        return "null";
    }

    public Object toPlainObject() {
        return null;
    }

    public boolean equals(Object obj) {
        return obj instanceof KsonNull;
    }

    public int hashCode() {
        return 0;
    }

    public boolean toBoolean() {
        return false;
    }
}
