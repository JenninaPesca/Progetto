package interpreter.parser.ast;

import interpreter.parser.ast.PrimLiteral;
import interpreter.visitors.Visitor;

public class BinLiteral extends PrimLiteral<Integer> {

<<<<<<< HEAD
	String pref = "0b";// modifica
	
=======
	String pref = "0b"; //modifica: salvare con il metodo che salva n  anche la prima parte della stringa
>>>>>>> 0ee0314d7850a4b6a6ad57d50c55267806e2c41b
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
	
	private String intToBin(int n) {
		String res=pref;
		int resto=0;
		while(n != 0) {
			//se il resto intero della divisione è 0 aggiungi 0 in coda
			if(n%2 == 0)
				res += "0";
			//se il resto intero della divisione è 1 aggiungi 1 in coda 
			else 
				res += "1";
			n = n/2;
		}
		return res;
	}
	
	@Override
<<<<<<< HEAD
	public String toString(){
		return getClass().getSimpleName() + "(" + this.intToBin(value) + ")";
	}
	
	public void main(String[] args) {
			BinLiteral b = new BinLiteral(2);
			System.out.println(this.intToBin());
			
			}
		}
	
=======
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIntLiteral(value);
	}
	
	public static void main(String[] args) {
		BinLiteral p = new BinLiteral(5);
		System.out.println(p.toString());
	}

>>>>>>> 0ee0314d7850a4b6a6ad57d50c55267806e2c41b
}


