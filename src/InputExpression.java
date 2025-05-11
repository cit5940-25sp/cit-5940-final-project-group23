public class InputExpression extends Expression {
    public InputExpression(int line) {
        super(ExpressionType.INPUT, line);
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitInputExpression(this);
    }
}