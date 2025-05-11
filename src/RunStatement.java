import java.util.List;

/**
 * Represents a do-while loop (run-while in your language).
 * The body executes once, then repeats while the condition is true.
 */
public class RunStatement extends Statement {
    private final List<Statement> body;
    private final Expression condition;
    
    public RunStatement(List<Statement> body, Expression condition, int line) {
        super(line);
        this.body = body;
        this.condition = condition;
    }
    
    public List<Statement> getBody() {
        return body;
    }
    
    public Expression getCondition() {
        return condition;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitRunStatement(this);
    }
}