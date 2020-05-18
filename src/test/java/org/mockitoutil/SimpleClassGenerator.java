/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;

public class SimpleClassGenerator {

    public static byte[] makeMarkerInterface(String qualifiedName) {
        String relativePath = qualifiedName.replace('.', '/');

        ClassWriter cw = new ClassWriter(0);
        cw.visit(
                V1_6,
                ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                relativePath,
                null,
                "java/lang/Object",
                null);
        cw.visitEnd();

        return cw.toByteArray();
    }
}
