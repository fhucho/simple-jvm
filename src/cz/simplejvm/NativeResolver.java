
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.Method;
import cz.simplejvm.ClassFile.MethodRefConstant;
import cz.simplejvm.StackFrame.Value;
import cz.simplejvm.app.NativeMethods;

public class NativeResolver {
	public static NativeMethod checkForNative(MethodRefConstant method) {
		if (!method.getClazz().getName().equals("cz/simplejvm/app/NativeMethods")) {
			return null;
		}

		if (method.getNameAndType().getName().equals("print")) {
			return new PrintMethod();
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

		public void invoke(Method method, StackFrame stackframe) {
			int paramsCount = method.getParamsCount();
			List<Value> params = new ArrayList<StackFrame.Value>();

			for (int i = paramsCount; i >= 0; i--) {
				params.add(stackframe.popFromStack());
			}
			run(params);
		}

		protected abstract void run(List<Value> params);

	}

	public static class PrintMethod extends NativeMethod {

		@Override
		protected void run(List<Value> params) {
			new NativeMethods().print(params.get(0).value);
		}

	}

}
