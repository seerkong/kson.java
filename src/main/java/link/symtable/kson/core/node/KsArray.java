package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;

import lombok.Getter;

@Getter
public class KsArray extends KsContainerNode implements SupportMethodCall {
    private List<KsNode> value;

    public KsArray() {
        value = new ArrayList<>();
    }

    public KsArray(List<KsNode> value) {
        this.value = value;
    }

    public KsArray(KsNode[] children) {
        this(new ArrayList<KsNode>(Arrays.asList(children)));
    }

    public String toString() {
        String innerItems = value.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return String.format("[%s]", innerItems);
    }

    public Object toPlainObject() {
        List<Object> result = new ArrayList<>();
        for (KsNode ksNode : value) {
            Object itemPrimaryObj = ksNode.toPlainObject();
            result.add(itemPrimaryObj);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsArray)) {
            return false;
        }
        KsArray other = (KsArray) obj;
        if (value.size() != other.value.size()) {
            return false;
        }
        for (int i = 0; i < value.size(); ++i) {
            if (!Objects.equals(value.get(i), other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int num = 0;
        if (value != null) {
            num += 7 * value.hashCode();
        }
        return num;
    }

    public KsNode get(int index) {
        return value.get(index);
    }

    public List<KsNode> getItems() {
        return value;
    }

    public int size() {
        return value.size();
    }

    public boolean isArray() {
        return true;
    }
    public KsArray asArray() {
        return this;
    }

    @Override
    public KsNode subscriptByIndex(ExecState state, int index) {
        if (index > (value.size() - 1)) {
            throw new RuntimeException("array out of index " + index);
        }
        return get(index);
    }

    @Override
    public KsNode callMethod(ExecState state, String methodName, KsNode[] args) {
        if ("append".equals(methodName)) {
            for (int i = 0; i < args.length; i++) {
                value.add(args[i]);
            }
            return this;
        } else {
            throw new NotImplementedException("methodName " + methodName + " is not supported");
        }
    }
}
