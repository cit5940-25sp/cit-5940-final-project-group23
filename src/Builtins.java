import java.util.*;

public class Builtins {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Handle calls to built-in functions.
     *
     * @param name name of the function
     * @param args arguments of the function
     * @return the result as an integer
     */
    public static int callFunction(String name, List<Integer> args) {
        switch (name) {
            case "abs":
                if (args.size() != 1) {
                    throw new RuntimeException("abs() takes 1 argument");
                }
                return Math.abs(args.get(0));

            case "max":
                if (args.size() != 2) {
                    throw new RuntimeException("max() takes 2 arguments");
                }
                return Math.max(args.get(0), args.get(1));

            case "min":
                if (args.size() != 2) {
                    throw new RuntimeException("min() takes 2 arguments");
                }
                return Math.min(args.get(0), args.get(1));

            case "print":
                if (args.size() != 1) {
                    throw new RuntimeException("print() takes 1 argument");
                }
                System.out.println(args.get(0));
                return args.get(0);

            case "input":
                if (!args.isEmpty()) {
                    throw new RuntimeException("input() takes no arguments");
                }
                return SCANNER.nextInt();

            default:
                throw new IllegalArgumentException("Unknown function: " + name);
        }
    }

    /**
     * Check whether the function is a built-in function.
     *
     * @param name name of the function
     * @return whether the function is a built-in
     */
    public static boolean isBuiltin(String name) {
        return switch (name) {
            case "abs", "max", "min", "print", "input" -> true;
            default -> false;
        };
    }
}
