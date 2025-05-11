import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Object> symbols = new HashMap<>();
    private final Map<String, Integer> functions = new HashMap<>(); 
    public void define(String name) {
        symbols.put(name, null);
    }
    
    public boolean isDefined(String name) {
        return symbols.containsKey(name);
    }

    // Function methods
    public void defineFunction(String name, int arity) {
        functions.put(name, arity);
    }
    
    public boolean isFunctionDefined(String name) {
        return functions.containsKey(name);
    }
    
    public int getFunctionArity(String name) {
        return functions.getOrDefault(name, -1);
    }
}