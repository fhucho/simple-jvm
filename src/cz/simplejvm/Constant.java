package cz.simplejvm;

public abstract class Constant {

	public void link(Constant[] constantPool) {
	}

	public static class Class extends Constant {
		private final int nameIndex;
		private String name;

		public Class(int nameIndex) {
			this.nameIndex = nameIndex;
		}

		@Override
		public void link(Constant[] constantPool) {
			name = ((Constant.Utf8) constantPool[nameIndex]).value;
		}

		public String getName() {
			return name;
		}
	}

	public static class Ref extends Constant {
		private final int classIndex;
		private final int nameAndTypeIndex;
		private Constant.Class clazz;
		private Constant.NameAndType nameAndType;

		public Ref(int classIndex, int nameAndTypeIndex) {
			this.classIndex = classIndex;
			this.nameAndTypeIndex = nameAndTypeIndex;
		}

		@Override
		public void link(Constant[] constantPool) {
			clazz = (Constant.Class) constantPool[classIndex];
			nameAndType = (Constant.NameAndType) constantPool[nameAndTypeIndex];
		}
		
		public Constant.Class getClazz() {
			return clazz;
		}
		
		public Constant.NameAndType getNameAndType() {
			return nameAndType;
		}
	}

	public static class FieldRef extends Ref {
		public FieldRef(int classIndex, int nameAndTypeIndex) {
			super(classIndex, nameAndTypeIndex);
		}
	}

	public static class MethodRef extends Ref {
		public MethodRef(int classIndex, int nameAndTypeIndex) {
			super(classIndex, nameAndTypeIndex);
		}
	}

	public static class InterfaceMethodRef extends Ref {
		public InterfaceMethodRef(int classIndex, int nameAndTypeIndex) {
			super(classIndex, nameAndTypeIndex);
		}
	}

	public static class Str extends Constant {
		private final int stringIndex;
		private String value;

		public Str(int stringIndex) {
			this.stringIndex = stringIndex;
		}
		
		@Override
		public void link(Constant[] constantPool) {
			value = ((Constant.Utf8) constantPool[stringIndex]).value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public static class Integer extends Constant {
		private final int value;

		public Integer(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}

	public static class Float extends Constant {
		private final float value;

		public Float(float value) {
			this.value = value;
		}
		
		public float getValue() {
			return value;
		}
	}

	public static class Long extends Constant {
		private final long value;

		public Long(long value) {
			this.value = value;
		}
		
		public long getValue() {
			return value;
		}
	}

	public static class Double extends Constant {
		private final double value;

		public Double(double value) {
			this.value = value;
		}
		
		public double getValue() {
			return value;
		}
	}

	public static class NameAndType extends Constant {
		private final int nameIndex;
		private final int descriptorIndex;
		private String name;
		private String descriptor;

		public NameAndType(int nameIndex, int descriptorIndex) {
			this.nameIndex = nameIndex;
			this.descriptorIndex = descriptorIndex;
		}
		
		@Override
		public void link(Constant[] constantPool) {
			name = ((Constant.Utf8) constantPool[nameIndex]).value;
			descriptor = ((Constant.Utf8) constantPool[descriptorIndex]).value;
		}
	}

	public static class Utf8 extends Constant {
		private final String value;

		public Utf8(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}

	public static class MethodHandle extends Constant {
		private final int referenceKind;
		private final int referenceIndex;

		public MethodHandle(int referenceKind, int referenceIndex) {
			this.referenceKind = referenceKind;
			this.referenceIndex = referenceIndex;
		}
	}

	public static class MethodType extends Constant {
		private final int descriptorIndex;
		private String descriptor;

		public MethodType(int descriptorIndex) {
			this.descriptorIndex = descriptorIndex;
		}
		
		@Override
		public void link(Constant[] constantPool) {
			descriptor = ((Constant.Utf8) constantPool[descriptorIndex]).value;
		}
	}

	public static class InvokeDynamic extends Constant {
		private final int bootstrapMethodAttrIndex;
		private final int nameAndTypeIndex;

		public InvokeDynamic(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
			this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
			this.nameAndTypeIndex = nameAndTypeIndex;
		}
	}
}
