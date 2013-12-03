
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.CodeAttribute;
import cz.simplejvm.ClassFile.Method;

public class StackFrame {
	public abstract static class Value {
		final int value;

		public Value(int value) {
			this.value = value;
		}

		public Value(Value src) {
			this.value = src.getValue();
		}

		public int getValue() {
			return value;
		}
	}

	public static class Reference extends Value {

		public Reference(int value) {
			super(value);
		}

		public Reference(Reference src) {
			super(src);
		}
	}

	public static class Int extends Value {

		public Int(int value) {
			super(value);
		}

		public Int(Int src) {
			super(src);
		}
	}

	public int programCounter;
	private final Value[] locals;
	private final List<Value> stack;
	private final ClassFile classFileref;
	private final Method methodRef;

	public StackFrame(ClassFile classFile, Method method) {
		CodeAttribute codeAttr = method.getCodeAttribute();
		locals = new Value[codeAttr.getMaxLocals()];
		stack = new ArrayList<Value>(codeAttr.getMaxStack());
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

	public Value popFromStack() {
		return stack.remove(stack.size() - 1);
	}

	public void pushToStack(Value value) {
		stack.add(value);
	}

	public void setLocal(int index, Value value) {
		locals[index] = value;
	}

	public Value getLocal(int index) {
		return locals[index];
	}

}
