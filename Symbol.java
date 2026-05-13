import java.util.List;
import java.util.ArrayList;

// ==========================================
// SÍMBOLO — una entrada en la Tabla de Símbolos
// Representa tanto variables como funciones declaradas.
// ==========================================
public class Symbol {

    // ------------------------------------------
    // Tipos posibles de símbolo (kind)
    // ------------------------------------------
    public enum Kind {
        VARIABLE,
        FUNCION
    }

    // ------------------------------------------
    // Atributos del símbolo
    // ------------------------------------------
    private final String name;          // Nombre del identificador  (ej: "precio", "main")
    private final String type;          // Tipo de dato              (ej: "int", "double", "void")
    private final Kind   kind;          // Categoría                 (VARIABLE o FUNCION)
    private final int    line;          // Línea de declaración      (para mensajes de error precisos)

    // Solo para Kind.FUNCION: lista de tipos de parámetros en orden
    private final List<String> paramTypes;

    // Marca para detectar variables declaradas pero nunca usadas (warning)
    private boolean used;

    // ------------------------------------------
    // Constructor para VARIABLES
    // ------------------------------------------
    public Symbol(String name, String type, Kind kind, int line) {
        this.name       = name;
        this.type       = type;
        this.kind       = kind;
        this.line       = line;
        this.paramTypes = new ArrayList<>();  // vacío para variables
        this.used       = false;
    }

    // ------------------------------------------
    // Constructor para FUNCIONES (incluye parámetros)
    // ------------------------------------------
    public Symbol(String name, String type, Kind kind, int line, List<String> paramTypes) {
        this.name       = name;
        this.type       = type;
        this.kind       = kind;
        this.line       = line;
        this.paramTypes = new ArrayList<>(paramTypes);
        this.used       = false;
    }

    // ------------------------------------------
    // Getters
    // ------------------------------------------
    public String       getName()       { return name; }
    public String       getType()       { return type; }
    public Kind         getKind()       { return kind; }
    public int          getLine()       { return line; }
    public List<String> getParamTypes() { return paramTypes; }
    public boolean      isUsed()        { return used; }

    // ------------------------------------------
    // Marcar el símbolo como "usado"
    // Se llama desde el SemanticAnalyzer cada vez que se
    // encuentra una referencia a este nombre en el código.
    // ------------------------------------------
    public void markAsUsed() {
        this.used = true;
    }

    // ------------------------------------------
    // Representación legible (útil para imprimir la tabla)
    // ------------------------------------------
    @Override
    public String toString() {
        if (kind == Kind.FUNCION) {
            return String.format("%-15s | %-8s | %-10s | línea %-4d | params: %s",
                    name, type, kind, line, paramTypes);
        }
        return String.format("%-15s | %-8s | %-10s | línea %-4d",
                name, type, kind, line);
    }
}
