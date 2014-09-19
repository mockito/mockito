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
import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;

/**
 * An {@link AnnotationVisitor} that collects the {@link Constant}s of the
 * annotations it visits.
 * 
 * @author Eric Bruneton
 */
public class AnnotationConstantsCollector extends AnnotationVisitor {

    private final ConstantPool cp;

    public AnnotationConstantsCollector(final AnnotationVisitor av,
            final ConstantPool cp) {
        super(Opcodes.ASM4, av);
        this.cp = cp;
    }

    @Override
    public void visit(final String name, final Object value) {
        if (name != null) {
            cp.newUTF8(name);
        }
        if (value instanceof Byte) {
            cp.newInteger(((Byte) value).byteValue());
        } else if (value instanceof Boolean) {
            cp.newInteger(((Boolean) value).booleanValue() ? 1 : 0);
        } else if (value instanceof Character) {
            cp.newInteger(((Character) value).charValue());
        } else if (value instanceof Short) {
            cp.newInteger(((Short) value).shortValue());
        } else if (value instanceof Type) {
            cp.newUTF8(((Type) value).getDescriptor());
        } else if (value instanceof byte[]) {
            byte[] v = (byte[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof boolean[]) {
            boolean[] v = (boolean[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i] ? 1 : 0);
            }
        } else if (value instanceof short[]) {
            short[] v = (short[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof char[]) {
            char[] v = (char[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof int[]) {
            int[] v = (int[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newInteger(v[i]);
            }
        } else if (value instanceof long[]) {
            long[] v = (long[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newLong(v[i]);
            }
        } else if (value instanceof float[]) {
            float[] v = (float[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newFloat(v[i]);
            }
        } else if (value instanceof double[]) {
            double[] v = (double[]) value;
            for (int i = 0; i < v.length; i++) {
                cp.newDouble(v[i]);
            }
        } else {
            cp.newConst(value);
        }
        av.visit(name, value);
    }

    @Override
    public void visitEnum(final String name, final String desc,
            final String value) {
        if (name != null) {
            cp.newUTF8(name);
        }
        cp.newUTF8(desc);
        cp.newUTF8(value);
        av.visitEnum(name, desc, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name,
            final String desc) {
        if (name != null) {
            cp.newUTF8(name);
        }
        cp.newUTF8(desc);
        return new AnnotationConstantsCollector(av.visitAnnotation(name, desc),
                cp);
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        if (name != null) {
            cp.newUTF8(name);
        }
        return new AnnotationConstantsCollector(av.visitArray(name), cp);
    }

    @Override
    public void visitEnd() {
        av.visitEnd();
    }
}
