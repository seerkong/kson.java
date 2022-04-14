package link.symtable.kson.core.interpreter;

import link.symtable.kson.core.node.KsNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ExecResult {
    KsNode data;
}
