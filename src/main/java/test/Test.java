package test;

public class Test {

	public static void main(String[] args) {
		
		Integer a = new Integer(0);
		Integer b = 0;
		Integer c = new Integer(0);
		
		print(a==0);
		print(a==b);
		print(a==c);
		
		print(a.equals(0));
		print(a.equals(b));
		print(a.equals(c));
	}
	
	private static void print(Object obj) {
		System.out.println(obj);
	}

	public static void increment(Integer a) {
		a = a+1;
		return;
	}
}
