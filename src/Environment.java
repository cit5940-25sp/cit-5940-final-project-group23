import java.util.*;

public class Environment {
    private final Deque<Map<String, Integer>> scopes = new ArrayDeque<>();

    public Environment() {
        enterScope();
    }

    void enterScope() {
        scopes.push(new HashMap<>());
    }

    void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        } else {
            throw new IllegalStateException("No existing scope!");
        }
    }

    void declare(String name, int value) {
        Map<String, Integer> scope = scopes.peek();
        if (scope == null) {
            throw new IllegalStateException("No existing scope!");
        } else if (scope.containsKey(name)) {
            throw new RuntimeException("Variable already declared!");
        } else {
            scope.put(name, value);
        }
    }

    void assign(String name, int value) {
        Map<String, Integer> scope = scopes.peek();
        if (scope == null) {
            throw new IllegalStateException("No existing scope!");
        } else if (!scope.containsKey(name)) {
            throw new RuntimeException("Variable not found!");
        } else {
            scope.put(name, value);
        }
    }

    int lookup(String name) {
        for (Map<String, Integer> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new RuntimeException("Variable not found!");
    }
}
