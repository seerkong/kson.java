package link.symtable.kson.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public class IndexedStream<TElement, TElementType> {
    private int index;
    private List<TElement> input;
    protected BiFunction<TElement, TElementType, Boolean> constraintChecker;

    public IndexedStream(Collection<TElement> input, BiFunction<TElement, TElementType, Boolean> constraintChecker) {
        this.input = new ArrayList<>(input);
        this.constraintChecker = constraintChecker;
    }

    public TElement current() throws ParseException {
        if (end()) {
            throw newEndOfStreamException();
        }
        return input.get(index);
    }

    public TElement consume() throws ParseException {
        TElement r = current();
        index++;
        return r;
    }

    public TElement consume(TElementType expectedType) throws ParseException {
        TElement elem = consume();
        if (constraintChecker.apply(elem, expectedType)) {
            return elem;
        }
        throw newParseException();
    }

    public boolean end() {
        return index >= input.size();
    }

    public ParseException newParseException() throws ParseException {
        String msg = String.format("Invalid token %s at position %d", current(), index);
        return new ParseException(msg);
    }

    public ParseException newEndOfStreamException() {
        return new ParseException("End of stream");
    }

    public String toString() {
        return String.format("(CharStream {Index=%d, Input=%s})", index, input);
    }
}
