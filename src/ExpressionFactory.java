import java.util.List;

/**
 * Factory class for creating Expression nodes in the AST.
 * This class centralizes the decision logic for creating different types of expressions.
 */
public class ExpressionFactory {
    
    /**
     * Create an appropriate expression based on the type of operation.
     * This method centralizes the decision logic for expression creation.
     */
    public static Expression create(String expressionType, Object... args) {
        switch (expressionType) {
            case "binary":
                return createBinary((Expression)args[0], (TokenType)args[1], (Expression)args[2], (Integer)args[3]);
            case "unary":
                return createUnary((TokenType)args[0], (Expression)args[1], (Integer)args[2]);
            case "literal":
                return createLiteral(args[0], (Integer)args[1]);
            case "variable":
                return createVariable((String)args[0], (Integer)args[1]);
            case "group":
                return createGroup((Expression)args[0], (Integer)args[1]);
            case "call":
                return createCall((String)args[0], (List<Expression>)args[1], (Integer)args[2]);
            case "input":
                return createInput((Integer)args[0]);
            default:
                throw new IllegalArgumentException("Unknown expression type: " + expressionType);
        }
    }
    
    /**
     * Creates a binary expression.
     */
    private static Expression createBinary(Expression left, TokenType operator, Expression right, int line) {
        return new BinaryExpression(left, operator, right, line);
    }
    
    /**
     * Creates a unary expression.
     */
    private static Expression createUnary(TokenType operator, Expression operand, int line) {
        return new UnaryExpression(operator, operand, line);
    }
    
    /**
     * Creates a literal expression with various types of values.
     */
    private static Expression createLiteral(Object value, int line) {
        if (value instanceof String) {
            try {
                Integer number = Integer.parseInt((String)value);
                return new LiteralExpression(number, line);
            } catch (NumberFormatException e) {
                // Could handle as an error or try parsing as a different type
                return new LiteralExpression(0, line); // Default fallback
            }
        } else {
            return new LiteralExpression(value, line);
        }
    }
    
    /**
     * Creates a variable expression.
     */
    private static Expression createVariable(String name, int line) {
        return new VariableExpression(name, line);
    }
    
    /**
     * Creates a grouped expression (parenthesized expression).
     */
    private static Expression createGroup(Expression expression, int line) {
        return new GroupExpression(expression, line);
    }
    
    /**
     * Creates a function call expression.
     */
    private static Expression createCall(String callee, List<Expression> arguments, int line) {
        if (callee.equals("input")) {
            return new InputExpression(line);
        }
        return new CallExpression(callee, arguments, line);
    }
    
    /**
     * Creates an input expression.
     */
    private static Expression createInput(int line) {
        return new InputExpression(line);
    }
}
    