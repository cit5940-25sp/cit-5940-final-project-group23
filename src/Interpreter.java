import java.util.*;

/**
 * Interpreter implements the ASTVisitor to interpret parsed expressions.
 * It uses an Environment for variable storage and supports function calls.
 */
public class Interpreter implements ASTVisitor {
    protected final Environment environment = new Environment();
    protected final SymbolTable symbolTable = new SymbolTable();

    public void defineFunction(String name, FunctionDeclarationStatement declaration) {
        symbolTable.define(name, declaration);
    }

    public void enterScope() {
        environment.enterScope();
    }

    public void exitScope() {
        environment.exitScope();
    }

    /**
     * Evaluates binary expressions (e.g., a + b, x == y).
     *
     * @param expr binary expression
     * @return the result of the binary calculation
     */
    @Override
    public Object visitBinaryExpression(BinaryExpression expr) {
        Object left = expr.getLeft().accept(this);
        Object right = expr.getRight().accept(this);

        int l = (int) left;
        int r = (int) right;

        return switch (expr.getOperator()) {
            case PLUS -> l + r;
            case MINUS -> l - r;
            case STAR -> l * r;
            case SLASH -> l / r;
            case MOD -> l % r;
            case EQ -> l == r ? 1 : 0;
            case NE -> l != r ? 1 : 0;
            case LT -> l < r ? 1 : 0;
            case LE -> l <= r ? 1 : 0;
            case GT -> l > r ? 1 : 0;
            case GE -> l >= r ? 1 : 0;
            default -> throw new RuntimeException("Unknown binary operator: " + expr.getOperator());
        };
    }

    /**
     * Evaluates unary expressions.
     *
     * @param expr Unary expression
     * @return the result of the Unary calculation
     */
    @Override
    public Object visitUnaryExpression(UnaryExpression expr) {
        int value = (int) expr.getOperand().accept(this);
        if (expr.getOperator() == TokenType.MINUS) {
            return -value;
        }
        throw new RuntimeException("Unknown unary operator: " + expr.getOperator());
    }

    /**
     * visit the value of a literal expression.
     *
     * @param expr literal expression variable
     * @return the value of literal expression
     */
    @Override
    public Object visitLiteralExpression(LiteralExpression expr) {
        return expr.getValue();
    }

    /**
     * visit the reference of a variable or returns its name.
     *
     * @param expr Variable expression
     * @return the value/name of the variable expression
     */
    @Override
    public Object visitVariableExpression(VariableExpression expr) {
        if (Builtins.isBuiltin(expr.getName())) {
            return expr.getName();
        }
        return environment.lookup(expr.getName());
    }

    /**
     * Evaluates grouped expressions.
     *
     * @param expr group expression
     * @return the result of the group
     */
    @Override
    public Object visitGroupExpression(GroupExpression expr) {
        return expr.getExpression().accept(this);
    }

    /**
     * Evaluates function calls (includes built-in functions).
     *
     * @param expr call expression
     * @return pass the evaluated functionName and args to callFunction
     */
    @Override
    public Object visitCallExpression(CallExpression expr) {
        String name = expr.getCallee();
        // 1) Evaluate argument expressions
        List<Integer> argValues = new ArrayList<>();
        for (Expression arg : expr.getArguments()) {
            argValues.add((Integer) arg.accept(this));
        }

        // 2) Built‐ins take precedence
        if (Builtins.isBuiltin(name)) {
            return Builtins.callFunction(name, argValues);
        }

        // 3) Otherwise user‐defined functions
        FunctionDeclarationStatement fnDecl = symbolTable.lookup(name);
        if (fnDecl == null) {
            throw new RuntimeException("Undefined function '" + name + "'");
        }

        // 4) Push new scope & bind params
        environment.enterScope();
        for (int i = 0; i < fnDecl.getParameters().size(); i++) {
            environment.declare(
                    fnDecl.getParameters().get(i),
                    argValues.get(i)
            );
        }

        Object result;
        try {
            for (Statement stmt : fnDecl.getBody()) {
                stmt.accept(this);
            }
            result = 0;                      // default if no return
        } catch (Return r) {
            result = r.value;               // actual return
        } finally {
            environment.exitScope();
        }

        return result;
    }

