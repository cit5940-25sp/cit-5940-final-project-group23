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

        System.out.println("TRACE: parse() called");
        // let the error propagate
        return statement();
    }

    /**
     * Grammar rule: statement → varDeclaration | functionDecl | varAssignment | ifStmt | whileStmt | runStmt | returnStmt | expressionStmt
     */
    private Statement statement() {
        System.out.println("TRACE: statement() called with token type: " + peek().getType());
        
        // Handle keyword statements
        if (match(TokenType.VAR)) {
            return varDeclaration();
        }
        
        if (match(TokenType.FUNCTION)) {
            return functionDeclaration();
        }
        
        if (match(TokenType.IF)) {
            return ifStatement();
        }
        
        if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        
        if (match(TokenType.RUN)) {
            return runStatement();
        }
        
        if (match(TokenType.RETURN)) {
            return returnStatement();
        }
        
        // Add explicit handling for print statements
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        
        // Assignment check - check without consuming first
        if (check(TokenType.IDENTIFIER)) {
            // Look ahead to see if this is an assignment
            if (checkAhead(1, TokenType.ASSIGN)) {
                // Only consume the identifier now that we know it's an assignment
                advance(); // Consume the identifier
                current--; // But go back to it for varAssignment() to handle
                return varAssignment();
            }
            // Otherwise, fall through to expressionStatement
        }
        
        // Invalid assignment target
        if (check(TokenType.NUMBER) && checkAhead(1, TokenType.ASSIGN)) {
            throw error(peek(), "Left side of assignment must be a variable name");
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
    * Grammar rule: printStmt → "print" expression
    */
    private Statement printStatement() {
        Token keyword = previous();
        
        // Print is followed directly by an expression
        Expression value = expression();
        
        return new PrintStatement(value, keyword.getLine());
    }


    /**
     * Grammar rule: expressionStmt → expression ";"
     */
    private Statement expressionStatement() {
        Expression expr = expression();
        System.out.println("TRACE: expressionStatement() called");
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
     * Grammar rule: primary → NUMBER | IDENTIFIER | "(" expression ")" | call
     * call → IDENTIFIER "(" arguments? ")"
     */
    private Expression primary() {
        System.out.println("DEBUG: primary() called with token type: " + peek().getType());
        if (match(TokenType.NUMBER)) {
            return ExpressionFactory.createNumberLiteral(previous().getValue(), previous().getLine());
        }
        
        //checking for input call
        if (match(TokenType.INPUT)) {
            return new InputExpression(previous().getLine());
        }
        
        if (match(TokenType.IDENTIFIER)) {
            Token token = previous();
            System.out.println("Found identifier: " + token.getValue());
            System.out.println("Next token type: " + peek().getType());
            System.out.println("Is LPAREN check: " + check(TokenType.LPAREN));
            
            
            // Check if this is a function call
            if (check(TokenType.LPAREN)) {
                return finishCall(token);
            }
            
            // Otherwise it's a variable reference
            return ExpressionFactory.createVariable(token.getValue(), token.getLine());
        }

        if (match(TokenType.LPAREN)) {
            Expression expr = expression();
            consume(TokenType.RPAREN, "Expect ')' after expression.");
            return ExpressionFactory.createGroup(expr, expr.getLine());
        }

        throw error(peek(), "Expected expression.");
    }

    /**
     * Parses the arguments of a function call.
     */
    private Expression finishCall(Token callee) {
        List<Expression> arguments = new ArrayList<>();
        System.out.println("Parsing function call: " + callee.getValue());
        // Parse the arguments
        consume(TokenType.LPAREN, "Expect '(' after function name.");
        if (!check(TokenType.RPAREN)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }
        
        Token paren = consume(TokenType.RPAREN, "Expect ')' after arguments.");
        
        return ExpressionFactory.createCall(callee.getValue(), arguments, callee.getLine());
    }

    /**
     * Grammar rule: runStmt → "run" block "while" "(" expression ")"
     * This is the do-while loop construct in your language
     */
    private Statement runStatement() {
        Token keyword = previous();
        
        // Parse the body (which executes at least once)
        consume(TokenType.LBRACE, "Expect '{' before run body.");
        List<Statement> body = block();
        
        // Parse the "while" condition that follows
        consume(TokenType.WHILE, "Expect 'while' after run body.");
        consume(TokenType.LPAREN, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after condition.");
        
        return new RunStatement(body, condition, keyword.getLine());
    }

    /**
     * Grammar rule: whileStmt → "while" "(" expression ")" block
     */
    private Statement whileStatement() {
        Token keyword = previous();
        
        consume(TokenType.LPAREN, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after while condition.");
        
        consume(TokenType.LBRACE, "Expect '{' before while body.");
        List<Statement> body = block();
        
        return new WhileStatement(condition, body, keyword.getLine());
    }

    /**
     * Grammar rule: functionDecl → "function" IDENTIFIER "(" parameters? ")" block
     * parameters → IDENTIFIER ("," IDENTIFIER)*
     */
    private Statement functionDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect function name.");
        
        consume(TokenType.LPAREN, "Expect '(' after function name.");
        
        List<String> parameters = new ArrayList<>();
        if (!check(TokenType.RPAREN)) {
            do {
                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name.").getValue());
            } while (match(TokenType.COMMA));
        }
        
        consume(TokenType.RPAREN, "Expect ')' after parameters.");
        
        // Parse function body (a block of statements)
        consume(TokenType.LBRACE, "Expect '{' before function body.");
        List<Statement> body = block();

        // Register the function in the symbol table
        symbolTable.defineFunction(name.getValue(), parameters.size());
        
        return new FunctionDeclarationStatement(name.getValue(), parameters, body, name.getLine());
    }

    /**
     * Grammar rule: ifStmt → "if" "(" expression ")" block ("elif" "(" expression ")" block)* ("else" block)?
     */
    private Statement ifStatement() {
        Token keyword = previous();
        
        consume(TokenType.LPAREN, "Expect '(' after 'if'.");
        Expression condition = expression();
        consume(TokenType.RPAREN, "Expect ')' after if condition.");
        
        consume(TokenType.LBRACE, "Expect '{' before if body.");
        List<Statement> thenBranch = block();
        
        List<Expression> elifConditions = new ArrayList<>();
        List<List<Statement>> elifBranches = new ArrayList<>();
        
        // Parse all elif branches
        while (match(TokenType.ELIF)) {
            consume(TokenType.LPAREN, "Expect '(' after 'elif'.");
            elifConditions.add(expression());
            consume(TokenType.RPAREN, "Expect ')' after elif condition.");
            
            consume(TokenType.LBRACE, "Expect '{' before elif body.");
            elifBranches.add(block());
        }
        
        // Parse optional else branch
        List<Statement> elseBranch = null;
        if (match(TokenType.ELSE)) {
            consume(TokenType.LBRACE, "Expect '{' before else body.");
            elseBranch = block();
        }
        
        return new IfStatement(condition, thenBranch, elifConditions, elifBranches, elseBranch, keyword.getLine());
    }

    /**
     * Grammar rule: returnStmt → "return" expression? ";"
     */
    private Statement returnStatement() {
        Token keyword = previous();
        Expression value = null;
        
        // Return can have an optional value
        if (!check(TokenType.SEMICOLON)) {
            value = expression();
        }
        
        return new ReturnStatement(value, keyword.getLine());
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
    
    

    /**
     * Parse a block of statements enclosed in braces.
     * Grammar rule: block → "{" statement* "}"
     */
    private List<Statement> block() {
        List<Statement> statements = new ArrayList<>();
        
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(statement());
        }
        
        consume(TokenType.RBRACE, "Expect '}' after block.");
        return statements;
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
