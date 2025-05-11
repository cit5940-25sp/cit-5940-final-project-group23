import java.util.*;

/**
 * Interpreter implements the ASTVisitor to interpret parsed expressions.
 * It uses an Environment for variable storage and supports function calls.
 */
public class Interpreter implements ASTVisitor {
    private final Environment environment = new Environment();

    public Interpreter() {
    }

    public void enterScope() {
        environment.enterScope();
    }

    public void exitScope() {
        environment.exitScope();
    }

    /**
     * Evaluates binary expressions (e.g., a + b, x == y).
     *
     * @param expr binary expression
     * @return the result of the binary calculation
     */
    @Override
    public Object visitBinaryExpression(BinaryExpression expr) {
        Object left = expr.getLeft().accept(this);
        Object right = expr.getRight().accept(this);

        int l = (int) left;
        int r = (int) right;

        switch (expr.getOperator()) {
            case PLUS: return l + r;
            case MINUS: return l - r;
            case STAR: return l * r;
            case SLASH: return l / r;
            case MOD: return l % r;
            case EQ: return l == r ? 1 : 0;
            case NE: return l != r ? 1 : 0;
            case LT: return l < r ? 1 : 0;
            case LE: return l <= r ? 1 : 0;
            case GT: return l > r ? 1 : 0;
            case GE: return l >= r ? 1 : 0;
            default:
                throw new RuntimeException("Unknown binary operator: " + expr.getOperator());
        }
    }

    /**
     * Evaluates unary expressions.
     *
     *  @param expr Unary expression
     *  @return the result of the Unary calculation
     */
    @Override
    public Object visitUnaryExpression(UnaryExpression expr) {
        int value = (int) expr.getOperand().accept(this);
        if (expr.getOperator() == TokenType.MINUS) {
            return -value;
        }
        throw new RuntimeException("Unknown unary operator: " + expr.getOperator());
    }

    /**
     * visit the value of a literal expression.
     *
     * @param expr literal expression variable
     * @return the value of literal expression
     */
    @Override
    public Object visitLiteralExpression(LiteralExpression expr) {
        return expr.getValue();
    }

    /**
     * visit the reference of a variable or returns its name.
     *
     * @param expr Variable expression
     * @return the value/name of the variable expression
     */
    @Override
    public Object visitVariableExpression(VariableExpression expr) {
        if (Builtins.isBuiltin(expr.getName())) {
            return expr.getName();
        }
        return environment.lookup(expr.getName());
    }

    /**
     * Evaluates grouped expressions.
     * @param expr group expression
     * @return the result of the group
     */
    @Override
    public Object visitGroupExpression(GroupExpression expr) {
        return expr.getExpression().accept(this);
    }

    /**
     * Evaluates function calls (includes built-in functions).
     *
     * @param expr call expression
     * @return pass the evaluated functionName and args to callFunction
     */
    @Override
    public Object visitCallExpression(CallExpression expr) {
        Object callee = expr.getCallee().accept(this);
        if (!(callee instanceof String functionName)) {
            throw new RuntimeException("Invalid function call");
        }
        List<Integer> args = new ArrayList<>();
        for (Expression arg : expr.getArguments()) {
            args.add((int) arg.accept(this));
        }
        return callFunction(functionName, args);
    }

    /**
     * Evaluates assignment expressions and updates the environment.
     *
     * @param expr assignment expression
     * @return the new value after assignment
     */
    @Override
    public Object visitAssignmentExpression(AssignmentExpression expr) {
        Object value = expr.getValue().accept(this);
        try {
            environment.assign(expr.getName(), (Integer) value);
        } catch (RuntimeException e) {
            // If variable not declared, declare it
            environment.declare(expr.getName(), (Integer) value);
        }
        return value;
    }


    /**
     * Calls a built-in function or throws an error if undefined.
     */
    public int callFunction(String name, List<Integer> args) {
        return 0;
    }
}

