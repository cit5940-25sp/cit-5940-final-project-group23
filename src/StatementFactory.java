import java.util.List;

/**
 * Factory class for creating Statement nodes in the AST.
 */
public class StatementFactory {
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
                                     List<Expression> elifConditions,
                                     List<List<Statement>> elifBranches,
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
