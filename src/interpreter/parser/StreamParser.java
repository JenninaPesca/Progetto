package interpreter.parser;

import static interpreter.parser.TokenType.*;

import interpreter.parser.ast.*;

/*
Prog ::= StmtSeq 'EOF'
 StmtSeq ::= Stmt (';' StmtSeq)?
 Stmt ::= 'var'? ID '=' Exp | 'print' Exp |  'for' ID ':' Exp '{' StmtSeq '}'
 ExpSeq ::= Exp (',' ExpSeq)?
 Exp ::= Add ('::' Exp)?
 Add ::= Mul ('+' Mul)*
 Mul::= Atom ('*' Atom)*
 Atom ::= '-' Atom | '[' ExpSeq ']' | NUM | ID | '(' Exp ')'

*/

public class StreamParser implements Parser {

	private final Tokenizer tokenizer;

	private void tryNext() throws ParserException {
		try {
			tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
	}

	private void match(TokenType expected) throws ParserException {
System.out.println("INIZIO (StreamParser) match"); //CANCELLA
System.out.println(" 	expected: "+expected); //CANCELLA
		final TokenType found = tokenizer.tokenType();
		System.out.println(" 	found: "+found); //CANCELLA
		if (found != expected)
			throw new ParserException(
					"Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
System.out.println("FINE (StreamParser) match"); //CANCELLA
	}

	private void consume(TokenType expected) throws ParserException {
System.out.println("INIZIO (StreamParser) consume"); //CANCELLA
System.out.println(" 	chiamo la match con expected: "+expected); //CANCELLA
		match(expected);
		System.out.println(" 	chiamo Trynext"); //CANCELLA
		tryNext();
System.out.println("FINE (StreamParser) consume"); //CANCELLA
	}

	private void unexpectedTokenError() throws ParserException {
		throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
	}

	public StreamParser(Tokenizer tokenizer) {
		System.out.println("(SP) costruttore"); //CANCELLA
		System.out.println(" 	tokenizer: "+tokenizer); //CANCELLA
		this.tokenizer = tokenizer;
	}

	@Override
	public Prog parseProg() throws ParserException {
System.out.println("INIZIO (StreamParser) parseProg"); //CANCELLA
System.out.println(" 	chiamo la trynext"); //CANCELLA
		tryNext(); // one look-ahead symbol
System.out.println(" 	creo nuovo Prog"); //CANCELLA
		Prog prog = new ProgClass(parseStmtSeq());
System.out.println(" 	chiamo match con EOF"); //CANCELLA
		match(EOF);
System.out.println("FINR parseProg"); //CANCELLA
		return prog;
	}

	private StmtSeq parseStmtSeq() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseStmtSeq"); //CANCELLA
		System.out.println(" 	chiamo parseStmt"); //CANCELLA
		Stmt stmt = parseStmt();
		System.out.println(" 	stmt: "+stmt); //CANCELLA
		if (tokenizer.tokenType() == STMT_SEP) {
			System.out.println(" 	chiamo trynext"); //CANCELLA
			tryNext();
			System.out.println("FINE (StreamParser) parseStmtSeq more"); //CANCELLA
			return new MoreStmt(stmt, parseStmtSeq());
		}
		System.out.println("FINE (StreamParser) parseStmt single"); //CANCELLA
		return new SingleStmt(stmt);
	}

	private ExpSeq parseExpSeq() throws ParserException {
		System.out.println("Inizio parseExpSeq"); //CANCELLA
		System.out.println(" 	chiamo parseExp"); //CANCELLA
		Exp exp = parseExp();
		System.out.println(" 	exp: "+exp); //CANCELLA
		if (tokenizer.tokenType() == EXP_SEP) {
			System.out.println(" 	chiamo trynext"); //CANCELLA
			tryNext();
			System.out.println("FINE (StreamParser) parseExpSeq more"); //CANCELLA
			return new MoreExp(exp, parseExpSeq());
		}
		System.out.println("FINE (StreamParser) parseExpSeq single"); //CANCELLA
		return new SingleExp(exp);
	}

	private Stmt parseStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseStmt"); //CANCELLA
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case PRINT:
			System.out.println("FINE (StreamParser) parseStmt print"); //CANCELLA
			return parsePrintStmt();
		case VAR:
			System.out.println("FINE (StreamParser) parseStmt var"); //CANCELLA
			return parseVarStmt();
		case IDENT:
			System.out.println("FINE (StreamParser) parseStmt ident"); //CANCELLA
			return parseAssignStmt();
		case FOR:
			System.out.println("FINE (StreamParser) parseStmt for"); //CANCELLA
			return parseForEachStmt();
		}
	}

	private PrintStmt parsePrintStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parsePrint"); //CANCELLA
		System.out.println(" 	chiamo consume con print"); //CANCELLA
		consume(PRINT); // or tryNext();
		System.out.println("FINE (StreamParser) parseprint"); //CANCELLA
		return new PrintStmt(parseExp());
	}

	private VarStmt parseVarStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseVarStmt"); //CANCELLA
		System.out.println(" 	chiamo consume con var"); //CANCELLA
		consume(VAR); // or tryNext();
		System.out.println(" 	chiamo parseIdent"); //CANCELLA
		Ident ident = parseIdent();
		System.out.println(" 	ident: "+ident); //CANCELLA
		System.out.println(" 	chiamo consume con assign"); //CANCELLA
		consume(ASSIGN);
		System.out.println("FINE (StreamParser) parseVarStmt: ha consumanto anche assign (=)"); //CANCELLA
		return new VarStmt(ident, parseExp());
	}

	private AssignStmt parseAssignStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseAssignStmt"); //CANCELLA
		System.out.println(" 	chiamo parseIdent"); //CANCELLA
		Ident ident = parseIdent();
		System.out.println(" 	ident: "+ident); //CANCELLA
		System.out.println(" 	chiamo consume con assign"); //CANCELLA
		consume(ASSIGN);
		System.out.println("FINE (StreamParser) parseAssignStmt"); //CANCELLA
		return new AssignStmt(ident, parseExp());
	}

	private ForEachStmt parseForEachStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseForEachStmt"); //CANCELLA
		System.out.println(" 	chiamo consume con for"); //CANCELLA
		consume(FOR); // or tryNext();
		Ident ident = parseIdent();
		System.out.println(" 	ident: "+ident); //CANCELLA
		System.out.println(" 	chiamo consume con int"); //CANCELLA
		consume(IN);
		Exp exp = parseExp();
		System.out.println(" 	exp: "+exp); //CANCELLA
		System.out.println(" 	chiamo consume con open_block"); //CANCELLA
		consume(OPEN_BLOCK);
		StmtSeq stmts = parseStmtSeq();
		System.out.println(" 	stmts: "+stmts); //CANCELLA
		System.out.println(" 	chiamo consume con close_block"); //CANCELLA
		consume(CLOSE_BLOCK);
		System.out.println("FINE (StreamParser) parseForEachStmt"); //CANCELLA
		return new ForEachStmt(ident, exp, stmts);
	}

	private Exp parseExp() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseExp"); //CANCELLA
		System.out.println(" 	chiamo parseAdd"); //CANCELLA
		Exp exp = parseAdd();
		System.out.println(" 	exp: "+exp); //CANCELLA
		if (tokenizer.tokenType() == PREFIX) {
			System.out.println(" 	chiamo try next"); //CANCELLA
			tryNext();
			exp = new Prefix(exp, parseExp());
			System.out.println(" 	exp: "+exp); //CANCELLA
		}
		System.out.println("FINE (StreamParser) parseExp"); //CANCELLA
		return exp;
	}

	private Exp parseAdd() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseAdd"); //CANCELLA
		System.out.println(" 	chiamo parseMul"); //CANCELLA
		Exp exp = parseMul();
		System.out.println(" 	exp: "+exp); //CANCELLA
		while (tokenizer.tokenType() == PLUS) {
			System.out.println(" 	chiamo try next"); //CANCELLA
			tryNext();
			exp = new Add(exp, parseMul());
			System.out.println(" 	exp: "+exp); //CANCELLA
		}
		System.out.println("FINE (StreamParser) parseAdd"); //CANCELLA
		return exp;
	}

	private Exp parseMul() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseMul"); //CANCELLA
		System.out.println(" 	chiamo parseAtom"); //CANCELLA
		Exp exp = parseAtom();
		System.out.println(" 	exp: "+exp); //CANCELLA
		while (tokenizer.tokenType() == TIMES) {
			System.out.println(" 	chiamo trynext"); //CANCELLA
			tryNext();
			exp = new Mul(exp, parseAtom());
		}
		System.out.println("FINE (StreamParser) parseMul"); //CANCELLA
		return exp;
	}

	private Exp parseAtom() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseAtom"); //CANCELLA
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case NUM:
			System.out.println("FINE (StreamParser) parseAtom chiama parsenum"); //CANCELLA
			return parseNum();
		case IDENT:
			System.out.println("FINE (StreamParser) parseAtom chiama parseident"); //CANCELLA
			return parseIdent();
		case MINUS:
			System.out.println("FINE (StreamParser) parseAtom chiama parseminus"); //CANCELLA
			return parseMinus();
		case OPEN_LIST:
			System.out.println("FINE (StreamParser) parseAtom chiama parselist"); //CANCELLA
			return parseList();
		case OPEN_PAR:
			System.out.println("FINE (StreamParser) parseAtom chiama parseroundPar"); //CANCELLA
			return parseRoundPar();
		}
	}

	private IntLiteral parseNum() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseNum"); //CANCELLA
		int val = tokenizer.intValue();
		System.out.println(" 	val: "+val); //CANCELLA
		System.out.println(" 	chiamo consume con num"); //CANCELLA
		consume(NUM); // or tryNext();
		System.out.println("FINE (StreamParser) parseNum"); //CANCELLA
		return new IntLiteral(val);
	}

	private Ident parseIdent() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseIdent"); //CANCELLA
		String name = tokenizer.tokenString();
		System.out.println(" 	name: "+name); //CANCELLA
		System.out.println(" 	chiamo consume con "); //CANCELLA
		consume(IDENT); // or tryNext();
		System.out.println("FINE (StreamParser) parseIdent"); //CANCELLA
		return new SimpleIdent(name);
	}

	private Sign parseMinus() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseMinus"); //CANCELLA
		System.out.println(" 	chiamo consume con minus"); //CANCELLA
		consume(MINUS); // or tryNext();
		System.out.println("FINE (StreamParser) parseMinus"); //CANCELLA
		return new Sign(parseAtom());
	}

	private ListLiteral parseList() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseList"); //CANCELLA
		System.out.println(" 	chiamo consume con open_list"); //CANCELLA
		consume(OPEN_LIST); // or tryNext();
		System.out.println(" 	chiamo parseExpSeq"); //CANCELLA
		ExpSeq exps = parseExpSeq();
		System.out.println(" 	exps: "+exps); //CANCELLA
		System.out.println(" 	chiamo consume con close_list"); //CANCELLA
		consume(CLOSE_LIST);
		System.out.println("FINE (StreamParser) parseList"); //CANCELLA
		return new ListLiteral(exps);
	}

	private Exp parseRoundPar() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseRoundPar"); //CANCELLA
		System.out.println(" 	chiamo consume con open_par"); //CANCELLA
		consume(OPEN_PAR); // or tryNext();
		System.out.println(" 	chiamo parseexp"); //CANCELLA
		Exp exp = parseExp();
		System.out.println(" 	exp: "+exp); //CANCELLA
		System.out.println(" 	chiamo consume con close_par"); //CANCELLA
		consume(CLOSE_PAR);
		System.out.println("FINE (StreamParser) parseRoundPar"); //CANCELLA
		return exp;
	}

}
