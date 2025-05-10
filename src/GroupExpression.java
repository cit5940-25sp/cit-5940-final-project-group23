/**
 * Represents a grouped (parenthesized) expression.
 */
public class GroupExpression extends Expression {
    private final Expression expression;
    
    public GroupExpression(Expression expression, int line) {
        super(ExpressionType.GROUP, line);
        this.expression = expression;
    }
    
    public Expression getExpression() {
        return expression;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitGroupExpression(this);
    }
}
