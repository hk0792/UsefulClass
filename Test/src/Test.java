import java.util.Date;

public class Test {
	public static final long magic = ('F' << 24 | 'a' << 16 | 'i' << 8 | 's'
			| 'e' | 'r');
	public static void main(String[] args) {

		double a = 5, b = 5, c = 7;

		String str=" sd dg  dd ";
		String t="*";
		
		System.out.println("");
		
		System.out.println("A=" + caculate(a, b, c));
		System.out.println("B=" + caculate(b, a, c));
		System.out.println("C=" + caculate(c, b, a));
		System.out.println("A+B+C=" + (caculate(a, b, c) + caculate(b, a, c)
				+ caculate(c, b, a)));
		
		System.out.println("magic="+magic);
	}

	private static double caculate(double a, double b, double c) {
		return Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2 * b * c)));
	}

}
