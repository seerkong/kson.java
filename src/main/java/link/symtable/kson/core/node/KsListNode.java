package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ExecState;

import lombok.Data;
import lombok.Getter;

@Data
public class KsListNode extends KsContainerNode {
    public static KsListNode NIL = null;

    private KsNode value;
    private KsListNode next = null;

    private KsListNode() {

    }

    public KsListNode(KsNode value) {
        this(value, null);
    }

    public KsListNode(KsNode value, KsListNode next) {
        this.value = value;
        this.next = next;
    }

    public static KsListNode makeByArray(KsNode[] children) {
        return makeByList(new ArrayList<KsNode>(Arrays.asList(children)));
    }

    public static KsListNode makeByList(List<KsNode> children) {
        KsListNode result = null;
        for (int i = children.size() - 1; i >= 0; i--) {
            KsNode v = children.get(i);
            KsListNode node = new KsListNode(v, result);
            result = node;
        }

        return result;
    }

    public boolean hasNext() {
        return next != null;
    }

    public String toString() {
        List<KsNode> chidlren = new ArrayList<>();
        KsListNode iter = this;
        while (iter != KsListNode.NIL) {
            chidlren.add(iter.getValue());
            iter = iter.getNext();
        }
        String innerItems = chidlren.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));

        return String.format("(%s)", innerItems);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsListNode)) {
            return false;
        }
        KsListNode other = (KsListNode) obj;
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
    public KsListNode asListNode() {
        return this;
    }

    public KsNode subscriptByString(ExecState state, String fieldName) {
        if ("value".equals(fieldName)) {
            return value;
        } else if ("next".equals(fieldName)) {
            return next;
        } else {
            throw new NotImplementedException("no field with name" + fieldName + " node:" + this.toString());
        }
    }

    public KsNode getNextValue() {
        return getNext().getValue();
    }

    public KsListNode getNextNext() {
        return getNext().getNext();
    }

    public KsNode getNextNextValue() {
        return getNextNext().getValue();
    }

    public KsListNode getNextNextNext() {
        return getNextNext().getNext();
    }
}
