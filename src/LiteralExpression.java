/**
 * Represents a literal value (number, string, etc.) in the AST.
 */
public class LiteralExpression extends Expression {
    private final Object value;
    
    public LiteralExpression(Object value, int line) {
        super(ExpressionType.LITERAL, line);
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitLiteralExpression(this);
    }
}
