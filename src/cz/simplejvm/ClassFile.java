package cz.simplejvm;

import cz.simplejvm.Constant.Ref;

public class ClassFile {
	private final int minorNumber;
	private final int majorNumber;

	public ClassFile(int minorNumber, int majorNumber) {
		this.minorNumber = minorNumber;
		this.majorNumber = majorNumber;
	}

	public static class Member {
		private final int accessFlags;
		private final int nameIndex;
		private final int descriptorIndex;
		private final Attribute[] attributes;

		public Member(int accessFlags, int nameIndex, int descriptorIndex,
				Attribute[] attributes) {
			this.accessFlags = accessFlags;
			this.nameIndex = nameIndex;
			this.descriptorIndex = descriptorIndex;
			this.attributes = attributes;
		}
	}

	public static class Field extends Member {
		public Field(int accessFlags, int nameIndex, int descriptorIndex,
				Attribute[] attributes) {
			super(accessFlags, nameIndex, descriptorIndex, attributes);
		}
	}

	public static class Method extends Member {
		public Method(int accessFlags, int nameIndex, int descriptorIndex,
				Attribute[] attributes) {
			super(accessFlags, nameIndex, descriptorIndex, attributes);

		}
	}

	public static class Attribute {
		private final int nameIndex;

		public Attribute(int nameIndex) {
			this.nameIndex = nameIndex;
		}
	}

	public static abstract class Constant {
		public void link(Constant[] constantPool) {
		}
	}

	public static class ClassConstant extends Constant {
		private final int nameIndex;
		private String name;

		public ClassConstant(int nameIndex) {
			this.nameIndex = nameIndex;
		}

		@Override
		public void link(Constant[] constantPool) {
			name = ((Utf8Constant) constantPool[nameIndex]).value;
		}

		public String getName() {
			return name;
		}
	}

	public static class RefConstant extends Constant {
		private final int classIndex;
		private final int nameAndTypeIndex;
		private ClassConstant clazz;
		private NameAndTypeConstant nameAndType;

		public RefConstant(int classIndex, int nameAndTypeIndex) {
			this.classIndex = classIndex;
			this.nameAndTypeIndex = nameAndTypeIndex;
		}

		@Override
		public void link(Constant[] constantPool) {
			clazz = (ClassConstant) constantPool[classIndex];
			nameAndType = (NameAndTypeConstant) constantPool[nameAndTypeIndex];
		}
		
		public ClassConstant getClazz() {
			return clazz;
		}
		
		public NameAndTypeConstant getNameAndType() {
			return nameAndType;
		}
	}

	public static class FieldRefConstant extends RefConstant {
		public FieldRefConstant(int classIndex, int nameAndTypeIndex) {
			super(classIndex, nameAndTypeIndex);
		}
	}

	public static class MethodRefConstant extends RefConstant {
		public MethodRefConstant(int classIndex, int nameAndTypeIndex) {
			super(classIndex, nameAndTypeIndex);
		}
	}

	public static class InterfaceMethodRefConstant extends RefConstant {
		public InterfaceMethodRefConstant(int classIndex, int nameAndTypeIndex) {
			super(classIndex, nameAndTypeIndex);
		}
	}

	public static class StringConstant extends Constant {
		private final int stringIndex;
		private String value;

		public StringConstant(int stringIndex) {
			this.stringIndex = stringIndex;
		}
		
		@Override
		public void link(Constant[] constantPool) {
			value = ((Utf8Constant) constantPool[stringIndex]).value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public static class IntegerConstant extends Constant {
		private final int value;

		public IntegerConstant(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}

	public static class FloatConstant extends Constant {
		private final float value;

		public FloatConstant(float value) {
			this.value = value;
		}
		
		public float getValue() {
			return value;
		}
	}

	public static class LongConstant extends Constant {
		private final long value;

		public LongConstant(long value) {
			this.value = value;
		}
		
		public long getValue() {
			return value;
		}
	}

	public static class DoubleConstant extends Constant {
		private final double value;

		public DoubleConstant(double value) {
			this.value = value;
		}
		
		public double getValue() {
			return value;
		}
	}

	public static class NameAndTypeConstant extends Constant {
		private final int nameIndex;
		private final int descriptorIndex;
		private String name;
		private String descriptor;

		public NameAndTypeConstant(int nameIndex, int descriptorIndex) {
			this.nameIndex = nameIndex;
			this.descriptorIndex = descriptorIndex;
		}
		
		@Override
		public void link(Constant[] constantPool) {
			name = ((Utf8Constant) constantPool[nameIndex]).value;
			descriptor = ((Utf8Constant) constantPool[descriptorIndex]).value;
		}
	}

	public static class Utf8Constant extends Constant {
		private final String value;

		public Utf8Constant(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public static class MethodHandleConstant extends Constant {
		private final int referenceKind;
		private final int referenceIndex;

		public MethodHandleConstant(int referenceKind, int referenceIndex) {
			this.referenceKind = referenceKind;
			this.referenceIndex = referenceIndex;
		}
	}

	public static class MethodTypeConstant extends Constant {
		private final int descriptorIndex;
		private String descriptor;

		public MethodTypeConstant(int descriptorIndex) {
			this.descriptorIndex = descriptorIndex;
		}
		
		@Override
		public void link(Constant[] constantPool) {
			descriptor = ((Utf8Constant) constantPool[descriptorIndex]).value;
		}
	}

	public static class InvokeDynamicConstant extends Constant {
		private final int bootstrapMethodAttrIndex;
		private final int nameAndTypeIndex;

		public InvokeDynamicConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
			this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
			this.nameAndTypeIndex = nameAndTypeIndex;
		}
	}
}
