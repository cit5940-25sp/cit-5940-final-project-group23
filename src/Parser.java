import java.util.List;

/**
 * Parser that converts tokens into an abstract syntax tree.
 * 
 * So this represt a heirarchy of expressions.
 */
public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Parse the tokens into an expression.
     */
    public Expression parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null; // Error handling here
        }
    }

    /**
     * Grammar rule: expression → equality
     */
    private Expression expression() {
        return assignment();
    }

    /**
     * Grammar rule: assignment → equality ("<-" assignment)?
     */
    private Expression assignment() {
        Expression expr = equality();

        if (match(TokenType.ASSIGN)) {
            Token equals = previous();
            Expression value = assignment();

            if (expr instanceof VariableExpression) {
                String name = ((VariableExpression) expr).getName();
                return ExpressionFactory.createAssignment(name, value, expr.getLine());
            }

            error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    /**
     * Grammar rule: equality → comparison (("=" | "~") comparison)*
     */
    private Expression equality() {
        Expression expr = comparison();

        while (match(TokenType.EQ, TokenType.NE)) {
            TokenType operator = previous().getType();
            Expression right = comparison();
            expr = ExpressionFactory.createBinary(expr, operator, right, expr.getLine());
        }

        return expr;
    }

    /**
     * Grammar rule: comparison → term (("<" | "<=" | ">" | ">=") term)*
     */
    private Expression comparison() {
        Expression expr = term();

        while (match(TokenType.LT, TokenType.LE, TokenType.GT, TokenType.GE)) {
            TokenType operator = previous().getType();
            Expression right = term();
            expr = ExpressionFactory.createBinary(expr, operator, right, expr.getLine());
        }

        return expr;
    }

    /**
     * Grammar rule: term → factor (("+" | "-") factor)*
     */
    private Expression term() {
        Expression expr = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            TokenType operator = previous().getType();
            Expression right = factor();
            expr = ExpressionFactory.createBinary(expr, operator, right, expr.getLine());
        }

        return expr;
    }

    /**
     * Grammar rule: factor → unary (("*" | "/" | "%") unary)*
     */
    private Expression factor() {
        Expression expr = unary();

        while (match(TokenType.STAR, TokenType.SLASH, TokenType.MOD)) {
            TokenType operator = previous().getType();
            Expression right = unary();
            expr = ExpressionFactory.createBinary(expr, operator, right, expr.getLine());
        }

        return expr;
    }

    /**
     * Grammar rule: unary → ("-") unary | primary
     */
    private Expression unary() {
        if (match(TokenType.MINUS)) {
            TokenType operator = previous().getType();
            Expression right = unary();
            return ExpressionFactory.createUnary(operator, right, right.getLine());
        }

        return primary();
    }

    /**
     * Grammar rule: primary → NUMBER | IDENTIFIER | "(" expression ")"
     */
    private Expression primary() {
        if (match(TokenType.NUMBER)) {
            return ExpressionFactory.createNumberLiteral(previous().getValue(), previous().getLine());
        }

        if (match(TokenType.IDENTIFIER)) {
            return ExpressionFactory.createVariable(previous().getValue(), previous().getLine());
        }

        if (match(TokenType.LPAREN)) {
            Expression expr = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return ExpressionFactory.createGroup(expr, expr.getLine());
        }

        throw error(peek(), "Expected expression.");
    }

    // Helper methods for parsing

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        // Report the error
        System.err.println("[line " + token.getLine() + "] Error at '" + 
                          token.getValue() + "': " + message);
        return new ParseError();
    }

    private static class ParseError extends RuntimeException {}
}
