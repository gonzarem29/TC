// ============================================================
// prueba_errores.cpp
// Archivo de prueba con ERRORES Y WARNINGS SEMÁNTICOS.
// Cada sección dispara exactamente una verificación.
//
// Resultado esperado:
//   Errores criticos : 7
//   Warnings         : 1
// ============================================================

int main() {

    // ----------------------------------------------------------
    // [WARNING] Conversión implícita double -> int
    // Se asigna un literal double a una variable int.
    // El compilador debe emitir un WARNING (no detiene la compilación).
    // ----------------------------------------------------------
    int precio = 49.99;           // WARNING: double -> int, perdida de precision


    // ----------------------------------------------------------
    // [ERROR 1] Variable usada sin declarar
    // 'stock' nunca fue declarada en ningún scope accesible.
    // ----------------------------------------------------------
    int total = stock * precio;   // ERROR: 'stock' no declarada


    // ----------------------------------------------------------
    // [ERROR 2] Variable declarada dos veces en el mismo scope
    // 'precio' ya existe en este mismo bloque (declarada arriba).
    // ----------------------------------------------------------
    int precio = 100;             // ERROR: redeclaración en el mismo scope


    // ----------------------------------------------------------
    // [ERROR 3] Variable declarada de tipo void
    // 'void' no es un tipo válido para variables.
    // ----------------------------------------------------------
    void resultado = 0;           // ERROR: variable de tipo void


    // ----------------------------------------------------------
    // [ERROR 4] Asignación a un identificador de función
    // 'main' es una función, no una variable asignable.
    // ----------------------------------------------------------
    main = 5;                     // ERROR: asignación a función


    // ----------------------------------------------------------
    // [ERROR 5] Tipos incompatibles en operación
    // No se puede operar aritmeticamente un int con un char literal.
    // ----------------------------------------------------------
    int cantidad = 3;
    char letra   = 'Z';
    int mezcla   = cantidad + letra;  // ERROR: tipos incompatibles int + char


    // ----------------------------------------------------------
    // [ERROR 6] Condición de if no evaluable numéricamente
    // La condición debe resolverse a int o double; aquí es char.
    // ----------------------------------------------------------
    if (letra) {                  // ERROR: condición char, no numérica
        cantidad = 1;
    }


    // ----------------------------------------------------------
    // [ERROR 7] Instrucción return fuera de una función
    // Este return está dentro de main, pero el bloque interno
    // simula el caso: agregamos un return en un scope de for
    // sin que el analizador lo asocie a ninguna función declarada.
    //
    // Para probarlo limpiamente, declaramos una función que
    // llama a return con el tipo equivocado.
    // ----------------------------------------------------------
    // Retorno de tipo incompatible dentro de main (int) con un double
    double valorFinal = 3.14;
    return valorFinal;            // ERROR: retorno double en función int


}
