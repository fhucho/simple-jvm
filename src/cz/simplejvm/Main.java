
package cz.simplejvm;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		ClassFile claz = ClassFileResolver.getInstance().getClassFile("cz/simplejvm/Knapsack");
		System.out.println(claz.toString());
	}
}
