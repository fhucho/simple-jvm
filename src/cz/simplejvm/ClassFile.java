
package cz.simplejvm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassFile {
	private final int minorNumber;
	private final int majorNumber;
	private final int accessFlags;
	private final ClassConstant thisClass;
	private final ClassConstant superClass;
	private final ClassConstant[] interfaces;
	private final Constant[] constantPool;
	private final Field[] fields;
	private final Method[] methods;
	private final Attribute[] attributes;

	public ClassFile(int minorNumber, int majorNumber, int accessFlags, ClassConstant thisClass, ClassConstant superClass, ClassConstant[] interfaces, Constant[] constantPool, Field[] fields,
			Method[] methods, Attribute[] attributes) {
		this.minorNumber = minorNumber;
		this.majorNumber = majorNumber;
		this.accessFlags = accessFlags;
		this.thisClass = thisClass;
		this.superClass = superClass;
		this.interfaces = interfaces;
		this.constantPool = constantPool;
		this.fields = fields;
		this.methods = methods;
		this.attributes = attributes;
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public ClassConstant getThisClass() {
		return thisClass;
	}

	public ClassConstant getSuperClass() {
		return superClass;
	}

	public ClassConstant[] getInterfaces() {
		return interfaces;
	}

	public Constant[] getConstantPool() {
		return constantPool;
	}

	public Field[] getFields() {
		return fields;
	}

	public Method[] getMethods() {
		return methods;
	}

	public Method getMethod(String methodName) {
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	private static String getUtf8Constant(Constant[] constantPool, int index) {
		return ((Utf8Constant) constantPool[index]).value;
	}

	public void link() {
		for (int i = 1; i < constantPool.length; i++) {
			if (constantPool[i] != null) {
				constantPool[i].link(constantPool);
			}
		}

		thisClass.link(constantPool);
		superClass.link(constantPool);

		for (ClassConstant iface : interfaces) {
			iface.link(constantPool);
		}

		for (Field field : fields) {
			field.link(constantPool);
		}

		for (Method method : methods) {
			method.link(constantPool);
		}
	}

	@Override
	public String toString() {
		String rtrn = thisClass.name + "\n";
		rtrn += superClass.name + "\n";
		for (Member member : methods) {
			rtrn += member.toString(constantPool) + "\n";
		}
		return rtrn;
	}

	public static class Member {
		private final int accessFlags;
		private final int nameIndex;
		private final int descriptorIndex;
		private final Attribute[] attributes;
		private String descriptor;
		private String name;

		public Member(int accessFlags, int nameIndex, int descriptorIndex, Attribute[] attributes) {
			this.accessFlags = accessFlags;
			this.nameIndex = nameIndex;
			this.descriptorIndex = descriptorIndex;
			this.attributes = attributes;
		}

		public void link(Constant[] constantPool) {
			name = getUtf8Constant(constantPool, nameIndex);
			descriptor = getUtf8Constant(constantPool, descriptorIndex);
		}

		public String toString(Constant[] constantPool) {

			NameAndTypeConstant nameType = new NameAndTypeConstant(nameIndex, descriptorIndex);
			nameType.link(constantPool);

			String rtrn = getClass().getSimpleName() + "-" + nameType.name +"-"+ nameType.descriptor + "\n";
			for (Attribute attr : attributes) {
				rtrn += "attr: " + attr + "\n";
			}
			return rtrn;
		}

		public String getDescriptor() {
			return descriptor;
		}

		public String getName() {
			return name;
		}

		public int getAccessFlags() {
			return accessFlags;
		}

		public Attribute[] getAttributes() {
			return attributes;
		}

	}

	public static class Field extends Member {
		public Field(int accessFlags, int nameIndex, int descriptorIndex, Attribute[] attributes) {
			super(accessFlags, nameIndex, descriptorIndex, attributes);
		}
	}

	public static class Method extends Member {

		private static Pattern allParamsPattern = Pattern.compile("(\\(.*?\\))");
		private static Pattern paramsPattern = Pattern.compile("(\\[?)(C|Z|S|I|J|F|D|(:?L[^;]+;))");

		private Integer numberOfParameters;

		public Method(int accessFlags, int nameIndex, int descriptorIndex, Attribute[] attributes) {
			super(accessFlags, nameIndex, descriptorIndex, attributes);
		}

		public int getParamsCount() {
			if (numberOfParameters == null) {
				numberOfParameters = calculateParamCount(getDescriptor());
			}
			return numberOfParameters;
		}

		public CodeAttribute getCodeAttribute() {
			try {
				return (CodeAttribute) getAttributes()[0];
			} catch (Exception e) {
				for (Attribute attr : getAttributes()) {
					if (attr instanceof CodeAttribute) {
						return (CodeAttribute) attr;
					}
				}
			}
			throw new RuntimeException("Code not found");

		}

		private int calculateParamCount(String methodRefType) {
			Matcher m = allParamsPattern.matcher(methodRefType);
			if (!m.find()) {
				throw new IllegalArgumentException("Method signature does not contain parameters");
			}
			String paramsDescriptor = m.group(1);
			Matcher mParam = paramsPattern.matcher(paramsDescriptor);

			int count = 0;
			while (mParam.find()) {
				count++;
			}
			return count;
		}

		@Override
		public String toString(Constant[] constantPool) {

			return getParamsCount() + ": " + super.toString(constantPool);
		}
	}

	public static abstract class Attribute {
	}

	public static class CodeAttribute extends Attribute {
		private final int maxStack;
		private final int maxLocals;
		private final int[] code;
		private final Attribute[] attributes;

		public CodeAttribute(int maxStack, int maxLocals, int[] code, Attribute[] attributes) {
			this.maxStack = maxStack;
			this.maxLocals = maxLocals;
			this.code = code;
			this.attributes = attributes;
		}



		public int getMaxStack() {
			return maxStack;
		}



		public int getMaxLocals() {
			return maxLocals;
		}



		public int[] getCode() {
			return code;
		}



		@Override
		public String toString() {
			String text = "maxStack: " + maxStack + "\n";
			text += "maxLocals: " + maxLocals + "\n";
			text += "code: \n";
			for (int instruct : code) {
				text += String.format("%02X ", instruct) + "\n";
			}
			text += "attributes: \n";
			for (Attribute attr : attributes) {
				text += "\t" + attr + "\n";
			}

			return text;
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
			name = getUtf8Constant(constantPool, nameIndex);
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
			value = getUtf8Constant(constantPool, stringIndex);
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
			name = getUtf8Constant(constantPool, nameIndex);
			descriptor = getUtf8Constant(constantPool, descriptorIndex);
		}

		public String getName() {
			return name;
		}

		public String getDescriptor() {
			return descriptor;
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
			descriptor = getUtf8Constant(constantPool, descriptorIndex);
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
