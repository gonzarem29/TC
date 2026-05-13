import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

// Extendemos de la clase base generada por ANTLR y le decimos que devolverá objetos ASTNode
public class ASTVisitor extends cppparserBaseVisitor<ASTNode> {

    // 1. REGLA INICIAL (prog)
    @Override
    public ASTNode visitProg(cppparser.ProgContext ctx) {
        BlockNode block = new BlockNode();
        for (cppparser.DeclaracionContext decl : ctx.declaracion()) {
            ASTNode node = visit(decl);
            if (node != null) block.addSentencia(node);
        }
        return block;
    }

    // 2. FUNCIÓN PRINCIPAL O DECLARACIONES (ej: int main() { ... })
    @Override
    public ASTNode visitDecFuncion(cppparser.DecFuncionContext ctx) {
        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();
        ASTNode body = visit(ctx.bloque());
        return new FuncDeclNode(tipo, id, body);
    }

    // 3. BLOQUE DE CÓDIGO { ... }
    @Override
    public ASTNode visitBloque(cppparser.BloqueContext ctx) {
        BlockNode block = new BlockNode();
        for (cppparser.SentenciaContext sent : ctx.sentencia()) {
            ASTNode node = visit(sent);
            if (node != null) block.addSentencia(node);
        }
        return block;
    }

    // 4. DECLARACIÓN DE VARIABLES (ej: double precio = 150.5;)
    @Override
    public ASTNode visitSentDecVariable(cppparser.SentDecVariableContext ctx) {
        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();
        ASTNode initExpr = null;
        if (ctx.expr() != null) {
            initExpr = visit(ctx.expr()); 
        }
        return new VarDeclNode(tipo, id, initExpr);
    }

    // 5. ASIGNACIÓN (ej: total = precio * cantidad;)
    @Override
    public ASTNode visitSentAsignacion(cppparser.SentAsignacionContext ctx) {
        String id = ctx.ID().getText();
        int ultimoIndice = ctx.expr().size() - 1;
        ASTNode expr = visit(ctx.expr(ultimoIndice)); 
        return new AssignNode(id, expr);
    }

    // 6. IF-ELSE
    @Override
    public ASTNode visitSentIf(cppparser.SentIfContext ctx) {
        ASTNode cond = visit(ctx.expr());
        ASTNode thenBlock = visit(ctx.bloque(0));
        ASTNode elseBlock = null;
        if (ctx.bloque().size() > 1) {
            elseBlock = visit(ctx.bloque(1));
        }
        return new IfNode(cond, thenBlock, elseBlock);
    }

    // 7. BUCLE FOR
    @Override
    public ASTNode visitSentFor(cppparser.SentForContext ctx) {
        ASTNode init = visit(ctx.sentencia()); // ej: int i = 0;
        ASTNode cond = visit(ctx.expr(0));     // ej: i < cantidad
        ASTNode update = visit(ctx.expr(1));   // ej: i++
        ASTNode body = visit(ctx.bloque());
        return new ForNode(init, cond, update, body);
    }

    // 8. BUCLE WHILE
    @Override
    public ASTNode visitSentWhile(cppparser.SentWhileContext ctx) {
        ASTNode cond = visit(ctx.expr());
        ASTNode body = visit(ctx.bloque());
        return new WhileNode(cond, body);
    }

    // 9. RETURN
    @Override
    public ASTNode visitSentReturn(cppparser.SentReturnContext ctx) {
        ASTNode expr = null;
        if (ctx.expr() != null) {
            expr = visit(ctx.expr());
        }
        return new ReturnNode(expr);
    }

    // 10. SENTENCIA DE INCREMENTO/DECREMENTO INDEPENDIENTE (ej: i++;)
    @Override
    public ASTNode visitSentIncDec(cppparser.SentIncDecContext ctx) {
        ASTNode expr = visit(ctx.expr());
        String op = ctx.op.getText();
        return new UnaryOpNode(op, expr);
    }

    // ==========================================
    // EXPRESIONES MATEMÁTICAS Y RELACIONALES
    // ==========================================

    @Override
    public ASTNode visitExprPostUnaria(cppparser.ExprPostUnariaContext ctx) {
        ASTNode expr = visit(ctx.expr());
        String op = ctx.op.getText();
        return new UnaryOpNode(op, expr);
    }

    @Override
    public ASTNode visitExprAddSub(cppparser.ExprAddSubContext ctx) {
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        return new BinOp(ctx.op.getText(), left, right);
    }

    @Override
    public ASTNode visitExprMulDiv(cppparser.ExprMulDivContext ctx) {
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        return new BinOp(ctx.op.getText(), left, right);
    }

    @Override
    public ASTNode visitExprRelacional(cppparser.ExprRelacionalContext ctx) {
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        return new BinOp(ctx.op.getText(), left, right);
    }

    @Override
    public ASTNode visitExprLiteral(cppparser.ExprLiteralContext ctx) {
        return new NumNode(ctx.getText());
    }

    @Override
    public ASTNode visitExprId(cppparser.ExprIdContext ctx) {
        return new IdNode(ctx.ID().getText());
    }

    @Override
    public ASTNode visitExprParentesis(cppparser.ExprParentesisContext ctx) {
        return visit(ctx.expr());
    }
}