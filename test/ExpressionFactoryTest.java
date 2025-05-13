import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ExpressionFactoryTest {

    @Test
    public void testCreateBinary() {
        // Create components for the binary expression
        Expression left = ExpressionFactory.createLiteral(5, 1);
        Expression right = ExpressionFactory.createLiteral(10, 1);
        
        // Create the binary expression
        Expression expr = ExpressionFactory.createBinary(left, TokenType.PLUS, right, 1);
        
        // Verify the correct type is created
        assertTrue("Should be a binary expression", expr instanceof BinaryExpression);
        
        // Verify the properties
        BinaryExpression binary = (BinaryExpression) expr;
        assertEquals("Left expression should match", left, binary.getLeft());
        assertEquals("Operator should match", TokenType.PLUS, binary.getOperator());
        assertEquals("Right expression should match", right, binary.getRight());
    }
    
    @Test
    public void testCreateUnary() {
        // Create operand for the unary expression
        Expression operand = ExpressionFactory.createLiteral(5, 1);
        
        // Create the unary expression
        Expression expr = ExpressionFactory.createUnary(TokenType.MINUS, operand, 1);
        
        // Verify the correct type is created
        assertTrue("Should be a unary expression", expr instanceof UnaryExpression);
        
        // Verify the properties
        UnaryExpression unary = (UnaryExpression) expr;
        assertEquals("Operator should match", TokenType.MINUS, unary.getOperator());
        assertEquals("Operand should match", operand, unary.getOperand());
    }
    
    @Test
    public void testCreateLiteral() {
        // Test numeric literal
        Expression expr1 = ExpressionFactory.createLiteral(42, 1);
        assertTrue("Should be a literal expression", expr1 instanceof LiteralExpression);
        assertEquals("Value should match", 42, ((LiteralExpression) expr1).getValue());
        
        // Test string literal
        Expression expr2 = ExpressionFactory.createLiteral("hello", 1);
        assertTrue("Should be a literal expression", expr2 instanceof LiteralExpression);
        assertEquals("Value should match", "hello", ((LiteralExpression) expr2).getValue());
        
        // Test numeric string (should be converted to int)
        Expression expr3 = ExpressionFactory.createLiteral("123", 1);
        assertTrue("Should be a literal expression", expr3 instanceof LiteralExpression);
        assertEquals("String number should be converted to int", 123, ((LiteralExpression) expr3).getValue());
    }
    
    
    
    @Test
    public void testCreateGroup() {
        Expression inner = ExpressionFactory.createLiteral(42, 1);
        Expression expr = ExpressionFactory.createGroup(inner, 1);
        assertTrue("Should be a group expression", expr instanceof GroupExpression);
        assertEquals("Inner expression should match", inner, ((GroupExpression) expr).getExpression());
    }
    
    @Test
    public void testCreateCall() {
        // Create arguments for the function call
        List<Expression> arguments = new ArrayList<>();
        arguments.add(ExpressionFactory.createLiteral(42, 1));
        arguments.add(ExpressionFactory.createVariable("x", 1));
        
        // Create the call expression
        Expression expr = ExpressionFactory.createCall("max", arguments, 1);
        assertTrue("Should be a call expression", expr instanceof CallExpression);
        
        // Verify the properties
        CallExpression call = (CallExpression) expr;
        assertEquals("Function name should match", "max", call.getCallee());
        assertEquals("Arguments list should match", arguments, call.getArguments());
    }
    
    @Test
    public void testCreateInputExpression() {
        Expression expr = ExpressionFactory.createInput(1);
        assertTrue("Should be an input expression", expr instanceof InputExpression);
    }
    
    @Test
    public void testInputCallRecognition() {
        // Test that a function call to "input" creates an InputExpression
        List<Expression> arguments = new ArrayList<>();
        Expression expr = ExpressionFactory.createCall("input", arguments, 1);
        assertTrue("Call to 'input' should create an InputExpression", expr instanceof InputExpression);
    }
     @Test
    public void testToIntConversions() {
        // Test integer conversion through the public create method
        
        // Test with integer
        Expression expr1 = ExpressionFactory.create(ExpressionType.LITERAL, "test", 42);
        assertEquals("Line number should be 42", 42, expr1.getLine());
        
        // Test with string containing a number (should convert to that number)
        Expression expr2 = ExpressionFactory.create(ExpressionType.LITERAL, "test", "123");
        assertEquals("Line number from numeric string should be 123", 123, expr2.getLine());
        
        // Test with null (should default to 0)
        Expression expr3 = ExpressionFactory.create(ExpressionType.LITERAL, "test", null);
        assertEquals("Line number from null should be 0", 0, expr3.getLine());
        
        // Test with non-numeric string (should default to 0)
        Expression expr4 = ExpressionFactory.create(ExpressionType.LITERAL, "test", "not-a-number");
        assertEquals("Line number from non-numeric string should be 0", 0, expr4.getLine());
        
        // Test with double (should truncate)
        Expression expr5 = ExpressionFactory.create(ExpressionType.LITERAL, "test", 99.9);
        assertEquals("Line number from double should be truncated to 99", 99, expr5.getLine());
    }
    
}