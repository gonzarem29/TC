import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TACGenerator {

    private List<Quadruple> code;
    private int tempCount;
    private int labelCount;

    public TACGenerator() {
        this.code = new ArrayList<>();
        this.tempCount = 0;
        this.labelCount = 0;
    }

    public String newTemp() {
        return "t" + (tempCount++);
    }

    public String newLabel() {
        return "L" + (labelCount++);
    }

    private void emit(String op, String arg1, String arg2, String result) {
        code.add(new Quadruple(op, arg1, arg2, result));
    }

    public List<Quadruple> getCode() {
        return code;
    }

    public void printCode() {
        System.out.println("\n--- CÓDIGO DE TRES DIRECCIONES (TAC) ---");
        for (Quadruple q : code) {
            System.out.println("  " + q.toString());
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Quadruple q : code) {
                writer.println(q.toString());
            }
            System.out.println("TAC guardado en: " + filename);
        } catch (IOException e) {
            System.err.println("Error al guardar TAC: " + e.getMessage());
        }
    }

    public void generate(ASTNode node) {
        genStmt(node);
    }

    private void genStmt(ASTNode node) {
        if (node == null) return;

        if (node instanceof BlockNode)          genBlock((BlockNode) node);
        else if (node instanceof FuncDeclNode)  genFuncDecl((FuncDeclNode) node);
        else if (node instanceof VarDeclNode)   genVarDecl((VarDeclNode) node);
        else if (node instanceof AssignNode)    genAssign((AssignNode) node);
        else if (node instanceof IfNode)        genIf((IfNode) node);
        else if (node instanceof WhileNode)     genWhile((WhileNode) node);
        else if (node instanceof ForNode)       genFor((ForNode) node);
        else if (node instanceof ReturnNode)    genReturn((ReturnNode) node);
        else if (node instanceof FuncCallNode) { genFuncCall((FuncCallNode) node); }
    }

    private String genExpr(ASTNode node) {
        if (node == null) return null;

        if (node instanceof NumNode)            return genNum((NumNode) node);
        if (node instanceof IdNode)             return genId((IdNode) node);
        if (node instanceof BinOp)              return genBinOp((BinOp) node);
        if (node instanceof UnaryOpNode)        return genUnaryOp((UnaryOpNode) node);
        if (node instanceof FuncCallNode)       return genFuncCallExpr((FuncCallNode) node);

        return null;
    }

    // --- Esqueleto: métodos a implementar ---

    private void genBlock(BlockNode node) {
        for (ASTNode s : node.sentencias) {
            genStmt(s);
        }
    }

    private void genFuncDecl(FuncDeclNode node) {
        System.out.println("[TAC] FuncDecl pendiente: " + node.id);
    }

    private void genVarDecl(VarDeclNode node) {
        System.out.println("[TAC] VarDecl pendiente: " + node.id);
    }

    private void genAssign(AssignNode node) {
        System.out.println("[TAC] Assign pendiente: " + node.id);
    }

    private void genIf(IfNode node) {
        System.out.println("[TAC] If pendiente");
    }

    private void genWhile(WhileNode node) {
        System.out.println("[TAC] While pendiente");
    }

    private void genFor(ForNode node) {
        System.out.println("[TAC] For pendiente");
    }

    private void genReturn(ReturnNode node) {
        System.out.println("[TAC] Return pendiente");
    }

    private void genFuncCall(FuncCallNode node) {
        System.out.println("[TAC] FuncCall pendiente: " + node.name);
    }

    private String genFuncCallExpr(FuncCallNode node) {
        System.out.println("[TAC] FuncCallExpr pendiente: " + node.name);
        return null;
    }

    private String genNum(NumNode node) {
        return node.value;
    }

    private String genId(IdNode node) {
        return node.name;
    }

    private String genBinOp(BinOp node) {
        String leftTemp = genExpr(node.left);
        String rightTemp = genExpr(node.right);
        String result = newTemp();
        emit(node.op, leftTemp, rightTemp, result);
        return result;
    }

    private String genUnaryOp(UnaryOpNode node) {
        String exprTemp = genExpr(node.expr);
        String op = node.op;

        if (op.equals("++") || op.equals("--")) {
            String result = newTemp();
            String arithOp = op.equals("++") ? "+" : "-";
            emit(arithOp, exprTemp, "1", result);
            emit("=", result, null, exprTemp);
            return result;
        }

        String result = newTemp();
        emit(op, exprTemp, null, result);
        return result;
    }
}
