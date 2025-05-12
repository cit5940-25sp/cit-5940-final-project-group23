import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ParserTest {

    // Helper methods
    private List<Token> tokenize(String input) {
        Lexer lexer = new Lexer(input);
        return lexer.tokenize();
    }
    
    private Statement parse(String input) {
        List<Token> tokens = tokenize(input);
        Parser parser = new Parser(tokens);
        return parser.parse();
    }
    
    private Expression parseExpression(String input) {
        Statement stmt = parse(input);
        if (stmt instanceof ExpressionStatement) {
            return ((ExpressionStatement) stmt).getExpression();
        }
        fail("Expected an expression statement, got: " + stmt.getClass().getSimpleName());
        return null;
    }
    
    // Basic expression component tests
    
    @Test
    public void testPrimary_Number() {
        Expression expr = parseExpression("42;");
        assertTrue("Should be a literal expression", expr instanceof LiteralExpression);
        assertEquals(42, ((LiteralExpression) expr).getValue());
    }
    

    
    @Test
    public void testPrimary_Group() {
        Expression expr = parseExpression("(42);");
        assertTrue("Should be a group expression", expr instanceof GroupExpression);
        Expression inner = ((GroupExpression) expr).getExpression();
        assertTrue("Inner should be literal", inner instanceof LiteralExpression);
        assertEquals(42, ((LiteralExpression) inner).getValue());
    }
    
    @Test
    public void testUnary() {
        Expression expr = parseExpression("-42;");
        assertTrue("Should be a unary expression", expr instanceof UnaryExpression);
        UnaryExpression unary = (UnaryExpression) expr;
        assertEquals(TokenType.MINUS, unary.getOperator());
        assertTrue("Operand should be literal", unary.getOperand() instanceof LiteralExpression);
    }
    
    @Test
    public void testFactor() {
        Expression expr = parseExpression("2 * 3;");
        assertTrue("Should be a binary expression", expr instanceof BinaryExpression);
        BinaryExpression binary = (BinaryExpression) expr;
        assertEquals(TokenType.STAR, binary.getOperator());
    }
    
    @Test
    public void testTerm() {
        Expression expr = parseExpression("2 + 3;");
        assertTrue("Should be a binary expression", expr instanceof BinaryExpression);
        BinaryExpression binary = (BinaryExpression) expr;
        assertEquals(TokenType.PLUS, binary.getOperator());
    }
    
    @Test
    public void testComparison() {
        Expression expr = parseExpression("2 < 3;");
        assertTrue("Should be a binary expression", expr instanceof BinaryExpression);
        BinaryExpression binary = (BinaryExpression) expr;
        assertEquals(TokenType.LT, binary.getOperator());
    }
    
    @Test
    public void testEquality() {
        Expression expr = parseExpression("2 = 3;");
        assertTrue("Should be a binary expression", expr instanceof BinaryExpression);
        BinaryExpression binary = (BinaryExpression) expr;
        assertEquals(TokenType.EQ, binary.getOperator());
    }
    
    // Complex expression tests
    
    @Test
    public void testComplexExpression() {
        // (2 + 3) * 4 - -5
        Expression expr = parseExpression("(2 + 3) * 4 - -5;");
        assertTrue("Should be a binary expression", expr instanceof BinaryExpression);
        BinaryExpression binary = (BinaryExpression) expr;
        assertEquals(TokenType.MINUS, binary.getOperator());
        
        assertTrue("Left should be binary", binary.getLeft() instanceof BinaryExpression);
        BinaryExpression left = (BinaryExpression) binary.getLeft();
        assertEquals(TokenType.STAR, left.getOperator());
        
        assertTrue("Left of * should be group", left.getLeft() instanceof GroupExpression);
        
        assertTrue("Right should be unary", binary.getRight() instanceof UnaryExpression);
    }
    
    // Statement tests
    
    @Test
    public void testVarDeclaration() {
        Statement stmt = parse("var x <- 42;");
        assertTrue("Should be a var declaration", stmt instanceof VarDeclarationStatement);
        VarDeclarationStatement decl = (VarDeclarationStatement) stmt;
        
        // Get the list of declarators
        List<VarDeclarator> declarators = decl.getDeclarators();
        
        // Check that we have exactly one declarator
        assertEquals("Should have 1 declarator", 1, declarators.size());
        
        // Check the declarator's name and initializer
        VarDeclarator declarator = declarators.get(0);
        assertEquals("x", declarator.getName());
        assertTrue("Initializer should be literal", declarator.getInitializer() instanceof LiteralExpression);
        assertEquals(42, ((LiteralExpression) declarator.getInitializer()).getValue());
    }
    
    @Test
    public void testVarAssignmentError() {
        // This should fail because x hasn't been declared
        try {
            parse("x <- 42;");
            fail("Should have thrown an error for undeclared variable");
        } catch (Exception e) {
            assertTrue("Should report appropriate error", 
                       e.getMessage().contains("undeclared") || 
                       e instanceof Parser.ParseError);
        }
    }
    
  
    
    @Test
    public void testVarDeclarationAndAssignment() {
        // Test both statements in one parse operation
        List<Token> tokens = tokenize("var x <- 10; x <- 42;");
        Parser parser = new Parser(tokens);
        
        // You'd need a way to get both statements or implement a Program class
        // For now, we'll just verify it doesn't throw an error
        Statement stmt = parser.parse();
        assertNotNull("Should parse successfully", stmt);
    }
    
    
    
    @Test
    public void testComplexAssignment() {
        // First declare the variable
        List<Token> declTokens = tokenize("var result <- 0; result <- (2 + 3) * 4");
        Parser parser = new Parser(declTokens);
        parser.parse();



    }
    
    @Test
    public void testMultipleVarDeclaration() {
        Statement stmt = parse("var price <- 10, tax <- 8, discount <- -3;");
        assertTrue("Should be a var declaration", stmt instanceof VarDeclarationStatement);
        
        VarDeclarationStatement decl = (VarDeclarationStatement) stmt;
        List<VarDeclarator> declarators = decl.getDeclarators();
        
        assertEquals("Should have 3 declarators", 3, declarators.size());
        
        // Check first variable
        assertEquals("price", declarators.get(0).getName());
        assertEquals(10, ((LiteralExpression) declarators.get(0).getInitializer()).getValue());
        
        // Check second variable
        assertEquals("tax", declarators.get(1).getName());
        assertEquals(8, ((LiteralExpression) declarators.get(1).getInitializer()).getValue());
        
        // Check third variable
        assertEquals("discount", declarators.get(2).getName());
        assertTrue(declarators.get(2).getInitializer() instanceof UnaryExpression);
    }
    
    // File parsing tests
    
    @Test
    public void testValidFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("files/builtins.txt")));
            List<Token> tokens = tokenize(content);
            
            // Check tokens are correctly lexed
            assertFalse("Should have tokens", tokens.isEmpty());
            assertEquals("Last token should be EOF", TokenType.EOF, tokens.get(tokens.size()-1).getType());
            
            // Parse the tokens
            Parser parser = new Parser(tokens);
            Statement stmt = parser.parse();
            
            assertNotNull("Should successfully parse", stmt);
            
        } catch (IOException e) {
            fail("Failed to read test file: " + e.getMessage());
        }
    }
    
    @Test
    public void testSyntaxErrorFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("files/syntax_error2.txt")));
            List<Token> tokens = tokenize(content);
            
            // Check tokens are correctly lexed
            assertFalse("Should have tokens", tokens.isEmpty());
            
            // Should detect missing closing brace
            try {
                Parser parser = new Parser(tokens);
                parser.parse();
                fail("Should have thrown a parse error for missing closing brace");
            } catch (Exception e) {
                // Expected exception
                assertTrue("Should report appropriate error", e.getMessage().contains("Expected") || 
                                                            e instanceof Parser.ParseError);
            }
            
        } catch (IOException e) {
            fail("Failed to read test file: " + e.getMessage());
        }
    }
    
    // Error reporting tests
    
    @Test
    public void testMissingClosingParen() {
        try {
            parseExpression("(2 + 3");
            fail("Should have thrown a parse error");
        } catch (Exception e) {
            // Expected - should report missing closing parenthesis
            assertTrue(true);
        }
    }
    
    @Test
    public void testInvalidAssignmentTarget() {
        try {
            parse("5 <- 10");
            fail("Should have thrown a parse error for invalid assignment target");
        } catch (Exception e) {
            // Expected - should report invalid assignment target
            assertTrue(true);
        }
    }
    
    @Test
    public void testEmptyExpression() {
        try {
            parseExpression("");
            fail("Should have thrown a parse error for empty expression");
        } catch (Exception e) {
            // Expected - should report empty expression
            assertTrue(true);
        }
    }
    
    @Test
    public void testNestedFunctionCall() {

        Expression expr = parseExpression("abs(-42);");
        assertTrue("Should be a call expression", expr instanceof CallExpression);
        
        CallExpression call = (CallExpression) expr;
        assertEquals("abs", call.getCallee());
        assertEquals(1, call.getArguments().size());
        assertTrue("Argument should be unary expression", call.getArguments().get(0) instanceof UnaryExpression);
    }

    @Test
    public void testFunctionCallAsArgument() {
        // Note: This needs to be parsed as an expression statement to test print
        Statement stmt = parse("min(abs(-42));");
        assertTrue("Should be an expression statement", stmt instanceof ExpressionStatement);
        
        Expression expr = ((ExpressionStatement) stmt).getExpression();
        assertTrue("Should be a call expression", expr instanceof CallExpression);
        
        CallExpression printCall = (CallExpression) expr;
        assertEquals("min", printCall.getCallee());
        assertEquals(1, printCall.getArguments().size());
        
        Expression arg = printCall.getArguments().get(0);
        assertTrue("Argument should be call expression", arg instanceof CallExpression);
        
        CallExpression absCall = (CallExpression) arg;
        assertEquals("abs", absCall.getCallee());
    }
}
