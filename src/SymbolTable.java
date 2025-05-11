import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Object> symbols = new HashMap<>();
    
    public void define(String name) {
        symbols.put(name, null);
    }
    
    public boolean isDefined(String name) {
        return symbols.containsKey(name);
    }
}