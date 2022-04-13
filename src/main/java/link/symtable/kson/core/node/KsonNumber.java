package link.symtable.kson.core.node;


public abstract class KsonNumber extends KsonValueNode {

    public abstract long toInt64Val();

    public abstract double toDoubleVal();
}