    /**
     * Use scanner to print out user input
     *
     * @param expr input expression
     * @return user input
     */
    @Override
    public Object visitInputExpression(InputExpression expr) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input: ");
        return scanner.nextInt();
    }

    /**
     * Evaluates declaration statement and updates the environment.
     *
     * @param stmt declaration statement var
     * @return null (declaration does not generate values, we only need to update the environment)
     */
    @Override
    public Object visitVarDeclarationStatement(VarDeclarationStatement stmt) {
        for (VarDeclarator decl : stmt.getDeclarators()) {
            Object value = decl.getInitializer().accept(this);
            environment.declare(decl.getName(), (Integer) value);
            symbolTable.defineVariable(decl.getName(), null); // Register name for parser-level checks
        }
        return null;
    }

    /**
     * Evaluates assignment statement and updates the environment.
     *
     * @param stmt assignment var
     * @return null (assignment does not generate new variable)
     */
    public Object visitVarAssignmentStatement(VarAssignmentStatement stmt) {
        int value = (Integer) stmt.getValue().accept(this);
        environment.assign(stmt.getName(), value);
        return null;
    }

    /**
     * Evaluates statements that contains expression.
     *
     * @param stmt expression statement var
     * @return the value after executing expression
     */
    @Override
    public Object visitExpressionStatement(ExpressionStatement stmt) {
        return stmt.getExpression().accept(this);
    }

    /**
     * Helper method that packages enterScope, run statement, and existScope into one.
     *
     * @param statements expression statement var
     */
    private void executeBlock(List<Statement> statements) {
        environment.enterScope();
        for (Statement stmt : statements) {
            stmt.accept(this);
        }
        environment.exitScope();
    }

    public static class Return extends RuntimeException {
        public final int value;
        public Return(int value) {
            super(null, null, false, false);
            this.value = value;
        }
    }

    /**
     * Evaluate if statement
     *
     * @param stmt if statement var
     * @return null (if is an action, and does not generate new value)
     */
    @Override
    public Object visitIfStatement(IfStatement stmt) {
        if ((int) stmt.getCondition().accept(this) != 0) {
            executeBlock(stmt.getThenBranch());
            return null;
        }
        for (int i = 0; i < stmt.getElifConditions().size(); i++) {
            if ((int) stmt.getElifConditions().get(i).accept(this) != 0) {
                executeBlock(stmt.getElifBranches().get(i));
                return null;
            }
        }
        if (stmt.hasElse()) {
            executeBlock(stmt.getElseBranch());
        }
        return null;
    }

    /**
     * Do-while loop: "run" executes body at least once then checks condition.
     * @param stmt run statement AST node
     * @return null
     */
    @Override
    public Object visitRunStatement(RunStatement stmt) {
        do {
            executeBlock(stmt.getBody());
        } while ((int) stmt.getCondition().accept(this) != 0);
        return null;
    }

    /**
     * Handles return statements by throwing a Return exception.
     * Requires an explicit return value; missing value is an error.
     * @param stmt return statement AST node
     */
    @Override
    public Object visitReturnStatement(ReturnStatement stmt) {
        System.out.println("Return statement");
        int returnValue = (stmt.getValue() == null)
                ? 0
                : (Integer) stmt.getValue().accept(this);
        throw new Return(returnValue);
    }

    /**
     * Prints the value of an expression to standard output.
     * @param stmt print statement AST node
     * @return null
     */
    @Override
    public Object visitPrintStatement(PrintStatement stmt) {
        Object value = stmt.getExpression().accept(this);
        System.out.println(value);
        return null;
    }

    @Override
    public Object visitFunctionDeclarationStatement(FunctionDeclarationStatement stmt) {
        defineFunction(stmt.getName(), stmt);
        symbolTable.define(stmt.getName(), stmt); // Store in symbol table for lookup
        return null;
    }

    /**
     * Loops while condition is non-zero, similar to classic while.
     * @param stmt while statement AST node
     * @return null
     */
    @Override
    public Object visitWhileStatement(WhileStatement stmt) {
        while ((int) stmt.getCondition().accept(this) != 0) {
            executeBlock(stmt.getBody());
        }
        return null;
    }

    /**
     * Direct utility method for calling user functions outside AST.
     * @param name function name
     * @param args argument list
     * @return integer result
     */
    public void callFunction(String name, List<Integer> args) {
        if (Builtins.isBuiltin(name)) {
            Builtins.callFunction(name, args);
            return;
        }
        FunctionDeclarationStatement function = symbolTable.lookup(name);
        if (function == null) {
            throw new RuntimeException("Function not defined: " + name);
        }
        if (function.getParameters().size() != args.size()) {
            throw new RuntimeException("Argument count mismatch in call to: " + name);
        }

        environment.enterScope();
        for (int i = 0; i < args.size(); i++) {
            environment.declare(function.getParameters().get(i), args.get(i));
        }

        for (Statement stmt : function.getBody()) {
            stmt.accept(this);
            if (stmt instanceof ReturnStatement) {
                break;
            }
        }
        environment.exitScope();
    }
}