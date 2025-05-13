import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class StatementFactoryTest {


    
    @Test
    public void testCreatePrint() {
        Expression value = ExpressionFactory.createLiteral("Hello", 1);
        Statement stmt = StatementFactory.createPrint(value, 1);
        assertTrue("Should be a print statement", stmt instanceof PrintStatement);

    }
    
    @Test
    public void testCreateExpression() {
        Expression expr = ExpressionFactory.createBinary(
            ExpressionFactory.createLiteral(5, 1),
            TokenType.PLUS,
            ExpressionFactory.createLiteral(3, 1),
            1
        );
        
        Statement stmt = StatementFactory.createExpression(expr, 1);
        assertTrue("Should be an expression statement", stmt instanceof ExpressionStatement);
        
        ExpressionStatement exprStmt = (ExpressionStatement) stmt;
        assertEquals("Should have correct expression", expr, exprStmt.getExpression());
    }
    
    @Test
    public void testCreateFunction() {
        List<String> parameters = new ArrayList<>();
        parameters.add("x");
        parameters.add("y");
        
        List<Statement> body = new ArrayList<>();
        body.add(StatementFactory.createReturn(
            ExpressionFactory.createBinary(
                ExpressionFactory.createVariable("x", 1),
                TokenType.PLUS,
                ExpressionFactory.createVariable("y", 1),
                1
            ),
            1
        ));
        
        Statement stmt = StatementFactory.createFunction("add", parameters, body, 1);
        assertTrue("Should be a function declaration", stmt instanceof FunctionDeclarationStatement);
        
        FunctionDeclarationStatement func = (FunctionDeclarationStatement) stmt;
        assertEquals("Should have correct name", "add", func.getName());
        assertEquals("Should have correct number of parameters", 2, func.getParameters().size());
        assertEquals("Should have correct body size", 1, func.getBody().size());
    }

    

    @Test
    public void testCreateReturn() {
        Expression value = ExpressionFactory.createLiteral(42, 1);
        Statement stmt = StatementFactory.createReturn(value, 1);
        assertTrue("Should be a return statement", stmt instanceof ReturnStatement);
        
        ReturnStatement returnStmt = (ReturnStatement) stmt;
        assertEquals("Should have correct value", value, returnStmt.getValue());
    }
    
    @Test
    public void testToIntConversions() {
        // Test integer conversion through the public create method
        
        // Test with integer
        Statement stmt1 = StatementFactory.create(StatementType.RETURN, ExpressionFactory.createLiteral(1, 1), 42);
        assertEquals("Line number should be 42", 42, stmt1.getLine());
        
        // Test with number (should truncate)
        Statement stmt2 = StatementFactory.create(StatementType.RETURN, ExpressionFactory.createLiteral(1, 1), 99);
        assertEquals("Line number should be 99", 99, stmt2.getLine());
        
        // Test with null (should default to 0)
        Statement stmt3 = StatementFactory.create(StatementType.RETURN, ExpressionFactory.createLiteral(1, 1), null);
        assertEquals("Line number from null should be 0", 0, stmt3.getLine());
    }
}