import java.util.*;

public class Interpreter {
    public Interpreter() {

    }

    public int callFunction(String name, List<Integer> args) {
        // TODO: look up FunctionDeclaration, create Environment, execute and return
        if (Builtins.isBuiltin(name)) {
            return Builtins.callFunction(name, args);
        }
        return 0;
    }

    // there will be more functions
}
