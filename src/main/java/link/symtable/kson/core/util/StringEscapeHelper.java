package link.symtable.kson.core.util;

public class StringEscapeHelper {
    public static String escapeString(String internalStr) {
        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < internalStr.length(); idx++) {
            char ch = internalStr.charAt(idx);
            switch (ch) {
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(ch);
                    break;
            }
        }
        return sb.toString();
    }
}
