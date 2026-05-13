// ============================================================
// prueba_valida.cpp
// Archivo de prueba VÁLIDO — cubre todas las construcciones
// soportadas sin generar errores semánticos.
// Resultado esperado: 0 errores, 0 warnings.
// ============================================================

// Función auxiliar: calcula el descuento sobre un precio
double aplicarDescuento(double precio, int porcentaje) {
    double factor = porcentaje;          // Promocion implicita int -> double (OK)
    double descuento = precio * factor;
    double resultado = precio - descuento;
    return resultado;
}

// Función auxiliar: devuelve el mayor de dos enteros
int maximo(int a, int b) {
    int resultado = 0;
    if (a >= b) {
        resultado = a;
    } else {
        resultado = b;
    }
    return resultado;
}

// Función principal
int main() {

    // ----------------------------------------------------------
    // 1. Declaración e inicialización de variables válidas
    // ----------------------------------------------------------
    int    cantidad  = 5;
    double precio    = 299.99;
    char   categoria = 'A';

    // ----------------------------------------------------------
    // 2. Promoción implícita int -> double (permitida, sin warning)
    // ----------------------------------------------------------
    double total = cantidad;    // int promovido a double: OK

    // ----------------------------------------------------------
    // 3. Operaciones aritméticas con tipos compatibles
    // ----------------------------------------------------------
    total = precio * cantidad;                 // double * int -> double
    double mitad = total / 2;                  // double / int -> double
    int    unidades = cantidad + 3;            // int + int -> int

    // ----------------------------------------------------------
    // 4. Operaciones relacionales y lógicas
    // ----------------------------------------------------------
    int esValido = 0;
    if (total >= 1000.0 && cantidad > 0) {
        esValido = 1;
    }

    // ----------------------------------------------------------
    // 5. Estructura if-else anidada
    // ----------------------------------------------------------
    double precioFinal = 0.0;
    if (esValido == 1) {
        precioFinal = aplicarDescuento(total, 10);
    } else {
        precioFinal = total;
    }

    // ----------------------------------------------------------
    // 6. Bucle for con variable de control local al scope del for
    // ----------------------------------------------------------
    double acumulado = 0.0;
    for (int i = 0; i < cantidad; i++) {
        acumulado = acumulado + precioFinal;
    }

    // ----------------------------------------------------------
    // 7. Bucle while con break y continue
    // ----------------------------------------------------------
    int intentos = 0;
    int limite   = 10;
    while (intentos < limite) {
        intentos = intentos + 1;
        if (intentos == 3) {
            continue;
        }
        if (intentos == 7) {
            break;
        }
    }

    // ----------------------------------------------------------
    // 8. Llamada a función y uso del resultado
    // ----------------------------------------------------------
    int mayorValor = maximo(cantidad, unidades);

    // ----------------------------------------------------------
    // 9. Reutilización de variables en scopes distintos (válido)
    // ----------------------------------------------------------
    if (mayorValor > 0) {
        int temporal = mayorValor * 2;   // 'temporal' existe solo en este scope
        acumulado = acumulado + temporal;
    }
    // Aquí 'temporal' ya no existe — no hay colisión

    // ----------------------------------------------------------
    // 10. Retorno de valor del tipo correcto (int)
    // ----------------------------------------------------------
    return 0;
}
