package android.util;

public class Log {
	
	public static void e(String logTag, InterruptedException e) {
		// TODO Auto-generated method stub
		System.err.println(logTag + "     " + e.getMessage());
		e.printStackTrace();
	}

	public static void e(String tag, String string) {
		// TODO Auto-generated method stub
		System.err.println(tag + "     " + string);
	}

	public static void e(String tag, String message, RuntimeException e) {
		// TODO Auto-generated method stub
		System.err.println(tag + "     " + message);
		e.printStackTrace();
	}

	public static void w(String logTag, InterruptedException e) {
		// TODO Auto-generated method stub
		System.err.println(logTag + "     " + e.getMessage());
		e.printStackTrace();
	}

	public static void w(String tag, String string) {
		// TODO Auto-generated method stub
		System.err.println(tag + "     " + string);
	}

	public static void w(String tag, String message, RuntimeException e) {
		// TODO Auto-generated method stub
		System.err.println(tag + "     " + message);
		e.printStackTrace();
	}

	public static void wtf(String tag, String message, Throwable t) {
		// TODO Auto-generated method stub
		System.err.println(tag + "     " + message);
		t.printStackTrace();
	}
	
	public static void v(String tag, String string) {
		// TODO Auto-generated method stub
		System.out.println(tag + "     " + string);
	}

}
