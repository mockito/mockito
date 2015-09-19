package org.mockitoutil;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_6;
import org.objectweb.asm.ClassWriter;

public class SimpleClassGenerator {

    public static byte[] makeMarkerInterface(String qualifiedName) {
        String relativePath = qualifiedName.replace('.', '/');

        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_6, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, relativePath, null, "java/lang/Object", null);
        cw.visitEnd();

        return cw.toByteArray();
    }

}
