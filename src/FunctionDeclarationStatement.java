import java.util.List;

/**
 * Represents a function declaration.
 */
public class FunctionDeclarationStatement extends Statement {
    private final String name;
    private final List<String> parameters;
    private final List<Statement> body;
    
    public FunctionDeclarationStatement(String name, List<String> parameters, List<Statement> body, int line) {
        super(line);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getParameters() {
        return parameters;
    }
    
    public List<Statement> getBody() {
        return body;
    }
    
    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitFunctionDeclarationStatement(this);
    }
}