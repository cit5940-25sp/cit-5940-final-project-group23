public class Token {
    private final TokenType type;
    private final String value;
    private final int line;

    /**
     * Initialize token.
     *
     * @param type type of token
     * @param value value of token
     * @param line line number
     */
    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    /**
     * Get the type of the token.
     *
     * @return type of token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Get the value of the token.
     *
     * @return value of token
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the line number of the token.
     *
     * @return line number of token
     */
    public int getLine() {
        return line;
    }

    /**
     * Print the token as string.
     *
     * @return token string
     */
    @Override
    public String toString() {
        return String.format("Token[%s, '%s', line %d]", type, value, line);
    }
}
