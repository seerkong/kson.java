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
public class KsonMap extends KsonContainerNode implements SupportMethodCall, Map {
    public static KsonMap EMPTY = new KsonMap();
    public LinkedHashMap<String, KsonNode> value;

    public KsonMap(LinkedHashMap<String, KsonNode> value) {
        this.value = value;
    }

    public KsonMap() {
        this(new LinkedHashMap<String, KsonNode>());
    }

    public KsonMap(Pair<String, KsonNode>[] members) {
        this(entriesToMap(new ArrayList<>(Arrays.asList(members))));
    }

    public KsonMap(Collection<Pair<String, KsonNode>> members) {
        this(entriesToMap(new ArrayList<>(members)));
    }

    private static LinkedHashMap<String, KsonNode> entriesToMap(Collection<Pair<String, KsonNode>> members) {
        LinkedHashMap<String, KsonNode> map = new LinkedHashMap<>();
        for (Map.Entry<String, KsonNode> member : members) {
            map.put(member.getKey(), member.getValue());
        }
        return map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> items = new ArrayList<>();
        for (Map.Entry<String, KsonNode> member : value.entrySet()) {
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
        for (Map.Entry<String, KsonNode> member : value.entrySet()) {
            result.put(member.getKey(), member.getValue().toPlainObject());
        }
        return result;
    }

    public Map<String, KsonNode> getValue() {
        return value;
    }

    public KsonNode get(String key) {
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
        return getValue().put((String)key, (KsonNode) value);
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
    public Set<Entry<String, KsonNode>> entrySet() {
        return getValue().entrySet();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KsonMap)) {
            return false;
        }
        KsonMap other = (KsonMap) obj;
        if (value.size() != other.getValue().size()) {
            return false;
        }
        for (Map.Entry<String, KsonNode> member : value.entrySet()) {
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

    public KsonNode subscriptByString(ExecState state, String fieldName) {
        if (value.containsKey(fieldName)) {
            return value.get(fieldName);
        } else {
            return KsonNull.NULL;
        }
    }

    @Override
    public KsonNode callMethod(ExecState state, String methodName, KsonNode[] args) {
        if ("put".equals(methodName)) {
            value.put(args[0].asString().getValue(), args[1]);
            return this;
        } else if ("keys".equals(methodName)) {
            List<KsonNode> keys = new ArrayList<>();
            value.keySet().forEach(k -> keys.add(new KsonString(k)));
            return new KsonArray(keys);
        } else {
            throw new NotImplementedException("methodName " + methodName + " is not supported");
        }
    }

    public boolean isMap() {
        return true;
    }
    public KsonMap asMap() {
        return this;
    }
}
