package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ExecState;

import lombok.Getter;

@Getter
public class KsonListNode extends KsonContainerNode {
    public static KsonListNode NIL = null;

    private KsonNode value;
    private KsonListNode next = null;

    private KsonListNode() {

    }

    public KsonListNode(KsonNode value) {
        this(value, null);
    }

    public KsonListNode(KsonNode value, KsonListNode next) {
        this.value = value;
        this.next = next;
    }

    public static KsonListNode makeByArray(KsonNode[] children) {
        return makeByList(new ArrayList<KsonNode>(Arrays.asList(children)));
    }

    public static KsonListNode makeByList(List<KsonNode> children) {
        KsonListNode result = null;
        for (int i = children.size() - 1; i >= 0; i--) {
            KsonNode v = children.get(i);
            KsonListNode node = new KsonListNode(v, result);
            result = node;
        }

        return result;
    }

    public boolean hasNext() {
        return next != null;
    }

    public String toString() {
        List<KsonNode> chidlren = new ArrayList<>();
        KsonListNode iter = this;
        while (iter != KsonListNode.NIL) {
            chidlren.add(iter.getValue());
            iter = iter.getNext();
        }
        String innerItems = chidlren.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));

        return String.format("(%s)", innerItems);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonListNode)) {
            return false;
        }
        KsonListNode other = (KsonListNode) obj;
        if (!Objects.equals(this.value, other.getValue())) {
            return false;
        }
        if (this.next == null && other.getNext() == null) {
            return true;
        } else if (this.next != null && other.getNext() != null) {
            return this.next.equals(other.getNext());
        } else {
            return false;
        }
    }

    public int hashCode() {
        int num = 0;
        if (value != null) {
            num += 7 * value.hashCode();
        }
        return num;
    }

    public boolean isListNode() {
        return true;
    }
    public KsonListNode asListNode() {
        return this;
    }

    public KsonNode subscriptByString(ExecState state, String fieldName) {
        if ("value".equals(fieldName)) {
            return value;
        } else if ("next".equals(fieldName)) {
            return next;
        } else {
            throw new NotImplementedException("no field with name" + fieldName + " node:" + this.toString());
        }
    }

    public KsonNode getNextValue() {
        return getNext().getValue();
    }

    public KsonListNode getNextNext() {
        return getNext().getNext();
    }

    public KsonNode getNextNextValue() {
        return getNextNext().getValue();
    }

    public KsonListNode getNextNextNext() {
        return getNextNext().getNext();
    }
}
