package link.symtable.kson.core.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import link.symtable.kson.core.interpreter.ExecState;
import link.symtable.kson.core.interpreter.oopsupport.SupportMethodCall;

import lombok.Getter;

@Getter
public class KsMap extends KsContainerNode implements SupportMethodCall, Map {
    public static KsMap EMPTY = new KsMap();
    public LinkedHashMap<String, KsNode> value;

    public KsMap(LinkedHashMap<String, KsNode> value) {
        this.value = value;
    }

    public KsMap() {
        this(new LinkedHashMap<String, KsNode>());
    }

    public KsMap(Pair<String, KsNode>[] members) {
        this(entriesToMap(new ArrayList<>(Arrays.asList(members))));
    }

    public KsMap(Collection<Pair<String, KsNode>> members) {
        this(entriesToMap(new ArrayList<>(members)));
    }

    private static LinkedHashMap<String, KsNode> entriesToMap(Collection<Pair<String, KsNode>> members) {
        LinkedHashMap<String, KsNode> map = new LinkedHashMap<>();
        for (Map.Entry<String, KsNode> member : members) {
            map.put(member.getKey(), member.getValue());
        }
        return map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> items = new ArrayList<>();
        for (Map.Entry<String, KsNode> member : value.entrySet()) {
            items.add(String.format("\"%s\": %s", member.getKey(), member.getValue()));
        }
        String innerItemsStr = items.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        sb.append("{");
        sb.append(innerItemsStr);
        sb.append("}");
        String r = sb.toString();
        return r;
    }

    public Object toPlainObject() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, KsNode> member : value.entrySet()) {
            result.put(member.getKey(), member.getValue().toPlainObject());
        }
        return result;
    }

    public Map<String, KsNode> getValue() {
        return value;
    }

    public KsNode get(String key) {
        return value.get(key);
    }

    @Override
    public int size() {
        return getValue().size();
    }

    @Override
    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return getValue().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getValue().containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return getValue().get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return getValue().put((String)key, (KsNode) value);
    }

    @Override
    public Object remove(Object key) {
        return getValue().remove(key);
    }

    @Override
    public void putAll(Map m) {
        getValue().putAll(m);
    }

    @Override
    public void clear() {
        getValue().clear();
    }

    @Override
    public Set keySet() {
        return getValue().keySet();
    }

    @Override
    public Collection values() {
        return getValue().values();
    }

    @Override
    public Set<Entry<String, KsNode>> entrySet() {
        return getValue().entrySet();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsMap)) {
            return false;
        }
        KsMap other = (KsMap) obj;
        if (value.size() != other.getValue().size()) {
            return false;
        }
        for (Map.Entry<String, KsNode> member : value.entrySet()) {
            if (!other.getValue().containsKey(member.getKey())
                    || Objects.equals(other.getValue().get(member.getKey()), value.get(member.getKey()))) {
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

    public boolean subscriptAcceptKey(ExecState state, String fieldName) {
        return value.containsKey(fieldName);
    }

    public KsNode subscriptByString(ExecState state, String fieldName) {
        if (value.containsKey(fieldName)) {
            return value.get(fieldName);
        } else {
            return KsNull.NULL;
        }
    }

    @Override
    public KsNode callMethod(ExecState state, String methodName, KsNode[] args) {
        if ("put".equals(methodName)) {
            value.put(args[0].asString().getValue(), args[1]);
            return this;
        } else if ("keys".equals(methodName)) {
            List<KsNode> keys = new ArrayList<>();
            value.keySet().forEach(k -> keys.add(new KsString(k)));
            return new KsArray(keys);
        } else {
            throw new NotImplementedException("methodName " + methodName + " is not supported");
        }
    }

    public boolean isMap() {
        return true;
    }
    public KsMap asMap() {
        return this;
    }
}
