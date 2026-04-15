// Archivo de prueba para el subconjunto de C++
int main() {
    double precio = 150.5;
    int cantidad = 3;
    
    /* Calculo del 
       total a pagar */
    double total = precio * cantidad;
    
    if (total >= 400.0) {
        total = total - 50.0; // Descuento
    }
    
    for (int i = 0; i < cantidad; i++) {
        precio = precio + 1;
    }
    
    return 0;
}
