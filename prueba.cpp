int main() {
    // 1. Declaraciones válidas
    int x = 10;
    double y = 5.5;

    // 2. Operación válida (int + double promueve a double)
    double total = x + y;

    // 3. WARNING (amarillo): intentar meter un double en un int
    int perdida_precision = 3.14; 

    // 4. ERROR (rojo): usar variable sin declarar
    z = 100; 

    // 5. ERROR (rojo): declarar variable dos veces en el mismo scope
    int x = 20; 

    // 6. ERROR (rojo): declarar variable tipo void
    void invalido = 0;
}