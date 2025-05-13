public abstract class Expression extends ASTNode {
    private final ExpressionType type;
    
    public Expression(ExpressionType type, int line) {
        super(line);
        this.type = type;
    }
    
    public ExpressionType getType() {
        return type;
    }
    
    /**
     * Accept method for the visitor pattern.
     * Each concrete subclass must implement this to call the appropriate visit method.
     * 
     * @param visitor The visitor that will process this expression
     * @return The result of the visitor's processing
     */
    public abstract Object accept(ASTVisitor visitor);
}
