public abstract class Statement extends ASTNode {
    public Statement(int line) {
        super(line);
    }
    
    public abstract Object accept(ASTVisitor visitor);
}