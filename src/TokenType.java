public enum TokenType {
    // Keywords
    VAR, FUNCTION, RETURN, IF, ELIF, ELSE, WHILE, RUN, PRINT, INPUT,

    // Literals
    IDENTIFIER, NUMBER,

    // Arithmetic Operators
    PLUS,       // +
    MINUS,      // -
    STAR,       // *
    SLASH,      // /
    MOD,        // %

    // Relational Operators
    LESS,       // <
    LESS_EQUAL, // <=
    GREATER,    // >
    GREATER_EQUAL, // >=
    EQUAL,      // =
    NOT_EQUAL,  // ~

    // Assignment
    ASSIGN,     // <-

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
