
package cz.simplejvm.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NativeMethods {
	public void testNative() {

	}

	public void print(int number) {
		System.out.print(number);
	}

	public void println(int number) {
		System.out.println(number);
	}

	public void print(char c) {
		System.out.print(c);
	}

	public void println(char c) {
		System.out.println(c);
	}

	public void println(char[] c) {
		System.out.println(c);
	}

	public void print(char[] c) {
		System.out.print(c);
	}

	public char[] readFromFile(char[] filename) {
		try {

			BufferedReader br = new BufferedReader(new FileReader(new String(filename)));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				String everything = sb.toString();
				return everything.toCharArray();
			} finally {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
