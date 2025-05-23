public interface ASTVisitor {
    // Expression visit methods
    Object visitBinaryExpression(BinaryExpression expr);
    Object visitUnaryExpression(UnaryExpression expr);
    Object visitLiteralExpression(LiteralExpression expr);
    Object visitVariableExpression(VariableExpression expr);
    Object visitGroupExpression(GroupExpression expr);
    Object visitCallExpression(CallExpression expr);
    Object visitInputExpression(InputExpression expr);
    
    // Statement visit methods
    Object visitVarDeclarationStatement(VarDeclarationStatement stmt);
    Object visitVarAssignmentStatement(VarAssignmentStatement stmt);
    Object visitExpressionStatement(ExpressionStatement stmt);
    Object visitIfStatement(IfStatement stmt);
    Object visitWhileStatement(WhileStatement stmt);
    Object visitRunStatement(RunStatement stmt);
    Object visitReturnStatement(ReturnStatement stmt);
    Object visitPrintStatement(PrintStatement stmt);
    Object visitFunctionDeclarationStatement(FunctionDeclarationStatement stmt);
}
