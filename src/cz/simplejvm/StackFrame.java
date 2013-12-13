package cz.simplejvm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Reference)) return false;
			Reference ref = (Reference) obj;
			return ref.value == value;
		}

		@Override
		public int hashCode() {
			return new Integer(value).hashCode();
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

	public Set<Reference> getGcRoots() {
		Set<Reference> references = new HashSet<Reference>();
		for (Value v : stack) {
			if (v instanceof Reference) references.add((Reference) v);
		}
		for (Value v : locals) {
			if (v instanceof Reference) references.add((Reference) v);
		}
		return references;
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

	public void updateReferences(Map<Reference, Reference> refMap) {
		for (int i = 0; i < locals.length; i++) {
			if (locals[i] instanceof Reference) {
				Reference ref = (Reference) locals[i];
				locals[i] = refMap.get(ref);
			}
		}

		for (int i = 0; i < stack.size(); i++) {
			if (stack.get(i) instanceof Reference) {
				Reference ref = (Reference) stack.get(i);
				stack.set(i, refMap.get(ref));
			}
		}
	}

}
