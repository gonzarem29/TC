parser grammar cppparser;

options {
    tokenVocab=cpplexer;
}

// ==========================================
// 1. REGLA INICIAL
// ==========================================
prog : declaracion+ EOF ;

// ==========================================
// 2. DECLARACIONES Y TIPOS
// ==========================================
declaracion
    : tipo ID LPAREN parametros? RPAREN bloque       # DecFuncion
    | tipo ID (LBRACKET NUM_INT RBRACKET)? SEMICOLON # DecVariableGlobal
    ;

tipo
    : INT | DOUBLE | CHAR | VOID
    ;

parametros
    : parametro (COMMA parametro)*
    ;

parametro
    : tipo ID (LBRACKET RBRACKET)?
    ;

// ==========================================
// 3. BLOQUES Y SENTENCIAS
// ==========================================
bloque
    : LBRACE sentencia* RBRACE
    ;

sentencia
    : tipo ID (LBRACKET NUM_INT RBRACKET)? (ASSIGN expr)? SEMICOLON  # SentDecVariable
    | ID (LBRACKET expr RBRACKET)? ASSIGN expr SEMICOLON             # SentAsignacion
    | llamadaFuncion SEMICOLON                                       # SentLlamadaFuncion
    | IF LPAREN expr RPAREN bloque (ELSE bloque)?                    # SentIf
    | WHILE LPAREN expr RPAREN bloque                                # SentWhile
    | FOR LPAREN sentencia expr SEMICOLON expr RPAREN bloque         # SentFor
    | RETURN expr? SEMICOLON                                         # SentReturn
    | BREAK SEMICOLON                                                # SentBreak
    | CONTINUE SEMICOLON                                             # SentContinue
    | expr op=(INC | DEC) SEMICOLON                                  # SentIncDec
    ;

// ==========================================
// 4. EXPRESIONES (Jerarquía de Precedencia)
// ==========================================
expr
    : llamadaFuncion                                  # ExprLlamada
    | ID (LBRACKET expr RBRACKET)?                    # ExprId
    | literal                                         # ExprLiteral
    | LPAREN expr RPAREN                              # ExprParentesis
    | op=(NOT | MINUS) expr                           # ExprUnaria
    | expr op=(INC | DEC)                             # ExprPostUnaria
    | expr op=(MUL | DIV | MOD) expr                  # ExprMulDiv
    | expr op=(PLUS | MINUS) expr                     # ExprAddSub
    | expr op=(LT | LE | GT | GE) expr                # ExprRelacional
    | expr op=(EQ | NE) expr                          # ExprIgualdad
    | expr AND expr                                   # ExprAnd
    | expr OR expr                                    # ExprOr
    ;

llamadaFuncion
    : ID LPAREN argumentos? RPAREN
    ;

argumentos
    : expr (COMMA expr)*
    ;

literal
    : NUM_INT | NUM_DOUBLE | CHAR_LIT | STRING_LIT
    ;
