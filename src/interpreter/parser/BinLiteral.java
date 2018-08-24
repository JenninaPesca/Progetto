package interpreter.parser;

import interpreter.parser.ast.PrimLiteral;
import interpreter.visitors.Visitor;

public class BinLiteral extends PrimLiteral<Integer> {

	String pref = "0b"; //non so se è utile ma almeno non si perde del tutto l'informazione modifica: potrebbe essere anche 0B
	public BinLiteral(int n) {
		super(n);
	}
	
	private String intToBin(int n) {
		String num="";
		int aux=n;
		while(aux!=0){
			if((aux%2) == 0){
				System.out.println(aux);
				num += "0";
			}
				
			else 
				num += "1";
			if (aux>2)
				aux = aux/2;
			else
				return num;
		}
		return num;
	}
	/*modifica: non funzionaaaaaaaa*/
	
	@Override
	public String toString() {
		return  getClass().getSimpleName() + " (" + pref + this.intToBin(value) + ")"; /*modifica: sbagliato*/
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}
	
	public static void main(String[] args) {
		BinLiteral p = new BinLiteral(36);
		System.out.println(p.toString());
	}

}