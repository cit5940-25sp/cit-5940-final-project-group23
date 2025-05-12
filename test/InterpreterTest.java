import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InterpreterTest extends Interpreter {
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
    public void testPrintStatement() {
        Interpreter interpreter = new Interpreter();
        Statement stmt = new PrintStatement(new LiteralExpression(99, 1), 1);
        stmt.accept(interpreter); // prints 99
    }

    @Test
    public void testVarDeclarationStatement() {
        Interpreter interpreter = new Interpreter();
        interpreter.enterScope();
        List<VarDeclarator> decls = List.of(new VarDeclarator("a", new LiteralExpression(7, 1)));
        Statement stmt = new VarDeclarationStatement(decls, 1);
        stmt.accept(interpreter);
        assertEquals(7, interpreter.environment.lookup("a"));
        interpreter.exitScope();
    }

    @Test
    public void testIfStatement() {
        Interpreter interpreter = new Interpreter();
        interpreter.enterScope();

        // Declare variable first
        interpreter.environment.declare("flag", 0);

        Expression condition = new LiteralExpression(1, 1);
        Statement body = new VarAssignmentStatement("flag", new LiteralExpression(1, 1), 1);
        IfStatement ifStmt = new IfStatement(
                condition,
                List.of(body),
                List.of(),
                List.of(),
                List.of(),
                1
        );
        ifStmt.accept(interpreter);

        assertEquals(1, interpreter.environment.lookup("flag"));
        interpreter.exitScope();
    }


    @Test
    public void testWhileStatement() {
        Interpreter interpreter = new Interpreter();
        interpreter.enterScope();

        interpreter.environment.declare("x", 0);

        Expression condition = new BinaryExpression(
                new VariableExpression("x", 1),
                TokenType.LT,
                new LiteralExpression(3, 1),
                1
        );

        Statement bodyStmt = new VarAssignmentStatement(
                "x",
                new BinaryExpression(
                        new VariableExpression("x", 1),
                        TokenType.PLUS,
                        new LiteralExpression(1, 1),
                        1
                ),
                1
        );

        WhileStatement whileStmt = new WhileStatement(condition, List.of(bodyStmt), 1);
        whileStmt.accept(interpreter);

        assertEquals(3, interpreter.environment.lookup("x"));
        interpreter.exitScope();
    }

    @Test
    public void testReturnStatement() {
        Interpreter interpreter = new Interpreter();

        ReturnStatement stmt = new ReturnStatement(new LiteralExpression(5, 1), 1);
        Object result = stmt.accept(interpreter);

        assertEquals(5, result);
    }
}