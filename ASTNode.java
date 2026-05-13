import java.util.ArrayList;
import java.util.List;

// 1. CLASE BASE PADRE
public abstract class ASTNode { 
    public int line;
    public abstract void print(String indent);
}

// ==========================================
// 2. EXPRESIONES (Nodos que devuelven valores)
// ==========================================

class BinOp extends ASTNode {
    String op;
    ASTNode left, right;

    public BinOp(String op, ASTNode left, ASTNode right) {
        this.op = op; this.left = left; this.right = right;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "BinOp(" + op + ")");
        if (left != null) left.print(indent + "  ");
        if (right != null) right.print(indent + "  ");
    }
}

class UnaryOpNode extends ASTNode {
    String op;
    ASTNode expr;

    public UnaryOpNode(String op, ASTNode expr) {
        this.op = op; this.expr = expr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "UnaryOp(" + op + ")");
        if (expr != null) expr.print(indent + "  ");
    }
}

class NumNode extends ASTNode {
    String value;

    public NumNode(String value) {
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Num(" + value + ")");
    }
}

class IdNode extends ASTNode {
    String name;

    public IdNode(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Id(" + name + ")");
    }
}

// ==========================================
// 3. ESTRUCTURAS DE CONTROL Y BLOQUES
// ==========================================

class FuncDeclNode extends ASTNode {
    String type, id;
    ASTNode body;
    List<VarDeclNode> params = new ArrayList<>();
    public FuncDeclNode(String type, String id, ASTNode body) {
        this.type = type; this.id = id; this.body = body;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "FuncDecl(" + type + " " + id + ")");
        if (body != null) body.print(indent + "  ");
    }
}

class FuncCallNode extends ASTNode {
    String name;
    List<ASTNode> args = new ArrayList<>();

    public FuncCallNode(String name) { this.name = name; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "FuncCall(" + name + ")");
        for (ASTNode a : args) a.print(indent + "  ");
    }
}

class BlockNode extends ASTNode {
    List<ASTNode> sentencias = new ArrayList<>();

    public void addSentencia(ASTNode sentencia) {
        this.sentencias.add(sentencia);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Block:");
        for (ASTNode s : sentencias) {
            if (s != null) s.print(indent + "  ");
        }
    }
}

class VarDeclNode extends ASTNode {
    String type, id;
    ASTNode initExpr;

    public VarDeclNode(String type, String id, ASTNode initExpr) {
        this.type = type; this.id = id; this.initExpr = initExpr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "VarDecl(" + type + " " + id + ")");
        if (initExpr != null) {
            initExpr.print(indent + "  ");
        }
    }
}

class AssignNode extends ASTNode {
    String id;
    ASTNode expr;

    public AssignNode(String id, ASTNode expr) {
        this.id = id; this.expr = expr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Assign(" + id + ")");
        if (expr != null) expr.print(indent + "  ");
    }
}

class IfNode extends ASTNode {
    ASTNode condition, thenBlock, elseBlock;

    public IfNode(ASTNode condition, ASTNode thenBlock, ASTNode elseBlock) {
        this.condition = condition; this.thenBlock = thenBlock; this.elseBlock = elseBlock;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "If:");
        System.out.println(indent + "  Condicion:");
        if (condition != null) condition.print(indent + "    ");
        System.out.println(indent + "  Entonces:");
        if (thenBlock != null) thenBlock.print(indent + "    ");
        if (elseBlock != null) {
            System.out.println(indent + "  Sino:");
            elseBlock.print(indent + "    ");
        }
    }
}

class ForNode extends ASTNode {
    ASTNode init, condition, update, body;

    public ForNode(ASTNode init, ASTNode condition, ASTNode update, ASTNode body) {
        this.init = init; this.condition = condition; this.update = update; this.body = body;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "For:");
        System.out.println(indent + "  Init:");
        if (init != null) init.print(indent + "    ");
        System.out.println(indent + "  Condicion:");
        if (condition != null) condition.print(indent + "    ");
        System.out.println(indent + "  Update:");
        if (update != null) update.print(indent + "    ");
        System.out.println(indent + "  Cuerpo:");
        if (body != null) body.print(indent + "    ");
    }
}

class WhileNode extends ASTNode {
    ASTNode condition, body;

    public WhileNode(ASTNode condition, ASTNode body) {
        this.condition = condition; this.body = body;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "While:");
        System.out.println(indent + "  Condicion:");
        if (condition != null) condition.print(indent + "    ");
        System.out.println(indent + "  Cuerpo:");
        if (body != null) body.print(indent + "    ");
    }
}

class ReturnNode extends ASTNode {
    ASTNode expr;

    public ReturnNode(ASTNode expr) {
        this.expr = expr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Return:");
        if (expr != null) expr.print(indent + "  ");
    }
}
