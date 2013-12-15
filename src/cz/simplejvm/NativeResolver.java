
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.MethodRefConstant;
import cz.simplejvm.Heap.PrimitiveArrayInstance;
import cz.simplejvm.StackFrame.Int;
import cz.simplejvm.StackFrame.Reference;
import cz.simplejvm.StackFrame.Value;
import cz.simplejvm.app.NativeMethods;

public class NativeResolver {
	public static NativeMethod checkForNative(MethodRefConstant method) {
		if (!method.getClazz().getName().equals("cz/simplejvm/app/NativeMethods")) {
			return null;
		}

		if (method.getNameAndType().getName().equals("print") && method.getNameAndType().getDescriptor().equals("(I)V")) {
			return new PrintMethod();
		}

		if (method.getNameAndType().getName().equals("println") && method.getNameAndType().getDescriptor().equals("(I)V")) {
			return new PrintlnMethod();
		}

		if (method.getNameAndType().getName().equals("print") && method.getNameAndType().getDescriptor().equals("(C)V")) {
			return new PrintcMethod();
		}

		if (method.getNameAndType().getName().equals("println") && method.getNameAndType().getDescriptor().equals("(C)V")) {
			return new PrintlncMethod();
		}

		if (method.getNameAndType().getName().equals("print") && method.getNameAndType().getDescriptor().equals("([C)V")) {
			return new PrintcaMethod();
		}

		if (method.getNameAndType().getName().equals("println") && method.getNameAndType().getDescriptor().equals("([C)V")) {
			return new PrintlncaMethod();
		}

		if (method.getNameAndType().getName().equals("readFromFile")) {
			return new ReadFromFileMethod();
		}

		if (method.getNameAndType().getName().equals("writeToFile")) {
			return new WriteToFileMethod();
		}

		if (method.getNameAndType().getName().equals("stringToIntArray")) {
			return new StringToIntArrayMethod();
		}

		return null;
	}

	public abstract static class NativeMethod {
		private Value value;
		private boolean hasValue;

		protected void setValue(Value value) {
			this.value = value;
			hasValue = true;
		}

		public boolean hasResult() {
			return hasValue;
		}

		public Value getResult() {
			return value;
		}

		public void invoke(MethodRefConstant method, StackFrame stackframe, Heap heap) {
			int paramsCount = method.getParamsCount();
			List<Value> params = new ArrayList<StackFrame.Value>();

			for (int i = paramsCount; i >= 0; i--) {
				params.add(stackframe.popFromStack());
			}
			run(params, heap);
		}

		protected abstract void run(List<Value> params, Heap heap);

	}

	public static class PrintMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			new NativeMethods().print(params.get(0).value);
		}

	}

	public static class PrintlnMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			new NativeMethods().println(params.get(0).value);
		}

	}

	public static class PrintcMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			new NativeMethods().print((char) params.get(0).value);
		}

	}

	public static class PrintlncMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			new NativeMethods().println((char) params.get(0).value);
		}

	}

	public static class PrintcaMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			PrimitiveArrayInstance arrayRef = heap.getPrimitiveArray((Reference) params.get(0));
			char[] array = new char[arrayRef.getLength()];
			for (int i = 0; i < arrayRef.getLength(); i++) {
				array[i] = (char) arrayRef.getItem(i).value;
			}
			new NativeMethods().print(array);
		}

	}

	public static class PrintlncaMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			PrimitiveArrayInstance arrayRef = heap.getPrimitiveArray((Reference) params.get(0));
			char[] array = new char[arrayRef.getLength()];
			for (int i = 0; i < arrayRef.getLength(); i++) {
				array[i] = (char) arrayRef.getItem(i).value;
			}
			new NativeMethods().println(array);
		}

	}

	public static class ReadFromFileMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			PrimitiveArrayInstance arrayRef = heap.getPrimitiveArray((Reference) params.get(0));
			char[] fname = new char[arrayRef.getLength()];
			for (int i = 0; i < arrayRef.getLength(); i++) {
				fname[i] = (char) arrayRef.getItem(i).value;
			}
			char[] text = new NativeMethods().readFromFile(fname);
			PrimitiveArrayInstance newArray = heap.newArray(5, text.length);
			for (int i = 0; i < text.length; i++) {
				newArray.setItem(i, new Int(text[i]));
			}

			setValue(newArray.getReference());
		}

	}

	public static class StringToIntArrayMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			PrimitiveArrayInstance arrayRef = heap.getPrimitiveArray((Reference) params.get(0));
			char[] text = new char[arrayRef.getLength()];
			for (int i = 0; i < arrayRef.getLength(); i++) {
				text[i] = (char) arrayRef.getItem(i).value;
			}


			int[] numbers = new NativeMethods().stringToIntArray(text);
			PrimitiveArrayInstance newArray = heap.newArray(10, numbers.length);
			for (int i = 0; i < numbers.length; i++) {
				newArray.setItem(i, new Int(numbers[i]));
			}
			setValue(newArray.getReference());
		}

	}


	public static class WriteToFileMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params, Heap heap) {
			PrimitiveArrayInstance arrayRef = heap.getPrimitiveArray((Reference) params.get(1));
			char[] fname = new char[arrayRef.getLength()];
			for (int i = 0; i < arrayRef.getLength(); i++) {
				fname[i] = (char) arrayRef.getItem(i).value;
			}

			arrayRef = heap.getPrimitiveArray((Reference) params.get(0));
			char[] text = new char[arrayRef.getLength()];
			for (int i = 0; i < arrayRef.getLength(); i++) {
				text[i] = (char) arrayRef.getItem(i).value;
			}

			new NativeMethods().writeToFile(fname, text);
		}

	}

}
