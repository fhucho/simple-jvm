
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

public class StackFrame {
    public int programCounter;
    private final int[] locals;
    private final List<Integer> stack;

    public StackFrame(int maxLocals, int maxStack) {
        locals = new int[maxLocals];
        stack = new ArrayList<Integer>(maxStack);
        programCounter = 0;
    }

    public int popFromStack() {
        return stack.remove(stack.size() - 1);
    }

    public void pushToStack(int value) {
        stack.add(value);
    }

    public void setLocal(int index, int value) {
        locals[index] = value;
    }

    public int getLocal(int index) {
        return locals[index];
    }

}
