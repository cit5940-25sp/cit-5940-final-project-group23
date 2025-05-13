/**
 * Represents a statement that consists of a single expression.
 * For example: a function call like print(x) or a bare expression like x+5;
 */
public class ExpressionStatement extends Statement {
    private final Expression expression;
    
    public ExpressionStatement(Expression expression, int line) {
        super(StatementType.EXPRESSION,line);
        this.expression = expression;
    }
    
    public Expression getExpression() {
        return expression;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitExpressionStatement(this);
    }
}