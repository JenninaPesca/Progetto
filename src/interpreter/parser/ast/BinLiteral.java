package interpreter.parser.ast;

import interpreter.parser.ast.PrimLiteral;
import interpreter.visitors.Visitor;

public class BinLiteral extends PrimLiteral<Integer> {

	String pref = "0b"; //modifica: salvare con il metodo che salva n  anche la prima parte della stringa
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
		}
		return num;
	}
	/*modifica: non funzionaaaaaaaa*/
	
	@Override
	public String toString() {
		return  getClass().getSimpleName() + " (" + this.intToBin(value) + ")"; /*modifica: sbagliato*/
	}
	
	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}
	
	public static void main(String[] args) {
		BinLiteral p = new BinLiteral(5);
		System.out.println(p.toString());
	}

}


