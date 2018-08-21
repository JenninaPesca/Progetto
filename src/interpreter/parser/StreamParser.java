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
 Atom ::= '-' Atom | '[' ExpSeq ']'| BIN | NUM | ID | '(' Exp ')'

*/

public class StreamParser implements Parser {

	private final Tokenizer tokenizer;

	private void tryNext() throws ParserException {
		System.out.println("INIZIO (StreamParser) tryNext"); //CANCELLA
		try {
			System.out.println("	chiamo la next"); //CANCELLA
			tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
		System.out.println("FINE (StreamParser) tryNext"); //CANCELLA

	}

	private void match(TokenType expected) throws ParserException {
		System.out.println("INIZIO (StreamParser) match con expected: "+expected); //CANCELLA
		final TokenType found = tokenizer.tokenType();
		System.out.println("	found: "+found); //CANCELLA
		if (found != expected)
			throw new ParserException(
					"Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
		System.out.println("FINE (StreamParser) match"); //CANCELLA
	}

	private void consume(TokenType expected) throws ParserException {
		System.out.println("INIZIO (StreamParser) consume"); //CANCELLA
		System.out.println("	chiamo match"); //CANCELLA
		match(expected);
		System.out.println("	chiamo tryNext"); //CANCELLA
		tryNext();
		System.out.println("FINE (StreamParser) consume"); //CANCELLA
	}

	private void unexpectedTokenError() throws ParserException {
		throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
	}

	public StreamParser(Tokenizer tokenizer) {
		System.out.println("(StreamParser) costruttore"); //CANCELLA
		this.tokenizer = tokenizer;
	}

	@Override
	public Prog parseProg() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseProg"); //CANCELLA
		System.out.println("	chiamo tryNext"); //CANCELLA
		tryNext(); // one look-ahead symbol
		Prog prog = new ProgClass(parseStmtSeq());
		System.out.println("	chiamo match"); //CANCELLA
		match(EOF);
		System.out.println("FINE (StreamParser) ParseProg"); //CANCELLA
		return prog;
	}

	private StmtSeq parseStmtSeq() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseStmtSeq"); //CANCELLA
		Stmt stmt = parseStmt();
		System.out.println("	stmt: "+stmt); //CANCELLA
		if (tokenizer.tokenType() == STMT_SEP) {
			System.out.println("	chiama tryNext"); //CANCELLA
			tryNext();
			System.out.println("FINE (StreamParser) ParseStmtSeq more"); //CANCELLA
			return new MoreStmt(stmt, parseStmtSeq());
		}
		System.out.println("FINE (StreamParser) ParseStmtSeq single"); //CANCELLA
		return new SingleStmt(stmt);
	}

