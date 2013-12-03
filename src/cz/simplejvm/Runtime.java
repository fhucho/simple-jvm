
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.ClassConstant;
import cz.simplejvm.ClassFile.Constant;
import cz.simplejvm.ClassFile.IntegerConstant;
import cz.simplejvm.ClassFile.Method;
import cz.simplejvm.ClassFile.MethodRefConstant;
import cz.simplejvm.ClassFile.NameAndTypeConstant;
import cz.simplejvm.Heap.ObjectInstance;
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

	public void start(String className, String methodName) {
		ClassFile newClassfile = ClassFileResolver.getInstance().getClassFile(className);
		Method newMethod = newClassfile.getMethod(methodName);
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

			System.out.print(sf().programCounter + " Instruction " + instruction+ " ");

			System.out.println(String.format("%02X ", instruction));
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
				ireturn();
				break;
			case Instructions.return_:
				return_();
				break;
			case Instructions.invokespecial:
			case Instructions.invokevirtual:
				invokeMethod();
				break;
			case Instructions.new_:
				new_();
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
			case Instructions.goto_:
				goto_();
				break;

			default:
				throw new RuntimeException("Instruction not supported " + instruction);
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

		ObjectInstance instance = heap.getObject(objectref);
		instance.putField(fieldRefIndex, value);

		sf().programCounter++;
	}

	private void getfield() {
		int fieldRefIndex = readInt();
		Reference objectref = (Reference) sf().popFromStack();

		ObjectInstance instance = heap.getObject(objectref);
		Value value = instance.getField(fieldRefIndex);

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

	private void invokeMethod() {
		int methodRefIndex = readInt();

		MethodRefConstant method = (MethodRefConstant) cp()[methodRefIndex];
		ClassConstant clazz = method.getClazz();
		NameAndTypeConstant name = method.getNameAndType();

		ClassFile newClassfile;
		try {
			newClassfile = ClassFileResolver.getInstance().getClassFile(clazz);
		} catch (Exception e) {//skip
			e.printStackTrace();
			sf().programCounter++;
			return;
		}

		Method newMethod = newClassfile.getMethod(name.getName());
		if (newMethod == null) {
			throw new RuntimeException("Method " + name.getName() + " not found");
		}

		//-------------handle native methods-------------
		NativeMethod nativeCheck = NativeResolver.checkForNative(method);
		if(nativeCheck!=null) {
			nativeCheck.invoke(newMethod, sf());
			if(nativeCheck.hasResult()) {
				sf().pushToStack(nativeCheck.getResult());
			}
			sf().programCounter++;
			return;
		}

		//--------------handle other methods---------------

		int paramsCount = newMethod.getParamsCount();
		StackFrame newStack = new StackFrame(newClassfile, newMethod);
		for (int i = paramsCount; i >= 0; i--) {// set local variables and new this reference
			newStack.setLocal(i, sf().popFromStack());
		}

		sf().programCounter++;
		addStackFrame(newStack);

	}

	private void new_() {
		int classIndex = readInt();
		Constant[] cpool = cp();
		ClassConstant clazz = (ClassConstant) cp()[classIndex];
		ClassFile newClassfile = ClassFileResolver.getInstance().getClassFile(clazz.getName());

		ObjectInstance newInstance = heap.newObject(newClassfile);
		sf().pushToStack(newInstance.getReference());
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
		sf().pushToStack(new Int(value.getValue() + cons));
		sf().programCounter++;

	}

	private void goto_() {
		sf().programCounter += readSignedShort();
	}

}
