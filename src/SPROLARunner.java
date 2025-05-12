import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SPROLARunner {
    /**
     * The main function for the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Invalid number of arguments");
            System.exit(1);
        }

        String path = args[0];
        String source = "";
        try {
            source = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.err.println("Failed to read file" + e);
            System.exit(2);
        }

        try {
            // Lexing
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();

            // Parsing
            Parser parser = new Parser(tokens);
            List<Statement> program = new ArrayList<>();
            while (!parser.isAtEnd()) {
                Statement stmt = parser.parse();
                program.add(stmt);
            }

            // Interpretation
            Interpreter interp = new Interpreter();
            for (Statement stmt : program) {
                if (stmt instanceof FunctionDeclarationStatement) {
                    stmt.accept(interp);
                }
            }
            for (Statement stmt : program) {
                if (!(stmt instanceof FunctionDeclarationStatement)) {
                    stmt.accept(interp);
                }
            }
            interp.callFunction("entry", Collections.emptyList());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(4);
        }
    }
}
