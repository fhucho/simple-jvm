
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.ClassConstant;
import cz.simplejvm.ClassFile.Constant;
import cz.simplejvm.ClassFile.IntegerConstant;
import cz.simplejvm.ClassFile.Method;
import cz.simplejvm.ClassFile.MethodRefConstant;
import cz.simplejvm.ClassFile.NameAndTypeConstant;
import cz.simplejvm.Heap.NormalObject;
import cz.simplejvm.Heap.PrimitiveArrayInstance;
import cz.simplejvm.NativeResolver.NativeMethod;
import cz.simplejvm.StackFrame.Int;
import cz.simplejvm.StackFrame.Reference;
import cz.simplejvm.StackFrame.Value;

public class Runtime {
	List<StackFrame> stackFrames;
	private Heap heap;
	private boolean finished = false;

	public Runtime() {
		stackFrames = new ArrayList<StackFrame>();
		heap = new Heap();
	}

	// ---------------------------
	// TODO: cache these values for fast access(remember last stackFrame and if it changes load new values into cache
	private int[] getByteCode() {
		return sf().getMethod().getCodeAttribute().getCode();
	}

	private StackFrame sf() {
		return stackFrames.get(stackFrames.size() - 1);
	}

	private int getCurrInst() {
		return getByteCode()[sf().programCounter];
	}

	private ClassFile cf() {
		return sf().getClassFile();
	}

	private Constant[] cp() {
		return cf().getConstantPool();
	}

	private void addStackFrame(StackFrame sf) {
		stackFrames.add(sf);
	}

	private void removeStackFrame() {
		stackFrames.remove(stackFrames.size() - 1);
		if (stackFrames.size() == 0) {
			finish();
		}
	}

	private void finish() {
		finished = true;
	}

	private boolean isFinished() {
		return finished;
	}

	public void start(String className, String methodName, String methodDesc) {
		ClassFile newClassfile = ClassFileResolver.getInstance().getClassFile(className);
		Method newMethod = newClassfile.getMethod(methodName, methodDesc);
		if (newMethod == null) {
			throw new RuntimeException("Method " + methodName + " not found");
		}
		StackFrame newStack = new StackFrame(newClassfile, newMethod);
		addStackFrame(newStack);
		run();
	}

	private void run() {
		while (!isFinished()) {
			int instruction = getCurrInst();

			// System.out.print(sf().programCounter + " Instruction ");
			// System.out.println(String.format("%02X ", instruction));
			executeCurrentInstruction(instruction);
		}
	}

	// -------------------

