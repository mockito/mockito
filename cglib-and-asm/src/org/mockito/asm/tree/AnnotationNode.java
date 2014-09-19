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
import org.mockito.asm.Opcodes;

/**
 * A node that represents an annotationn.
 * 
 * @author Eric Bruneton
 */
public class AnnotationNode extends AnnotationVisitor {

    /**
     * The class descriptor of the annotation class.
     */
    public String desc;

    /**
     * The name value pairs of this annotation. Each name value pair is stored
     * as two consecutive elements in the list. The name is a {@link String},
     * and the value may be a {@link Byte}, {@link Boolean}, {@link Character},
     * {@link Short}, {@link Integer}, {@link Long}, {@link Float},
     * {@link Double}, {@link String} or {@link org.mockito.asm.Type}, or an
     * two elements String array (for enumeration values), a
     * {@link AnnotationNode}, or a {@link List} of values of one of the
     * preceding types. The list may be <tt>null</tt> if there is no name value
     * pair.
     */
    public List<Object> values;

    /**
     * Constructs a new {@link AnnotationNode}. <i>Subclasses must not use this
     * constructor</i>. Instead, they must use the
     * {@link #AnnotationNode(int, String)} version.
     * 
     * @param desc
     *            the class descriptor of the annotation class.
     */
    public AnnotationNode(final String desc) {
        this(Opcodes.ASM4, desc);
    }

    /**
     * Constructs a new {@link AnnotationNode}.
     * 
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}.
     * @param desc
     *            the class descriptor of the annotation class.
     */
    public AnnotationNode(final int api, final String desc) {
        super(api);
        this.desc = desc;
    }

    /**
     * Constructs a new {@link AnnotationNode} to visit an array value.
     * 
     * @param values
     *            where the visited values must be stored.
     */
    AnnotationNode(final List<Object> values) {
        super(Opcodes.ASM4);
        this.values = values;
    }

    // ------------------------------------------------------------------------
    // Implementation of the AnnotationVisitor abstract class
    // ------------------------------------------------------------------------

    @Override
    public void visit(final String name, final Object value) {
        if (values == null) {
            values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            values.add(name);
        }
        values.add(value);
    }

    @Override
    public void visitEnum(final String name, final String desc,
            final String value) {
        if (values == null) {
            values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            values.add(name);
        }
        values.add(new String[] { desc, value });
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name,
            final String desc) {
        if (values == null) {
            values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            values.add(name);
        }
        AnnotationNode annotation = new AnnotationNode(desc);
        values.add(annotation);
        return annotation;
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        if (values == null) {
            values = new ArrayList<Object>(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            values.add(name);
        }
        List<Object> array = new ArrayList<Object>();
        values.add(array);
        return new AnnotationNode(array);
    }

    @Override
    public void visitEnd() {
    }

    // ------------------------------------------------------------------------
    // Accept methods
    // ------------------------------------------------------------------------

    /**
     * Checks that this annotation node is compatible with the given ASM API
     * version. This methods checks that this node, and all its nodes
     * recursively, do not contain elements that were introduced in more recent
     * versions of the ASM API than the given version.
     * 
     * @param api
     *            an ASM API version. Must be one of {@link Opcodes#ASM4}.
     */
    public void check(final int api) {
        // nothing to do
    }

    /**
     * Makes the given visitor visit this annotation.
     * 
     * @param av
     *            an annotation visitor. Maybe <tt>null</tt>.
     */
    public void accept(final AnnotationVisitor av) {
        if (av != null) {
            if (values != null) {
                for (int i = 0; i < values.size(); i += 2) {
                    String name = (String) values.get(i);
                    Object value = values.get(i + 1);
                    accept(av, name, value);
                }
            }
            av.visitEnd();
        }
    }

    /**
     * Makes the given visitor visit a given annotation value.
     * 
     * @param av
     *            an annotation visitor. Maybe <tt>null</tt>.
     * @param name
     *            the value name.
     * @param value
     *            the actual value.
     */
    static void accept(final AnnotationVisitor av, final String name,
            final Object value) {
        if (av != null) {
            if (value instanceof String[]) {
                String[] typeconst = (String[]) value;
                av.visitEnum(name, typeconst[0], typeconst[1]);
            } else if (value instanceof AnnotationNode) {
                AnnotationNode an = (AnnotationNode) value;
                an.accept(av.visitAnnotation(name, an.desc));
            } else if (value instanceof List) {
                AnnotationVisitor v = av.visitArray(name);
                List<?> array = (List<?>) value;
                for (int j = 0; j < array.size(); ++j) {
                    accept(v, null, array.get(j));
                }
                v.visitEnd();
            } else {
                av.visit(name, value);
            }
        }
    }
}
