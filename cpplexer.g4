lexer grammar cpplexer;

// ==========================================
// 1. PALABRAS RESERVADAS (KEYWORDS)
// Tipos de datos soportados:
INT    : 'int' ;
CHAR   : 'char' ;
DOUBLE : 'double' ;
VOID   : 'void' ;

// Estructuras de control y bucles:
IF       : 'if' ;
ELSE     : 'else' ;
FOR      : 'for' ;
WHILE    : 'while' ;
BREAK    : 'break' ;
CONTINUE : 'continue' ;
RETURN   : 'return' ;

// ==========================================
// 2. IDENTIFICADORES (Variables y Funciones)
ID : [a-zA-Z_][a-zA-Z0-9_]* ;

// ==========================================
// 3. LITERALES
NUM_INT    : [+-]?[0-9]+ ;
NUM_DOUBLE : [+-]?[0-9]+ '.' [0-9]+ ;
CHAR_LIT : '\'' . '\'' ;
STRING_LIT : '"' ~["\r\n]* '"' ;

// ==========================================
// 4. OPERADORES Y ASIGNACIÓN (Nombres semánticos)
EQ  : '==' ;
NE  : '!=' ;
LE  : '<=' ;
GE  : '>=' ;
AND : '&&' ;
OR  : '||' ;
INC : '++' ;
DEC : '--' ;

// Un carácter
ASSIGN : '=' ;
PLUS   : '+' ;
MINUS  : '-' ;
MUL    : '*' ;
DIV    : '/' ;
MOD    : '%' ;
LT     : '<' ;
GT     : '>' ;
NOT    : '!' ;

// ==========================================
// 5. SÍMBOLOS DE AGRUPACIÓN Y PUNTUACIÓN
LPAREN    : '(' ;
RPAREN    : ')' ;
LBRACE    : '{' ;
RBRACE    : '}' ;
LBRACKET  : '[' ; 
RBRACKET  : ']' ; 
SEMICOLON : ';' ;
COMMA     : ',' ;

// ==========================================
// 6. RUIDO (Comentarios y Espacios) - Se ignoran
WS            : [ \t\r\n]+ -> skip ;
LINE_COMMENT  : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;

// ==========================================
// 7. MANEJO DE ERRORES LÉXICOS
// Estrategia Catch-all: Captura cualquier carácter inválido
ERROR : . ;