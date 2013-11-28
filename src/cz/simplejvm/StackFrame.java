
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.Method;

public class StackFrame {
	public abstract static class StackValue {
		final int value;

		public StackValue(int value) {
			this.value = value;
		}

		public StackValue(StackValue src) {
			this.value = src.getValue();
		}

		public int getValue() {
			return value;
		}
	}

	public static class Reference extends StackValue {

		public Reference(int value) {
			super(value);
		}

		public Reference(Reference src) {
			super(src);
		}
	}

	public static class Int extends StackValue {

		public Int(int value) {
			super(value);
		}

		public Int(Int src) {
			super(src);
		}
	}

	public int programCounter;
	private final StackValue[] locals;
	private final List<StackValue> stack;
	private final ClassFile classFileref;
	private final Method methodRef;

	public StackFrame(ClassFile classFile, Method method, int maxLocals, int maxStack) {
		locals = new StackValue[maxLocals];
		stack = new ArrayList<StackValue>(maxStack);
		programCounter = 0;
		classFileref = classFile;
		methodRef = method;
	}

	public ClassFile getClassFile() {
		return classFileref;
	}

	public Method getMethod() {
		return methodRef;
	}

	public StackValue popFromStack() {
		return stack.remove(stack.size() - 1);
	}

	public void pushToStack(StackValue value) {
		stack.add(value);
	}

	public void setLocal(int index, StackValue value) {
		locals[index] = value;
	}

	public StackValue getLocal(int index) {
		return locals[index];
	}

}
