import java.util.List;

/**
 * Represents a variable declaration statement (var x <- value, y <- value, ...).
 */
public class VarDeclarationStatement extends Statement {
    private final List<VarDeclarator> declarators;
    
    public VarDeclarationStatement(List<VarDeclarator> declarators, int line) {
        super(line);
        this.declarators = declarators;
    }
    
    public List<VarDeclarator> getDeclarators() {
        return declarators;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitVarDeclarationStatement(this);
    }
}