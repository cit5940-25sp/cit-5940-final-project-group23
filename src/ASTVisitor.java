public interface ASTVisitor {
     // Existing expression methods
     Object visitBinaryExpression(BinaryExpression expr);
     Object visitUnaryExpression(UnaryExpression expr);
     Object visitLiteralExpression(LiteralExpression expr);
     Object visitVariableExpression(VariableExpression expr);
     
     // New expression methods
     Object visitGroupExpression(GroupExpression expr);
     Object visitCallExpression(CallExpression expr);
     
     // New method
     Object visitAssignmentExpression(AssignmentExpression expr);
}
