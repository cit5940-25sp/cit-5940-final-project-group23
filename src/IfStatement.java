import java.util.List;

/**
 * Represents an if statement with optional elif and else branches.
 */
public class IfStatement extends Statement {
    private final Expression condition;
    private final List<Statement> thenBranch;
    private final List<Expression> elifConditions;
    private final List<List<Statement>> elifBranches;
    private final List<Statement> elseBranch;
    
    public IfStatement(Expression condition, List<Statement> thenBranch, 
                      List<Expression> elifConditions, List<List<Statement>> elifBranches,
                      List<Statement> elseBranch, int line) {
        super(StatementType.IF,line);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elifConditions = elifConditions;
        this.elifBranches = elifBranches;
        this.elseBranch = elseBranch;
    }
    
    public Expression getCondition() {
        return condition;
    }
    
    public List<Statement> getThenBranch() {
        return thenBranch;
    }
    
    public List<Expression> getElifConditions() {
        return elifConditions;
    }
    
    public List<List<Statement>> getElifBranches() {
        return elifBranches;
    }
    
    public List<Statement> getElseBranch() {
        return elseBranch;
    }
    
    public boolean hasElse() {
        return elseBranch != null;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitIfStatement(this);
    }
}