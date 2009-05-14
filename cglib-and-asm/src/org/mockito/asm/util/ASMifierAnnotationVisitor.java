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

/**
 * An {@link AnnotationVisitor} that prints the ASM code that generates the
 * annotations it visits.
 * 
 * @author Eric Bruneton
 */
public class ASMifierAnnotationVisitor extends AbstractVisitor implements
        AnnotationVisitor
{

    /**
     * Identifier of the annotation visitor variable in the produced code.
     */
    protected final int id;

    /**
     * Constructs a new {@link ASMifierAnnotationVisitor}.
     * 
     * @param id identifier of the annotation visitor variable in the produced
     *        code.
     */
    public ASMifierAnnotationVisitor(final int id) {
        this.id = id;
    }

    // ------------------------------------------------------------------------
    // Implementation of the AnnotationVisitor interface
    // ------------------------------------------------------------------------

    public void visit(final String name, final Object value) {
        buf.setLength(0);
        buf.append("av").append(id).append(".visit(");
        ASMifierAbstractVisitor.appendConstant(buf, name);
        buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(buf, value);
        buf.append(");\n");
        text.add(buf.toString());
    }

    public void visitEnum(
        final String name,
        final String desc,
        final String value)
    {
        buf.setLength(0);
        buf.append("av").append(id).append(".visitEnum(");
        ASMifierAbstractVisitor.appendConstant(buf, name);
        buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(buf, desc);
        buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(buf, value);
        buf.append(");\n");
        text.add(buf.toString());
    }

    public AnnotationVisitor visitAnnotation(
        final String name,
        final String desc)
    {
        buf.setLength(0);
        buf.append("{\n");
        buf.append("AnnotationVisitor av").append(id + 1).append(" = av");
        buf.append(id).append(".visitAnnotation(");
        ASMifierAbstractVisitor.appendConstant(buf, name);
        buf.append(", ");
        ASMifierAbstractVisitor.appendConstant(buf, desc);
        buf.append(");\n");
        text.add(buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(id + 1);
        text.add(av.getText());
        text.add("}\n");
        return av;
    }

    public AnnotationVisitor visitArray(final String name) {
        buf.setLength(0);
        buf.append("{\n");
        buf.append("AnnotationVisitor av").append(id + 1).append(" = av");
        buf.append(id).append(".visitArray(");
        ASMifierAbstractVisitor.appendConstant(buf, name);
        buf.append(");\n");
        text.add(buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(id + 1);
        text.add(av.getText());
        text.add("}\n");
        return av;
    }

    public void visitEnd() {
        buf.setLength(0);
        buf.append("av").append(id).append(".visitEnd();\n");
        text.add(buf.toString());
    }
}
