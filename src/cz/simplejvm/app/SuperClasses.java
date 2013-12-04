
package cz.simplejvm.app;

public class SuperClasses {


	public static void start() {
		new SuperClasses().test();
	}

	void test() {
		new Main().test();
	}

	private static class SuperSuperMain {

		NativeMethods nm = new NativeMethods();

		public SuperSuperMain() {
			nm.print(11);
		}
		public void test() {
			nm.print(11000);
		}

	}

	private static class SuperMain  extends SuperSuperMain
	{

		public SuperMain() {
			nm.print(10);
		}

		//		@Override
		//		public void test() {
		//			nm.print(10000);
		//		}

	}

	private static class Main extends SuperMain {

		public Main() {
			super();
			nm.print(9);
		}

		@Override
		public void test() {
			nm.print(9000);
			super.test();
		}

		//		@Override
		//		public void test() {
		//			nm.print(2);
		//		}
	}

}
