package link.symtable.kson.core.parser;

import java.util.Collection;

import link.symtable.kson.core.util.IndexedStream;

public class CharStream extends IndexedStream<Character, Character> {
    public CharStream(Collection<Character> input) {
        super(input, (c, expected) -> c.charValue() == expected.charValue());
    }
}
