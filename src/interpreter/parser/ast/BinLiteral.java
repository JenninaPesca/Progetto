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
	public String toString(){
		return getClass().getSimpleName() + "(" + this.intToBin(value) + ")";
	}
	
	public void main(String[] args) {
			BinLiteral b = new BinLiteral(2);
			System.out.println(this.intToBin());
			
			}
		}
	
}


