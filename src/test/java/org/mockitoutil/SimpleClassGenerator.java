package org.mockitoutil;

import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.*;

public class SimpleClassGenerator {

    public static byte[] makeMarkerInterface(String qualifiedName) {
        String relativePath = qualifiedName.replace('.', '/');

        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_6, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, relativePath, null, "java/lang/Object", null);
        cw.visitEnd();

        return cw.toByteArray();
    }

}
