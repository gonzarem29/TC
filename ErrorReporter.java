// ==========================================
// REPORTADOR DE ERRORES Y WARNINGS
//
// Centraliza todos los mensajes del análisis semántico
// usando códigos ANSI para diferenciar por severidad:
// ==========================================
public class ErrorReporter {

    private static final String ROJO = "\u001B[31m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String VERDE = "\u001B[32m";
    private static final String AZUL = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    // Contadores para el resumen final
    private int cantErrores = 0;
    private int cantWarnings = 0;

    // ------------------------------------------
    // ERROR CRÍTICO (Rojo)
    // ------------------------------------------
    public void error(int linea, String mensaje) {
        cantErrores++;
        System.out.println(ROJO + "[ERROR SEMÁNTICO | Línea " + linea + "] " + mensaje + RESET);
    }

    // ------------------------------------------
    // WARNING (Amarillo)
    // ------------------------------------------
    public void warning(int linea, String mensaje) {
        cantWarnings++;
        System.out.println(AMARILLO + "[WARNING | Línea " + linea + "] " + mensaje + RESET);
    }

    // ------------------------------------------
    // INFO — declaración o verificación exitosa (Verde)
    // ------------------------------------------
    public void info(String mensaje) {
        System.out.println(VERDE + "[OK] " + mensaje + RESET);
    }

    // ------------------------------------------
    // RESUMEN FINAL
    // ------------------------------------------
    public void printSummary() {
        System.out.println("\n" + AZUL + "══════════════════════════════════════════" + RESET);
        System.out.println(AZUL + "       RESUMEN DEL ANÁLISIS SEMÁNTICO     " + RESET);
        System.out.println(AZUL + "══════════════════════════════════════════" + RESET);
        if (cantErrores == 0 && cantWarnings == 0) {
            System.out.println(VERDE + " Análisis completado sin errores ni warnings." + RESET);
        } else {
            if (cantErrores > 0) {
                System.out.println(ROJO + " Errores críticos : " + cantErrores + RESET);
            }
            if (cantWarnings > 0) {
                System.out.println(AMARILLO + " Warnings         : " + cantWarnings + RESET);
            }
        }
        System.out.println(AZUL + "══════════════════════════════════════════\n" + RESET);
    }

    // Getters para que otras clases puedan consultar el estado
    public int getCantErrores() { return cantErrores; }
    public int getCantWarnings() { return cantWarnings; }
    public boolean hayErrores() { return cantErrores > 0; }
}