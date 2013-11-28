
package cz.simplejvm;

import java.util.ArrayList;
import java.util.List;

import cz.simplejvm.ClassFile.ClassConstant;
import cz.simplejvm.ClassFile.Constant;
import cz.simplejvm.ClassFile.FieldRefConstant;
import cz.simplejvm.ClassFile.IntegerConstant;
import cz.simplejvm.ClassFile.NameAndTypeConstant;
import cz.simplejvm.ClassFile.StringConstant;

public class Runtime {
    List<StackFrame> stackFrames;

    public Runtime() {
        stackFrames = new ArrayList<StackFrame>();
    }

    private int[] getByteCode() {
        return null;
    }

    private StackFrame sf() {
        return stackFrames.get(stackFrames.size() - 1);
    }

    private int getCurrInst() {
        return getByteCode()[sf().programCounter];
    }

    private ClassFile cf() {
        return null;
    }

    private Constant[] cp() {
        return null;
    }

    private void executeCurrentInstruction() {
        int instruction = getCurrInst();
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
            case Instructions.ldc:
                ldc();
                break;


            default:
                break;
        }
    }

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
        sf().pushToStack(value);
        sf().programCounter++;
    }

    private void store() {
        sf().programCounter++;
        int index = getCurrInst();
        store(index);
    }

    private void store(int index) {
        int value = sf().popFromStack();
        sf().setLocal(index, value);
        sf().programCounter++;
    }
    private void putfield() {
        sf().programCounter++;
        int indexbyte1 = getCurrInst();
        sf().programCounter++;
        int indexbyte2 = getCurrInst();
        long fieldRefIndex = indexbyte1 << 8 + indexbyte2;
        int value = sf().popFromStack();
        int objectref = sf().popFromStack();

        FieldRefConstant frc = new FieldRefConstant(objectref, (int) fieldRefIndex);
        frc.link(cp());
        ClassConstant clazz = frc.getClazz();
        NameAndTypeConstant name = frc.getNameAndType();
        //TODO: Save to heap
        sf().programCounter++;
    }

    private void ldc() {
        sf().programCounter++;
        int index = getCurrInst();
        try {
            StringConstant stringConstant = new StringConstant(index);
            stringConstant.link(cp());
            //TODO: Save String to heap and push reference


        } catch (ClassCastException e) {
            IntegerConstant intConstant = new IntegerConstant(index);
            intConstant.link(cp());
            sf().pushToStack(intConstant.getValue());
        }

        sf().programCounter++;
    }

}
