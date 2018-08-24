package interpreter.parser;

import interpreter.parser.ast.PrimLiteral;
import interpreter.visitors.Visitor;

public class BinLiteral extends PrimLiteral<Integer> {

	String pref = "0b"; //non so se è utile ma almeno non si perde del tutto l'informazione modifica: potrebbe essere anche 0B
	public BinLiteral(int n) {
		super(n);
	}
	
	@Override
	public String toString() {
		return pref + getClass().getSimpleName() + "(" + value + ")"; /*modifica: sbagliato*/
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}

}