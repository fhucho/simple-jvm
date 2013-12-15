
package cz.simplejvm.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

	public int[] stringToIntArray(char[] string) {
		String s = new String(string);
		String[] parts = s.replaceAll("\\s+", " ").split(" ");
		List<Integer> ints = new ArrayList<Integer>();
		for (String part : parts) {
			try {
				ints.add(Integer.parseInt(part));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int[] result = new int[ints.size()];
		for (int i = 0; i < ints.size(); i++) {
			result[i] = ints.get(i);
		}
		return result;
	}

	public void writeToFile(char[] filename, char[] text) {
		try {
			PrintWriter writer = new PrintWriter(new String(filename), "UTF-8");
			writer.print(text);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