	private ExpSeq parseExpSeq() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseExpSeq"); //CANCELLA
		Exp exp = parseExp();
		System.out.println("	exp: "+exp); //CANCELLA
		if (tokenizer.tokenType() == EXP_SEP) {
			System.out.println("	chiama tryNext"); //CANCELLA
			tryNext();
			System.out.println("FINE (StreamParser) ParseExpSeq more"); //CANCELLA
			return new MoreExp(exp, parseExpSeq());
		}
		System.out.println("FINE (StreamParser) ParseExpSeq single"); //CANCELLA
		return new SingleExp(exp);
	}

	private Stmt parseStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseStmt"); //CANCELLA
		switch (tokenizer.tokenType()) {
		default:
			System.out.println("FINE (StreamParser) ParseStmt caso default"); //CANCELLA
			System.out.println("    chiamo unexpectedTokenError()");
			unexpectedTokenError();
		case PRINT:
			System.out.println("FINE (StreamParser) ParseStmt caso PRINT"); //CANCELLA
			System.out.println("    chiamo parsePrintStmt");
			return parsePrintStmt();
		case VAR:
			System.out.println("FINE (StreamParser) ParseStmt caso VAR"); //CANCELLA
			System.out.println("    chiamo parseVarStmt");
			return parseVarStmt();
		case IDENT:
			System.out.println("FINE (StreamParser) ParseStmt caso IDENT"); //CANCELLA
			System.out.println("    chiamo parseAssignStmt");
			return parseAssignStmt();
		case FOR:
			System.out.println("FINE (StreamParser) ParseStmt caso PRINT"); //CANCELLA
			System.out.println("    chiamo parsePrintStmt");
			return parseForEachStmt();
		}
	}

	private PrintStmt parsePrintStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parsePrintStmt");
		System.out.println("	chiamo consume con PRINT");
		consume(PRINT); // or tryNext();
		System.out.println("FINE (StreamParser) parsePrintStamt");
		return new PrintStmt(parseExp());
	}

	private VarStmt parseVarStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseVarStmt");
		System.out.println("	chiamo consume con VAR");
		consume(VAR); // or tryNext();
		System.out.println("	chiamo parseIdent");
		Ident ident = parseIdent();
		System.out.println("	ident: "+ident);
		System.out.println("	chiamo consume con ASSIGN");
		consume(ASSIGN);
		System.out.println("FINE (StreamParser) parseVarStmt che chiama parseExp");
		return new VarStmt(ident, parseExp());
	}

	private AssignStmt parseAssignStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseAssignStmt");
		System.out.println("	chiamo parseIdent");
		Ident ident = parseIdent();
		System.out.println("	ident: "+ident);
		System.out.println("	chiamo consume con ASSIGN");
		consume(ASSIGN);
		System.out.println("FINE (StreamParser) parseAssignStmt che chiama parseExp");
		return new AssignStmt(ident, parseExp());
	}

	private ForEachStmt parseForEachStmt() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseForEachStmt");
		System.out.println("	chiamo consume con FOR");
		consume(FOR); // or tryNext();
		System.out.println("	chiamo parseIdent()");
		Ident ident = parseIdent();
		System.out.println("	ident: "+ident);
		System.out.println("	chiamo consume con IN");
		consume(IN);
		System.out.println("	chiamo parseExp");
		Exp exp = parseExp();
		System.out.println("	exp: "+exp);
		System.out.println("	chiamo consume con OPEN_BLOCK");
		consume(OPEN_BLOCK);
		System.out.println("	chiamo patseStmtSeq()");
		StmtSeq stmts = parseStmtSeq();
		System.out.println("	stmts: "+stmts);
		System.out.println("	chiamo consume con CLOSE_BLOCK");
		consume(CLOSE_BLOCK);
		System.out.println("FINE (StreamParser) parseForEachStmt");
		return new ForEachStmt(ident, exp, stmts);
	}

	private Exp parseExp() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseExp");
		System.out.println("	chiamo parseAdd");
		Exp exp = parseAdd();
		System.out.println("	exp: "+exp);
		if (tokenizer.tokenType() == PREFIX) {
			System.out.println("	chiamo tryNext");
			tryNext();
			System.out.println("	chiamo parseExp");
			exp = new Prefix(exp, parseExp());
		}
		System.out.println("FINE (StreamParser) parseExp exp di tipo Prefix: "+ exp);
		return exp;
	}

	private Exp parseAdd() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseAdd");
		System.out.println("	chiamo parseMul");
		Exp exp = parseMul();
		System.out.println("	exp: "+exp);
		while (tokenizer.tokenType() == PLUS) {
			System.out.println("	tokenizer.tokenType(): "+tokenizer.tokenType());
			System.out.println("	chiamo tryNext");
			tryNext();
			System.out.println("	chiamo parseMul");
			exp = new Add(exp, parseMul());
		}
		System.out.println("FINE (StreamParser) parseAdd exp di tipo Add: "+exp);
		return exp;
	}

	private Exp parseMul() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseMul");
		System.out.println("	chiamo parseAtom");
		Exp exp = parseAtom();
		System.out.println("exp: "+exp);
		while (tokenizer.tokenType() == TIMES) {
			System.out.println("	tokenizer.tokenType(): "+tokenizer.tokenType());
			System.out.println("	chiamo trynext");
			tryNext();
			System.out.println("	chiamo parseAtom()");
			exp = new Mul(exp, parseAtom());
		}
		System.out.println("FINE (StreamParser) parseMul exp di tipo Mul: "+exp);
		return exp;
	}

	private Exp parseAtom() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseAtom");
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		/*--fatto da me-- inizio*/
		case BIN:
			System.out.println("FINE (StreamParser) ParseAtom caso BIN"); //CANCELLA
			System.out.println("    chiamo parseBin");
			return parseBin();
		/*--fatto da me-- fine*/
		case NUM:
			System.out.println("FINE (StreamParser) ParseAtom caso NUM"); //CANCELLA
			System.out.println("    chiamo parseNum");
			return parseNum();
		case IDENT:
			System.out.println("FINE (StreamParser) ParseAtom caso IDENT"); //CANCELLA
			System.out.println("    chiamo parseIdent");
			return parseIdent();
		case MINUS:
			System.out.println("FINE (StreamParser) ParseAtom caso MINUS"); //CANCELLA
			System.out.println("    chiamo parseMinus");
			return parseMinus();
		case OPEN_LIST:
			System.out.println("FINE (StreamParser) ParseAtom caso OPEN_LIST"); //CANCELLA
			System.out.println("    chiamo parseList");
			return parseList();
		case OPEN_PAR:
			System.out.println("FINE (StreamParser) ParseAtom caso OPEN_PAR"); //CANCELLA
			System.out.println("    chiamo parseRoundPar");
			return parseRoundPar();
		}
	}
	
	/*--fatto da me-- inizio*/
	private BinLiteral parseBin() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseBin "); //CANCELLA
		System.out.println("	guardo cosa c'è dentro tokenizer.intValue();"); //CANCELLA
		int val = tokenizer.binValue();
		System.out.println("	val: "+val); //CANCELLA
		System.out.println("     chiamo consume con BIN");
		consume(BIN); // or tryNext();
		System.out.println("FINE (StreamParser) parseNum");
		return new BinLiteral(val);
	}
	/*--fatto da me-- fine*/
	
	private IntLiteral parseNum() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseNUM "); //CANCELLA
		System.out.println("	guardo cosa c'è dentro tokenizer.intValue();"); //CANCELLA
		int val = tokenizer.intValue();
		System.out.println("	val: "+val); //CANCELLA
		System.out.println("     chiamo consume con NUM");
		consume(NUM); // or tryNext();
		System.out.println("FINE (StreamParser) parseNum");
		return new IntLiteral(val);
	}

	private Ident parseIdent() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseIdent "); //CANCELLA
		System.out.println("	guardo cosa c'è dentro tikenizer.tokenString"); //CANCELLA
		String name = tokenizer.tokenString();
		System.out.println("	name: "+name); //CANCELLA
		System.out.println("	chiamo consume con IDENT"); //CANCELLA
		consume(IDENT); // or tryNext();
		System.out.println("FINE (StreamParser) ParseNUM "); //CANCELLA
		return new SimpleIdent(name);
	}

	private Sign parseMinus() throws ParserException {
		System.out.println("INIZIO (StreamParser) parseMinus "); //CANCELLA
		System.out.println("	chiamo consume con MINUS"); //CANCELLA
		consume(MINUS); // or tryNext();
		System.out.println("FINE (StreamParser) ParseMinus ritorna un Sign(parseAtom())"); //CANCELLA
		return new Sign(parseAtom());
	}

	private ListLiteral parseList() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseList "); //CANCELLA
		System.out.println("	chiamo consume con OPEN_LIST"); //CANCELLA
		consume(OPEN_LIST); // or tryNext();
		System.out.println("	chiamo parseExpseq"); //CANCELLA
		ExpSeq exps = parseExpSeq();
		System.out.println("	exps: "+exps); //CANCELLA
		System.out.println("	chiamo consume con CLOSE_LIST"); //CANCELLA
		consume(CLOSE_LIST);
		System.out.println("FINE (StreamParser) ParseList "); //CANCELLA
		return new ListLiteral(exps);
	}

	private Exp parseRoundPar() throws ParserException {
		System.out.println("INIZIO (StreamParser) ParseRoundPar "); //CANCELLA
		System.out.println("	chiamo consume con OPEN_PAR"); //CANCELLA
		consume(OPEN_PAR); // or tryNext();
		System.out.println("	chiamo parseExp"); //CANCELLA
		Exp exp = parseExp();
		System.out.println("	exp: "+exp); //CANCELLA
		System.out.println("	chiamo consume con CLOSE_PAR"); //CANCELLA
		consume(CLOSE_PAR);
		System.out.println("FINE (StreamParser) ParseRoundPar "); //CANCELLA
		return exp;
	}

}
