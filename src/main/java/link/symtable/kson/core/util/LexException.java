package link.symtable.kson.core.util;


public class LexException extends RuntimeException {
    public LexException(String message, int row, int column) {
        super(String.format("message: %s, row %d, column %d", message, row, column));
    }
}
