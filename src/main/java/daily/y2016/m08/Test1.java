package daily.y2016.m08;

public class Test1 {

	public void test(Object o) {
		System.out.println("object");
	}
	
	public void test(String s) {
		System.out.println("String");
	}
	
	public static void main(String[] args) {
		Test1 test = new Test1();
		test.test(null);
	}
}
