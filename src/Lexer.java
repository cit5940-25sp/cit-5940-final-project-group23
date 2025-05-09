import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public String input;
    private int currentPosition;
    private int line;

    public Lexer(String input) {
        this.input = input;
        this.currentPosition = 0;
        this.line = 1;
    }

    public List<Token> Tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (currentPosition < input.length()) {
            char c = input.charAt(currentPosition);
            switch (c) {
                case ' ': case '\r': case '\t': break;
                case '\n': line++; break;
                case '+': tokens.add(TokenFactory.makeToken(TokenType.PLUS, "+", line)); break;
                case '*': tokens.add(TokenFactory.makeToken(TokenType.STAR, "*", line)); break;
                case '/': tokens.add(TokenFactory.makeToken(TokenType.SLASH, "/", line)); break;
                case '%': tokens.add(TokenFactory.makeToken(TokenType.MOD, "%", line)); break;
                case '(': tokens.add(TokenFactory.makeToken(TokenType.LPAREN, "(", line)); break;
                case ')': tokens.add(TokenFactory.makeToken(TokenType.RPAREN, ")", line)); break;
                case '{': tokens.add(TokenFactory.makeToken(TokenType.LBRACE, "{", line)); break;
                case '}': tokens.add(TokenFactory.makeToken(TokenType.RBRACE, "}", line)); break;
                case ';': tokens.add(TokenFactory.makeToken(TokenType.SEMICOLON, ";", line)); break;
                case ',': tokens.add(TokenFactory.makeToken(TokenType.COMMA, ",", line)); break;

                case '-':
                    if (followedBy('-') && followedBy('-')) {   // if is comment
                        while (currentPosition < input.length() && input.charAt(currentPosition) != '\n' ) {
                            char ignore = input.charAt(currentPosition++);
                        }
                    } else {    // if is minus sign
                        tokens.add(TokenFactory.makeToken(TokenType.MINUS, "-", line));
                    }
                    break;


            }
        }
        return tokens;
    }

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
}
