package tyuukannkadai;

public class Main {
	public static void main(String [] args) {
		
		String formula = Input_class.formula();
		
		double result = Cal_class.cal(formula);
		
		System.out.println("解答" + result);
	}
}
