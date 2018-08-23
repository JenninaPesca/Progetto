package interpreter.parser.ast;

import interpreter.visitors.Visitor;

public class BinLiteral extends PrimLiteral<Integer>{

	String pref = "0b";// modifica
	public BinLiteral(int n) {
		super(n);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName() + "(" + value + ")";
	}
	
}


