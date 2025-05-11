import java.util.List;

/**
 * Represents a function call expression in the AST.
 * Example: add(x, y)
 */
public class CallExpression extends Expression {
    private final String callee;          // The name of the function being called
    private final List<Expression> arguments;  // The arguments passed to the function
    
    /**
     * Constructor for a function call expression.
     * 
     * @param callee The name of the function being called
     * @param arguments The list of argument expressions
     * @param line The source line number where this expression appears
     */
    public CallExpression(String callee, List<Expression> arguments, int line) {
        super(ExpressionType.CALL, line);
        this.callee = callee;
        this.arguments = arguments;
    }
    
    /**
     * Get the name of the function being called.
     */
    public String getCallee() {
        return callee;
    }
    
    /**
     * Get the list of argument expressions.
     */
    public List<Expression> getArguments() {
        return arguments;
    }
    
    /**
     * Get the number of arguments.
     */
    public int getArgumentCount() {
        return arguments.size();
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitCallExpression(this);
    }
}