	private void executeCurrentInstruction(int instruction) {
		switch (instruction) {
			case Instructions.aload_0:
			case Instructions.iload_0:
				load(0);
				break;
			case Instructions.aload_1:
			case Instructions.iload_1:
				load(1);
				break;
			case Instructions.aload_2:
			case Instructions.iload_2:
				load(2);
				break;
			case Instructions.aload_3:
			case Instructions.iload_3:
				load(3);
				break;
			case Instructions.aload:
			case Instructions.iload:
				load();
				break;
			case Instructions.iconst_m1:
				iconst(-1);
				break;
			case Instructions.iconst_0:
				iconst(0);
				break;
			case Instructions.iconst_1:
				iconst(1);
				break;
			case Instructions.iconst_2:
				iconst(2);
				break;
			case Instructions.iconst_3:
				iconst(3);
				break;
			case Instructions.iconst_4:
				iconst(4);
				break;
			case Instructions.iconst_5:
				iconst(5);
				break;
			case Instructions.astore:
			case Instructions.istore:
				store();
				break;
			case Instructions.astore_0:
			case Instructions.istore_0:
				store(0);
				break;
			case Instructions.astore_1:
			case Instructions.istore_1:
				store(1);
				break;
			case Instructions.astore_2:
			case Instructions.istore_2:
				store(2);
				break;
			case Instructions.astore_3:
			case Instructions.istore_3:
				store(3);
				break;
			case Instructions.putfield:
				putfield();
				break;
			case Instructions.getfield:
				getfield();
				break;
			case Instructions.ldc:
				ldc();
				break;
			case Instructions.iadd:
				iadd();
				break;
			case Instructions.ireturn:
			case Instructions.areturn:
				ireturn();
				break;
			case Instructions.return_:
				return_();
				break;
			case Instructions.invokespecial:
				invokeSpecial();
				break;
			case Instructions.invokevirtual:
				invokeVirtual();
				break;
			case Instructions.new_:
				new_();
				break;
			case Instructions.newarray:
				newarray();
				break;
			case Instructions.dup:
				dup();
				break;
			case Instructions.bipush:
				bipush();
				break;
			case Instructions.sipush:
				sipush();
				break;
			case Instructions.iinc:
				iinc();
				break;
			case Instructions.pop:
				pop();
				break;
			case Instructions.goto_:
				goto_();
				break;
			case Instructions.if_icmplt:
				if_icmplt();
				break;
			case Instructions.if_icmple:
				if_icmple();
				break;
			case Instructions.if_icmpge:
				if_icmpge();
				break;
			case Instructions.if_icmpgt:
				if_icmpgt();
				break;
			case Instructions.if_icmpeq:
			case Instructions.if_acmpeq:
				if_cmpeq();
				break;
			case Instructions.if_icmpne:
			case Instructions.if_acmpne:
				if_cmpne();
				break;
			case Instructions.ifne:
				ifne();
				break;
			case Instructions.ifeq:
				ifeq();
				break;
			case Instructions.ifgt:
				ifgt();
				break;
			case Instructions.ifge:
				ifge();
				break;
			case Instructions.iflt:
				iflt();
				break;
			case Instructions.ifle:
				ifle();
				break;
			case Instructions.iastore:
			case Instructions.bastore:
			case Instructions.castore:
				iastore();
				break;

			case Instructions.iaload:
			case Instructions.baload:
				iaload();
				break;
			case Instructions.arraylength:
				arraylength();
				break;
			case Instructions.aconst_null:
				aconst_null();
				break;

			default:
				throw new RuntimeException("Instruction not supported " + String.format("%02X ", instruction));
		}
	}

	private int readInt() {

		sf().programCounter++;
		int indexbyte1 = getCurrInst();
		sf().programCounter++;
		int indexbyte2 = getCurrInst();
		return (((indexbyte1 & 0xFF) << 8) | (indexbyte2 & 0xFF));
	}

	private short readSignedShort() {
		sf().programCounter++;
		int indexbyte1 = getCurrInst();
		sf().programCounter++;
		int indexbyte2 = getCurrInst();
		return (short) (((indexbyte1 & 0xFF) << 8) | (indexbyte2 & 0xFF));
	}

	// ---------------------------------------

	private void load() {
		sf().programCounter++;
		int index = getCurrInst();
		load(index);
	}

	private void load(int index) {
		sf().pushToStack(sf().getLocal(index));
		sf().programCounter++;

	}

	private void iconst(int value) {
		sf().pushToStack(new Int(value));
		sf().programCounter++;
	}

	private void store() {
		sf().programCounter++;
		int index = getCurrInst();
		store(index);
	}

	private void store(int index) {
		Value value = sf().popFromStack();
		sf().setLocal(index, value);
		sf().programCounter++;
	}

	private void putfield() {
		int fieldRefIndex = readInt();
		Value value = sf().popFromStack();
		Reference objectref = (Reference) sf().popFromStack();

		NormalObject instance = heap.getNormalObject(objectref);
		instance.putField(fieldRefIndex, value, cf());

		sf().programCounter++;
	}

	private void getfield() {
		int fieldRefIndex = readInt();
		Reference objectref = (Reference) sf().popFromStack();

		Constant[] cp = cp();
		NormalObject instance = heap.getNormalObject(objectref);
		Value value = instance.getField(fieldRefIndex, cf());

		sf().pushToStack(value);

		sf().programCounter++;
	}

	private void ldc() {
		sf().programCounter++;
		int index = getCurrInst();
		try {
			Value value = new Int(((IntegerConstant) cp()[index]).getValue());
			sf().pushToStack(value);
		} catch (ClassCastException e) {
			throw new UnsupportedOperationException();
			// stringy nejsou podporovany
			// StringConstant stringConstant = new StringConstant(index);
			// stringConstant.link(cp());
			// TODO: Save String to heap and push reference
		}

		sf().programCounter++;
	}

