package link.symtable.kson.core.node;

import lombok.Getter;

@Getter
public abstract class KsFunction extends KsNodeBase {
    public String name = null;
    public int requiredArgsCount = 0;
    public boolean hasVariableArgs = false;
}
