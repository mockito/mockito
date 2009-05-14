/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
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
package org.mockito.asm.util;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;

/**
 * An abstract trace visitor.
 * 
 * @author Eric Bruneton
 */
public abstract class TraceAbstractVisitor extends AbstractVisitor {

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for internal
     * type names in bytecode notation.
     */
    public static final int INTERNAL_NAME = 0;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for field
     * descriptors, formatted in bytecode notation
     */
    public static final int FIELD_DESCRIPTOR = 1;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for field
     * signatures, formatted in bytecode notation
     */
    public static final int FIELD_SIGNATURE = 2;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for method
     * descriptors, formatted in bytecode notation
     */
    public static final int METHOD_DESCRIPTOR = 3;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for method
     * signatures, formatted in bytecode notation
     */
    public static final int METHOD_SIGNATURE = 4;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for class
     * signatures, formatted in bytecode notation
     */
    public static final int CLASS_SIGNATURE = 5;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for field or
     * method return value signatures, formatted in default Java notation
     * (non-bytecode)
     */
    public static final int TYPE_DECLARATION = 6;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for class
     * signatures, formatted in default Java notation (non-bytecode)
     */
    public static final int CLASS_DECLARATION = 7;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for method
     * parameter signatures, formatted in default Java notation (non-bytecode)
     */
    public static final int PARAMETERS_DECLARATION = 8;

    /**
     * Tab for class members.
     */
    protected String tab = "  ";

    /**
     * Prints a disassembled view of the given annotation.
     * 
     * @param desc the class descriptor of the annotation class.
     * @param visible <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values.
     */
    public AnnotationVisitor visitAnnotation(
        final String desc,
        final boolean visible)
    {
        buf.setLength(0);
        buf.append(tab).append('@');
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append('(');
        text.add(buf.toString());
        TraceAnnotationVisitor tav = createTraceAnnotationVisitor();
        text.add(tav.getText());
        text.add(visible ? ")\n" : ") // invisible\n");
        return tav;
    }

    /**
     * Prints a disassembled view of the given attribute.
     * 
     * @param attr an attribute.
     */
    public void visitAttribute(final Attribute attr) {
        buf.setLength(0);
        buf.append(tab).append("ATTRIBUTE ");
        appendDescriptor(-1, attr.type);

        if (attr instanceof Traceable) {
            ((Traceable) attr).trace(buf, null);
        } else {
            buf.append(" : unknown\n");
        }

        text.add(buf.toString());
    }

    /**
     * Does nothing.
     */
    public void visitEnd() {
        // does nothing
    }

    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------

    protected TraceAnnotationVisitor createTraceAnnotationVisitor() {
        return new TraceAnnotationVisitor();
    }

    /**
     * Appends an internal name, a type descriptor or a type signature to
     * {@link #buf buf}.
     * 
     * @param type indicates if desc is an internal name, a field descriptor, a
     *        method descriptor, a class signature, ...
     * @param desc an internal name, type descriptor, or type signature. May be
     *        <tt>null</tt>.
     */
    protected void appendDescriptor(final int type, final String desc) {
        if (type == CLASS_SIGNATURE || type == FIELD_SIGNATURE
                || type == METHOD_SIGNATURE)
        {
            if (desc != null) {
                buf.append("// signature ").append(desc).append('\n');
            }
        } else {
            buf.append(desc);
        }
    }

}
