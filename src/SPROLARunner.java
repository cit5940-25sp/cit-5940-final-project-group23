import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class SPROLARunner {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected exactly one argument");
        }
        String path = args[0];
        String source = "";
        try {
            source = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.err.println("Failed to read file");
            System.exit(2);
        }

        try {
            // Lexing
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();

            // Parsing
            Parser parser = new Parser(tokens);
            ASTNode program = parser.parse();

            // Interpretation
            Interpreter interp = new Interpreter();
            // TODO: insert interpreter entry point function
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(4);
        }
    }
}