	private void iadd() {
		Value value1 = sf().popFromStack();
		Value value2 = sf().popFromStack();
		sf().pushToStack(new Int(value1.getValue() + value2.getValue()));
		sf().programCounter++;
	}

	private void ireturn() {
		Value value = sf().popFromStack();
		removeStackFrame();
		if (!isFinished()) {
			sf().pushToStack(value);
		}
	}

	private void return_() {
		removeStackFrame();
	}

	private boolean checkForNative(MethodRefConstant method) {

		NativeMethod nativeCheck = NativeResolver.checkForNative(method);
		if (nativeCheck != null) {
			System.out.println("Invoke native method " + method.getClazz().getName() + ": " + method.getNameAndType().getName());
			nativeCheck.invoke(method, sf(), heap);
			if (nativeCheck.hasResult()) {
				sf().pushToStack(nativeCheck.getResult());
			}
			sf().programCounter++;
			return true;
		}
		return false;
	}

	private void invokeSpecial() {
		int methodRefIndex = readInt();

		MethodRefConstant method = (MethodRefConstant) cp()[methodRefIndex];
		ClassConstant clazz = method.getClazz();
		NameAndTypeConstant name = method.getNameAndType();

		// -------------handle native methods-------------
		if (checkForNative(method)) {
			return;
		}
		// --------------handle other methods---------------

		List<Value> params = new ArrayList<StackFrame.Value>();// pop parameters from stack so we can get Object reference
		int paramsCount = method.getParamsCount();
		for (int i = 0; i <= paramsCount; i++) {
			params.add(sf().popFromStack());
		}

		// iterate through superclasses until we find the method
		ClassFile classFile;
		Method newMethod;
		try {
			classFile = ClassFileResolver.getInstance().getClassFile(clazz);
			while ((newMethod = classFile.getMethod(name.getName(), name.getDescriptor())) == null) {
				classFile = ClassFileResolver.getInstance().getClassFile(classFile.getSuperClass());
			}
		} catch (Exception e) {// skip library methods
			if (!name.getName().equals("<init>") || !e.getMessage().contains("java/lang/Object")) {
				System.err.println("Method " + name.getName() + " not found");
				e.printStackTrace();
			}
			sf().programCounter++;
			return;
		}
		System.out.println("Invoke special method " + classFile.getThisClass().getName() + ": " + name.getName());

		StackFrame newStack = new StackFrame(classFile, newMethod);
		for (int i = 0; i <= paramsCount; i++) {
			newStack.setLocal(paramsCount - i, params.get(i));// set parameters
		}

		sf().programCounter++;
		addStackFrame(newStack);


	}

	private void invokeVirtual() {
		Gc.gc(heap, stackFrames);
		int methodRefIndex = readInt();

		MethodRefConstant method = (MethodRefConstant) cp()[methodRefIndex];
		NameAndTypeConstant name = method.getNameAndType();

		// -------------handle native methods-------------
		if (checkForNative(method)) {
			return;
		}
		// --------------handle other methods---------------

		List<Value> params = new ArrayList<StackFrame.Value>();// pop parameters from stack so we can get Object reference
		int paramsCount = method.getParamsCount();
		for (int i = 0; i <= paramsCount; i++) {
			params.add(sf().popFromStack());
		}

		Reference objectRef = (Reference) params.get(paramsCount);
		NormalObject instance = heap.getNormalObject(objectRef);

		// iterate through superclasses until we find the method
		ClassFile classFile = instance.getClassFile();
		Method newMethod;
		while ((newMethod = classFile.getMethod(name.getName(), name.getDescriptor())) == null) {
			try {
				classFile = ClassFileResolver.getInstance().getClassFile(classFile.getSuperClass());
			} catch (Exception e) {// method not found
				System.err.println("Method " + name.getName() + " not found");
				e.printStackTrace();
				finish();
				return;
			}
		}
		System.out.println("Invoke virtual method " + classFile.getThisClass().getName() + ": " + name.getName());

		StackFrame newStack = new StackFrame(classFile, newMethod);
		for (int i = 0; i <= paramsCount; i++) {
			newStack.setLocal(paramsCount - i, params.get(i));// set parameters
		}

		sf().programCounter++;
		addStackFrame(newStack);

	}

