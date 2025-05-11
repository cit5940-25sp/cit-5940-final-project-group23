public interface ASTVisitor {
    // Expression methods
    Object visitBinaryExpression(BinaryExpression expr);
    Object visitUnaryExpression(UnaryExpression expr);
    Object visitLiteralExpression(LiteralExpression expr);
    Object visitVariableExpression(VariableExpression expr);
    Object visitGroupExpression(GroupExpression expr);
    Object visitCallExpression(CallExpression expr);
    
    // Statement methods
    Object visitVarDeclarationStatement(VarDeclarationStatement stmt);
    Object visitVarAssignmentStatement(VarAssignmentStatement stmt);
    Object visitExpressionStatement(ExpressionStatement stmt);
    // Other statement methods...
}
