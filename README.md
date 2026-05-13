# 🛠️ Compilador de Subconjunto C++

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![ANTLR4](https://img.shields.io/badge/ANTLR-4.13.1-9B59B6?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Estado](https://img.shields.io/badge/Estado-Etapa%203%20Completada-27AE60?style=for-the-badge)

**Trabajo Final — Técnicas de Compilación · 2026**  
*Gonzalo Gabriel Rementeria*

</div>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura del Compilador](#-arquitectura-del-compilador)
- [Estado de Implementación](#-estado-de-implementación)
- [Subconjunto de C++ Soportado](#-subconjunto-de-c-soportado)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instrucciones de Ejecución](#️-instrucciones-de-ejecución)
- [Ejemplos de Salida](#-ejemplos-de-salida)
- [Próximas Etapas](#-próximas-etapas)

---

## 📌 Descripción

Este repositorio contiene la implementación de un compilador completo para un subconjunto del lenguaje **C++**, desarrollado en **Java** con **ANTLR4** como herramienta de generación de analizadores léxicos y sintácticos.

El proyecto se construye de forma incremental como Trabajo Final de la materia Técnicas de Compilación, abarcando todas las fases clásicas de la compilación: análisis léxico, sintáctico, semántico, generación de código intermedio y optimización.

---

## 🏗️ Arquitectura del Compilador

```
Código Fuente (.cpp)
        │
        ▼
┌───────────────────┐
│  Análisis Léxico  │  ← cpplexer.g4  →  Tabla de Tokens
│   (Etapa 1) ✅    │
└────────┬──────────┘
         │  Token Stream
         ▼
┌───────────────────┐
│ Análisis Sintáct. │  ← cppparser.g4  →  Parse Tree / AST
│   (Etapa 2) ✅    │
└────────┬──────────┘
         │  AST
         ▼
┌───────────────────┐
│ Análisis Semánt.  │  ← SemanticAnalyzer.java  →  Tabla de Símbolos
│   (Etapa 3) ✅    │                             →  Errores / Warnings
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│  Generación Cód.  │  (Etapa 4 — Próximamente)
│    Intermedio     │
└────────┬──────────┘
         │
         ▼
┌───────────────────┐
│   Optimización    │  (Etapa 5 — Próximamente)
└───────────────────┘
```

---

## ✅ Estado de Implementación

### Etapa 1 — Análisis Léxico `✅ Completa`

Implementada en `cpplexer.g4`, cubre:

| Categoría | Elementos |
|-----------|-----------|
| **Tipos de dato** | `int`, `double`, `char`, `void` |
| **Estructuras de control** | `if`, `else`, `for`, `while`, `break`, `continue`, `return` |
| **Literales** | Enteros, doubles, caracteres (`'x'`), strings (`"..."`) |
| **Operadores** | Aritméticos, relacionales, lógicos, incremento/decremento |
| **Puntuación** | `()`, `{}`, `[]`, `;`, `,` |
| **Ruido** | Espacios, comentarios de línea y bloque (ignorados con `skip`) |
| **Errores léxicos** | Regla *catch-all* (`ERROR : .`) que captura caracteres inválidos sin detener el análisis |

**Salida:** tabla de tokens por consola con Tipo, Lexema y Posición (línea:columna).

---

### Etapa 2 — Análisis Sintáctico `✅ Completa`

Implementada en `cppparser.g4` + `ASTVisitor.java` + `ASTNode.java`:

- **Gramática libre de contexto** con precedencia correcta de operadores (multiplicativa > aditiva > relacional > igualdad > lógica).
- **Visualización gráfica** del *Parse Tree* con `TreeViewer` de ANTLR (ventana interactiva).
- **Patrón Visitor** para traducción dirigida por sintaxis: recorre el *Parse Tree* de abajo hacia arriba y construye el **AST**.
- **AST** representado por una jerarquía de clases (`BinOp`, `VarDeclNode`, `FuncDeclNode`, `IfNode`, `ForNode`, `WhileNode`, `ReturnNode`, etc.), impreso en consola con indentación semántica.

---

### Etapa 3 — Análisis Semántico `✅ Completa`

Implementada en `SemanticAnalyzer.java`, `SymbolTable.java`, `Symbol.java` y `ErrorReporter.java`:

**Tabla de Símbolos:**
- Estructura de pila de scopes (`Stack<Map<String, Symbol>>`).
- Apertura/cierre automático de ámbitos al entrar/salir de bloques, funciones, `if`, `for`, `while`.
- Búsqueda de identificadores desde el scope más interno hacia el global.

**Verificaciones implementadas:**

| Verificación | Severidad |
|---|---|
| Variable usada sin declarar | 🔴 Error |
| Variable declarada dos veces en el mismo scope | 🔴 Error |
| Variable declarada de tipo `void` | 🔴 Error |
| Asignación a un identificador de función | 🔴 Error |
| Tipos incompatibles en operación o asignación | 🔴 Error |
| Instrucción `return` fuera de una función | 🔴 Error |
| Condición de `if`/`while`/`for` no evaluable numéricamente | 🔴 Error |
| Conversión implícita `double` → `int` (pérdida de precisión) | 🟡 Warning |
| Promoción implícita `int` → `double` | ✅ Permitido |

**Reporte con colores ANSI:**
```
[ERROR SEMÁNTICO | Línea 5]  ...  (rojo)
[WARNING         | Línea 8]  ...  (amarillo)
[OK]                         ...  (verde)
```

---

## 🧩 Subconjunto de C++ Soportado

```cpp
// Tipos de dato
int     x = 10;
double  precio = 150.5;
char    letra = 'A';
void    miFuncion() { ... }

// Estructuras de control
if (x > 5) { ... } else { ... }
while (x > 0) { ... }
for (int i = 0; i < 10; i++) { ... }
break;
continue;

// Expresiones
x = a + b * c - d / e % f;
x == y,  x != y,  x < y,  x <= y,  x > y,  x >= y
a && b,  a || b,  !a
i++,  i--,  ++i,  --i

// Funciones
int suma(int a, int b) { return a + b; }
int main() { ... return 0; }
```

---

## 📂 Estructura del Proyecto

```
TC/
├── cpplexer.g4          ← Gramática léxica (tokens y reglas)
├── cppparser.g4         ← Gramática sintáctica (estructura y precedencia)
│
├── Main.java            ← Orquestador principal (Etapas 1, 2 y 3)
├── ASTNode.java         ← Jerarquía de nodos del AST
├── ASTVisitor.java      ← Visitor: Parse Tree → AST
│
├── SemanticAnalyzer.java ← Analizador semántico (Etapa 3)
├── SymbolTable.java      ← Tabla de símbolos con manejo de scopes
├── Symbol.java           ← Entidad de símbolo (variable / función)
├── ErrorReporter.java    ← Sistema de reporte con colores ANSI
│
├── pruebas/
│   ├── prueba.cpp        ← Programa de prueba válido
│   └── pruebaErronea.cpp ← Programa con errores léxicos/semánticos
│
├── pom.xml              ← Configuración Maven (dependencias ANTLR4)
└── README.md
```

---

## 🛠️ Instrucciones de Ejecución

### Prerrequisitos

- Java 17+
- Maven 3.x
- ANTLR4 Tool (para generar fuentes desde las gramáticas)

### Paso 1 — Generar el Lexer y Parser desde las gramáticas

```bash
antlr4 -visitor cpplexer.g4 cppparser.g4
```

### Paso 2 — Compilar todas las clases Java

```bash
javac *.java 
```

### Paso 3 — Ejecutar el compilador

```bash
java Main prueba.cpp 
```



---

## 📊 Ejemplos de Salida

### AST generado para `prueba.cpp`

```
--- ÁRBOL DE SINTAXIS ABSTRACTA (AST) ---
Block:
  FuncDecl(int main)
    Block:
      VarDecl(double precio)
        Num(150.5)
      VarDecl(int cantidad)
        Num(3)
      VarDecl(double total)
        BinOp(*)
          Id(precio)
          Id(cantidad)
      If:
        Condicion:
          BinOp(>=)
            Id(total)
            Num(400.0)
        Entonces:
          Block:
            Assign(total)
              BinOp(-)
                Id(total)
                Num(50.0)
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
      Return:
        Num(0)
```

### Reporte semántico para `prueba.cpp` (con errores)

```
══════════════════════════════════════════
       RESUMEN DEL ANÁLISIS SEMÁNTICO
══════════════════════════════════════════
 Errores críticos : 3
 Warnings         : 1
══════════════════════════════════════════
```

---

## 🔮 Próximas Etapas

| Etapa | Descripción | Estado |
|-------|-------------|--------|
| **Etapa 4** | Generación de código intermedio de tres direcciones | 🔲 Pendiente |
| **Etapa 5** | Optimización (propagación de constantes, eliminación de código muerto, subexpresiones comunes) | 🔲 Pendiente |

---

<div align="center">

*Materia: Técnicas de Compilación · Universidad · 2026*

</div>
