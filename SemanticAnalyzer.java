import java.util.HashMap;

// ==========================================
// ANALIZADOR SEMÁNTICO (Etapa 3 FINAL)
// Recorre el AST validando tipos, ámbitos y declaraciones
// Reportando la línea exacta del código fuente
// ==========================================
public class SemanticAnalyzer {

    private final SymbolTable tabla;
    private final ErrorReporter reporter;

    private String tipoFuncionActual = null;
    private String nombreFuncionActual = null;

    public SemanticAnalyzer() {
        this.tabla = new SymbolTable();
        this.reporter = new ErrorReporter();
    }

    public ErrorReporter getReporter() { return reporter; }

    public void analyze(ASTNode raiz) {
        System.out.println("\n\u001B[34m══════════════════════════════════════════\u001B[0m");
        System.out.println("\u001B[34m INICIANDO ANÁLISIS SEMÁNTICO (Etapa 3) \u001B[0m");
        System.out.println("\u001B[34m══════════════════════════════════════════\u001B[0m");
        analyzeNode(raiz);
        tabla.printTable();
    }

    private String analyzeNode(ASTNode node) {
        if (node == null) return "void";
        
        if (node instanceof BlockNode) return analyzeBlock((BlockNode) node);
        if (node instanceof FuncDeclNode) return analyzeFuncDecl((FuncDeclNode) node);
        if (node instanceof VarDeclNode) return analyzeVarDecl((VarDeclNode) node);
        if (node instanceof AssignNode) return analyzeAssign((AssignNode) node);
        
        if (node instanceof BinOp) return analyzeBinOp((BinOp) node);
        if (node instanceof UnaryOpNode) return analyzeUnaryOp((UnaryOpNode) node);
        if (node instanceof NumNode) return analyzeNum((NumNode) node);
        if (node instanceof IdNode) return analyzeId((IdNode) node);
        
        if (node instanceof IfNode) return analyzeIf((IfNode) node); 
        if (node instanceof ForNode) return analyzeFor((ForNode) node); 
        if (node instanceof WhileNode) return analyzeWhile((WhileNode) node);
        if (node instanceof ReturnNode) return analyzeReturn((ReturnNode) node);

        return "void";
    }

    private String analyzeBlock(BlockNode node) {
        tabla.enterScope();
        for (ASTNode sentencia : node.sentencias) {
            analyzeNode(sentencia);
        }
        tabla.exitScope();
        return "void";
    }

    private String analyzeFuncDecl(FuncDeclNode node) {
        if (tabla.isDeclaredLocally(node.id)) {
            reporter.error(node.line, "La función '" + node.id + "' ya ha sido declarada.");
            return "error";
        }

        Symbol sym = new Symbol(node.id, node.type, Symbol.Kind.FUNCION, node.line);
        tabla.define(sym);
        reporter.info("Función '" + node.id + "' declarada (" + node.type + ")");

        this.tipoFuncionActual = node.type;
        this.nombreFuncionActual = node.id;
        if (node.body != null) analyzeNode(node.body);
        this.tipoFuncionActual = null;
        this.nombreFuncionActual = null;
        return "void";
    }

    private String analyzeVarDecl(VarDeclNode node) {
        if (node.type.equals("void")) {
            reporter.error(node.line, "La variable '" + node.id + "' no puede ser declarada como 'void'.");
            return "error";
        }
        if (tabla.isDeclaredLocally(node.id)) {
            reporter.error(node.line, "La variable '" + node.id + "' ya fue declarada en este mismo ámbito.");
            return "error";
        }

        Symbol sym = new Symbol(node.id, node.type, Symbol.Kind.VARIABLE, node.line);
        tabla.define(sym);
        reporter.info("Variable declarada: " + node.id + " -> " + node.type);

        if (node.initExpr != null) {
            String exprType = analyzeNode(node.initExpr);
            verificarCompatibilidad(node.type, exprType, "Inicialización de '" + node.id + "'", node.line);
        }
        return "void";
    }

    private String analyzeAssign(AssignNode node) {
        Symbol sym = tabla.resolve(node.id);
        if (sym == null) {
            reporter.error(node.line, "Uso de variable no declarada: '" + node.id + "'");
            return "error";
        }
        if (sym.getKind() == Symbol.Kind.FUNCION) {
            reporter.error(node.line, "No se puede asignar un valor a la función '" + node.id + "'");
            return "error";
        }

        String exprType = analyzeNode(node.expr);
        verificarCompatibilidad(sym.getType(), exprType, "Asignación a la variable '" + node.id + "'", node.line);
        return sym.getType();
    }

