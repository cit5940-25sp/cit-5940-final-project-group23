public abstract class Expression extends ASTNode{
    private final ExpressionType type;
    
    public Expression(ExpressionType type, int line) {
        super(line);
        this.type = type;
    }
    
    public ExpressionType getType() {
        return type;
    }
}
