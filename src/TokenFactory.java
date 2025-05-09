public class TokenFactory {
    public static Token makeToken(TokenType type, String value, Integer line) {
        return new Token(type, value, line);
    }

    public static Token identify(String value, int line) {
        return switch (value) {
            case "var" -> makeToken(TokenType.VAR, value, line);
            case "function" -> makeToken(TokenType.FUNCTION, value, line);
            case "return" -> makeToken(TokenType.RETURN, value, line);
            case "if" -> makeToken(TokenType.IF, value, line);
            case "elif" -> makeToken(TokenType.ELIF, value, line);
            case "else" -> makeToken(TokenType.ELSE, value, line);
            case "while" -> makeToken(TokenType.WHILE, value, line);
            case "run" -> makeToken(TokenType.RUN, value, line);
            case "print" -> makeToken(TokenType.PRINT, value, line);
            case "input" -> makeToken(TokenType.INPUT, value, line);
            default -> makeToken(TokenType.IDENTIFIER, value, line);
        };
    }
}
