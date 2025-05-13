import java.util.List;

/**
 * Factory class for creating Expression nodes in the AST.
 * This class centralizes the decision logic for creating different types of expressions.
 */
public class ExpressionFactory {
    
    /**
     * Create an appropriate expression based on the expression type.
     * This method centralizes the decision logic for expression creation.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Expression create(ExpressionType type, Object... args) {
        try {
           
            
            switch (type) {
                case BINARY:
                    return createBinaryImpl((Expression)args[0], (TokenType)args[1], (Expression)args[2], toInt(args[3]));
                case UNARY:
                    return createUnaryImpl((TokenType)args[0], (Expression)args[1], toInt(args[2]));
                case LITERAL:
                    return createLiteralImpl(args[0], toInt(args[1]));
                case VARIABLE:
                    return createVariableImpl((String)args[0], toInt(args[1]));
                case GROUP:
                    return createGroupImpl((Expression)args[0], toInt(args[1]));
                case CALL:
                    return createCallImpl((String)args[0], (List<Expression>)args[1], toInt(args[2]));
                case INPUT:
                    return createInputImpl(toInt(args[0]));
                default:
                    throw new IllegalArgumentException("Unknown expression type: " + type);
            }
        } catch (Exception e) {
            System.err.println("Error creating expression of type: " + type);
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Helper method to safely convert various types to an integer
     */
    private static int toInt(Object obj) {
        if (obj == null) {
            System.err.println("Warning: Null value being converted to int, using default 0");
            return 0;
        }
        
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                System.err.println("Warning: Cannot convert string '" + obj + "' to integer, using default 0");
                return 0; // Default line number
            }
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        
        System.err.println("Warning: Unknown type for int conversion: " + obj.getClass().getName() + ", value: " + obj);
        return 0; // Default if conversion is not possible
    }
    
    // Private implementation methods
    
    private static Expression createBinaryImpl(Expression left, TokenType operator, Expression right, int line) {
        return new BinaryExpression(left, operator, right, line);
    }
    
    private static Expression createUnaryImpl(TokenType operator, Expression operand, int line) {
        return new UnaryExpression(operator, operand, line);
    }
    
    private static Expression createLiteralImpl(Object value, int line) {
        // Convert string literals to integers if they represent numbers
        if (value instanceof String) {
            try {
                int numericValue = Integer.parseInt((String) value);
                return new LiteralExpression(numericValue, line);
            } catch (NumberFormatException e) {
                // Not a number, leave as string
                return new LiteralExpression(value, line);
            }
        }
        return new LiteralExpression(value, line);
    }
    
    private static Expression createVariableImpl(String name, int line) {
        return new VariableExpression(name, line);
    }
    
    private static Expression createGroupImpl(Expression expression, int line) {
        return new GroupExpression(expression, line);
    }
    
    private static Expression createCallImpl(String callee, List<Expression> arguments, int line) {
        if (callee.equals("input")) {
            return new InputExpression(line);
        }
        return new CallExpression(callee, arguments, line);
    }
    
    private static Expression createInputImpl(int line) {
        return new InputExpression(line);
    }
    
    // Public convenience methods - these remain unchanged
    
    /**
     * Create a binary expression
     */
    public static Expression createBinary(Expression left, TokenType operator, Expression right, int line) {
        return create(ExpressionType.BINARY, left, operator, right, line);
    }
    
    /**
     * Create a unary expression
     */
    public static Expression createUnary(TokenType operator, Expression right, int line) {
        return create(ExpressionType.UNARY, operator, right, line);
    }
    
    /**
     * Create a literal expression
     */
    public static Expression createLiteral(Object value, int line) {
        return create(ExpressionType.LITERAL, value, line);
    }
    
    /**
     * Create a variable expression
     */
    public static Expression createVariable(String name, int line) {
        return create(ExpressionType.VARIABLE, name, line);
    }
    
    /**
     * Create a group expression
     */
    public static Expression createGroup(Expression expression, int line) {
        return create(ExpressionType.GROUP, expression, line);
    }
    
    /**
     * Create a call expression
     */
    public static Expression createCall(String callee, List<Expression> arguments, int line) {
        return create(ExpressionType.CALL, callee, arguments, line);
    }
    
    /**
     * Create an input expression
     */
    public static Expression createInput(int line) {
        return create(ExpressionType.INPUT, line);
    }
}
