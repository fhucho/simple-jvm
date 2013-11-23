package cz.simplejvm;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		String fileName = "./bin/cz/simplejvm/ExampleClass.class";
		DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
		new ClassFileReader().readClassFile(dis);
		dis.close();
	}
}
