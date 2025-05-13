/**
 * Defines the types of expressions in the AST.
 */
public enum ExpressionType {
    // Expression types
    BINARY,   // Binary operations like a + b
    UNARY,    // Unary operations like -a
    LITERAL,  // Literal values like 42
    VARIABLE, // Variable references like x
    GROUP,    // Grouped expressions like (a + b)
    CALL,      // Function calls like foo(a, b)
    INPUT      // Input expression for reading user input
}