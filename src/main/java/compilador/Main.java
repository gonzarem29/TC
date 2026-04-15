package compilador;

import gramatica.cpplexer;
import org.antlr.v4.runtime.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java Main <archivo_fuente.cpp>");
            System.exit(1);
        }

        boolean hasLexicalErrors = false;

        try {
            CharStream input = CharStreams.fromFileName(args[0]);
            
            cpplexer lexer = new cpplexer(input);
            
            lexer.removeErrorListeners();
            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
                                        int line, int charPositionInLine, String msg, RecognitionException e) {
                    System.err.println("\u001B[31mError léxico en línea " + line + ":" + charPositionInLine + " -> " + msg + "\u001B[0m");
                }
            });

            System.out.println("---------------------------------------------------------");
            System.out.printf("%-15s | %-20s | %-15s%n", "TIPO (TOKEN)", "LEXEMA", "POSICION (L:C)");
            System.out.println("---------------------------------------------------------");

            Token token;
            while ((token = lexer.nextToken()).getType() != Token.EOF) {
                if (token.getType() == cpplexer.ERROR) {
                    System.err.println("\u001B[31mError léxico en línea " + token.getLine() + ":" 
                            + token.getCharPositionInLine() + " -> Símbolo no reconocido: '" + token.getText() + "'\u001B[0m");
                    hasLexicalErrors = true;
                } else {
                    String tokenName = cpplexer.VOCABULARY.getSymbolicName(token.getType());
                    System.out.printf("%-15s | %-20s | %d:%d%n", 
                            tokenName, token.getText(), token.getLine(), token.getCharPositionInLine());
                }
            }
            System.out.println("---------------------------------------------------------");

            if (hasLexicalErrors) {
                System.err.println("\u001B[31mEl análisis finalizó con errores léxicos.\u001B[0m");
                System.exit(2);
            } else {
                System.out.println("\u001B[32mAnálisis léxico finalizado con éxito.\u001B[0m");
                System.exit(0);
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            System.exit(1);
        }
    }
}
