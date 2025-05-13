public abstract class Statement extends ASTNode {
    private final StatementType type;
    
    public Statement(StatementType type, int line) {
        super(line);
        this.type = type;
    }
    
    public StatementType getType() {
        return type;
    }
    
    public abstract Object accept(ASTVisitor visitor);
}