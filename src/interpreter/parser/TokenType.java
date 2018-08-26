package interpreter.parser;

public enum TokenType { // important: IDENT, NUM, and SKIP must have ordinals 1, 2 and 3
	EOF, IDENT, NUM, SKIP, IN, FOR, PRINT, IF, ELSE, DO, WHILE, PREFIX, VAR, OPT, EMPTY, DEF, GET, PLUS, LOGICAND, EQUALITY, BANG, TIMES, ASSIGN, OPEN_PAR, CLOSE_PAR, STMT_SEP, EXP_SEP, 
	OPEN_BLOCK, CLOSE_BLOCK, MINUS, OPEN_LIST, CLOSE_LIST
}
