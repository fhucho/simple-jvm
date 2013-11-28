package cz.simplejvm;

import java.io.DataInputStream;
import java.io.IOException;

public class ClassFileReader {
	private static final int TAG_CLASS = 7;
	private static final int TAG_FIELDREF = 9;
	private static final int TAG_METHODREF = 10;
	private static final int TAG_INTERFACEMETHODREF = 11;
	private static final int TAG_STRING = 8;
	private static final int TAG_INTEGER = 3;
	private static final int TAG_FLOAT = 4;
	private static final int TAG_LONG = 5;
	private static final int TAG_DOUBLE = 6;
	private static final int TAG_NAMEANDTYPE = 12;
	private static final int TAG_UTF8 = 1;
	private static final int TAG_METHODHANDLE = 15;
	private static final int TAG_METHODTYPE = 16;
	private static final int TAG_INVOKEDYNAMIC = 18;

	private DataInputStream dis;

	public ClassFile readClassFile(DataInputStream dis) throws IOException {
		this.dis = dis;

		readMagicNumber();
		int minorNumber = dis.readUnsignedShort();
		int majorNumber = dis.readUnsignedShort();

		ClassFile.Constant[] constantPool = readConstantPool();

		int accessFlags = dis.readUnsignedShort();
		ClassFile.ClassConstant thisClass = (ClassFile.ClassConstant) constantPool[dis
				.readUnsignedShort()];
		ClassFile.ClassConstant superClass = (ClassFile.ClassConstant) constantPool[dis
				.readUnsignedShort()];
		ClassFile.ClassConstant[] interfaces = readInterfaces(constantPool);

		ClassFile.Field[] fields = readFields(constantPool);
		ClassFile.Method[] methods = readMethods(constantPool);
		ClassFile.Attribute[] attributes = readAttributes(constantPool);

		ClassFile cf = new ClassFile(minorNumber, majorNumber, accessFlags, thisClass,
				superClass, interfaces, constantPool, fields, methods, attributes);
		cf.link();
		return cf;
	}

	private void readMagicNumber() throws IOException {
		if (!(dis.readUnsignedByte() == 0xCA && dis.readUnsignedByte() == 0xFE
				&& dis.readUnsignedByte() == 0xBA && dis.readUnsignedByte() == 0xBE)) {
			throw new InvalidClassFileException();
		}
	}

	public ClassFile.Constant[] readConstantPool() throws IOException {
		int constantPoolCount = dis.readUnsignedShort();
		ClassFile.Constant[] constantPool = new ClassFile.Constant[constantPoolCount + 1];

		int index = 1;
		while (index < constantPoolCount) {
			index += readConstant(constantPool, index);
		}

		return constantPool;
	}

	private int readConstant(ClassFile.Constant[] constantPool, int index) throws IOException {
		int tag = dis.readUnsignedByte();

		switch (tag) {
		case TAG_CLASS:
			constantPool[index] = readClassConstant();
			return 1;
		case TAG_FIELDREF:
			constantPool[index] = readFieldRefConstant();
			return 1;
		case TAG_METHODREF:
			constantPool[index] = readMethodRefConstant();
			return 1;
		case TAG_INTERFACEMETHODREF:
			constantPool[index] = readInterfaceMethodRefConstant();
			return 1;
		case TAG_STRING:
			constantPool[index] = readStringConstant();
			return 1;
		case TAG_INTEGER:
			constantPool[index] = readIntegerConstant();
			return 1;
		case TAG_FLOAT:
			constantPool[index] = readFloatConstant();
			return 1;
		case TAG_LONG:
			constantPool[index] = readLongConstant();
			return 2;
		case TAG_DOUBLE:
			constantPool[index] = readDoubleConstant();
			return 2;
		case TAG_NAMEANDTYPE:
			constantPool[index] = readNameAndTypeConstant();
			return 1;
		case TAG_UTF8:
			constantPool[index] = readUtf8Constant();
			return 1;
		case TAG_METHODHANDLE:
			constantPool[index] = readMethodHandleConstant();
			return 1;
		case TAG_METHODTYPE:
			constantPool[index] = readMethodTypeConstant();
			return 1;
		case TAG_INVOKEDYNAMIC:
			constantPool[index] = readInvokeDynamicConstant();
			return 1;
		default:
			throw new InvalidClassFileException();
		}
	}

	private ClassFile.ClassConstant readClassConstant() throws IOException {
		int nameIndex = dis.readUnsignedShort();
		return new ClassFile.ClassConstant(nameIndex);
	}

	private ClassFile.FieldRefConstant readFieldRefConstant() throws IOException {
		int classIndex = dis.readUnsignedShort();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new ClassFile.FieldRefConstant(classIndex, nameAndTypeIndex);
	}

	private ClassFile.MethodRefConstant readMethodRefConstant() throws IOException {
		int classIndex = dis.readUnsignedShort();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new ClassFile.MethodRefConstant(classIndex, nameAndTypeIndex);
	}

	private ClassFile.InterfaceMethodRefConstant readInterfaceMethodRefConstant()
			throws IOException {
		int classIndex = dis.readUnsignedShort();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new ClassFile.InterfaceMethodRefConstant(classIndex, nameAndTypeIndex);
	}

