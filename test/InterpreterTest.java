import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InterpreterTest extends Interpreter {
    private Interpreter interpreter;
    private ByteArrayOutputStream outContent;
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        interpreter = new Interpreter();
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        interpreter.exitScope();
    }

    @Test
    public void testScopeShadowing() {
        // Outer scope
        interpreter.environment.declare("a", 1);
        assertEquals(1, interpreter.environment.lookup("a"));

        // Enter inner scope and shadow 'a'
        interpreter.enterScope();
        interpreter.environment.declare("a", 2);
        assertEquals(2, interpreter.environment.lookup("a"));

        // Exit inner scope, original 'a' should be restored
        interpreter.exitScope();
        assertEquals(1, interpreter.environment.lookup("a"));
    }

    @Test(expected = RuntimeException.class)
    public void testExitRemovesInnerVariable() {
        // Enter a new scope
        interpreter.enterScope();
        // Declare 'x' only in the inner scope
        interpreter.environment.declare("x", 5);
        assertEquals(5, interpreter.environment.lookup("x"));

        // Exit the inner scope; lookup should now fail
        interpreter.exitScope();
        interpreter.environment.lookup("x"); // should throw RuntimeException
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
    public void testVarAssignmentStatement() {
        interpreter.environment.declare("z", 0);
        Statement stmt = new VarAssignmentStatement(
                "z",
                new LiteralExpression(77,1),
                1
        );
        stmt.accept(interpreter);
        assertEquals(77, interpreter.environment.lookup("z"));
    }

    @Test
    public void testExpressionStatement() {
        Expression expr = new BinaryExpression(
                new LiteralExpression(8,1),
                TokenType.PLUS,
                new LiteralExpression(2,1),
                1
        );
        Statement stmt = new ExpressionStatement(expr, 1);
        assertEquals(10, stmt.accept(interpreter));
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
    public void testIfStatementElifBranch() {
        interpreter.environment.declare("b", 0);
        Expression cond = new LiteralExpression(0, 1);
        Expression elifCond = new LiteralExpression(1, 1);
        Statement elifStmt = new VarAssignmentStatement(
                "b",
                new LiteralExpression(9, 1),
                1
        );

        IfStatement ifStmt = new IfStatement(
                cond,
                List.of(),
                List.of(elifCond),
                List.of(List.of(elifStmt)),
                List.of(),
                1
        );

        ifStmt.accept(interpreter);
        assertEquals(9, interpreter.environment.lookup("b"));
    }



    @Test
    public void testIfStatementElseBranch() {
        interpreter.environment.declare("b", 0);
        Expression cond = new LiteralExpression(0,1);
        Statement elseStmt = new VarAssignmentStatement(
                "b",
                new LiteralExpression(9,1),
                1
        );
        IfStatement ifStmt = new IfStatement(
                cond,
                List.of(),
                List.of(), List.of(),
                List.of(elseStmt),
                1
        );
        ifStmt.accept(interpreter);
        assertEquals(9, interpreter.environment.lookup("b"));
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
    public void testRunStatement() {
        interpreter.environment.declare("n", 0);
        Statement body = new VarAssignmentStatement(
                "n",
                new LiteralExpression(2,1),
                1
        );
        RunStatement run = new RunStatement(List.of(body),
                new BinaryExpression(
                        new VariableExpression("n",1),
                        TokenType.LT,
                        new LiteralExpression(2,1),
                        1
                ),
                1
        );
        run.accept(interpreter);
        assertEquals(2, interpreter.environment.lookup("n"));
    }

    @Test(expected = Interpreter.Return.class)
    public void testVisitReturnStatementThrows() {
        Statement ret = new ReturnStatement(new LiteralExpression(123,1), 1);
        ret.accept(interpreter);
    }

    @Test
    public void testFunctionDeclarationAndCall() {
        FunctionDeclarationStatement fn = new FunctionDeclarationStatement(
                "inc",
                List.of("x"),
                List.of(new ReturnStatement(
                        new BinaryExpression(
                                new VariableExpression("x",1),
                                TokenType.PLUS,
                                new LiteralExpression(1,1),
                                1
                        ),
                        1
                )),
                1
        );
        fn.accept(interpreter);
        Object result = interpreter.callFunction("inc", List.of(5));
        assertEquals(6, result);
    }

    @Test
    public void testCallBuiltinsInput() {
        System.setIn(new ByteArrayInputStream("42\n".getBytes()));
        Expression in = new InputExpression(1);
        assertEquals(42, in.accept(interpreter));
    }
    
    @Test
    public void testCallBuiltinsPrint() {
        Expression call = new CallExpression(
                "print",
                List.of(new LiteralExpression(7,1)),
                1
        );
        Object r = call.accept(interpreter);
        assertEquals(7, r);
    }

    @Test
    public void testBuiltinAbs() {
        // positive
        Expression exprPos = new CallExpression("abs", List.of(new LiteralExpression(5,1)), 1);
        assertEquals(5, exprPos.accept(interpreter));
        // negative
        Expression exprNeg = new CallExpression("abs", List.of(new LiteralExpression(-7,1)), 1);
        assertEquals(7, exprNeg.accept(interpreter));
    }

    @Test
    public void testBuiltinMax() {
        Expression expr = new CallExpression(
                "max",
                List.of(new LiteralExpression(4,1), new LiteralExpression(9,1)),
                1
        );
        assertEquals(9, expr.accept(interpreter));

        // reversed args
        Expression expr2 = new CallExpression(
                "max",
                List.of(new LiteralExpression(12,1), new LiteralExpression(3,1)),
                1
        );
        assertEquals(12, expr2.accept(interpreter));
    }

    @Test
    public void testBuiltinMin() {
        Expression expr = new CallExpression(
                "min",
                List.of(new LiteralExpression(4,1), new LiteralExpression(9,1)),
                1
        );
        assertEquals(4, expr.accept(interpreter));

        Expression expr2 = new CallExpression(
                "min",
                List.of(new LiteralExpression(12,1), new LiteralExpression(3,1)),
                1
        );
        assertEquals(3, expr2.accept(interpreter));
    }


    //Edges Case
    @Test
    public void testBuiltinAbsZero() {
        Expression expr = new CallExpression("abs", List.of(new LiteralExpression(0, 1)), 1);
        assertEquals(0, expr.accept(interpreter));
    }

    @Test
    public void testBuiltinMaxEqualArgs() {
        Expression expr = new CallExpression(
                "max",
                List.of(new LiteralExpression(5, 1), new LiteralExpression(5, 1)),
                1
        );
        assertEquals(5, expr.accept(interpreter));
    }

    @Test
    public void testBuiltinMinEqualArgs() {
        Expression expr = new CallExpression(
                "min",
                List.of(new LiteralExpression(7, 1), new LiteralExpression(7, 1)),
                1
        );
        assertEquals(7, expr.accept(interpreter));
    }

    /** Calling an undefined function should throw */
    @Test(expected = RuntimeException.class)
    public void testUndefinedFunctionThrows() {
        new CallExpression("noSuchFunc", List.of(), 1).accept(interpreter);
    }

    /** Referencing an undeclared variable should throw */
    @Test(expected = RuntimeException.class)
    public void testVariableLookupError() {
        new VariableExpression("undefVar", 1).accept(interpreter);
    }

    /** Division by zero should throw ArithmeticException */
    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        Expression expr = new BinaryExpression(
                new LiteralExpression(5, 1),
                TokenType.SLASH,
                new LiteralExpression(0, 1),
                1
        );
        expr.accept(interpreter);
    }

}