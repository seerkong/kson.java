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
public class KsonArray extends KsonContainerNode implements SupportMethodCall {
    private List<KsonNode> value;

    public KsonArray() {
        value = new ArrayList<>();
    }

    public KsonArray(List<KsonNode> value) {
        this.value = value;
    }

    public KsonArray(KsonNode[] children) {
        this(new ArrayList<KsonNode>(Arrays.asList(children)));
    }

    public String toString() {
        String innerItems = value.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return String.format("[%s]", innerItems);
    }

    public Object toPlainObject() {
        List<Object> result = new ArrayList<>();
        for (KsonNode ksonNode : value) {
            Object itemPrimaryObj = ksonNode.toPlainObject();
            result.add(itemPrimaryObj);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonArray)) {
            return false;
        }
        KsonArray other = (KsonArray) obj;
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

    public KsonNode get(int index) {
        return value.get(index);
    }

    public List<KsonNode> getItems() {
        return value;
    }

    public int size() {
        return value.size();
    }

    public boolean isArray() {
        return true;
    }
    public KsonArray asArray() {
        return this;
    }

    @Override
    public KsonNode subscriptByIndex(ExecState state, int index) {
        if (index > (value.size() - 1)) {
            throw new RuntimeException("array out of index " + index);
        }
        return get(index);
    }

    @Override
    public KsonNode callMethod(ExecState state, String methodName, KsonNode[] args) {
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
