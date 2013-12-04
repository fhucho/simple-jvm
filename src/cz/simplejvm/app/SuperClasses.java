
package cz.simplejvm.app;

public class SuperClasses {


	public static void start() {
		new SuperClasses().test();
	}

	void test() {
		new Main().test();
	}

	private static class SuperMain {

		NativeMethods nm = new NativeMethods();

		public SuperMain() {
			nm.print(11);
		}
		public void test() {
			nm.print(1);
		}

	}

	private static class Main extends SuperMain {

		public Main() {
			super();
			nm.print(22);
		}

		//		@Override
		//		public void test() {
		//			nm.print(2);
		//		}
	}

}