    private String analyzeId(IdNode node) {
        Symbol sym = tabla.resolve(node.name);
        if (sym == null) {
            reporter.error(node.line, "Uso de identificador no declarado: '" + node.name + "'");
            return "error"; 
        }
        return sym.getType();
    }

    private String analyzeNum(NumNode node) {
        if (node.value.contains(".")) return "double";
        return "int";
    }

    private String analyzeUnaryOp(UnaryOpNode node) {
        String tipoExpr = analyzeNode(node.expr);
        if (tipoExpr.equals("error")) return "error";
        if (!tipoExpr.equals("int") && !tipoExpr.equals("double")) {
            reporter.error(node.line, "El operador unario '" + node.op + "' requiere un tipo numérico.");
            return "error";
        }
        return tipoExpr; 
    }

    private String analyzeBinOp(BinOp node) {
        String leftType = analyzeNode(node.left);
        String rightType = analyzeNode(node.right);
        if (leftType.equals("error") || rightType.equals("error")) return "error";

        if (node.op.matches("==|!=|<|<=|>|>=")) {
            if (!leftType.equals(rightType) && !(leftType.matches("int|double") && rightType.matches("int|double"))) {
                reporter.error(node.line, "Tipos incompatibles para comparación: '" + leftType + "' y '" + rightType + "'.");
                return "error";
            }
            return "int";
        }

        if (!leftType.matches("int|double") || !rightType.matches("int|double")) {
            reporter.error(node.line, "Operación matemática '" + node.op + "' inválida entre '" + leftType + "' y '" + rightType + "'.");
            return "error";
        }
        if (leftType.equals("double") || rightType.equals("double")) return "double";
        return "int";
    }

    private String analyzeIf(IfNode node) {
        String condType = analyzeNode(node.condition);
        if (!condType.equals("error") && !condType.equals("int") && !condType.equals("double")) {
            reporter.error(node.line, "La condición del 'if' debe ser evaluable numéricamente.");
        }
        analyzeNode(node.thenBlock);
        if (node.elseBlock != null) analyzeNode(node.elseBlock);
        return "void";
    }

    private String analyzeWhile(WhileNode node) {
        String condType = analyzeNode(node.condition);
        if (!condType.equals("error") && !condType.equals("int") && !condType.equals("double")) {
            reporter.error(node.line, "La condición del 'while' debe ser evaluable numéricamente.");
        }
        analyzeNode(node.body);
        return "void";
    }

    private String analyzeFor(ForNode node) {
        tabla.enterScope(); 
        if (node.init != null) analyzeNode(node.init);
        if (node.condition != null) {
            String condType = analyzeNode(node.condition);
            if (!condType.equals("error") && !condType.equals("int") && !condType.equals("double")) {
                reporter.error(node.line, "La condición del 'for' debe ser evaluable numéricamente.");
            }
        }
        if (node.update != null) analyzeNode(node.update);
        if (node.body != null) analyzeNode(node.body);
        tabla.exitScope();
        return "void";
    }

    private String analyzeReturn(ReturnNode node) {
        if (this.tipoFuncionActual == null) {
            reporter.error(node.line, "Instrucción 'return' fuera de una función.");
            return "error";
        }
        String exprType = "void";
        if (node.expr != null) exprType = analyzeNode(node.expr);
        verificarCompatibilidad(this.tipoFuncionActual, exprType, "Retorno de la función '" + this.nombreFuncionActual + "'", node.line);
        return "void";
    }

    // Recibe int line para reportarlo con precisión
    private void verificarCompatibilidad(String tipoDestino, String tipoValor, String contexto, int line) {
        if (tipoValor.equals("error")) return; 

        if (tipoDestino.equals("int") && tipoValor.equals("double")) {
            reporter.warning(line, contexto + ": Conversión implícita de 'double' a 'int'. Pérdida de precisión.");
        } 
        else if (tipoDestino.equals("double") && tipoValor.equals("int")) {
            // OK
        } 
        else if (!tipoDestino.equals(tipoValor)) {
            reporter.error(line, contexto + ": Tipos incompatibles. Se esperaba '" + tipoDestino + "' pero se encontró '" + tipoValor + "'.");
        }
    }
}