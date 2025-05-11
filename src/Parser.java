import java.util.ArrayList;
import java.util.List;

/**
 * Parser that converts tokens into an abstract syntax tree.
 * This is a recursive descent parser with symbol table validation.
 */
public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private final SymbolTable symbolTable;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.symbolTable = new SymbolTable();
        this.current = 0;
    }

    /**
     * Parse the tokens into a statement.
     * @throws ParseError if there is a syntax error
     */
    public Statement parse() throws ParseError {
        // let the error propagate
        return statement();
    }

    /**
     * Grammar rule: statement → varDeclaration | varAssignment | expressionStmt
     */
    private Statement statement() {
        if (match(TokenType.VAR)) {
            return varDeclaration();
        }
        
        // Check for assignments
        if (check(TokenType.NUMBER) && checkAhead(1, TokenType.ASSIGN)) {
            // This is an invalid assignment - don't consume tokens, just report error
            throw error(peek(), "Left side of assignment must be a variable name");
        }
        
        if (match(TokenType.IDENTIFIER) && check(TokenType.ASSIGN)) {
            current--; // Go back to the identifier
            return varAssignment();
        }

        return expressionStatement();
    }

    // Add this helper method
    private boolean checkAhead(int distance, TokenType type) {
        if (current + distance >= tokens.size()) return false;
        return tokens.get(current + distance).getType() == type;
    }

    /**
     * Grammar rule: varDeclaration → "var" varDeclarator ("," varDeclarator)* ";"
     * varDeclarator → IDENTIFIER "<-" expression
     */
    private Statement varDeclaration() {
        // Get the starting line number for the declaration
        int line = previous().getLine();
        
        // Parse the first variable declarator
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name after 'var'.");
        
        // Check if variable is already defined in this scope
        if (symbolTable.isDefined(name.getValue())) {
            throw error(name, "Variable '" + name.getValue() + "' already declared in this scope.");
        }
        
        consume(TokenType.ASSIGN, "Expect '<-' after variable name in declaration.");
        Expression initializer = expression();
        
        // Register the variable in the symbol table
        symbolTable.define(name.getValue());
        
        // Create a list to hold all the declarations
        List<VarDeclarator> declarators = new ArrayList<>();
        declarators.add(new VarDeclarator(name.getValue(), initializer));
        
        // Check for additional declarators
        while (match(TokenType.COMMA)) {
            // Parse the next variable declarator
            name = consume(TokenType.IDENTIFIER, "Expect variable name after ','.");
            
            // Check if variable is already defined in this scope
            if (symbolTable.isDefined(name.getValue())) {
                throw error(name, "Variable '" + name.getValue() + "' already declared in this scope.");
            }
            
            consume(TokenType.ASSIGN, "Expect '<-' after variable name in declaration.");
            initializer = expression();
            
            // Register the variable in the symbol table
            symbolTable.define(name.getValue());
            
            // Add to the list of declarators
            declarators.add(new VarDeclarator(name.getValue(), initializer));
        }
        
        return new VarDeclarationStatement(declarators, line);
    }

    /**
     * Grammar rule: varAssignment → IDENTIFIER "<-" expression ";"
     */
    private Statement varAssignment() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");
        
        // Check if variable exists before allowing assignment
        if (!symbolTable.isDefined(name.getValue())) {
            throw error(name, "Cannot assign to undeclared variable '" + name.getValue() + "'.");
        }
        
        consume(TokenType.ASSIGN, "Expect '<-' after variable name.");
        Expression value = expression();

        return new VarAssignmentStatement(name.getValue(), value, name.getLine());
    }

    /**
     * Grammar rule: expressionStmt → expression ";"
     */
    private Statement expressionStatement() {
        Expression expr = expression();
        return new ExpressionStatement(expr, expr.getLine());
    }

    /**
     * Grammar rule: expression → equality
     * this is the starting point of the expression parsing
     */
    private Expression expression() {
        return equality();
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

    // if there is a matching type, then it consumes it and return true
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    // if there is a matching type it consumes and return's the token
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    // check the current token type without consuming it 
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    // returns the consumed token and moving the current token pointer to next token
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

    /**
     * Helper method for error reporting
     */
    private ParseError error(Token token, String message) {
        System.err.println("[line " + token.getLine() + "] Error at '" + 
                          token.getValue() + "': " + message);
        return new ParseError(message);
    }

    /**
     * Error class for parse errors
     */
    public static class ParseError extends RuntimeException {
        public ParseError(String message) {
            super(message);
        }
        
        public ParseError() {
            super();
        }
    }
}
