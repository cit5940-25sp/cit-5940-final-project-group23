public class TokenFactory {
    /**
     * Create a new token.
     *
     * @param type type of token
     * @param value value of token
     * @param line line number of token
     * @return the new token
     */
    public static Token makeToken(TokenType type, String value, Integer line) {
        return new Token(type, value, line);
    }

    /**
     * Determine if the string is a keyword or identifier,
     * and make a token of the corresponding type
     *
     * @param value value of token
     * @param line line number of token
     * @return the new token
     */
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
