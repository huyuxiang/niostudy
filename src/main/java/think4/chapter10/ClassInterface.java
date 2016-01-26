package think4.chapter10;

public interface ClassInterface {
	
	void howdy();
	class Test implements ClassInterface {
		public void howdy() {
			System.out.println("Howdy");
		}
		
		public static void main(String args[]) {
			new Test().howdy();
		}
	}
	
}
