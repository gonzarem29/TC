import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.TreeViewer; 
import java.util.Arrays;            
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Uso: java Main <archivo_fuente.cpp>");
            System.exit(1);
        }

        try {
            // CORRECCIÓN: Se agregó  para convertir el String[] a String
            CharStream input = CharStreams.fromFileName(args[0]);
            
            // 1. Lexer (Etapa 1)
            cpplexer lexer = new cpplexer(input);
            
            // 2. Token stream (Conecta Lexer con Parser)
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            
            // 3. Parser (Etapa 2)
            cppparser parser = new cppparser(tokens);
            
            // 4. Manejo de Errores Sintácticos
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
                                        int line, int charPositionInLine, String msg, RecognitionException e) {
                    System.err.println("\u001B[31mError sintáctico en línea " + line + ":" + charPositionInLine + " -> " + msg + "\u001B[0m");
                }
            });

            // 5. Construir el árbol sintáctico desde la regla raíz 'prog'
            ParseTree tree = parser.prog();

            // 6. Visualizar el árbol sintáctico en formato texto (Consola)
            System.out.println("--- ÁRBOL SINTÁCTICO (PARSE TREE) ---");
            System.out.println(tree.toStringTree(parser));

            // 7. Visualizar el árbol gráficamente (Ventana Interactiva)
            System.out.println("Abriendo ventana con el Árbol Sintáctico...");
            TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
            viewer.open();

            // 8. CONSTRUCCIÓN DEL AST (Etapa 2 finalizada)
            System.out.println("Construyendo Árbol de Sintaxis Abstracta (AST)...");
            ASTVisitor visitor = new ASTVisitor();
            ASTNode astRoot = visitor.visit(tree); // Aquí recorremos el Parse Tree y generamos el AST
            System.out.println("\u001B[32m¡AST construido con éxito!\u001B[0m");
            System.out.println("\n--- ÁRBOL DE SINTAXIS ABSTRACTA (AST) ---");
            astRoot.print(""); // Imprime el AST limpio

              // ------------------------------------------------------
            // ETAPA 3: ANÁLISIS SEMÁNTICO (NUEVO)
            // ------------------------------------------------------
            SemanticAnalyzer analizadorSemantico = new SemanticAnalyzer();
            
            // Le pasamos la raíz del AST que construimos en la Etapa 2
            analizadorSemantico.analyze(astRoot);
            
            // Imprimimos el cuadro de resumen final con colores
            analizadorSemantico.getReporter().printSummary();
            
            // Si hay errores críticos, detenemos la compilación aquí mismo
            if (analizadorSemantico.getReporter().hayErrores()) {
                System.out.println("\u001B[31mEl proceso de compilación se detuvo debido a errores semánticos.\u001B[0m");
                return;
            }


        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            System.exit(1);
        }
    }
}
