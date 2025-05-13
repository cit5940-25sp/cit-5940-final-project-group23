

/**
 * Represents a return statement from a function.
 */
public class ReturnStatement extends Statement {
    private final Expression value; // Can be null for return without value
    
    public ReturnStatement(Expression value, int line) {
        super(StatementType.RETURN,line);
        this.value = value;
    }
    
    public Expression getValue() {
        return value;
    }
    
    public boolean hasValue() {
        return value != null;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitReturnStatement(this);
    }
}