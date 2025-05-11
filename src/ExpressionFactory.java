import java.util.List;

/**
 * Factory class for creating Expression nodes in the AST.
 */
public class ExpressionFactory {
    /**
     * Creates a binary expression.
     */
    public static Expression createBinary(Expression left, TokenType operator, Expression right, int line) {
        return new BinaryExpression(left, operator, right, line);
    }
    
    /**
     * Creates a unary expression.
     */
    public static Expression createUnary(TokenType operator, Expression operand, int line) {
        return new UnaryExpression(operator, operand, line);
    }
    
    /**
     * Creates a literal expression for a number.
     */
    public static Expression createNumberLiteral(String value, int line) {
        try {
            Integer number = Integer.parseInt(value);
            return new LiteralExpression(number, line);
        } catch (NumberFormatException e) {
            // Could handle as an error or try parsing as a different type
            return new LiteralExpression(0, line); // Default fallback
        }
    }
    
    /**
     * Creates a variable expression.
     */
    public static Expression createVariable(String name, int line) {
        return new VariableExpression(name, line);
    }
    
    /**
     * Creates a grouped expression (parenthesized expression).
     */
    public static Expression createGroup(Expression expression, int line) {
        return new GroupExpression(expression, line);
    }
    
    /**
     * Creates a function call expression.
     */
    public static Expression createCall(String callee, List<Expression> arguments, int line) {
        return new CallExpression(callee, arguments, line);
    }
}
