public enum TokenType {
    // Keywords
    VAR, FUNCTION, RETURN, IF, ELIF, ELSE, WHILE, RUN, PRINT, INPUT,

    // Literals
    IDENTIFIER, NUMBER, // note: identifier includes MIN, MAX and ABS

    // Arithmetic Operators
    PLUS,   // +
    MINUS,  // -
    STAR,   // *
    SLASH,  // /
    MOD,    // %

    // Relational Operators
    LT,     // <
    LE,     // <=
    GT,     // >
    GE,     // >=
    EQ,     // =
    NE,     // ~

    // Assignment
    ASSIGN, // <-

    // Delimiters
    COMMA,      // ,
    SEMICOLON,  // ;
    LPAREN,     // (
    RPAREN,     // )
    LBRACE,     // {
    RBRACE,     // }

    // Special
    COMMENT,    // ---
    EOF,        // end of file
    ERROR       // error message
}
