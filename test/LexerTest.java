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
        assertEquals(TokenType.EOF, tokens.get(tokens.size() - 1).getType());   // FIXME: something wrong with EOF
    }
}
