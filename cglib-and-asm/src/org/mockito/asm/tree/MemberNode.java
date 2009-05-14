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
package org.mockito.asm.tree;

import java.util.ArrayList;
import java.util.List;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;

/**
 * An abstract class, field or method node.
 * 
 * @author Eric Bruneton
 */
public abstract class MemberNode {

    /**
     * The runtime visible annotations of this class, field or method. This list
     * is a list of {@link AnnotationNode} objects. May be <tt>null</tt>.
     * 
     * @associates org.mockito.asm.tree.AnnotationNode
     * @label visible
     */
    public List visibleAnnotations;

    /**
     * The runtime invisible annotations of this class, field or method. This
     * list is a list of {@link AnnotationNode} objects. May be <tt>null</tt>.
     * 
     * @associates org.mockito.asm.tree.AnnotationNode
     * @label invisible
     */
    public List invisibleAnnotations;

    /**
     * The non standard attributes of this class, field or method. This list is
     * a list of {@link Attribute} objects. May be <tt>null</tt>.
     * 
     * @associates org.mockito.asm.Attribute
     */
    public List attrs;

    /**
     * Constructs a new {@link MemberNode}.
     */
    protected MemberNode() {
    }

    /**
     * Visits an annotation of this class, field or method.
     * 
     * @param desc the class descriptor of the annotation class.
     * @param visible <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values.
     */
    public AnnotationVisitor visitAnnotation(
        final String desc,
        final boolean visible)
    {
        AnnotationNode an = new AnnotationNode(desc);
        if (visible) {
            if (visibleAnnotations == null) {
                visibleAnnotations = new ArrayList(1);
            }
            visibleAnnotations.add(an);
        } else {
            if (invisibleAnnotations == null) {
                invisibleAnnotations = new ArrayList(1);
            }
            invisibleAnnotations.add(an);
        }
        return an;
    }

    /**
     * Visits a non standard attribute of this class, field or method.
     * 
     * @param attr an attribute.
     */
    public void visitAttribute(final Attribute attr) {
        if (attrs == null) {
            attrs = new ArrayList(1);
        }
        attrs.add(attr);
    }

    /**
     * Visits the end of this class, field or method.
     */
    public void visitEnd() {
    }
}
