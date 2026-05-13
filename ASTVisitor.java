import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

// Extendemos de la clase base generada por ANTLR y le decimos que devolverá objetos ASTNode
public class ASTVisitor extends cppparserBaseVisitor<ASTNode> {

    // ==========================================
    // 1. REGLA INICIAL Y BLOQUES
    // ==========================================
    @Override
    public ASTNode visitProg(cppparser.ProgContext ctx) {
        BlockNode block = new BlockNode();
        for (cppparser.DeclaracionContext decl : ctx.declaracion()) {
            ASTNode node = visit(decl);
            if (node != null) block.addSentencia(node);
        }
        block.line = ctx.getStart().getLine();
        return block;
    }

    @Override
    public ASTNode visitBloque(cppparser.BloqueContext ctx) {
        BlockNode block = new BlockNode();
        for (cppparser.SentenciaContext sent : ctx.sentencia()) {
            ASTNode node = visit(sent);
            if (node != null) block.addSentencia(node);
        }
        block.line = ctx.getStart().getLine();
        return block;
    }

    // ==========================================
    // 2. DECLARACIONES DE FUNCIONES Y VARIABLES
    // ==========================================
    @Override
    public ASTNode visitDecFuncion(cppparser.DecFuncionContext ctx) {
        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();
        ASTNode body = visit(ctx.bloque());
        
        FuncDeclNode nodo = new FuncDeclNode(tipo, id, body);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitSentDecVariable(cppparser.SentDecVariableContext ctx) {
        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();
        ASTNode initExpr = null;
        if (ctx.expr() != null) {
            initExpr = visit(ctx.expr()); 
        }
        
        VarDeclNode nodo = new VarDeclNode(tipo, id, initExpr);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    // ==========================================
    // 3. ASIGNACIÓN
    // ==========================================
    @Override
    public ASTNode visitSentAsignacion(cppparser.SentAsignacionContext ctx) {
        String id = ctx.ID().getText();
        int ultimoIndice = ctx.expr().size() - 1;
        ASTNode expr = visit(ctx.expr(ultimoIndice)); 
        
        AssignNode nodo = new AssignNode(id, expr);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    // ==========================================
    // 4. SENTENCIAS DE CONTROL Y BUCLES
    // ==========================================
    @Override
    public ASTNode visitSentIf(cppparser.SentIfContext ctx) {
        ASTNode cond = visit(ctx.expr());
        ASTNode thenBlock = visit(ctx.bloque(0));
        ASTNode elseBlock = null;
        if (ctx.bloque().size() > 1) {
            elseBlock = visit(ctx.bloque(1));
        }
        
        IfNode nodo = new IfNode(cond, thenBlock, elseBlock);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitSentFor(cppparser.SentForContext ctx) {
        ASTNode init = visit(ctx.sentencia()); // ej: int i = 0;
        ASTNode cond = visit(ctx.expr(0));     // ej: i < cantidad
        ASTNode update = visit(ctx.expr(1));   // ej: i++
        ASTNode body = visit(ctx.bloque());
        
        ForNode nodo = new ForNode(init, cond, update, body);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitSentWhile(cppparser.SentWhileContext ctx) {
        ASTNode cond = visit(ctx.expr());
        ASTNode body = visit(ctx.bloque());
        
        WhileNode nodo = new WhileNode(cond, body);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    // ==========================================
    // 5. RETURN Y OPERACIONES UNARIAS (Statements)
    // ==========================================
    @Override
    public ASTNode visitSentReturn(cppparser.SentReturnContext ctx) {
        ASTNode expr = null;
        if (ctx.expr() != null) {
            expr = visit(ctx.expr());
        }
        
        ReturnNode nodo = new ReturnNode(expr);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitSentIncDec(cppparser.SentIncDecContext ctx) {
        ASTNode expr = visit(ctx.expr());
        String op = ctx.op.getText();
        
        UnaryOpNode nodo = new UnaryOpNode(op, expr);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    // ==========================================
    // 6. EXPRESIONES MATEMÁTICAS Y RELACIONALES
    // ==========================================
    @Override
    public ASTNode visitExprPostUnaria(cppparser.ExprPostUnariaContext ctx) {
        ASTNode expr = visit(ctx.expr());
        String op = ctx.op.getText();
        
        UnaryOpNode nodo = new UnaryOpNode(op, expr);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitExprAddSub(cppparser.ExprAddSubContext ctx) {
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        
        BinOp nodo = new BinOp(ctx.op.getText(), left, right);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitExprMulDiv(cppparser.ExprMulDivContext ctx) {
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        
        BinOp nodo = new BinOp(ctx.op.getText(), left, right);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitExprRelacional(cppparser.ExprRelacionalContext ctx) {
        ASTNode left = visit(ctx.expr(0));
        ASTNode right = visit(ctx.expr(1));
        
        BinOp nodo = new BinOp(ctx.op.getText(), left, right);
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitExprLiteral(cppparser.ExprLiteralContext ctx) {
        NumNode nodo = new NumNode(ctx.getText());
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitExprId(cppparser.ExprIdContext ctx) {
        IdNode nodo = new IdNode(ctx.ID().getText());
        nodo.line = ctx.getStart().getLine();
        return nodo;
    }

    @Override
    public ASTNode visitExprParentesis(cppparser.ExprParentesisContext ctx) {
        // Los paréntesis no necesitan un nodo propio en el AST ni línea, 
        // simplemente devuelven el nodo que está adentro.
        return visit(ctx.expr());
    }
}
