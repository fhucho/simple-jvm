
package cz.simplejvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.simplejvm.ClassFile.Field;
import cz.simplejvm.ClassFile.FieldRefConstant;
import cz.simplejvm.StackFrame.Int;
import cz.simplejvm.StackFrame.Reference;
import cz.simplejvm.StackFrame.Value;

public class Heap {
	private final List<Instance> objects = new ArrayList<Instance>();

	public ArrayInstance getArray(Reference reference) {
		return (ArrayInstance) objects.get(reference.value);
	}

	public PrimitiveArrayInstance getPrimitiveArray(Reference reference) {
		return (PrimitiveArrayInstance) objects.get(reference.value);
	}

	public ObjectInstance getObject(Reference reference) {
		return (ObjectInstance) objects.get(reference.value);
	}

	public ObjectInstance newObject(ClassFile classFile) {
		Reference reference = new Reference(objects.size());
		ObjectInstance o = new ObjectInstance(classFile, reference);
		objects.add(o);
		return o;
	}

	public ArrayInstance newArray(ClassFile classFile, int length) {
		Reference reference = new Reference(objects.size());
		ArrayInstance a = new ArrayInstance(classFile, length, reference);
		objects.add(a);
		return a;
	}

	public PrimitiveArrayInstance newArray(int primitiveType, int length) {
		Reference reference = new Reference(objects.size());
		PrimitiveArrayInstance a = new PrimitiveArrayInstance(primitiveType, length, reference);
		objects.add(a);
		return a;
	}

	private static class Instance {
		private final Reference reference;
		protected final ClassFile classFile;

		public Instance(ClassFile classFile, Reference reference) {
			this.reference = reference;
			this.classFile = classFile;
		}

		public Reference getReference() {
			return reference;
		}
	}

	public static class ObjectInstance extends Instance {
		private final Map<String, Value> fields;

		public ObjectInstance(ClassFile classFile, Reference reference) {
			super(classFile, reference);
			fields = new HashMap<String, Value>();
			for (Field field : classFile.getFields()) {
				fields.put(field.getName(), null);
			}
		}

		public Value getField(int field) {
			FieldRefConstant fr = (FieldRefConstant) (classFile.getConstantPool()[field]);
			String name = fr.getNameAndType().getName();
			return fields.get(name);
		}

		public void putField(int field, Value value) {
			FieldRefConstant fr = (FieldRefConstant) (classFile.getConstantPool()[field]);
			String name = fr.getNameAndType().getName();
			fields.put(name, value);
		}
	}

	public static class ArrayInstance extends Instance {
		private final Value[] items;

		public ArrayInstance(ClassFile classFile, int length, Reference reference) {
			super(classFile, reference);
			items = new Value[length];
		}

		public Value getItem(int index) {
			return items[index];
		}

		public int getLength() {
			return items.length;
		}

		public void setItem(int index, Value value) {
			items[index] = value;
		}
	}

	public static class PrimitiveArrayInstance extends Instance {
		private final int[] items;

		public PrimitiveArrayInstance(int type, int length, Reference reference) {
			super(null, reference);
			items = new int[length];
		}

		public Int getItem(int index) {
			return new Int(items[index]);
		}

		public int getLength() {
			return items.length;
		}

		public void setItem(int index, Int value) {
			items[index] = value.getValue();
		}
	}
}
