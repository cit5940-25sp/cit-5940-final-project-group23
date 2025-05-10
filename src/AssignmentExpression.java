/**
 * Represents a variable assignment expression.
 */
public class AssignmentExpression extends Expression {
    private final String name;
    private final Expression value;
    
    public AssignmentExpression(String name, Expression value, int line) {
        super(ExpressionType.ASSIGNMENT, line);
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public Expression getValue() {
        return value;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitAssignmentExpression(this);
    }
}