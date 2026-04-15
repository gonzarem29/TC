# Compilador de Subconjunto C++ — Trabajo Final (Etapa 1)

**Materia:** Técnicas de Compilación  
**Autor:** Gonzalo Gabriel Rementeria  

---

## 📌 Descripción del Proyecto
Este repositorio contiene la implementación de un compilador para un subconjunto del lenguaje C++. El proyecto se está desarrollando de forma incremental y actualmente cubre la **Etapa 1: Análisis Léxico**.

El objetivo principal de esta etapa es leer un archivo fuente en C++, escanear el flujo de caracteres e identificar los lexemas válidos, transformándolos en **Tokens** clasificados, o reportando un error léxico si se encuentra un símbolo no reconocido por la gramática.

## 🚀 Estado de Implementación: Etapa 1 (Analizador Léxico)
El analizador léxico actual cumple con todos los requisitos de la primera entrega:
1. **Reconocimiento de Tokens:** Tokenización exitosa utilizando expresiones regulares y reglas de ANTLR4.
2. **Tabla de Salida:** Generación por consola de una tabla estructurada mostrando el Tipo de Token, el Lexema y la Posición (Línea:Columna).
3. **Manejo de Errores Léxicos:** Implementación de reglas *catch-all* para capturar caracteres inválidos, reportando su ubicación exacta sin detener el flujo de análisis.
4. **Códigos de Salida:** El programa finaliza con código `0` (éxito) o `2` (errores léxicos) y utiliza colores en la terminal (Verde para éxito, Rojo para errores).

### Cobertura del Subconjunto de C++
El lexer (`cpplexer.g4`) es capaz de reconocer:
* **Tipos de Datos:** `int`, `char`, `double`, `void`.
* **Palabras Reservadas:** `if`, `else`, `for`, `while`, `break`, `continue`, `return`.
* **Literales:** Números enteros (`NUM_INT`), números decimales (`NUM_DOUBLE`), caracteres (`CHAR`) y cadenas de texto (`STRING`).
* **Operadores:** 
  * Aritméticos: `+`, `-`, `*`, `/`, `%`, `++`, `--`
  * Relacionales: `==`, `!=`, `<`, `<=`, `>`, `>=`
  * Lógicos: `&&`, `||`, `!`
  * Asignación: `=`
* **Puntuación y Agrupación:** `(`, `)`, `{`, `}`, `;`, `,`
* **Ruido (Ignorado):** Espacios en blanco, tabulaciones, saltos de línea, comentarios de una línea (`//`) y comentarios de bloque (`/* */`).

---

## 📂 Estructura del Proyecto

```text
TC/
 ├── .gitignore
 ├── README.md
 ├── pom.xml                                 <-- Configuración de Maven y dependencias ANTLR
 ├── pruebas/
 │    └── prueba.cpp                         <-- Archivo de prueba con código C++ válido
 └── src/
      └── main/
           ├── antlr4/gramatica/
           │    └── cpplexer.g4              <-- Gramática léxica principal
           └── java/compilador/
                └── Main.java                <-- Orquestador principal

--------------------------------------------------------------------------------
📊 Ejemplo de Salida Esperada
Al ejecutar el programa sobre un archivo C++ válido, la consola imprimirá lo siguiente:
---------------------------------------------------------
TIPO (TOKEN)    | LEXEMA               | POSICION (L:C)
---------------------------------------------------------
INT             | int                  | 2:0
ID              | main                 | 2:4
LPAREN          | (                    | 2:8
RPAREN          | )                    | 2:9
LBRACE          | {                    | 2:11
DOUBLE          | double               | 3:4
...
---------------------------------------------------------
