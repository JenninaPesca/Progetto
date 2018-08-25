package interpreter.parser;

import static interpreter.parser.TokenType.*;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class StreamTokenizer implements Tokenizer {
	private static final String regEx;
	private static final Map<String, TokenType> keywords = new HashMap<>();
	private static final Map<String, TokenType> symbols = new HashMap<>();

	private boolean hasNext = true; // any stream contains at least the EOF
									// token
	private TokenType tokenType;
	private String tokenString;
	private int intValue;
	private final Scanner scanner;

	static {
		// remark: groups must correspond to the ordinal of the corresponding
		// token type
		final String identRegEx = "([a-zA-Z][a-zA-Z0-9]*)"; // group 1
		final String numRegEx = "(0|[1-9][0-9]*)"; // group 2
		final String skipRegEx = "(\\s+|//.*)"; // group 3
		final String symbolRegEx = "\\+|\\*|=|\\(|\\)|;|,|\\{|\\}|-|::|:|\\[|\\]";
		regEx = identRegEx + "|" + numRegEx + "|" + skipRegEx + "|" + symbolRegEx;
	}

	static {
		keywords.put("true", TRUE); //fatto da me-- aggiungo la keyword true
		keywords.put("false", FALSE); //fatto da me-- aggiungo la keyword false
		keywords.put("for", FOR);
		keywords.put("print", PRINT);
		keywords.put("var", VAR);
	}

	static {
		symbols.put("+", PLUS);
		symbols.put("*", TIMES);
		symbols.put("::", PREFIX);
		symbols.put("=", ASSIGN);
		symbols.put(":", IN);
		symbols.put("(", OPEN_PAR);
		symbols.put(")", CLOSE_PAR);
		symbols.put(";", STMT_SEP);
		symbols.put(",", EXP_SEP);
		symbols.put("{", OPEN_BLOCK);
		symbols.put("}", CLOSE_BLOCK);
		symbols.put("-", MINUS);
		symbols.put("[", OPEN_LIST);
		symbols.put("]", CLOSE_LIST);
	}

	public StreamTokenizer(Reader reader) {
		System.out.println("INIZIO (StreamTokenizer) costruttore"); //CANCELLA
		scanner = new StreamScanner(regEx, reader);
		System.out.println("FINE (StreamTokenizer) costruttore"); //CANCELLA
	}

	private void checkType() {
		System.out.println("INIZIO(StreamTokenizer) checkType"); //CANCELLA
		System.out.println(" 	chiamo scanner.group"); //CANCELLA
		tokenString = scanner.group();
		System.out.println(" 	chiamo scanner.group con "+ IDENT.ordinal()); //CANCELLA
		if (scanner.group(IDENT.ordinal()) != null) { // IDENT or a keyword
			tokenType = keywords.get(tokenString);
			if (tokenType == null)
				tokenType = IDENT;
			System.out.println("FINE (StreamTokenizer) checkType ident"); //CANCELLA
			return;
		}
		System.out.println(" 	chiamo scanner.group con "+ NUM.ordinal()); //CANCELLA
		if (scanner.group(NUM.ordinal()) != null) { // NUM
			tokenType = NUM;
			intValue = Integer.parseInt(tokenString);
			System.out.println("FINE (StreamTokenizer) checkType num"); //CANCELLA
			return;
		}
		System.out.println(" 	chiamo scanner.group con "+ SKIP.ordinal()); //CANCELLA
		if (scanner.group(SKIP.ordinal()) != null) { // SKIP
			tokenType = SKIP;
			System.out.println("FINE (StreamTokenizer) checkType skip"); //CANCELLA
			return;
		}
		tokenType = symbols.get(tokenString); // a symbol
		System.out.println("FINE (StreamTokenizer) checkType symbol"); //CANCELLA
		if (tokenType == null)
			throw new AssertionError("Fatal error");
	}

	@Override
	public TokenType next() throws TokenizerException {
		System.out.println("INIZIO (StreamTokenizer) next"); //CANCELLA
		do {
			tokenType = null;
			tokenString = "";
			try {
				//osserva: caso eof, perch� non lo prende??
				if (hasNext && !scanner.hasNext()) {
					hasNext = false;
					return tokenType = EOF;
				}
				System.out.println(" 	chiamo scanner.next"); //CANCELLA
				scanner.next();
			} catch (ScannerException e) {
				throw new TokenizerException(e);
			}
			checkType();
		} while (tokenType == SKIP);
		System.out.println("FINE (StreamTokenizer) next"); //CANCELLA
		return tokenType;
	}

	private void checkValidToken() {
		System.out.println("INIZIO (StreamTokenizer) checkValidToken"); //CANCELLA
		if (tokenType == null)
			throw new IllegalStateException();
		System.out.println("FINE (StreamTokenizer) checkValidToken"); //CANCELLA
	}

	private void checkValidToken(TokenType ttype) {
		System.out.println("INIZIO (StreamTokenizer) checkValidToken con TokenType"); //CANCELLA
		System.out.println(" 	ttype: "+ ttype+" tokentype: "+tokenType); //CANCELLA
		if (tokenType != ttype)
			throw new IllegalStateException();
		System.out.println("FINE (StreamTokenizer) checkValidToken con Tokentype"); //CANCELLA
	}

	@Override
	public String tokenString() {
System.out.println("INIZIO (StreamTokenizer) tokenString"); //CANCELLA
System.out.println(" 	chiamo checkvalidtoken"); //CANCELLA
		checkValidToken();
System.out.println("FINE (StreamTokenizer) tokenString"); //CANCELLA
		return tokenString;
	}

	@Override
	public int intValue() {
System.out.println("INIZIO (StreamTokenizer) intValue"); //CANCELLA
System.out.println(" 	chiamo checkvalidtoken con num"); //CANCELLA
		checkValidToken(NUM);
System.out.println("FINE (StreamTokenizer) intValue"); //CANCELLA
		return intValue;
	}

	@Override
	public TokenType tokenType() {
		System.out.println("INIZIO (StreamTokenizer) tokenType"); //CANCELLA
		System.out.println(" 	chiamo checkvalidtoken"); //CANCELLA
		checkValidToken();
		System.out.println("FINE (StreamTokenizer) tokenType"); //CANCELLA
		return tokenType;
	}

	@Override
	public boolean hasNext() {
		System.out.println("(StreamTokenizer) hasNext"); //CANCELLA
		return hasNext;
	}

	@Override
	public void close() throws TokenizerException {
		System.out.println("INIZIO (StreamTokenizer) close"); //CANCELLA
		try {
			System.out.println(" 	chiamo scanner.close"); //CANCELLA
			scanner.close();
		} catch (ScannerException e) {
			throw new TokenizerException(e);
		}
		System.out.println("FINE (StreamTokenizer) close"); //CANCELLA
	}
}
