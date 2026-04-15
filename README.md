#  	Técnicas de Compilación - Trabajo Final - Primera parte

##  Resumen 
Diseñar e implementar un compilador para un subconjunto del lenguaje C++ utilizando la herramienta ANTLR4, aplicando los conceptos teóricos y prácticos vistos en la materia de Técnicas de Compilación. 
##  Descripción 
El estudiante deberá desarrollar un compilador completo que sea capaz de analizar, verificar y generar código para programas escritos en un subconjunto del lenguaje C++. El compilador deberá implementar todas las fases del proceso de compilación: análisis léxico, análisis sintáctico, análisis semántico, generación de código intermedio y optimización. 

##  Funcionalidades Requeridas 
###  1. Análisis Léxico 
● Implementar un analizador léxico utilizando ANTLR4 que reconozca los tokens del lenguaje. 

● Identificar y reportar errores léxicos. 

● Generar una tabla de tokens. 

###  2. Análisis Sintáctico 
● Implementar un analizador sintáctico utilizando ANTLR4 que verifique la estructura gramatical del programa. 

● Construir un árbol de sintaxis abstracta (AST). 

● Identificar y reportar errores sintácticos. 

● Visualizar el árbol sintáctico generado. 

###  3. Análisis Semántico 
● Implementar un analizador semántico que verifique la coherencia semántica del programa. 

● Construir y mantener una tabla de símbolos. 

● Verificar tipos de datos y compatibilidad en operaciones. 

● Verificar el ámbito de las variables y funciones. 

● Reportar errores semánticos (con detalles específicos). 

● Distinguir entre errores (críticos) y warnings (no críticos). 

###  4. Generación de Código Intermedio 
● Implementar un generador de código de tres direcciones. 

● Manejar expresiones aritméticas y lógicas. 

● Manejar estructuras de control (if-else, bucles). 

● Manejar llamadas a funciones y retorno de valores. 

###  5. Optimización de Código 
● Implementar al menos tres técnicas de optimización, que pueden incluir: 

○ Propagación de constantes 

○ Eliminación de código muerto 


○ Simplificación de expresiones 

○ Eliminación de subexpresiones comunes 

○ Optimización de bucles 

###  6. Salidas del Compilador 
● Generar archivos de salida para el código intermedio y optimizado. 

● Implementar un sistema de reporte de errores y warnings que utilice colores para diferenciarlos (verde para éxito, amarillo para warnings, rojo para errores). 

Subconjunto del Lenguaje C++ a Implementar 
Tipos de Datos 
● int 

● char 

● double 

● void (para funciones) 

Estructuras de Control 
● Condicionales (if-else) 

● Bucles (for, while) 

● Sentencias de control de bucle (break, continue) 

Elementos del Lenguaje 
● Declaración de variables 

● Declaración de funciones 

● Expresiones aritméticas y lógicas 

● Llamadas a funciones 

● Retorno de valores 

● Asignaciones 
