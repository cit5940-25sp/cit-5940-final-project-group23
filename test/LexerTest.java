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

    @Test
    public void test4() {
        Lexer lexer = new Lexer("var x y1 --- this is comment\n+");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.VAR, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(2).getType());
        assertEquals(TokenType.PLUS, tokens.get(3).getType());
    }

    @Test
    public void test5() {
        Lexer lexer = new Lexer("a\nb\nc\nd\ne\nf\ng\nh\ni\nj");
        List<Token> tokens = lexer.tokenize();
        assertEquals(1, tokens.get(0).getLine());
        assertEquals(2, tokens.get(1).getLine());
        assertEquals(10, tokens.get(9).getLine());
    }

    @Test
    public void test6() {
        Lexer lexer = new Lexer("< <= > >= <-");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.LT, tokens.get(0).getType());
        assertEquals(TokenType.LE, tokens.get(1).getType());
        assertEquals(TokenType.GT, tokens.get(2).getType());
        assertEquals(TokenType.GE, tokens.get(3).getType());
        assertEquals(TokenType.ASSIGN, tokens.get(4).getType());
    }

    @Test
    public void test7() {
        Lexer lexer = new Lexer("if else print");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.IF, tokens.get(0).getType());
        assertEquals(TokenType.ELSE, tokens.get(1).getType());
        assertEquals(TokenType.PRINT, tokens.get(2).getType());
    }

    @Test
    public void test8() {
        Lexer lexer = new Lexer("-a");
        List<Token> tokens = lexer.tokenize();
        assertEquals(TokenType.MINUS, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals("a", tokens.get(1).getValue());
    }
}
