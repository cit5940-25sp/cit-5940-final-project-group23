public abstract class ASTNode {
    private final int line;
    //this keeps track of the line number in the source code where this node is located
    public ASTNode(int line) {
        this.line = line;
    }
    
    public int getLine() {
        return line;
    }
    
    /**
     * Accept method for the visitor pattern.
     * @param visitor The visitor to accept
     * @return The result of the visitor's operation
     */
    public abstract Object accept(ASTVisitor visitor);

}
