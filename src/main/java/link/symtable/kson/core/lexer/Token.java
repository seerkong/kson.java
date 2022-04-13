package link.symtable.kson.core.lexer;

import lombok.Getter;


@Getter
public class Token {
    public TokenType type;
    public String value;
    public int row;
    public int column;

    public Token(TokenType type, String value, int row, int column) {
        this.type = type;
        this.value = value;
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return String.format("(Token {type=%s, value=<%s>, row=%d, column=%d})", type, value, row, column);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return type == other.type
                && value.equals(other.value)
                && row == other.row
                && column == other.column;
    }

    public int hashCode() {
        int num = 0;
        if (value != null) {
            num += 7 * value.hashCode();
        }
        return num;
    }
}
