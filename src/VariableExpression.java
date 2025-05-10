/**
 * Represents a variable reference in the AST.
 */
public class VariableExpression extends Expression {
    private final String name;
    
    public VariableExpression(String name, int line) {
        super(ExpressionType.VARIABLE, line);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitVariableExpression(this);
    }
}