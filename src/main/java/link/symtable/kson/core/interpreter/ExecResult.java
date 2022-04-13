package link.symtable.kson.core.interpreter;

import link.symtable.kson.core.node.KsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ExecResult {
    KsonNode data;
}
