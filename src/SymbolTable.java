import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private static class Scope {
        final Map<String, Object> variables = new HashMap<>();
        final Map<String, Object> functions = new HashMap<>();
    }
    
    private final List<Scope> scopes = new ArrayList<>();
    
    public SymbolTable() {
        // Start with global scope
        scopes.add(new Scope());
    }
    
    public void enterScope() {
        scopes.add(new Scope());
    }
    
    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.remove(scopes.size() - 1);
        }
    }
    
    // This is the missing method the Interpreter uses
    public void define(String name, Object value) {
        // For functions, store in the functions map
        getCurrentScope().functions.put(name, value);
    }
    
    public void defineVariable(String name, Object value) {
        // Define in current scope
        getCurrentScope().variables.put(name, value);
    }
    
    // Add function lookup
    public FunctionDeclarationStatement lookup(String name) {
        // Look for function in all scopes from current to global
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).functions.containsKey(name)) {
                return (FunctionDeclarationStatement) scopes.get(i).functions.get(name);
            }
        }
        return null;
    }
    
    // Check if a function exists
    public boolean isFunctionDefined(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).functions.containsKey(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isVariableDefined(String name) {
        // Check only current scope
        return getCurrentScope().variables.containsKey(name);
    }
    
    public boolean isDefined(String name) {
        // Check all scopes from current to global for variables
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).variables.containsKey(name)) {
                return true;
            }
        }
        return false;
    }
    
    private Scope getCurrentScope() {
        return scopes.get(scopes.size() - 1);
    }
}
