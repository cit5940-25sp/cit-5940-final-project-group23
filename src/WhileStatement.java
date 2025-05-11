import java.util.List;

/**
 * Represents a while loop statement.
 */
public class WhileStatement extends Statement {
    private final Expression condition;
    private final List<Statement> body;
    
    public WhileStatement(Expression condition, List<Statement> body, int line) {
        super(line);
        this.condition = condition;
        this.body = body;
    }
    
    public Expression getCondition() {
        return condition;
    }
    
    public List<Statement> getBody() {
        return body;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitWhileStatement(this);
    }
}