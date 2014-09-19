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
import org.mockito.asm.Handle;
import org.mockito.asm.Label;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;

/**
 * An {@link MethodVisitor} that collects the {@link Constant}s of the methods
 * it visits.
 * 
 * @author Eric Bruneton
 */
public class MethodConstantsCollector extends MethodVisitor {

    private final ConstantPool cp;

    public MethodConstantsCollector(final MethodVisitor mv,
            final ConstantPool cp) {
        super(Opcodes.ASM4, mv);
        this.cp = cp;
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        cp.newUTF8("AnnotationDefault");
        return new AnnotationConstantsCollector(mv.visitAnnotationDefault(), cp);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc,
            final boolean visible) {
        cp.newUTF8(desc);
        if (visible) {
            cp.newUTF8("RuntimeVisibleAnnotations");
        } else {
            cp.newUTF8("RuntimeInvisibleAnnotations");
        }
        return new AnnotationConstantsCollector(mv.visitAnnotation(desc,
                visible), cp);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter,
            final String desc, final boolean visible) {
        cp.newUTF8(desc);
        if (visible) {
            cp.newUTF8("RuntimeVisibleParameterAnnotations");
        } else {
            cp.newUTF8("RuntimeInvisibleParameterAnnotations");
        }
        return new AnnotationConstantsCollector(mv.visitParameterAnnotation(
                parameter, desc, visible), cp);
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        cp.newClass(type);
        mv.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner,
            final String name, final String desc) {
        cp.newField(owner, name, desc);
        mv.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner,
            final String name, final String desc) {
        boolean itf = opcode == Opcodes.INVOKEINTERFACE;
        cp.newMethod(owner, name, desc, itf);
        mv.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
            Object... bsmArgs) {
        cp.newInvokeDynamic(name, desc, bsm, bsmArgs);
        mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        cp.newConst(cst);
        mv.visitLdcInsn(cst);
    }

    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        cp.newClass(desc);
        mv.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public void visitTryCatchBlock(final Label start, final Label end,
            final Label handler, final String type) {
        if (type != null) {
            cp.newClass(type);
        }
        mv.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public void visitLocalVariable(final String name, final String desc,
            final String signature, final Label start, final Label end,
            final int index) {
        if (signature != null) {
            cp.newUTF8("LocalVariableTypeTable");
            cp.newUTF8(name);
            cp.newUTF8(signature);
        }
        cp.newUTF8("LocalVariableTable");
        cp.newUTF8(name);
        cp.newUTF8(desc);
        mv.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        cp.newUTF8("LineNumberTable");
        mv.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        cp.newUTF8("Code");
        mv.visitMaxs(maxStack, maxLocals);
    }
}
