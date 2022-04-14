package link.symtable.kson.core.node;


public abstract class KsNumber extends KsValueNode {

    public abstract long toInt64Val();

    public abstract double toDoubleVal();
}
