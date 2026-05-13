# Compilador de Subconjunto C++ — Trabajo Final (Etapas 1 y 2)

**Materia:** Técnicas de Compilación  
**Autor:** Gonzalo Gabriel Rementeria

---

## 📌 Descripción del Proyecto
Este repositorio contiene la implementación de un compilador para un subconjunto del lenguaje C++. El proyecto se está desarrollando de forma incremental para el Trabajo Final de la materia [1].

Actualmente, el proyecto cubre exitosamente la **Etapa 1: Análisis Léxico** y la **Etapa 2: Análisis Sintáctico**. El compilador es capaz de escanear el código fuente, clasificar los tokens, validar la estructura gramatical, visualizar el Árbol de Derivación (Parse Tree) y construir un Árbol de Sintaxis Abstracta (AST) limpio y enfocado en la semántica [2].

---

## 🚀 Estado de Implementación: Etapa 1 (Analizador Léxico)
El analizador léxico (`cpplexer.g4`) cumple con todos los requisitos de la primera entrega:
1. **Reconocimiento de Tokens:** Tokenización exitosa utilizando expresiones regulares y reglas de ANTLR4.
2. **Tabla de Salida:** Generación por consola de una tabla estructurada mostrando el Tipo de Token, el Lexema y la Posición (Línea:Columna).
3. **Manejo de Errores Léxicos:** Implementación de regla *catch-all* para capturar caracteres inválidos, reportando su ubicación exacta sin detener el flujo de análisis [3].
4. **Cobertura de C++:** Reconoce tipos de datos (`int`, `double`, etc.), estructuras de control (`if`, `for`, `while`), literales, puntuación y operadores relacionales/matemáticos. Ignora el "ruido" como los espacios y comentarios [4].

---

## 🚀 Estado de Implementación: Etapa 2 (Analizador Sintáctico)
Se ha superado la segunda fase del compilador, encargada de validar la jerarquía del programa y construir sus estructuras de datos, cumpliendo los siguientes hitos [2]:

1. **Reglas Sintácticas (`cppparser.g4`):** Implementación de una gramática libre de contexto que respeta la precedencia de operadores matemáticos, las declaraciones de funciones (`main`), bloques de código, asignaciones y bucles (`for`, `while`) [5, 6].
2. **Visualización del Parse Tree:** Integración de la herramienta `TreeViewer` de ANTLR dentro de Java para desplegar una ventana gráfica interactiva con el Árbol de Derivación completo generado automáticamente (incluyendo todo el detalle sintáctico como llaves y puntos y comas) [7, 8].
3. **Patrón Visitor y Traducción Dirigida por Sintaxis:** Creación de un traductor semántico (`ASTVisitor.java`) que recorre el Parse Tree utilizando un flujo *bottom-up* para extraer solo la información relevante [9, 10].
4. **Construcción del Árbol de Sintaxis Abstracta (AST):** Diseño de una jerarquía de clases personalizadas en Java (`ASTNode.java`) con programación defensiva. El compilador construye internamente el AST y lo imprime en la consola mediante un formato de texto indentado, demostrando una estructura compacta y puramente semántica (sin ruido sintáctico) lista para ser evaluada [11, 12].

---

## 📂 Estructura del Proyecto
El proyecto utiliza una estructura de archivos plana ubicada en la raíz del repositorio para facilitar su compilación directa por consola:

```text
TC/
 ├── .gitignore
 ├── README.md
 ├── cpplexer.g4          <-- Gramática léxica (Tokens y Reglas base)
 ├── cppparser.g4         <-- Gramática sintáctica (Estructuras y Precedencia)
 ├── Main.java            <-- Orquestador principal (Lexer, Parser y GUI)
 ├── ASTNode.java         <-- Jerarquía de clases de los nodos semánticos del AST
 ├── ASTVisitor.java      <-- Visitor que traduce el Parse Tree al AST
 └── prueba.cpp           <-- Archivo de prueba con el código C++ fuente

--------------------------------------------------------------------------------
🛠️ Instrucciones de Ejecución
Para compilar y ejecutar el proyecto desde la terminal, posicionarse en la raíz del repositorio y ejecutar el siguiente flujo de comandos (requiere tener ANTLR4 configurado en el sistema)
:
Generar el Lexer, Parser y los base Visitors:
Compilar todas las clases Java:
Ejecutar el compilador sobre el archivo de prueba:

--------------------------------------------------------------------------------
📊 Ejemplo de Salida del Árbol Sintáctico Abstracto (AST)
Al ejecutar el programa, además de abrirse la ventana gráfica del Parse Tree, la consola imprimirá la versión compacta y semántica del código (el AST). Ejemplo de una asignación y bucle procesados:
--- ÁRBOL DE SINTAXIS ABSTRACTA (AST) ---
Block:
  FuncDecl(int main)
    Block:
      VarDecl(double precio)
        Num(150.5)
      VarDecl(int cantidad)
        Num(3)
      Assign(total)
        BinOp(*)
          Id(precio)
          Id(cantidad)
      For:
        Init:
          VarDecl(int i)
            Num(0)
        Condicion:
          BinOp(<)
            Id(i)
            Id(cantidad)
        Update:
          UnaryOp(++)
            Id(i)
        Cuerpo:
          Block:
            Assign(precio)
              BinOp(+)
                Id(precio)
                Num(1)
