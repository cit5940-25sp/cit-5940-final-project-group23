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

        switch (expr.getOperator()) {
            case PLUS:
                return l + r;
            case MINUS:
                return l - r;
            case STAR:
                return l * r;
            case SLASH:
                return l / r;
            case MOD:
                return l % r;
            case EQ:
                return l == r ? 1 : 0;
            case NE:
                return l != r ? 1 : 0;
            case LT:
                return l < r ? 1 : 0;
            case LE:
                return l <= r ? 1 : 0;
            case GT:
                return l > r ? 1 : 0;
            case GE:
                return l >= r ? 1 : 0;
            default:
                throw new RuntimeException("Unknown binary operator: " + expr.getOperator());
        }
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
        String functionName = expr.getCallee();
        List<Integer> args = new ArrayList<>();
        for (Expression arg : expr.getArguments()) {
            args.add((int) arg.accept(this));
        }
        return callFunction(functionName, args);
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
        Object value = stmt.getValue().accept(this);
        try {
            environment.assign(stmt.getName(), (Integer) value);
        } catch (RuntimeException e) {
            environment.declare(stmt.getName(), (Integer) value); //Declare if not already declared
        }
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

    @Override
    public Object visitRunStatement(RunStatement stmt) {
        do {
            executeBlock(stmt.getBody());
        } while ((int) stmt.getCondition().accept(this) != 0);
        return null;
    }

    @Override
    public Object visitReturnStatement(ReturnStatement stmt) {
        if (stmt.hasValue()) {
            return stmt.getValue().accept(this);
        }
        return 0;
    }

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


    @Override
    public Object visitWhileStatement(WhileStatement stmt) {
        while ((int) stmt.getCondition().accept(this) != 0) {
            executeBlock(stmt.getBody());
        }
        return null;
    }

    public int callFunction(String name, List<Integer> args) {
        if (Builtins.isBuiltin(name)) {
            return Builtins.callFunction(name, args);
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

        Object result = 0;
        for (Statement stmt : function.getBody()) {
            Object val = stmt.accept(this);
            if (stmt instanceof ReturnStatement) {
                result = val;
                break;
            }
        }
        environment.exitScope();
        return (int) result;
    }

}