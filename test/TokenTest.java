import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenCreation() {
        Token t = new Token(TokenType.IDENTIFIER, "x", 1);
        assertEquals(TokenType.IDENTIFIER, t.getType());
        assertEquals("x", t.getValue());
        assertEquals(1, t.getLine());
    }

    @Test
    public void testToStringFormat() {
        Token t = new Token(TokenType.NUMBER, "42", 3);
        assertEquals("Token[NUMBER, '42', line 3]", t.toString());
    }

    @Test
    public void testDifferentTokenTypes() {
        Token keyword = new Token(TokenType.PRINT, "print", 2);
        Token number = new Token(TokenType.NUMBER, "100", 4);
        Token symbol = new Token(TokenType.PLUS, "+", 5);

        assertEquals("print", keyword.getValue());
        assertEquals(TokenType.NUMBER, number.getType());
        assertEquals("+", symbol.getValue());
    }
}
