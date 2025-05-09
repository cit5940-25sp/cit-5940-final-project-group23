import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class LexerTest {
    @Test
    public void test1() {
        Lexer lexer = new Lexer("()+-;,=~");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.LPAREN, tokens.get(0).getType());
        assertEquals(TokenType.RPAREN, tokens.get(1).getType());
        assertEquals(TokenType.PLUS, tokens.get(2).getType());
        assertEquals(TokenType.NE, tokens.get(7).getType());
        assertEquals(TokenType.EOF, tokens.get(tokens.size() - 1).getType());
    }

    @Test
    public void test2() {
        Lexer lexer = new Lexer("0 7 42 1234567890");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(TokenType.NUMBER, tokens.get(1).getType());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
        assertEquals(TokenType.NUMBER, tokens.get(3).getType());
        assertEquals(TokenType.EOF, tokens.get(4).getType());
    }

    @Test
    public void test3() {
        Lexer lexer = new Lexer("var x y1 function return");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.VAR, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(2).getType());
        assertEquals(TokenType.FUNCTION, tokens.get(3).getType());
        assertEquals(TokenType.RETURN, tokens.get(4).getType());
    }
}
