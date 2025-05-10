/**
 * Represents a binary expression with a left expression, operator, and right expression.
 */
public class BinaryExpression extends Expression {
    private final Expression left;
    private final TokenType operator;
    private final Expression right;
    
    public BinaryExpression(Expression left, TokenType operator, Expression right, int line) {
        super(ExpressionType.BINARY, line);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    public Expression getLeft() {
        return left;
    }
    
    public TokenType getOperator() {
        return operator;
    }
    
    public Expression getRight() {
        return right;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitBinaryExpression(this);
    }
}
