
package cz.simplejvm;

import cz.simplejvm.ClassFile.NameAndTypeConstant;
import cz.simplejvm.StackFrame.StackValue;
import cz.simplejvm.StackFrame.Int;

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

	public void setField(NameAndTypeConstant name, StackValue value) {
		// TODO: save to heap
	}

	public StackValue getField(NameAndTypeConstant name) {
		// TODO: load Reference or Int
		return new Int(0);
	}

}
