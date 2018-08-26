package interpreter.parser.ast;

import static java.util.Objects.requireNonNull;

import interpreter.visitors.Visitor;

public class IfElseStmt implements Stmt{
	private final Exp exp; //espressione dentro if controlla:deve essere booleana??
	private final StmtSeq firstBlock; //stmtseq dentro then
	private StmtSeq secondBlock = null; //stmtseq dentro else --> NB puo essere null
	
	public IfElseStmt(Exp exp, StmtSeq firstBlock) {
		this.exp = requireNonNull(exp);
		this.firstBlock = requireNonNull(firstBlock);
	}

	public IfElseStmt(Exp exp, StmtSeq firstBlock, StmtSeq secondBlock) {
		this.exp = requireNonNull(exp);
		this.firstBlock = requireNonNull(firstBlock);
		this.secondBlock = secondBlock;
	}

	@Override
	public String toString() {
		if (secondBlock == null)
			return getClass().getSimpleName() + "(" + exp + "," + firstBlock + ")";
		return getClass().getSimpleName() + "(" + exp + "," + firstBlock + "," + secondBlock + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitPrintStmt(exp); //modifica:da rifare
	}
}