	private ClassFile.StringConstant readStringConstant() throws IOException {
		int stringIndex = dis.readUnsignedShort();
		return new ClassFile.StringConstant(stringIndex);
	}

	private ClassFile.IntegerConstant readIntegerConstant() throws IOException {
		return new ClassFile.IntegerConstant(dis.readInt());
	}

	private ClassFile.FloatConstant readFloatConstant() throws IOException {
		return new ClassFile.FloatConstant(dis.readFloat());
	}

	private ClassFile.LongConstant readLongConstant() throws IOException {
		return new ClassFile.LongConstant(dis.readLong());
	}

	private ClassFile.DoubleConstant readDoubleConstant() throws IOException {
		return new ClassFile.DoubleConstant(dis.readDouble());
	}

	private ClassFile.NameAndTypeConstant readNameAndTypeConstant() throws IOException {
		int nameIndex = dis.readUnsignedShort();
		int descriptorIndex = dis.readUnsignedShort();
		return new ClassFile.NameAndTypeConstant(nameIndex, descriptorIndex);
	}

	private ClassFile.Utf8Constant readUtf8Constant() throws IOException {
		return new ClassFile.Utf8Constant(dis.readUTF());
	}

	private ClassFile.MethodHandleConstant readMethodHandleConstant() throws IOException {
		int referenceKind = dis.readUnsignedByte();
		int referenceIndex = dis.readUnsignedShort();
		return new ClassFile.MethodHandleConstant(referenceKind, referenceIndex);
	}

	private ClassFile.MethodTypeConstant readMethodTypeConstant() throws IOException {
		int descriptorIndex = dis.readUnsignedByte();
		return new ClassFile.MethodTypeConstant(descriptorIndex);
	}

	private ClassFile.InvokeDynamicConstant readInvokeDynamicConstant() throws IOException {
		int bootstrapMethodAttrIndex = dis.readUnsignedByte();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new ClassFile.InvokeDynamicConstant(bootstrapMethodAttrIndex, nameAndTypeIndex);
	}

	private ClassFile.ClassConstant[] readInterfaces(ClassFile.Constant[] constantPool)
			throws IOException {
		ClassFile.ClassConstant[] interfaces = new ClassFile.ClassConstant[dis
				.readUnsignedShort()];
		for (int i = 0; i < interfaces.length; i++) {
			interfaces[i] = (ClassFile.ClassConstant) constantPool[dis.readUnsignedShort()];
		}
		return interfaces;
	}

	private ClassFile.Field[] readFields(ClassFile.Constant[] constantPool) throws IOException {
		ClassFile.Field[] fields = new ClassFile.Field[dis.readUnsignedShort()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = readField(constantPool);
		}
		return fields;
	}

	private ClassFile.Field readField(ClassFile.Constant[] constantPool) throws IOException {
		int accessFlags = dis.readUnsignedShort();
		int nameIndex = dis.readUnsignedShort();
		int descriptorIndex = dis.readUnsignedShort();
		ClassFile.Attribute[] attributes = readAttributes(constantPool);
		return new ClassFile.Field(accessFlags, nameIndex, descriptorIndex, attributes);
	}

	private ClassFile.Method[] readMethods(ClassFile.Constant[] constantPool)
			throws IOException {
		ClassFile.Method[] methods = new ClassFile.Method[dis.readUnsignedShort()];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = readMethod(constantPool);
		}
		return methods;
	}

	private ClassFile.Method readMethod(ClassFile.Constant[] constantPool) throws IOException {
		int accessFlags = dis.readUnsignedShort();
		int nameIndex = dis.readUnsignedShort();
		int descriptorIndex = dis.readUnsignedShort();
		ClassFile.Attribute[] attributes = readAttributes(constantPool);
		return new ClassFile.Method(accessFlags, nameIndex, descriptorIndex, attributes);
	}

	private ClassFile.Attribute[] readAttributes(ClassFile.Constant[] constantPool)
			throws IOException {
		ClassFile.Attribute[] attributes = new ClassFile.Attribute[dis.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = readAttribute(constantPool);
		}
		return attributes;
	}

	private ClassFile.Attribute readAttribute(ClassFile.Constant[] constantPool)
			throws IOException {
		int nameIndex = dis.readUnsignedShort();
		String name = ((ClassFile.Utf8Constant) constantPool[nameIndex]).getValue();
		int length = dis.readInt();

		if (name.equals("Code")) {
			return readCodeAttribute(constantPool);
		} else {
			dis.readFully(new byte[length]);
			return null;
		}
	}

	private ClassFile.CodeAttribute readCodeAttribute(ClassFile.Constant[] constantPool)
			throws IOException {
		int maxStack = dis.readUnsignedShort();
		int maxLocals = dis.readUnsignedShort();

		int[] code = new int[dis.readInt()];
		for (int i = 0; i < code.length; i++) {
			code[i] = dis.readUnsignedByte();
		}

		int exceptionTableLength = dis.readUnsignedShort();
		dis.readFully(new byte[exceptionTableLength * 8]);

		ClassFile.Attribute[] attributes = readAttributes(constantPool);

		return new ClassFile.CodeAttribute(maxStack, maxLocals, code, attributes);
	}

	@SuppressWarnings("serial")
	public static class InvalidClassFileException extends RuntimeException {
	}
}
