package cz.simplejvm;


public class ExampleClass {
	int a;


	public ExampleClass() {
		a= 5456455;
	}

	public native void testStr(String str);





	public static void start() {
		ExampleClass ex = new ExampleClass();

		int array[] =new int[20];
		for(int i=0; i<12; i++) {
			array[i]=i;
		}




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
		testNative();
		testStr("dsdssds");
	}

	public native void testNative();

}

