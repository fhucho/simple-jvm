package cz.simplejvm.app;


public class ExampleClass {
	int a;
	NativeMethods nativeMethods = new NativeMethods();


	public ExampleClass() {
		a= 10000;
	}




	public void addTest(int number) {
		int result = a+number+3;
		nativeMethods.print(result);
	}

	public void testBoolean() {
		boolean aaa = true;
		if(aaa!=false) {
			nativeMethods.print(0);
		}else {
			nativeMethods.print(1);
		}
	}


	public static void start() {
		int step=3;
		int size=10;
		int c=0;
		for(int i=0; i<size; i++) {
			new NativeMethods().print(i);
			c+=step;
		}

		ExampleClass clazz = new ExampleClass();
		new NativeMethods().print(c);
		clazz.addTest(c);
		clazz.testBoolean();
	}

	public int arrayTest1(int a) {

		ExampleClass array[] =new ExampleClass[20];
		for(int i=0; i<12; i++) {
			array[i]=new ExampleClass();
		}
		return a;

	}

	public int arrayTest3(int a) {

		boolean array[] =new boolean[20];
		for(int i=0; i<12; i++) {
			array[i]=(i%2==0);
		}
		return a;

	}

	public int arrayTest2() {

		ExampleClass array[] =new ExampleClass[20];
		for(int i=0; i<12; i++) {
			array[i]=new ExampleClass();
		}
		return 20;

	}
}

