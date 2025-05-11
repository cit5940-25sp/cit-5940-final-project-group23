/**
 * Represents a single variable declarator (name and initializer).
 */
public class VarDeclarator {
    private final String name;
    private final Expression initializer;
    
    public VarDeclarator(String name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
    
    public String getName() {
        return name;
    }
    
    public Expression getInitializer() {
        return initializer;
    }
}