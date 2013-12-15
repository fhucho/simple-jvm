
package cz.simplejvm;

import java.io.IOException;

import cz.simplejvm.app.ExampleClass;

public class Main {
	public static void main(String[] args) throws IOException {
		ClassFile claz = ClassFileResolver.getInstance().getClassFile("cz/simplejvm/app/ExampleClass");
		System.out.println(claz.toString());

		Runtime runtime = new Runtime();
		// runtime.start("cz/simplejvm/app/Knapsack", "start", "()V");
		runtime.start("cz/simplejvm/app/ExampleClass", "start", "()V");

		System.out.println("--------------------");
		// Knapsack.start();
		ExampleClass.start();
	}
}
