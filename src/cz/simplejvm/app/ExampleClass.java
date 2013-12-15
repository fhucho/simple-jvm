
package cz.simplejvm.app;

public class ExampleClass extends Object {
	private class DataHolder{
		char[] data;
		public DataHolder(char[] fname) {
			data=nativeMethods.readFromFile(fname);
		}

	}
	int a;
	NativeMethods nativeMethods = new NativeMethods();

	public ExampleClass() {
		a = 10000;
	}

	void testGc() {
		char[] fname = {
				'i', 'n', 'p', 'u', 't', '.', 't', 'x', 't'
		};
		DataHolder data = new DataHolder(fname);
		int i = 1;
		while (i < 1000) {
			i++;
			DataHolder tmp = new DataHolder(fname);
		}
		new NativeMethods().writeToFile(new char[] {
				'o', 'u', 't'
		}, data.data);

	}




	public static void start() {
		new ExampleClass().testGc();
	}

	public int arrayTest1(int a) {

		ExampleClass array[] = new ExampleClass[20];
		for (int i = 0; i < 12; i++) {
			array[i] = new ExampleClass();
		}
		return a;

	}

	public int arrayTest3(int a) {

		boolean array[] = new boolean[20];
		for (int i = 0; i < 12; i++) {
			array[i] = (i % 2 == 0);
		}
		return a;

	}

	public int arrayTest2() {

		ExampleClass array[] = new ExampleClass[20];
		for (int i = 0; i < 12; i++) {
			array[i] = new ExampleClass();
		}
		return 20;

	}
}
