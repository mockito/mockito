/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2011 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mockito.asm.optimizer;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.Label;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;
import org.mockito.asm.commons.Remapper;
import org.mockito.asm.commons.RemappingMethodAdapter;

/**
 * A {@link MethodVisitor} that renames fields and methods, and removes debug
 * info.
 * 
 * @author Eugene Kuleshov
 */
public class MethodOptimizer extends RemappingMethodAdapter implements Opcodes {

    private final ClassOptimizer classOptimizer;

    public MethodOptimizer(ClassOptimizer classOptimizer, int access,
            String desc, MethodVisitor mv, Remapper remapper) {
        super(access, desc, mv, remapper);
        this.classOptimizer = classOptimizer;
    }

    // ------------------------------------------------------------------------
    // Overridden methods
    // ------------------------------------------------------------------------

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        // remove annotations
        return null;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        // remove annotations
        return null;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter,
            final String desc, final boolean visible) {
        // remove annotations
        return null;
    }

    @Override
    public void visitLocalVariable(final String name, final String desc,
            final String signature, final Label start, final Label end,
            final int index) {
        // remove debug info
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        // remove debug info
    }

    @Override
    public void visitFrame(int type, int local, Object[] local2, int stack,
            Object[] stack2) {
        // remove frame info
    }

    @Override
    public void visitAttribute(Attribute attr) {
        // remove non standard attributes
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if (!(cst instanceof Type)) {
            super.visitLdcInsn(cst);
            return;
        }

        // transform Foo.class to foo$(class) for 1.2 compatibility
        String ldcName = ((Type) cst).getInternalName();
        String fieldName = "class$" + ldcName.replace('/', '$');

        FieldVisitor fv = classOptimizer.syntheticFieldVisitor(ACC_STATIC
                | ACC_SYNTHETIC, fieldName, "Ljava/lang/Class;");
        fv.visitEnd();

        if (!classOptimizer.class$) {
            MethodVisitor mv = classOptimizer.visitMethod(ACC_STATIC
                    | ACC_SYNTHETIC, "class$",
                    "(Ljava/lang/String;)Ljava/lang/Class;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2,
                    "java/lang/ClassNotFoundException");
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName",
                    "(Ljava/lang/String;)Ljava/lang/Class;");
            mv.visitLabel(l1);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l2);
            mv.visitMethodInsn(INVOKEVIRTUAL,
                    "java/lang/ClassNotFoundException", "getMessage",
                    "()Ljava/lang/String;");
            mv.visitVarInsn(ASTORE, 1);
            mv.visitTypeInsn(NEW, "java/lang/NoClassDefFoundError");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/NoClassDefFoundError",
                    "<init>", "(Ljava/lang/String;)V");
            mv.visitInsn(ATHROW);
            mv.visitMaxs(3, 2);
            mv.visitEnd();

            classOptimizer.class$ = true;
        }

        String clsName = classOptimizer.clsName;
        mv.visitFieldInsn(GETSTATIC, clsName, fieldName, "Ljava/lang/Class;");
        Label elseLabel = new Label();
        mv.visitJumpInsn(IFNONNULL, elseLabel);
        mv.visitLdcInsn(ldcName.replace('/', '.'));
        mv.visitMethodInsn(INVOKESTATIC, clsName, "class$",
                "(Ljava/lang/String;)Ljava/lang/Class;");
        mv.visitInsn(DUP);
        mv.visitFieldInsn(PUTSTATIC, clsName, fieldName, "Ljava/lang/Class;");
        Label endLabel = new Label();
        mv.visitJumpInsn(GOTO, endLabel);
        mv.visitLabel(elseLabel);
        mv.visitFieldInsn(GETSTATIC, clsName, fieldName, "Ljava/lang/Class;");
        mv.visitLabel(endLabel);
    }
}
