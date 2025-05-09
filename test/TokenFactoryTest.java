import org.junit.Test;
import static org.junit.Assert.*;

public class TokenFactoryTest {
    @Test
    public void testMakeToken() {
        Token t1 = TokenFactory.makeToken(TokenType.IDENTIFIER, "x", 1);
        assertEquals(TokenType.IDENTIFIER, t1.getType());
        assertEquals("x", t1.getValue());
    }

    @Test
    public void testMakeTokenType() {
        Token t1 = TokenFactory.symbol(TokenType.PLUS, "+", 3);
        assertEquals(TokenType.PLUS, t1.getType());
        assertEquals("+", t1.getValue());
        Token t2 = TokenFactory.identify("if", 3);
        assertEquals(TokenType.IF, t2.getType());
        assertEquals("if", t2.getValue());
        Token t3 = TokenFactory.eof("", 10);
        assertEquals(TokenType.EOF, t3.getType());
        assertEquals("", t3.getValue());
        Token t4 = TokenFactory.error("@", 5);
        assertEquals(TokenType.ERROR, t4.getType());
        assertEquals("@", t4.getValue());
        Token t5 = TokenFactory.comment("---", 3);
        assertEquals(TokenType.COMMENT, t5.getType());
        assertEquals("---", t5.getValue());
    }
}
