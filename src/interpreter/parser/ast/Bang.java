package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class Bang extends UnaryOp{
	
	public Bang(Exp exp) {
		super(exp);
	}
//modifica
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitBang(exp);
	}
}
