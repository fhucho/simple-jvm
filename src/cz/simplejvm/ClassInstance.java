
package cz.simplejvm;

import cz.simplejvm.ClassFile.NameAndTypeConstant;

public class ClassInstance {
	// TODO: fields
	// TODO: rest

	private ClassInstance() {
	}

	public ClassInstance(ClassFile from) {
		int fieldLengths = from.getFields().length;
	}

	public int saveToHeap() {
		// TODO: return heap reference
		return 0;
	}

	public static ClassInstance loadFromHeap(ClassFile fieldClassfile, int reference) {
		return new ClassInstance();
	}

	public void setField(NameAndTypeConstant name, int value) {
		// TODO: save to heap
	}

}
