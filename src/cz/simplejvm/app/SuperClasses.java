
package cz.simplejvm.app;

public class SuperClasses {


	public static void start() {
		new SuperClasses().test();
	}

	void test() {
		new Main().test();
	}

	private static class SuperSuperMain {
		char[] string = {'t', 'e', 's', 't'};
		char[] string2 = {'a', 'h', 'o', 'j'};

		NativeMethods nm = new NativeMethods();

		public SuperSuperMain() {
			nm.println(11);
		}
		public void test() {
			nm.println(11000);
			nm.println(string);
		}

	}

	private static class SuperMain  extends SuperSuperMain
	{

		public SuperMain() {
			nm.println(10);
		}

		//		@Override
		//		public void test() {
		//			nm.print(10000);
		//		}

	}

	private static class Main extends SuperMain {

		public Main() {
			super();
			nm.println(9);
			nm.println('c');
			nm.println(string2);
		}

		@Override
		public void test() {
			nm.println(9000);
			super.test();
		}

		//		@Override
		//		public void test() {
		//			nm.print(2);
		//		}
	}

}
