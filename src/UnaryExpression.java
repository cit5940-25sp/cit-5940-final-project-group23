/**
 * Represents a unary expression with an operator and operand.
 */
public class UnaryExpression extends Expression {
    private final TokenType operator;
    private final Expression operand;
    
    public UnaryExpression(TokenType operator, Expression operand, int line) {
        super(ExpressionType.UNARY, line);
        this.operator = operator;
        this.operand = operand;
    }
    
    public TokenType getOperator() {
        return operator;
    }
    
    public Expression getOperand() {
        return operand;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitUnaryExpression(this);
    }
}