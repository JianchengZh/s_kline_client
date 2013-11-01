package android.util;


public class PrefixPrinter {
	Printer printer = null;
	static String prefix = null;

	public static Printer create(Printer pw_a, String prefix_a) {
		// TODO Auto-generated method stub
		prefix = prefix_a;
		
		return pw_a;
	}

}
