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
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;

/**
 * A {@link ClassVisitor} that collects the {@link Constant}s of the classes it
 * visits.
 * 
 * @author Eric Bruneton
 */
public class ClassConstantsCollector extends ClassVisitor {

    private final ConstantPool cp;

    public ClassConstantsCollector(final ClassVisitor cv, final ConstantPool cp) {
        super(Opcodes.ASM4, cv);
        this.cp = cp;
    }

    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName,
            final String[] interfaces) {
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            cp.newUTF8("Deprecated");
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            cp.newUTF8("Synthetic");
        }
        cp.newClass(name);
        if (signature != null) {
            cp.newUTF8("Signature");
            cp.newUTF8(signature);
        }
        if (superName != null) {
            cp.newClass(superName);
        }
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; ++i) {
                cp.newClass(interfaces[i]);
            }
        }
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(final String source, final String debug) {
        if (source != null) {
            cp.newUTF8("SourceFile");
            cp.newUTF8(source);
        }
        if (debug != null) {
            cp.newUTF8("SourceDebugExtension");
        }
        cv.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(final String owner, final String name,
            final String desc) {
        cp.newUTF8("EnclosingMethod");
        cp.newClass(owner);
        if (name != null && desc != null) {
            cp.newNameType(name, desc);
        }
        cv.visitOuterClass(owner, name, desc);
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
        return new AnnotationConstantsCollector(cv.visitAnnotation(desc,
                visible), cp);
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        // can do nothing
        cv.visitAttribute(attr);
    }

    @Override
    public void visitInnerClass(final String name, final String outerName,
            final String innerName, final int access) {
        cp.newUTF8("InnerClasses");
        if (name != null) {
            cp.newClass(name);
        }
        if (outerName != null) {
            cp.newClass(outerName);
        }
        if (innerName != null) {
            cp.newUTF8(innerName);
        }
        cv.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(final int access, final String name,
            final String desc, final String signature, final Object value) {
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            cp.newUTF8("Synthetic");
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            cp.newUTF8("Deprecated");
        }
        cp.newUTF8(name);
        cp.newUTF8(desc);
        if (signature != null) {
            cp.newUTF8("Signature");
            cp.newUTF8(signature);
        }
        if (value != null) {
            cp.newConst(value);
        }
        return new FieldConstantsCollector(cv.visitField(access, name, desc,
                signature, value), cp);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
            final String desc, final String signature, final String[] exceptions) {
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            cp.newUTF8("Synthetic");
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            cp.newUTF8("Deprecated");
        }
        cp.newUTF8(name);
        cp.newUTF8(desc);
        if (signature != null) {
            cp.newUTF8("Signature");
            cp.newUTF8(signature);
        }
        if (exceptions != null) {
            cp.newUTF8("Exceptions");
            for (int i = 0; i < exceptions.length; ++i) {
                cp.newClass(exceptions[i]);
            }
        }
        return new MethodConstantsCollector(cv.visitMethod(access, name, desc,
                signature, exceptions), cp);
    }
}
