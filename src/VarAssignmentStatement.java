public class VarAssignmentStatement extends Statement {
    private final String name;
    private final Expression value;
    
    public VarAssignmentStatement(String name, Expression value, int line) {
        super(StatementType.VAR_ASSIGNMENT,line);
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
        return visitor.visitVarAssignmentStatement(this);
    }
}