import java.util.List;

/**
 * Factory class for creating Statement nodes in the AST.
 * This class centralizes the decision logic for creating different types of statements.
 */
public class StatementFactory {
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Statement create(StatementType type, Object... args) {
        switch (type) {
            case VAR_DECLARATION:
                return createVarDeclarationImpl((List<VarDeclarator>)args[0], toInt(args[1]));
            case VAR_ASSIGNMENT:
                return createVarAssignmentImpl((String)args[0], (Expression)args[1], toInt(args[2]));
            case PRINT:
                return createPrintImpl((Expression)args[0], toInt(args[1]));
            case EXPRESSION:
                return createExpressionImpl((Expression)args[0], toInt(args[1]));
            case FUNCTION:
                return createFunctionImpl((String)args[0], (List<String>)args[1], (List<Statement>)args[2], toInt(args[3]));
            case IF:
                return createIfImpl((Expression)args[0], (List<Statement>)args[1], 
                               (List<Expression>)args[2], (List<List<Statement>>)args[3], 
                               (List<Statement>)args[4], toInt(args[5]));
            case WHILE:
                return createWhileImpl((Expression)args[0], (List<Statement>)args[1], toInt(args[2]));
            case RUN:
                return createRunImpl((List<Statement>)args[0], (Expression)args[1], toInt(args[2]));
            case RETURN:
                return createReturnImpl((Expression)args[0], toInt(args[1]));
            default:
                throw new IllegalArgumentException("Unknown statement type: " + type);
        }
    }
    
    /**
     * Helper method to safely convert various types to an integer
     */
    private static int toInt(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                return 0; // Default line number
            }
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return 0; // Default if conversion is not possible
    }
    
    // Private implementation methods that actually create objects
    
    private static Statement createVarDeclarationImpl(List<VarDeclarator> declarators, int line) {
        return new VarDeclarationStatement(declarators, line);
    }
    
    private static Statement createVarAssignmentImpl(String name, Expression value, int line) {
        return new VarAssignmentStatement(name, value, line);
    }
    
    private static Statement createPrintImpl(Expression value, int line) {
        return new PrintStatement(value, line);
    }
    
    private static Statement createExpressionImpl(Expression expr, int line) {
        return new ExpressionStatement(expr, line);
    }
    
    private static Statement createFunctionImpl(String name, List<String> parameters, List<Statement> body, int line) {
        return new FunctionDeclarationStatement(name, parameters, body, line);
    }
    
    private static Statement createIfImpl(Expression condition, List<Statement> thenBranch, 
                                    List<Expression> elifConditions, List<List<Statement>> elifBranches,
                                    List<Statement> elseBranch, int line) {
        return new IfStatement(condition, thenBranch, elifConditions, elifBranches, elseBranch, line);
    }
    
    private static Statement createWhileImpl(Expression condition, List<Statement> body, int line) {
        return new WhileStatement(condition, body, line);
    }
    
    private static Statement createRunImpl(List<Statement> body, Expression condition, int line) {
        return new RunStatement(body, condition, line);
    }
    
    private static Statement createReturnImpl(Expression value, int line) {
        return new ReturnStatement(value, line);
    }
    
    // Public convenience methods
    
    /**
     * Creates a variable declaration statement.
     */
    public static Statement createVarDeclaration(List<VarDeclarator> declarators, int line) {
        return create(StatementType.VAR_DECLARATION, declarators, line);
    }
    
    /**
     * Creates a variable assignment statement.
     */
    public static Statement createVarAssignment(String name, Expression value, int line) {
        return create(StatementType.VAR_ASSIGNMENT, name, value, line);
    }
    
    /**
     * Creates a print statement.
     */
    public static Statement createPrint(Expression value, int line) {
        return create(StatementType.PRINT, value, line);
    }
    
    /**
     * Creates an expression statement.
     */
    public static Statement createExpression(Expression expr, int line) {
        return create(StatementType.EXPRESSION, expr, line);
    }
    
    /**
     * Creates a function declaration statement.
     */
    public static Statement createFunction(String name, List<String> parameters, List<Statement> body, int line) {
        return create(StatementType.FUNCTION, name, parameters, body, line);
    }
    
    /**
     * Creates an if statement.
     */
    public static Statement createIf(Expression condition, List<Statement> thenBranch, 
                                    List<Expression> elifConditions, List<List<Statement>> elifBranches,
                                    List<Statement> elseBranch, int line) {
        return create(StatementType.IF, condition, thenBranch, elifConditions, elifBranches, elseBranch, line);
    }
    
    /**
     * Creates a while statement.
     */
    public static Statement createWhile(Expression condition, List<Statement> body, int line) {
        return create(StatementType.WHILE, condition, body, line);
    }
    
    /**
     * Creates a run statement (do-while loop).
     */
    public static Statement createRun(List<Statement> body, Expression condition, int line) {
        return create(StatementType.RUN, body, condition, line);
    }
    
    /**
     * Creates a return statement.
     */
    public static Statement createReturn(Expression value, int line) {
        return create(StatementType.RETURN, value, line);
    }
}
