import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    // La pila que representa la jerarquía de scopes [4, 5].
    // Cada Map representa un scope individual (nombre -> Symbol)
    private Stack<Map<String, Symbol>> scopes = new Stack<>();

    public SymbolTable() {
        // Al instanciar la tabla, abrimos automáticamente el scope global (nivel 0)
        enterScope();
    }

    // Abre un nuevo nivel (al entrar a un bloque {}, función, if, for) [4]
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    // Cierra el nivel actual, eliminando todas sus variables locales [6]
    public void exitScope() {
        // Mejora de seguridad: evitar EmptyStackException [6]
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    // Inserta un símbolo en el scope actual [2]
    public void define(Symbol sym) {
        if (!scopes.isEmpty()) {
            // Usamos getName() de tu clase Symbol
            scopes.peek().put(sym.getName(), sym); 
        }
    }

    // Busca un símbolo desde el scope más interno hacia el externo [3]
    public Symbol resolve(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        return null; // Si termina el bucle y no lo encuentra, la variable no existe
    }

    // Verifica si una variable ya fue declarada en el scope ACTUAL 
    public boolean isDeclaredLocally(String name) {
        if (scopes.isEmpty()) return false;
        return scopes.peek().containsKey(name);
    }

    // Método para imprimir la tabla al finalizar el análisis
    public void printTable() {
        System.out.println("\n\u001B[36m--- ESTADO FINAL DE LA TABLA DE SÍMBOLOS (SCOPE GLOBAL) ---\u001B[0m");
        if (!scopes.isEmpty()) {
            Map<String, Symbol> globalScope = scopes.get(0);
            for (Symbol sym : globalScope.values()) {
                System.out.println("  " + sym.toString());
            }
        }
    }
}