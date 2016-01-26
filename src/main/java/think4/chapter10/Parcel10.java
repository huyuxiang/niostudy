package think4.chapter10;

public class Parcel10 {
	public Destination destination(final String dest, final float price) {
		return new Destination() {
			private int cost;
			{
				cost = Math.round(price);
				if(cost>100) {
					System.out.println("Over budget!");
				}
			}
			private String label = dest;
			public String readLabel() {
				return label;
			}
		};
	}
	
	public static void main(String args[]) {
		Parcel10 p = new Parcel10();
		Destination d = p.destination("Tasmania", 101.385F);
	}
}
