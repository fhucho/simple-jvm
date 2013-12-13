package cz.simplejvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import cz.simplejvm.ClassFile.Field;
import cz.simplejvm.ClassFile.FieldRefConstant;
import cz.simplejvm.StackFrame.Int;
import cz.simplejvm.StackFrame.Reference;
import cz.simplejvm.StackFrame.Value;

public class Heap {
	private final List<HeapObject> objects = new ArrayList<HeapObject>();

	public ArrayObject getArray(Reference reference) {
		return (ArrayObject) objects.get(reference.value);
	}

	public PrimitiveArrayInstance getPrimitiveArray(Reference reference) {
		return (PrimitiveArrayInstance) objects.get(reference.value);
	}

	public HeapObject getObject(Reference reference) {
		return objects.get(reference.value);
	}

	public List<HeapObject> getObjects() {
		return objects;
	}

	public NormalObject getNormalObject(Reference reference) {
		return (NormalObject) objects.get(reference.value);
	}

	public NormalObject newObject(ClassFile classFile) {
		Reference reference = new Reference(objects.size());
		NormalObject o = new NormalObject(classFile, reference);
		objects.add(o);
		return o;
	}

	public ArrayObject newArray(ClassFile classFile, int length) {
		Reference reference = new Reference(objects.size());
		ArrayObject a = new ArrayObject(classFile, length, reference);
		objects.add(a);
		return a;
	}

	public PrimitiveArrayInstance newArray(int primitiveType, int length) {
		Reference reference = new Reference(objects.size());
		PrimitiveArrayInstance a = new PrimitiveArrayInstance(primitiveType, length, reference);
		objects.add(a);
		return a;
	}

	public void updateReferences(Map<Reference, Reference> refMap) {
		for (HeapObject obj : objects) {

		}
	}

	public abstract static class HeapObject {
		protected final ClassFile classFile;

		private Gc.Color color;
		private Reference reference;

		public HeapObject(ClassFile classFile, Reference reference) {
			this.reference = reference;
			this.classFile = classFile;
			this.color = Gc.Color.WHITE;
		}

		public Gc.Color getColor() {
			return color;
		}

		public Reference getReference() {
			return reference;
		}
		
		public void grayAllWhiteReferences(Heap heap, Set<Reference> graySet) {
		}

		public void setColor(Gc.Color color) {
			this.color = color;
		}

		public void updateReferences(Map<Reference, Reference> refMap) {
			reference = refMap.get(reference);
		}
	}

	public static class NormalObject extends HeapObject {
		private final Map<String, Value> fields;

		public NormalObject(ClassFile classFile, Reference reference) {
			super(classFile, reference);
			fields = new HashMap<String, Value>();
			for (Field field : classFile.getFields()) {
				fields.put(field.getName(), null);
			}
		}

		public Value getField(int field, ClassFile classFile) {
			FieldRefConstant fr = (FieldRefConstant) (classFile.getConstantPool()[field]);
			String name = fr.getNameAndType().getName();
			return fields.get(name);
		}

		@Override
		public void grayAllWhiteReferences(Heap heap, Set<Reference> graySet) {
			for (Value value : fields.values()) {
				if (value instanceof Reference) {
					Reference ref = (Reference) value;
					heap.getObject(ref).setColor(Gc.Color.GRAY);
					graySet.add(ref);
				}
			}
		}

		public void putField(int field, Value value, ClassFile classFile) {
			FieldRefConstant fr = (FieldRefConstant) (classFile.getConstantPool()[field]);
			String name = fr.getNameAndType().getName();
			fields.put(name, value);
		}

		public ClassFile getClassFile() {
			return classFile;
		}

		@Override
		public void updateReferences(Map<Reference, Reference> refMap) {
			for (String fieldName : fields.keySet()) {
				Value value = fields.get(fieldName);
				if (value instanceof Reference) {
					Reference ref = (Reference) value;
					fields.put(fieldName, refMap.get(ref));
				}
			}
		}
	}

	public static class ArrayObject extends HeapObject {
		private final Value[] items;

		public ArrayObject(ClassFile classFile, int length, Reference reference) {
			super(classFile, reference);
			items = new Value[length];
		}

		public Value getItem(int index) {
			return items[index];
		}

		public int getLength() {
			return items.length;
		}

		@Override
		public void grayAllWhiteReferences(Heap heap, Set<Reference> graySet) {
			for (Value value : items) {
				if (value instanceof Reference) {
					Reference ref = (Reference) value;
					heap.getObject(ref).setColor(Gc.Color.GRAY);
					graySet.add(ref);
				}
			}
		}

		public void setItem(int index, Value value) {
			items[index] = value;
		}

		@Override
		public void updateReferences(Map<Reference, Reference> refMap) {
			for (int i = 0; i < items.length; i++) {
				Value value = items[i];
				if (value instanceof Reference) {
					Reference ref = (Reference) value;
					items[i] = refMap.get(ref);
				}
			}
		}
	}

	public static class PrimitiveArrayInstance extends HeapObject {
		private final Int[] items;

		public PrimitiveArrayInstance(int type, int length, Reference reference) {
			super(null, reference);
			items = new Int[length];
			for (int i = 0; i < length; i++) {
				items[i] = new Int(0);
			}
		}

		public Int getItem(int index) {
			return items[index];
		}

		public int getLength() {
			return items.length;
		}

		public void setItem(int index, Int value) {
			items[index] = value;
		}
	}
}
