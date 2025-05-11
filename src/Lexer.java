import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int currentPosition;
    private int line;

    /**
     * Initialize the Lexer class
     *
     * @param input the input string
     */
    public Lexer(String input) {
        this.input = input;
        this.currentPosition = 0;
        this.line = 1;
    }

    /**
     * Convert the input into tokens.
     *
     * @return the list of tokens
     */
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (currentPosition < input.length()) {
            char c = input.charAt(currentPosition++);
            switch (c) {
                case ' ':
                case '\r':
                case '\t':
                    break;
                case '\n':
                    line++;
                    break;
                case '+':
                    tokens.add(TokenFactory.makeToken(TokenType.PLUS, "+", line));
                    break;
                case '*':
                    tokens.add(TokenFactory.makeToken(TokenType.STAR, "*", line));
                    break;
                case '/':
                    tokens.add(TokenFactory.makeToken(TokenType.SLASH, "/", line));
                    break;
                case '%':
                    tokens.add(TokenFactory.makeToken(TokenType.MOD, "%", line));
                    break;
                case '(':
                    tokens.add(TokenFactory.makeToken(TokenType.LPAREN, "(", line));
                    break;
                case ')':
                    tokens.add(TokenFactory.makeToken(TokenType.RPAREN, ")", line));
                    break;
                case '{':
                    tokens.add(TokenFactory.makeToken(TokenType.LBRACE, "{", line));
                    break;
                case '}':
                    tokens.add(TokenFactory.makeToken(TokenType.RBRACE, "}", line));
                    break;
                case ';':
                    tokens.add(TokenFactory.makeToken(TokenType.SEMICOLON, ";", line));
                    break;
                case ',':
                    tokens.add(TokenFactory.makeToken(TokenType.COMMA, ",", line));
                    break;
                case '=':
                    tokens.add(TokenFactory.makeToken(TokenType.EQ, "=", line));
                    break;
                case '~':
                    tokens.add(TokenFactory.makeToken(TokenType.NE, "~", line));
                    break;

                case '-':
                    if (followedBy('-') && followedBy('-')) {   // if is comment
                        while (currentPosition < input.length() &&
                                input.charAt(currentPosition) != '\n') {
                            char ignore = input.charAt(currentPosition++);
                        }
                    } else {    // if is minus sign
                        tokens.add(TokenFactory.makeToken(TokenType.MINUS, "-", line));
                    }
                    break;

                case '<':
                    if (followedBy('-')) {
                        tokens.add(TokenFactory.makeToken(TokenType.ASSIGN, "<-", line));
                    } else if (followedBy('=')) {
                        tokens.add(TokenFactory.makeToken(TokenType.LE, "<=", line));
                    } else {
                        tokens.add(TokenFactory.makeToken(TokenType.LT, "<", line));
                    }
                    break;

                case '>':
                    if (followedBy('=')) {
                        tokens.add(TokenFactory.makeToken(TokenType.GE, ">=", line));
                    } else {
                        tokens.add(TokenFactory.makeToken(TokenType.GT, ">", line));
                    }
                    break;

                default:
                    if (isDigit(c)) {   // continue reading digits until the end of the number is reached
                        StringBuilder num = new StringBuilder();
                        num.append(c);
                        while (currentPosition < input.length() &&
                                isDigit(input.charAt(currentPosition))) {
                            num.append(input.charAt(currentPosition++));
                        }
                        tokens.add(TokenFactory.makeToken(TokenType.NUMBER,
                                num.toString(), line));
                    } else if (isAlphabetic(c)) {
                        tokens.add(identifier(c));
                    } else {    // invalid content, generate error token
                        tokens.add(TokenFactory.makeToken(TokenType.ERROR,
                                                            String.valueOf(c), line));
                    }
            }
        }
        tokens.add(TokenFactory.makeToken(TokenType.EOF, "", line));
        return tokens;
    }

    /**
     * Return whether the current char is followed by the target.
     *
     * @param target the target char
     * @return whether the current char is followed by target
     */
    private boolean followedBy(char target) {
        if (currentPosition >= input.length()) {
            return false;
        }
        if (input.charAt(currentPosition) != target) {
            return false;
        }
        currentPosition++;
        return true;
    }

    /**
     * Continue reading from first until a keyword or identifier token can be generated.
     *
     * @param first the first char
     * @return the new token
     */
    private Token identifier(char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        while (currentPosition < input.length() &&
                (isDigit(input.charAt(currentPosition)) ||
                        isAlphabetic(input.charAt(currentPosition)) ||
                        input.charAt(currentPosition) == '_')) {
            sb.append(input.charAt(currentPosition++));
        }
        return TokenFactory.identify(sb.toString(), line);
    }
}

