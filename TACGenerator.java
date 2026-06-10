import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TACGenerator {

    private List<Quadruple> code;
    private int tempCount;
    private int labelCount;
    private Deque<String> breakLabels;
    private Deque<String> continueLabels;

    public TACGenerator() {
        this.code = new ArrayList<>();
        this.tempCount = 0;
        this.labelCount = 0;
        this.breakLabels = new ArrayDeque<>();
        this.continueLabels = new ArrayDeque<>();
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
        else if (node instanceof FuncCallNode)  genFuncCall((FuncCallNode) node);
        else if (node instanceof BreakNode)     genBreak();
        else if (node instanceof ContinueNode)  genContinue();
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

    private void genBlock(BlockNode node) {
        for (ASTNode s : node.sentencias) {
            genStmt(s);
        }
    }

    private void genFuncDecl(FuncDeclNode node) {
        emit("label", null, null, "FUNC_" + node.id);
        genStmt(node.body);
    }

    private void genVarDecl(VarDeclNode node) {
        if (node.initExpr != null) {
            String exprTemp = genExpr(node.initExpr);
            emit("=", exprTemp, null, node.id);
        }
    }

    private void genAssign(AssignNode node) {
        String exprTemp = genExpr(node.expr);
        emit("=", exprTemp, null, node.id);
    }

    private void genIf(IfNode node) {
        String labelElse = newLabel();
        String labelEnd = newLabel();
        String condTemp = genExpr(node.condition);
        emit("if_False", condTemp, null, labelElse);
        genStmt(node.thenBlock);
        emit("goto", null, null, labelEnd);
        emit("label", null, null, labelElse);
        if (node.elseBlock != null) {
            genStmt(node.elseBlock);
        }
        emit("label", null, null, labelEnd);
    }

    private void genWhile(WhileNode node) {
        String labelCond = newLabel();
        String labelEnd = newLabel();
        breakLabels.push(labelEnd);
        continueLabels.push(labelCond);
        emit("label", null, null, labelCond);
        String condTemp = genExpr(node.condition);
        emit("if_False", condTemp, null, labelEnd);
        genStmt(node.body);
        emit("goto", null, null, labelCond);
        emit("label", null, null, labelEnd);
        breakLabels.pop();
        continueLabels.pop();
    }

    private void genFor(ForNode node) {
        String labelCond = newLabel();
        String labelEnd = newLabel();
        breakLabels.push(labelEnd);
        continueLabels.push(labelCond);
        genStmt(node.init);
        emit("label", null, null, labelCond);
        String condTemp = genExpr(node.condition);
        emit("if_False", condTemp, null, labelEnd);
        genStmt(node.body);
        genStmt(node.update);
        emit("goto", null, null, labelCond);
        emit("label", null, null, labelEnd);
        breakLabels.pop();
        continueLabels.pop();
    }

    private void genReturn(ReturnNode node) {
        if (node.expr != null) {
            String exprTemp = genExpr(node.expr);
            emit("return", exprTemp, null, null);
        } else {
            emit("return", null, null, null);
        }
    }

    private void genFuncCall(FuncCallNode node) {
        for (ASTNode arg : node.args) {
            String argTemp = genExpr(arg);
            emit("param", argTemp, null, null);
        }
        String result = newTemp();
        emit("call", node.name, null, result);
    }

    private String genFuncCallExpr(FuncCallNode node) {
        for (ASTNode arg : node.args) {
            String argTemp = genExpr(arg);
            emit("param", argTemp, null, null);
        }
        String result = newTemp();
        emit("call", node.name, null, result);
        return result;
    }

    private void genBreak() {
        if (!breakLabels.isEmpty()) {
            emit("goto", null, null, breakLabels.peek());
        }
    }

    private void genContinue() {
        if (!continueLabels.isEmpty()) {
            emit("goto", null, null, continueLabels.peek());
        }
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
