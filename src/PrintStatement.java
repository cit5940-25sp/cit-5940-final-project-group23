/**
 * Represents a print statement in the AST.
 * Example: print x
 */
public class PrintStatement extends Statement {
    private final Expression expression;
    
    public PrintStatement(Expression expression, int line) {
        super(line);
        this.expression = expression;
    }
    
    public Expression getExpression() {
        return expression;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitPrintStatement(this);
    }
}