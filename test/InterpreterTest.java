import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class InterpreterTest {
    private Interpreter interpreter;

    @Before
    public void setUp() {
        interpreter = new Interpreter();
    }

    @Test
    public void testArithmetic() {
        Expression plus = new BinaryExpression(
                new LiteralExpression(5, 1), TokenType.PLUS, new LiteralExpression(3, 1), 1);
        assertEquals(8, plus.accept(interpreter));
        Expression mod = new BinaryExpression(
                new LiteralExpression(10, 1), TokenType.MOD, new LiteralExpression(3, 1), 10);
        assertEquals(1, mod.accept(interpreter));
    }

    @Test
    public void testRational() {
        Expression eq = new BinaryExpression(
                new LiteralExpression(5, 1), TokenType.EQ, new LiteralExpression(5, 1), 1);
        Expression ne = new BinaryExpression(
                new LiteralExpression(5, 1), TokenType.NE, new LiteralExpression(3, 1), 1);
        Expression eqFalse = new BinaryExpression(
                new LiteralExpression(5, 1), TokenType.EQ, new LiteralExpression(2, 1), 1);
        Expression neFalse = new BinaryExpression(
                new LiteralExpression(5, 1), TokenType.NE, new LiteralExpression(5, 1), 1);
        Expression lt = new BinaryExpression(
                new LiteralExpression(3, 1), TokenType.LT, new LiteralExpression(5, 1), 1);
        Expression le = new BinaryExpression(
                new LiteralExpression(5, 1), TokenType.LE, new LiteralExpression(5, 1), 1);
        Expression gt = new BinaryExpression(
                new LiteralExpression(6, 1), TokenType.GT, new LiteralExpression(3, 1), 1);
        Expression ge = new BinaryExpression(
                new LiteralExpression(7, 1), TokenType.GE, new LiteralExpression(7, 1), 1);

        assertEquals(1, eq.accept(interpreter));
        assertEquals(1, ne.accept(interpreter));
        assertEquals(0, eqFalse.accept(interpreter));
        assertEquals(0, neFalse.accept(interpreter));
        assertEquals(1, lt.accept(interpreter));
        assertEquals(1, le.accept(interpreter));
        assertEquals(1, gt.accept(interpreter));
        assertEquals(1, ge.accept(interpreter));
    }


    @Test
    public void testUnaryExpression() {
        Expression expr = new UnaryExpression(TokenType.MINUS, new LiteralExpression(3, 1), 1);
        assertEquals(-3, expr.accept(interpreter));
    }

    @Test
    public void testLiteralExpression() {
        Expression expr = new LiteralExpression(10, 1);
        assertEquals(10, expr.accept(interpreter));
    }

    @Test
    public void testGroupedExpression() {
        // 5 *(3+2)
        Expression grouped = new GroupExpression(
                new BinaryExpression(
                        new LiteralExpression(3, 1),
                        TokenType.PLUS,
                        new LiteralExpression(2, 1),
                        1
                ),
                1
        );

        Expression expr = new BinaryExpression(
                new LiteralExpression(5, 1),
                TokenType.STAR,
                grouped,
                1
        );

        assertEquals(25, expr.accept(interpreter));
    }


    @Test
    public void testAssignmentExpression() {
        interpreter.enterScope();
        Expression assign = new AssignmentExpression("x", new LiteralExpression(20, 1), 1);
        assign.accept(interpreter);
        Expression variable = new VariableExpression("x", 1);
        assertEquals(20, variable.accept(interpreter));
        interpreter.exitScope();
    }

    @Test
    public void testBuiltinFunctionPrint() {
        Expression call = new CallExpression(
                new VariableExpression("print", 1),
                Arrays.asList(new LiteralExpression(1130, 1)),
                1
        );
        assertEquals(1130, call.accept(interpreter));
    }
}