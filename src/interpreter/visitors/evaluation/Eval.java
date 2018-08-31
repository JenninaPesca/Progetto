package interpreter.visitors.evaluation;

import interpreter.environments.EnvironmentException;
import interpreter.environments.GenEnvironment;
import interpreter.parser.ast.Exp;
import interpreter.parser.ast.ExpSeq;
import interpreter.parser.ast.Ident;
import interpreter.parser.ast.SimpleIdent;
import interpreter.parser.ast.Stmt;
import interpreter.parser.ast.StmtSeq;
import interpreter.visitors.Visitor;

public class Eval implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new EvaluatorException(e);
		}
		return null;
	}
	
	// dynamic semantics of expressions; a value is returned by the visitor
	/*literal*/
	@Override
	public Value visitListLiteral(ExpSeq exps) {
		return exps.accept(this);
	}
	
	@Override
	public Value visitIntLiteral(int value) {
		return new IntValue(value);
	}
	
	@Override
	public Value visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}
	/*fatto da me inizio*/
	@Override
	public Value visitBoolLiteral(boolean value) {
		//TODO
		return null;
	}
	/*fatto da me fine*/
	/*operatori binari*/
	@Override
	public Value visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() + right.accept(this).asInt());
	}

	@Override
	public Value visitMul(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() * right.accept(this).asInt());
	}

	@Override
	public Value visitPrefix(Exp left, Exp right) {
		Value el = left.accept(this);
		return right.accept(this).asList().prefix(el);
	}
	/*fatto da me inizio*/
	@Override
	public Value visitAnd(Exp left, Exp right) {
		//TODO
		return null;
	}
	
	@Override
	public Value visitEq(Exp left, Exp right) {
		//TODO
		return null;
	}
	/*fatto da me fine*/
	//operatori unari
	@Override
	public Value visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).asInt());
	}
	/*fatto da me inizio*/
	@Override
	public Value visitNot(Exp exp) {
		//TODO
		return null;
	}
	
	@Override
	public Value visitOpt(Exp exp) {
		//TODO
		return null;
	}
	
	@Override
	public Value visitEmpty(Exp exp) {
		//TODO
		return null;	
	}
	
	@Override
	public Value visitDef(Exp exp) {
		//TODO
		return null;
	}
	
	@Override
	public Value visitGet(Exp exp) {
		//TODO
		return null;
	}
	/*fatto da me fine*/


	// dynamic semantics of sequences of expressions
	// a list of values is returned by the visitor

	@Override
	public Value visitSingleExp(Exp exp) {
		return new ListValue(exp.accept(this), new ListValue());
	}

	@Override
	public Value visitMoreExp(Exp first, ExpSeq rest) {
		return new ListValue(first.accept(this), rest.accept(this).asList());
	}
	
	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Ident ident, Exp exp) {
		env.update(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		System.out.println(exp.accept(this));
		return null;
	}
	
	@Override
	public Value visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		ListValue list = exp.accept(this).asList();
		for (Value val : list) {
			env.enterLevel();
			env.dec(ident, val);
			block.accept(this);
			env.exitLevel();
		}
		return null;
	}
	/*fatto da me inizio*/
	@Override 
	public Value visitIfThenStmt(Exp exp, StmtSeq then_seq) {
		//TODO
		return null;
	}
	
	@Override
	public Value visitIfThenElseStmt(Exp exp, StmtSeq then_seq, StmtSeq else_seq) {
		//TODO
		return null;
	}
	
	@Override
	public Value visitDoWhileStmt(StmtSeq block, Exp exp) {
		//TODO
		return null;	
	}
	/*fatto da me fine*/

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}



}
