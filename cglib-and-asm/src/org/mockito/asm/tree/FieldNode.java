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
package org.mockito.asm.tree;

import java.util.ArrayList;
import java.util.List;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.Opcodes;

/**
 * A node that represents a field.
 * 
 * @author Eric Bruneton
 */
public class FieldNode extends FieldVisitor {

    /**
     * The field's access flags (see {@link org.mockito.asm.Opcodes}). This
     * field also indicates if the field is synthetic and/or deprecated.
     */
    public int access;

    /**
     * The field's name.
     */
    public String name;

    /**
     * The field's descriptor (see {@link org.mockito.asm.Type}).
     */
    public String desc;

    /**
     * The field's signature. May be <tt>null</tt>.
     */
    public String signature;

    /**
     * The field's initial value. This field, which may be <tt>null</tt> if the
     * field does not have an initial value, must be an {@link Integer}, a
     * {@link Float}, a {@link Long}, a {@link Double} or a {@link String}.
     */
    public Object value;

    /**
     * The runtime visible annotations of this field. This list is a list of
     * {@link AnnotationNode} objects. May be <tt>null</tt>.
     * 
     * @associates org.mockito.asm.tree.AnnotationNode
     * @label visible
     */
    public List<AnnotationNode> visibleAnnotations;

    /**
     * The runtime invisible annotations of this field. This list is a list of
     * {@link AnnotationNode} objects. May be <tt>null</tt>.
     * 
     * @associates org.mockito.asm.tree.AnnotationNode
     * @label invisible
     */
    public List<AnnotationNode> invisibleAnnotations;

    /**
     * The non standard attributes of this field. This list is a list of
     * {@link Attribute} objects. May be <tt>null</tt>.
     * 
     * @associates org.mockito.asm.Attribute
     */
    public List<Attribute> attrs;

    /**
     * Constructs a new {@link FieldNode}. <i>Subclasses must not use this
     * constructor</i>. Instead, they must use the
     * {@link #FieldNode(int, int, String, String, String, Object)} version.
     * 
     * @param access
     *            the field's access flags (see
     *            {@link org.mockito.asm.Opcodes}). This parameter also
     *            indicates if the field is synthetic and/or deprecated.
     * @param name
     *            the field's name.
     * @param desc
     *            the field's descriptor (see {@link org.mockito.asm.Type
     *            Type}).
     * @param signature
     *            the field's signature.
     * @param value
     *            the field's initial value. This parameter, which may be
     *            <tt>null</tt> if the field does not have an initial value,
     *            must be an {@link Integer}, a {@link Float}, a {@link Long}, a
     *            {@link Double} or a {@link String}.
     */
    public FieldNode(final int access, final String name, final String desc,
            final String signature, final Object value) {
        this(Opcodes.ASM4, access, name, desc, signature, value);
    }

    /**
     * Constructs a new {@link FieldNode}. <i>Subclasses must not use this
     * constructor</i>. Instead, they must use the
     * {@link #FieldNode(int, int, String, String, String, Object)} version.
     * 
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}.
     * @param access
     *            the field's access flags (see
     *            {@link org.mockito.asm.Opcodes}). This parameter also
     *            indicates if the field is synthetic and/or deprecated.
     * @param name
     *            the field's name.
     * @param desc
     *            the field's descriptor (see {@link org.mockito.asm.Type
     *            Type}).
     * @param signature
     *            the field's signature.
     * @param value
     *            the field's initial value. This parameter, which may be
     *            <tt>null</tt> if the field does not have an initial value,
     *            must be an {@link Integer}, a {@link Float}, a {@link Long}, a
     *            {@link Double} or a {@link String}.
     */
    public FieldNode(final int api, final int access, final String name,
            final String desc, final String signature, final Object value) {
        super(api);
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.value = value;
    }

    // ------------------------------------------------------------------------
    // Implementation of the FieldVisitor abstract class
    // ------------------------------------------------------------------------

    @Override
    public AnnotationVisitor visitAnnotation(final String desc,
            final boolean visible) {
        AnnotationNode an = new AnnotationNode(desc);
        if (visible) {
            if (visibleAnnotations == null) {
                visibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            visibleAnnotations.add(an);
        } else {
            if (invisibleAnnotations == null) {
                invisibleAnnotations = new ArrayList<AnnotationNode>(1);
            }
            invisibleAnnotations.add(an);
        }
        return an;
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        if (attrs == null) {
            attrs = new ArrayList<Attribute>(1);
        }
        attrs.add(attr);
    }

    @Override
    public void visitEnd() {
    }

    // ------------------------------------------------------------------------
    // Accept methods
    // ------------------------------------------------------------------------

    /**
     * Checks that this field node is compatible with the given ASM API version.
     * This methods checks that this node, and all its nodes recursively, do not
     * contain elements that were introduced in more recent versions of the ASM
     * API than the given version.
     * 
     * @param api
     *            an ASM API version. Must be one of {@link Opcodes#ASM4}.
     */
    public void check(final int api) {
        // nothing to do
    }

    /**
     * Makes the given class visitor visit this field.
     * 
     * @param cv
     *            a class visitor.
     */
    public void accept(final ClassVisitor cv) {
        FieldVisitor fv = cv.visitField(access, name, desc, signature, value);
        if (fv == null) {
            return;
        }
        int i, n;
        n = visibleAnnotations == null ? 0 : visibleAnnotations.size();
        for (i = 0; i < n; ++i) {
            AnnotationNode an = visibleAnnotations.get(i);
            an.accept(fv.visitAnnotation(an.desc, true));
        }
        n = invisibleAnnotations == null ? 0 : invisibleAnnotations.size();
        for (i = 0; i < n; ++i) {
            AnnotationNode an = invisibleAnnotations.get(i);
            an.accept(fv.visitAnnotation(an.desc, false));
        }
        n = attrs == null ? 0 : attrs.size();
        for (i = 0; i < n; ++i) {
            fv.visitAttribute(attrs.get(i));
        }
        fv.visitEnd();
    }
}
