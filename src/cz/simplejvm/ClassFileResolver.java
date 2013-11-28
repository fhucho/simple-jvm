
package cz.simplejvm;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import cz.simplejvm.ClassFile.ClassConstant;

public class ClassFileResolver {
    private static ClassFileResolver instance;
    HashMap<String, ClassFile> classFiles;

    private ClassFileResolver() {
        classFiles = new HashMap<String, ClassFile>();
    }

    public static ClassFileResolver getInstance() {
        if (instance == null) {
            instance = new ClassFileResolver();
        }
        return instance;
    }

    public ClassFile getClassFile(ClassConstant classConst) {
        return getClassFile(classConst.getName());
    }

    public ClassFile getClassFile(String className) {
        ClassFile classFile = classFiles.get(className);
        if (classFile == null) {
            classFile = loadClassFile(className);
            classFiles.put(className, classFile);
        }

        return classFile;
    }

    private ClassFile loadClassFile(String className) {
        if (className.startsWith("cz")) {
            try {
                String fileName = "./bin/" + className + ".class";
                DataInputStream dis;
                dis = new DataInputStream(new FileInputStream(fileName));
                ClassFile claz = new ClassFileReader().readClassFile(dis);
                dis.close();
                return claz;
            } catch (IOException e) {
                throw new RuntimeException("Class " + className + " was not found");
            }
        } else {
            throw new RuntimeException("Class " + className + " not yet supported");
        }
    }
}
