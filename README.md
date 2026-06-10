# Compilador de Subconjunto C++

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![ANTLR4](https://img.shields.io/badge/ANTLR-4.13.1-9B59B6?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Estado](https://img.shields.io/badge/Estado-Etapa%204%20Completada-27AE60?style=for-the-badge)

**Trabajo Final — Técnicas de Compilación · 2026**  
*Gonzalo Gabriel Rementeria*

</div>

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Arquitectura del Compilador](#arquitectura-del-compilador)
- [Estado de Implementación](#estado-de-implementación)
- [Subconjunto de C++ Soportado](#subconjunto-de-c-soportado)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Instrucciones de Ejecución](#instrucciones-de-ejecución)
- [Ejemplos de Salida](#ejemplos-de-salida)
- [Próximas Etapas](#próximas-etapas)

---

## Descripción

Este repositorio contiene la implementación de un compilador completo para un subconjunto del lenguaje **C++**, desarrollado en **Java** con **ANTLR4** como herramienta de generación de analizadores léxicos y sintácticos, y **Maven** para la gestión de dependencias y construcción.

El proyecto se construye de forma incremental como Trabajo Final de la materia Técnicas de Compilación, abarcando todas las fases clásicas de la compilación: análisis léxico, sintáctico, semántico y generación de código intermedio (TAC).

---

## Arquitectura del Compilador

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
         │  AST verificado
         ▼
┌───────────────────────┐
│ Generación Cód. Inter.│  ← Quadruple.java / TACGenerator.java
│   (Etapa 4) ✅        │  →  Código de Tres Direcciones (TAC)
└────────┬──────────────┘
         │
         ▼
┌───────────────────┐
│   Optimización    │  (Etapa 5 — Próximamente)
└───────────────────┘
```

---

## Estado de Implementación

### Etapa 1 — Análisis Léxico ✅ Completa

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

### Etapa 2 — Análisis Sintáctico ✅ Completa

Implementada en `cppparser.g4` + `ASTVisitor.java` + `ASTNode.java`:

- **Gramática libre de contexto** con precedencia correcta de operadores (multiplicativa > aditiva > relacional > igualdad > lógica).
- **Visualización gráfica** del *Parse Tree* con `TreeViewer` de ANTLR (ventana interactiva, opcional).
- **Patrón Visitor** para traducción dirigida por sintaxis: recorre el *Parse Tree* de abajo hacia arriba y construye el **AST**.
- **AST** representado por una jerarquía de clases (`BinOp`, `VarDeclNode`, `FuncDeclNode`, `IfNode`, `ForNode`, `WhileNode`, `ReturnNode`, etc.), impreso en consola con indentación semántica.
- **Freno ante errores sintácticos**: si el parser encuentra errores, se detiene la compilación sin generar AST ni TAC.

---

### Etapa 3 — Análisis Semántico ✅ Completa

Implementada en `SemanticAnalyzer.java`, `SymbolTable.java`, `Symbol.java` y `ErrorReporter.java`:

**Tabla de Símbolos:**
- Estructura de pila de scopes (`Stack<Map<String, Symbol>>`).
- Apertura/cierre automático de ámbitos al entrar/salir de bloques, funciones, `if`, `for`, `while`.
- Búsqueda de identificadores desde el scope más interno hacia el global.

**Verificaciones implementadas:**

| Verificación | Severidad |
|---|---|
| Variable usada sin declarar | Error |
| Variable declarada dos veces en el mismo scope | Error |
| Variable declarada de tipo `void` | Error |
| Asignación a un identificador de función | Error |
| Tipos incompatibles en operación o asignación | Error |
| Instrucción `return` fuera de una función | Error |
| Condición de `if`/`while`/`for` no evaluable numéricamente | Error |
| Conversión implícita `double` → `int` (pérdida de precisión) | Warning |
| Promoción implícita `int` → `double` | Permitido |

**Reporte con colores ANSI:**
```
[ERROR SEMÁNTICO | Línea 5]  ...  (rojo)
[WARNING         | Línea 8]  ...  (amarillo)
[OK]                         ...  (verde)
```

---

### Etapa 4 — Generación de Código Intermedio TAC ✅ Completa

Implementada en `Quadruple.java` y `TACGenerator.java`:

- **Representación**: cuádruplas `(op, arg1, arg2, result)` para claridad y facilidad de optimización.
- **Recorrido postorden** del AST: las expresiones se evalúan de hojas a raíz, generando temporales intermedios.
- **Estructuras de control** traducidas con saltos condicionales e incondicionales usando etiquetas.

| Construcción | TAC Emitido |
|---|---|
| Constante `5` | Se retorna como `"5"` |
| Variable `x` | Se retorna como `"x"` |
| `a + b` | `t0 = a + b` |
| `x = expr` | `t1 = expr` / `x = t1` |
| `if (cond) { ... } else { ... }` | `if_False cond goto L0` / código then / `goto L1` / `L0:` código else / `L1:` |
| `while (cond) { ... }` | `L0:` cond / `if_False cond goto L1` / cuerpo / `goto L0` / `L1:` |
| `for (init; cond; update) { ... }` | init / `L0:` cond / `if_False goto L1` / cuerpo / update / `goto L0` / `L1:` |
| `break` | `goto Lend` (etiqueta de fin del bucle más interno) |
| `continue` | `goto Lcond` (etiqueta de condición del bucle más interno) |
| `func(...)` | `param x` / `param y` / `t0 = call func` |
| `return x` | `return x` |
| `++x` / `x++` | `t0 = x + 1` / `x = t0` |

**Salida:** código TAC en consola + archivo `output/<nombre>.tac`.

---

## Subconjunto de C++ Soportado

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

## Estructura del Proyecto

```
Final/
├── pom.xml                     ← Configuración Maven
├── src/
│   ├── main/
│   │   ├── java/               ← Código fuente del compilador
│   │   │   ├── Main.java            ← Orquestador (Etapas 1 a 4)
│   │   │   ├── ASTNode.java         ← Jerarquía de nodos del AST
│   │   │   ├── ASTVisitor.java      ← Visitor: Parse Tree → AST
│   │   │   ├── SemanticAnalyzer.java← Análisis semántico (Etapa 3)
│   │   │   ├── SymbolTable.java     ← Tabla de símbolos con scopes
│   │   │   ├── Symbol.java          ← Entidad de símbolo
│   │   │   ├── ErrorReporter.java   ← Reporte de errores con colores
│   │   │   ├── Quadruple.java       ← Estructura de cuádrupla TAC
│   │   │   └── TACGenerator.java    ← Generación de TAC (Etapa 4)
│   │   └── antlr4/             ← Gramáticas ANTLR
│   │       ├── cpplexer.g4          ← Gramática léxica
│   │       └── cppparser.g4         ← Gramática sintáctica
├── output/                     ← Archivos .tac generados
│   └── prueba_valida.tac
├── prueba.cpp                  ← Archivos de prueba .cpp
├── prueba_valida.cpp
├── prueba_errores.cpp
├── pruebaError.cpp
├── prueba2.cpp
├── prueba3.cpp
└── README.md
```

---

## Instrucciones de Ejecución

### Prerrequisitos

- Java 17+
- Apache Maven 3.x

### Compilar todo (ANTLR + código fuente)

```bash
mvn clean compile
```

### Ejecutar el compilador

```bash
mvn exec:java "-Dexec.args=prueba_valida.cpp"
```

Para probar con otros archivos:

```bash
mvn exec:java "-Dexec.args=prueba_errores.cpp"
```

> La ventana del árbol sintáctico se abre automáticamente cuando las dependencias de visualización están disponibles.

---

## Ejemplos de Salida

### Salida exitosa — `prueba_valida.cpp`

```
--- ARBOL SINTACTICO (PARSE TREE) ---
(prog (declaracion ... ) ... )

--- ARBOL DE SINTAXIS ABSTRACTA (AST) ---
Block:
  FuncDecl(int main)
    Block:
      VarDecl(int cantidad)
        Num(5)
      ...

══════════════════════════════════════════
 RESUMEN DEL ANALISIS SEMANTICO
══════════════════════════════════════════
 Analisis completado sin errores ni warnings.
══════════════════════════════════════════

══════════════════════════════════════════
 GENERANDO CODIGO INTERMEDIO (Etapa 4)
══════════════════════════════════════════

--- CODIGO DE TRES DIRECCIONES (TAC) ---
  FUNC_aplicarDescuento:
  factor = porcentaje
  t0 = precio * factor
  descuento = t0
  t1 = precio - descuento
  resultado = t1
  return resultado
  FUNC_maximo:
  resultado = 0
  t2 = a >= b
  if_False t2 goto L0
  resultado = a
  goto L1
  L0:
  resultado = b
  L1:
  return resultado
  FUNC_main:
  cantidad = 5
  precio = 299.99
  ...
  return 0
TAC guardado en: output/prueba_valida.tac
```

### Salida con errores sintácticos — `pruebaError.cpp`

```
Error sintáctico en línea 3:14 -> mismatched input '@' ...
Error sintáctico en línea 3:17 -> missing {'++', '--'} at ';'
Compilación detenida por errores sintácticos.
```

### Salida con errores semánticos — `prueba_errores.cpp`

```
══════════════════════════════════════════
 RESUMEN DEL ANALISIS SEMANTICO
══════════════════════════════════════════
 Errores críticos : 3
 Warnings         : 1
══════════════════════════════════════════

El proceso de compilación se detuvo debido a errores semánticos.
```

---

## Próximas Etapas

| Etapa | Descripción | Estado |
|-------|-------------|--------|
| **Etapa 5** | Optimización de código intermedio (propagación de constantes, eliminación de código muerto, subexpresiones comunes) | Pendiente |

---

<div align="center">

*Materia: Técnicas de Compilación · Universidad Blas Pascal · 2026*

</div>
