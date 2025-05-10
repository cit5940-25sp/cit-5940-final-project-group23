import java.util.*;

public class Environment {
    private final Deque<Map<String, Integer>> scopes = new ArrayDeque<>();

    /**
     * Construct an Environment.
     */
    public Environment() {
        enterScope();
    }

    /**
     * Push an empty scope onto the stack.
     */
    void enterScope() {
        scopes.push(new HashMap<>());
    }

    /**
     * Exit from a scope.
     */
    void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        } else {
            throw new IllegalStateException("No existing scope!");
        }
    }

    /**
     * Handle variable declaration.
     *
     * @param name name of the variable
     * @param value value of the variable
     */
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

    /**
     * Handle variable assignment.
     *
     * @param name name of the variable
     * @param value value of the variable
     */
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

    /**
     * Look up the value of a variable.
     *
     * @param name name of the variable
     */
    int lookup(String name) {
        for (Map<String, Integer> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new RuntimeException("Variable not found!");
    }
}
