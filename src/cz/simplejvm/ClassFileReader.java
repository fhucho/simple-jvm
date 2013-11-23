package cz.simplejvm;

import java.io.DataInputStream;
import java.io.IOException;

public class ClassFileReader {
	private DataInputStream dis;

	public ClassFile readClassFile(DataInputStream dis) throws IOException {
		this.dis = dis;

		readMagicNumber();

		int minorNumber = dis.readUnsignedShort();
		int majorNumber = dis.readUnsignedShort();

		Constant[] constantPool = new ConstantPoolReader().readConstantPool(dis);

		int accessFlags = dis.readUnsignedShort();

		int thisClass = dis.readUnsignedShort();
		int superClass = dis.readUnsignedShort();

		int[] interfaceIndexes = readInterfaceIndexes();

		ClassFile.Field[] fields = readFields(constantPool);
		
		ClassFile.Method[] methods = readMethods(constantPool);
		
		ClassFile.Attribute[] attributes = readAttributes(constantPool);

		return new ClassFile(minorNumber, majorNumber);
	}

	private void readMagicNumber() throws IOException {
		if (!(dis.readUnsignedByte() == 0xCA && dis.readUnsignedByte() == 0xFE
				&& dis.readUnsignedByte() == 0xBA && dis.readUnsignedByte() == 0xBE)) {
			throw new InvalidClassFileException();
		}
	}

	private int[] readInterfaceIndexes() throws IOException {
		int[] interfaceIndexes = new int[dis.readUnsignedShort()];
		for (int i = 0; i < interfaceIndexes.length; i++) {
			interfaceIndexes[i] = dis.readUnsignedShort();
		}
		return interfaceIndexes;
	}

	private ClassFile.Field[] readFields(Constant[] constantPool) throws IOException {
		ClassFile.Field[] fields = new ClassFile.Field[dis.readUnsignedShort()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = readField(constantPool);
		}
		return fields;
	}

	private ClassFile.Field readField(Constant[] constantPool) throws IOException {
		int accessFlags = dis.readUnsignedShort();
		int nameIndex = dis.readUnsignedShort();
		int descriptorIndex = dis.readUnsignedShort();
		ClassFile.Attribute[] attributes = readAttributes(constantPool);
		return new ClassFile.Field(accessFlags, nameIndex, descriptorIndex, attributes);
	}

	private ClassFile.Method[] readMethods(Constant[] constantPool) throws IOException {
		ClassFile.Method[] methods = new ClassFile.Method[dis.readUnsignedShort()];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = readMethod(constantPool);
		}
		return methods;
	}
	
	private ClassFile.Method readMethod(Constant[] constantPool) throws IOException {
		int accessFlags = dis.readUnsignedShort();
		int nameIndex = dis.readUnsignedShort();
		int descriptorIndex = dis.readUnsignedShort();
		ClassFile.Attribute[] attributes = readAttributes(constantPool);
		return new ClassFile.Method(accessFlags, nameIndex, descriptorIndex, attributes);
	}

	private ClassFile.Attribute[] readAttributes(Constant[] constantPool) throws IOException {
		ClassFile.Attribute[] attributes = new ClassFile.Attribute[dis.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			readAttribute(constantPool);
		}
		return attributes;
	}

	private void readAttribute(Constant[] constantPool) throws IOException {
		int nameIndex = dis.readUnsignedShort();
		int length = dis.readInt();
		dis.readFully(new byte[length]);
	}
}
