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
        Token t1 = TokenFactory.identify("var", 1);
        assertEquals(TokenType.VAR, t1.getType());
        Token t2 = TokenFactory.identify("if", 3);
        assertEquals(TokenType.IF, t2.getType());
        assertEquals("if", t2.getValue());
        Token t3 = TokenFactory.identify("function", 1);
        assertEquals(TokenType.FUNCTION, t3.getType());
        Token t4 = TokenFactory.identify("x", 1);
        assertEquals(TokenType.IDENTIFIER, t4.getType());
        Token t5 = TokenFactory.identify("return", 1);
        assertEquals(TokenType.RETURN, t5.getType());
        Token t6 = TokenFactory.identify("else", 1);
        assertEquals(TokenType.ELSE, t6.getType());
        Token t7 = TokenFactory.identify("elif", 1);
        assertEquals(TokenType.ELIF, t7.getType());
        Token t8 = TokenFactory.identify("while", 1);
        assertEquals(TokenType.WHILE, t8.getType());
        Token t9 = TokenFactory.identify("run", 1);
        assertEquals(TokenType.RUN, t9.getType());
    }
}
