package cz.simplejvm.app;


public class ExampleClass {
	int a;


	public ExampleClass() {
		a= 10000;
	}




	public void addTest(int number) {
		int result = a+number+3;
		new NativeMethods().print(result);
	}


	public static void start() {
		int a=3;
		int b=6;
		int c = a+b;
		ExampleClass clazz = new ExampleClass();
		new NativeMethods().print(c);
		clazz.addTest(c);
	}

	public int arrayTest1(int a) {

		ExampleClass array[] =new ExampleClass[20];
		for(int i=0; i<12; i++) {
			array[i]=new ExampleClass();
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

