import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Object> symbols = new HashMap<>();
    private final Map<String, FunctionDeclarationStatement> functions = new HashMap<>();

    public void define(String name, FunctionDeclarationStatement declaration) {
        functions.put(name, declaration);
    }

    public boolean isDefined(String name) {
        return functions.containsKey(name);
    }

    public FunctionDeclarationStatement lookup(String name) {
        return functions.get(name);
    }

    public void defineVariable(String name, Object value) {
        symbols.put(name, value);
    }

    public boolean isVariableDefined(String name) {
        return symbols.containsKey(name);
    }
}
