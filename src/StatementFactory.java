import java.util.List;

/**
 * Factory class for creating Statement nodes in the AST.
 * This class centralizes the decision logic for creating different types of statements.
 */
public class StatementFactory {
    
    /**
     * Create an appropriate statement based on the type.
     * This method centralizes the decision logic for statement creation.
     */
    public static Statement create(String statementType, Object... args) {
        switch (statementType) {
            case "varDeclaration":
                return createVarDeclaration((List<VarDeclarator>)args[0], (Integer)args[1]);
            case "varAssignment":
                return createVarAssignment((String)args[0], (Expression)args[1], (Integer)args[2]);
            case "print":
                return createPrint((Expression)args[0], (Integer)args[1]);
            case "expression":
                return createExpression((Expression)args[0], (Integer)args[1]);
            case "function":
                return createFunction((String)args[0], (List<String>)args[1], (List<Statement>)args[2], (Integer)args[3]);
            case "if":
                return createIf((Expression)args[0], (List<Statement>)args[1], 
                               (List<Expression>)args[2], (List<List<Statement>>)args[3], 
                               (List<Statement>)args[4], (Integer)args[5]);
            case "while":
                return createWhile((Expression)args[0], (List<Statement>)args[1], (Integer)args[2]);
            case "run":
                return createRun((List<Statement>)args[0], (Expression)args[1], (Integer)args[2]);
            case "return":
                return createReturn((Expression)args[0], (Integer)args[1]);
            default:
                throw new IllegalArgumentException("Unknown statement type: " + statementType);
        }
    }
    
    /**
     * Creates a variable declaration statement.
     */
    public static Statement createVarDeclaration(List<VarDeclarator> declarators, int line) {
        return new VarDeclarationStatement(declarators, line);
    }
    
    /**
     * Creates a variable assignment statement.
     */
    public static Statement createVarAssignment(String name, Expression value, int line) {
        return new VarAssignmentStatement(name, value, line);
    }
    
    /**
     * Creates a print statement.
     */
    public static Statement createPrint(Expression value, int line) {
        return new PrintStatement(value, line);
    }
    
    /**
     * Creates an expression statement.
     */
    public static Statement createExpression(Expression expr, int line) {
        return new ExpressionStatement(expr, line);
    }
    
    /**
     * Creates a function declaration statement.
     */
    public static Statement createFunction(String name, List<String> parameters, List<Statement> body, int line) {
        return new FunctionDeclarationStatement(name, parameters, body, line);
    }
    
    /**
     * Creates an if statement.
     */
    public static Statement createIf(Expression condition, List<Statement> thenBranch, 
                                   List<Expression> elifConditions, List<List<Statement>> elifBranches,
                                   List<Statement> elseBranch, int line) {
        return new IfStatement(condition, thenBranch, elifConditions, elifBranches, elseBranch, line);
    }
    
    /**
     * Creates a while statement.
     */
    public static Statement createWhile(Expression condition, List<Statement> body, int line) {
        return new WhileStatement(condition, body, line);
    }
    
    /**
     * Creates a run statement (do-while loop).
     */
    public static Statement createRun(List<Statement> body, Expression condition, int line) {
        return new RunStatement(body, condition, line);
    }
    
    /**
     * Creates a return statement.
     */
    public static Statement createReturn(Expression value, int line) {
        return new ReturnStatement(value, line);
    }
}
