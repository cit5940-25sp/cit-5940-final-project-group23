import java.util.List;

/**
 * Represents a function call expression.
 */
public class CallExpression extends Expression {
    private final Expression callee;
    private final List<Expression> arguments;
    
    public CallExpression(Expression callee, List<Expression> arguments, int line) {
        super(ExpressionType.CALL, line);
        this.callee = callee;
        this.arguments = arguments;
    }
    
    public Expression getCallee() {
        return callee;
    }
    
    public List<Expression> getArguments() {
        return arguments;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitCallExpression(this);
    }
}