	private void new_() {
		Gc.gc(heap, stackFrames);
		int classIndex = readInt();
		ClassConstant clazz = (ClassConstant) cp()[classIndex];
		ClassFile newClassfile = ClassFileResolver.getInstance().getClassFile(clazz.getName());

		NormalObject newInstance = heap.newObject(newClassfile);
		sf().pushToStack(newInstance.getReference());
		sf().programCounter++;
	}

	private void newarray() {
		Gc.gc(heap, stackFrames);
		sf().programCounter++;
		int type = getCurrInst();
		Int size = (Int) sf().popFromStack();

		PrimitiveArrayInstance array = heap.newArray(type, size.value);
		sf().pushToStack(array.getReference());
		sf().programCounter++;
	}

	private void iastore() {
		Int value = (Int) sf().popFromStack();
		Int index = (Int) sf().popFromStack();
		Reference reference = (Reference) sf().popFromStack();

		PrimitiveArrayInstance array = heap.getPrimitiveArray(reference);
		array.setItem(index.value, value);
		sf().programCounter++;
	}

	private void iaload() {
		Int index = (Int) sf().popFromStack();
		Reference reference = (Reference) sf().popFromStack();

		PrimitiveArrayInstance array = heap.getPrimitiveArray(reference);
		sf().pushToStack(array.getItem(index.value));
		sf().programCounter++;
	}

	private void arraylength() {
		Reference reference = (Reference) sf().popFromStack();
		PrimitiveArrayInstance array = heap.getPrimitiveArray(reference);
		sf().pushToStack(new Int(array.getLength()));

		sf().programCounter++;
	}

	private void dup() {
		Value value = sf().popFromStack();
		sf().pushToStack(value);
		sf().pushToStack(value);
		sf().programCounter++;
	}

	private void bipush() {
		sf().programCounter++;
		int value = getCurrInst();
		sf().pushToStack(new Int(value));
		sf().programCounter++;
	}

	private void sipush() {
		int value = readInt();
		sf().pushToStack(new Int(value));
		sf().programCounter++;
	}

	private void iinc() {
		sf().programCounter++;
		int index = getCurrInst();
		sf().programCounter++;
		int cons = getCurrInst();
		Int value = (Int) sf().getLocal(index);
		sf().setLocal(index, new Int(value.getValue() + cons));
		sf().programCounter++;

	}

	private void pop() {
		sf().popFromStack();
		sf().programCounter++;
	}

	private void goto_() {
		int pc = sf().programCounter;
		short offset = readSignedShort();
		// System.out.println("Goto offset " + offset + ", pc = " + pc);
		sf().programCounter = pc + offset;
	}

	private void if_icmplt() {
		Int value1 = (Int) sf().popFromStack();
		Int value2 = (Int) sf().popFromStack();
		if (value2.value < value1.value) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void if_icmple() {
		Int value1 = (Int) sf().popFromStack();
		Int value2 = (Int) sf().popFromStack();
		if (value2.value <= value1.value) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void if_icmpgt() {
		Int value1 = (Int) sf().popFromStack();
		Int value2 = (Int) sf().popFromStack();
		if (value2.value > value1.value) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void if_icmpge() {
		Int value1 = (Int) sf().popFromStack();
		Int value2 = (Int) sf().popFromStack();
		if (value2.value >= value1.value) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void if_cmpne() {
		Value value1 = sf().popFromStack();
		Value value2 = sf().popFromStack();
		if (value2.value != value1.value) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void if_cmpeq() {
		Value value1 = sf().popFromStack();
		Value value2 = sf().popFromStack();
		if (value2.value == value1.value) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void ifne() {
		Int value = (Int) sf().popFromStack();
		if (value.value != 0) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}


	private void ifeq() {
		Int value = (Int) sf().popFromStack();
		if (value.value == 0) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void ifgt() {
		Int value = (Int) sf().popFromStack();
		if (value.value > 0) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void ifge() {
		Int value = (Int) sf().popFromStack();
		if (value.value >= 0) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void iflt() {
		Int value = (Int) sf().popFromStack();
		if (value.value < 0) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void ifle() {
		Int value = (Int) sf().popFromStack();
		if (value.value <= 0) {
			goto_();
		} else {
			sf().programCounter += 3;
		}
	}

	private void aconst_null() {
		sf().pushToStack(null);
		sf().programCounter++;
	}

}
