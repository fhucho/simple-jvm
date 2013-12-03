package cz.simplejvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.simplejvm.ClassFile.Field;
import cz.simplejvm.ClassFile.FieldRefConstant;

public class Heap {
	private final List<Instance> objects = new ArrayList<Instance>();

	public ArrayInstance getArray(int reference) {
		return (ArrayInstance) objects.get(reference);
	}

	public ObjectInstance getObject(int reference) {
		return (ObjectInstance) objects.get(reference);
	}

	public ObjectInstance newObject(ClassFile classFile) {
		int reference = objects.size();
		ObjectInstance o = new ObjectInstance(classFile, reference);
		objects.add(o);
		return o;
	}

	public ArrayInstance newArray(ClassFile classFile, int length) {
		int reference = objects.size();
		ArrayInstance a = new ArrayInstance(classFile, length, reference);
		objects.add(a);
		return a;
	}

	private static class Instance {
		private final int reference;
		protected final ClassFile classFile;

		public Instance(ClassFile classFile, int reference) {
			this.reference = reference;
			this.classFile = classFile;
		}

		public int getReference() {
			return reference;
		}
	}

	public static class ObjectInstance extends Instance {
		private final Map<String, Integer> fields;

		public ObjectInstance(ClassFile classFile, int reference) {
			super(classFile, reference);
			fields = new HashMap<String, Integer>();
			for (Field field : classFile.getFields()) {
				fields.put(field.getName(), 0);
			}
		}

		public int getField(int field) {
			FieldRefConstant fr = (FieldRefConstant) (classFile.getConstantPool()[field]);
			String name = fr.getNameAndType().getName();
			return fields.get(name);
		}

		public void putField(int field, int value) {
			FieldRefConstant fr = (FieldRefConstant) (classFile.getConstantPool()[field]);
			String name = fr.getNameAndType().getName();
			fields.put(name, value);
		}
	}

	public static class ArrayInstance extends Instance {
		private final int[] items;

		public ArrayInstance(ClassFile classFile, int length, int reference) {
			super(classFile, reference);
			items = new int[length];
		}

		public int getItem(int index) {
			return items[index];
		}

		public int getLength() {
			return items.length;
		}

		public void setItem(int index, int value) {
			items[index] = value;
		}
	}
}
