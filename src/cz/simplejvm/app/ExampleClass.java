package cz.simplejvm.app;


public class ExampleClass {
	int a;


	public ExampleClass() {
		a= 5456455;
	}

	public native void testStr(String str);



	private void test2(int number) {
		new NativeMethods().print(a+number+3);
	}

	public static void start() {
		int a=3;
		int b=6;
		int c = a+b;
		ExampleClass clazz = new ExampleClass();
		new NativeMethods().print(c);
		clazz.a=5;
		clazz.test2(c);
	}

	public int testArray(int a) {

		ExampleClass array[] =new ExampleClass[20];
		for(int i=0; i<12; i++) {
			array[i]=new ExampleClass();
		}
		return a;

	}

	public int testArray2() {

		ExampleClass array[] =new ExampleClass[20];
		for(int i=0; i<12; i++) {
			array[i]=new ExampleClass();
		}
		return 20;

	}

	public ExampleClass test() {
		testArray(45);
		return null;
	}

	public void testParams(int a, int b, int c) {
		//		testNative();
		testStr("dsdssds");
	}

}

