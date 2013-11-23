package cz.simplejvm;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolReader {
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

	public Constant[] readConstantPool(DataInputStream dis) throws IOException {
		this.dis = dis;

		int constantPoolCount = dis.readUnsignedShort();
		Constant[] constantPool = new Constant[constantPoolCount + 1];

		int index = 1;
		while (index < constantPoolCount) {
			index += readConstant(constantPool, index);
		}

		for (int i = 1; i < constantPoolCount; i++) {
			if (constantPool[i] != null) constantPool[i].link(constantPool);
		}
		
		return constantPool;
	}

	private int readConstant(Constant[] constantPool, int index) throws IOException {
		int tag = dis.readUnsignedByte();

		switch (tag) {
		case TAG_CLASS:
			constantPool[index] = readClass();
			return 1;
		case TAG_FIELDREF:
			constantPool[index] = readFieldRef();
			return 1;
		case TAG_METHODREF:
			constantPool[index] = readMethodRef();
			return 1;
		case TAG_INTERFACEMETHODREF:
			constantPool[index] = readInterfaceMethodRef();
			return 1;
		case TAG_STRING:
			constantPool[index] = readString();
			return 1;
		case TAG_INTEGER:
			constantPool[index] = readInteger();
			return 1;
		case TAG_FLOAT:
			constantPool[index] = readFloat();
			return 1;
		case TAG_LONG:
			constantPool[index] = readLong();
			return 2;
		case TAG_DOUBLE:
			constantPool[index] = readDouble();
			return 2;
		case TAG_NAMEANDTYPE:
			constantPool[index] = readNameAndType();
			return 1;
		case TAG_UTF8:
			constantPool[index] = readUtf8();
			return 1;
		case TAG_METHODHANDLE:
			constantPool[index] = readMethodHandle();
			return 1;
		case TAG_METHODTYPE:
			constantPool[index] = readMethodType();
			return 1;
		case TAG_INVOKEDYNAMIC:
			constantPool[index] = readInvokeDynamic();
			return 1;
		default:
			throw new InvalidClassFileException();
		}
	}

	private Constant.Class readClass() throws IOException {
		int nameIndex = dis.readUnsignedShort();
		return new Constant.Class(nameIndex);
	}

	private Constant.FieldRef readFieldRef() throws IOException {
		int classIndex = dis.readUnsignedShort();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new Constant.FieldRef(classIndex, nameAndTypeIndex);
	}

	private Constant.MethodRef readMethodRef() throws IOException {
		int classIndex = dis.readUnsignedShort();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new Constant.MethodRef(classIndex, nameAndTypeIndex);
	}

	private Constant.InterfaceMethodRef readInterfaceMethodRef() throws IOException {
		int classIndex = dis.readUnsignedShort();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new Constant.InterfaceMethodRef(classIndex, nameAndTypeIndex);
	}

	private Constant.Str readString() throws IOException {
		int stringIndex = dis.readUnsignedShort();
		return new Constant.Str(stringIndex);
	}

	private Constant.Integer readInteger() throws IOException {
		return new Constant.Integer(dis.readInt());
	}

	private Constant.Float readFloat() throws IOException {
		return new Constant.Float(dis.readFloat());
	}

	private Constant.Long readLong() throws IOException {
		return new Constant.Long(dis.readLong());
	}

	private Constant.Double readDouble() throws IOException {
		return new Constant.Double(dis.readDouble());
	}

	private Constant.NameAndType readNameAndType() throws IOException {
		int nameIndex = dis.readUnsignedShort();
		int descriptorIndex = dis.readUnsignedShort();
		return new Constant.NameAndType(nameIndex, descriptorIndex);
	}

	private Constant.Utf8 readUtf8() throws IOException {
		return new Constant.Utf8(dis.readUTF());
	}

	private Constant.MethodHandle readMethodHandle() throws IOException {
		int referenceKind = dis.readUnsignedByte();
		int referenceIndex = dis.readUnsignedShort();
		return new Constant.MethodHandle(referenceKind, referenceIndex);
	}

	private Constant.MethodType readMethodType() throws IOException {
		int descriptorIndex = dis.readUnsignedByte();
		return new Constant.MethodType(descriptorIndex);
	}

	private Constant.InvokeDynamic readInvokeDynamic() throws IOException {
		int bootstrapMethodAttrIndex = dis.readUnsignedByte();
		int nameAndTypeIndex = dis.readUnsignedShort();
		return new Constant.InvokeDynamic(bootstrapMethodAttrIndex, nameAndTypeIndex);
	}

}
