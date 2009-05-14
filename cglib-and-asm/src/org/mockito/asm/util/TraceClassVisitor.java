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

import java.io.FileInputStream;
import java.io.PrintWriter;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;
import org.mockito.asm.ClassReader;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;
import org.mockito.asm.signature.SignatureReader;

/**
 * A {@link ClassVisitor} that prints a disassembled view of the classes it
 * visits. This class visitor can be used alone (see the {@link #main main}
 * method) to disassemble a class. It can also be used in the middle of class
 * visitor chain to trace the class that is visited at a given point in this
 * chain. This may be uselful for debugging purposes. <p> The trace printed when
 * visiting the <tt>Hello</tt> class is the following: <p> <blockquote>
 * 
 * <pre>
 * // class version 49.0 (49)
 * // access flags 33
 * public class Hello {
 *
 *  // compiled from: Hello.java
 *
 *   // access flags 1
 *   public &lt;init&gt; ()V
 *     ALOAD 0
 *     INVOKESPECIAL java/lang/Object &lt;init&gt; ()V
 *     RETURN
 *     MAXSTACK = 1
 *     MAXLOCALS = 1
 *
 *   // access flags 9
 *   public static main ([Ljava/lang/String;)V
 *     GETSTATIC java/lang/System out Ljava/io/PrintStream;
 *     LDC &quot;hello&quot;
 *     INVOKEVIRTUAL java/io/PrintStream println (Ljava/lang/String;)V
 *     RETURN
 *     MAXSTACK = 2
 *     MAXLOCALS = 1
 * }
 * </pre>
 * 
 * </blockquote> where <tt>Hello</tt> is defined by: <p> <blockquote>
 * 
 * <pre>
 * public class Hello {
 *
 *     public static void main(String[] args) {
 *         System.out.println(&quot;hello&quot;);
 *     }
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 */
public class TraceClassVisitor extends TraceAbstractVisitor implements
        ClassVisitor
{

    /**
     * The {@link ClassVisitor} to which this visitor delegates calls. May be
     * <tt>null</tt>.
     */
    protected final ClassVisitor cv;

    /**
     * The print writer to be used to print the class.
     */
    protected final PrintWriter pw;

    /**
     * Prints a disassembled view of the given class to the standard output. <p>
     * Usage: TraceClassVisitor [-debug] &lt;fully qualified class name or class
     * file name &gt;
     * 
     * @param args the command line arguments.
     * 
     * @throws Exception if the class cannot be found, or if an IO exception
     *         occurs.
     */
    public static void main(final String[] args) throws Exception {
        int i = 0;
        int flags = ClassReader.SKIP_DEBUG;

        boolean ok = true;
        if (args.length < 1 || args.length > 2) {
            ok = false;
        }
        if (ok && "-debug".equals(args[0])) {
            i = 1;
            flags = 0;
            if (args.length != 2) {
                ok = false;
            }
        }
        if (!ok) {
            System.err.println("Prints a disassembled view of the given class.");
            System.err.println("Usage: TraceClassVisitor [-debug] "
                    + "<fully qualified class name or class file name>");
            return;
        }
        ClassReader cr;
        if (args[i].endsWith(".class") || args[i].indexOf('\\') > -1
                || args[i].indexOf('/') > -1)
        {
            cr = new ClassReader(new FileInputStream(args[i]));
        } else {
            cr = new ClassReader(args[i]);
        }
        cr.accept(new TraceClassVisitor(new PrintWriter(System.out)),
                getDefaultAttributes(),
                flags);
    }

    /**
     * Constructs a new {@link TraceClassVisitor}.
     * 
     * @param pw the print writer to be used to print the class.
     */
    public TraceClassVisitor(final PrintWriter pw) {
        this(null, pw);
    }

    /**
     * Constructs a new {@link TraceClassVisitor}.
     * 
     * @param cv the {@link ClassVisitor} to which this visitor delegates calls.
     *        May be <tt>null</tt>.
     * @param pw the print writer to be used to print the class.
     */
    public TraceClassVisitor(final ClassVisitor cv, final PrintWriter pw) {
        this.cv = cv;
        this.pw = pw;
    }

    // ------------------------------------------------------------------------
    // Implementation of the ClassVisitor interface
    // ------------------------------------------------------------------------

    public void visit(
        final int version,
        final int access,
        final String name,
        final String signature,
        final String superName,
        final String[] interfaces)
    {
        int major = version & 0xFFFF;
        int minor = version >>> 16;
        buf.setLength(0);
        buf.append("// class version ")
                .append(major)
                .append('.')
                .append(minor)
                .append(" (")
                .append(version)
                .append(")\n");
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            buf.append("// DEPRECATED\n");
        }
        buf.append("// access flags ").append(access).append('\n');

        appendDescriptor(CLASS_SIGNATURE, signature);
        if (signature != null) {
            TraceSignatureVisitor sv = new TraceSignatureVisitor(access);
            SignatureReader r = new SignatureReader(signature);
            r.accept(sv);
            buf.append("// declaration: ")
                    .append(name)
                    .append(sv.getDeclaration())
                    .append('\n');
        }

        appendAccess(access & ~Opcodes.ACC_SUPER);
        if ((access & Opcodes.ACC_ANNOTATION) != 0) {
            buf.append("@interface ");
        } else if ((access & Opcodes.ACC_INTERFACE) != 0) {
            buf.append("interface ");
        } else if ((access & Opcodes.ACC_ENUM) == 0) {
            buf.append("class ");
        }
        appendDescriptor(INTERNAL_NAME, name);

        if (superName != null && !"java/lang/Object".equals(superName)) {
            buf.append(" extends ");
            appendDescriptor(INTERNAL_NAME, superName);
            buf.append(' ');
        }
        if (interfaces != null && interfaces.length > 0) {
            buf.append(" implements ");
            for (int i = 0; i < interfaces.length; ++i) {
                appendDescriptor(INTERNAL_NAME, interfaces[i]);
                buf.append(' ');
            }
        }
        buf.append(" {\n\n");

        text.add(buf.toString());

        if (cv != null) {
            cv.visit(version, access, name, signature, superName, interfaces);
        }
    }

    public void visitSource(final String file, final String debug) {
        buf.setLength(0);
        if (file != null) {
            buf.append(tab)
                    .append("// compiled from: ")
                    .append(file)
                    .append('\n');
        }
        if (debug != null) {
            buf.append(tab)
                    .append("// debug info: ")
                    .append(debug)
                    .append('\n');
        }
        if (buf.length() > 0) {
            text.add(buf.toString());
        }

        if (cv != null) {
            cv.visitSource(file, debug);
        }
    }

    public void visitOuterClass(
        final String owner,
        final String name,
        final String desc)
    {
        buf.setLength(0);
        buf.append(tab).append("OUTERCLASS ");
        appendDescriptor(INTERNAL_NAME, owner);
        buf.append(' ');
        if (name != null) {
            buf.append(name).append(' ');
        }
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        buf.append('\n');
        text.add(buf.toString());

        if (cv != null) {
            cv.visitOuterClass(owner, name, desc);
        }
    }

    public AnnotationVisitor visitAnnotation(
        final String desc,
        final boolean visible)
    {
        text.add("\n");
        AnnotationVisitor tav = super.visitAnnotation(desc, visible);
        if (cv != null) {
            ((TraceAnnotationVisitor) tav).av = cv.visitAnnotation(desc,
                    visible);
        }
        return tav;
    }

    public void visitAttribute(final Attribute attr) {
        text.add("\n");
        super.visitAttribute(attr);

        if (cv != null) {
            cv.visitAttribute(attr);
        }
    }

    public void visitInnerClass(
        final String name,
        final String outerName,
        final String innerName,
        final int access)
    {
        buf.setLength(0);
        buf.append(tab).append("// access flags ");
        buf.append(access & ~Opcodes.ACC_SUPER).append('\n');
        buf.append(tab);
        appendAccess(access);
        buf.append("INNERCLASS ");
        appendDescriptor(INTERNAL_NAME, name);
        buf.append(' ');
        appendDescriptor(INTERNAL_NAME, outerName);
        buf.append(' ');
        appendDescriptor(INTERNAL_NAME, innerName);
        buf.append('\n');
        text.add(buf.toString());

        if (cv != null) {
            cv.visitInnerClass(name, outerName, innerName, access);
        }
    }

    public FieldVisitor visitField(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final Object value)
    {
        buf.setLength(0);
        buf.append('\n');
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            buf.append(tab).append("// DEPRECATED\n");
        }
        buf.append(tab).append("// access flags ").append(access).append('\n');
        if (signature != null) {
            buf.append(tab);
            appendDescriptor(FIELD_SIGNATURE, signature);

            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            buf.append(tab)
                    .append("// declaration: ")
                    .append(sv.getDeclaration())
                    .append('\n');
        }

        buf.append(tab);
        appendAccess(access);

        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append(' ').append(name);
        if (value != null) {
            buf.append(" = ");
            if (value instanceof String) {
                buf.append('\"').append(value).append('\"');
            } else {
                buf.append(value);
            }
        }

        buf.append('\n');
        text.add(buf.toString());

        TraceFieldVisitor tav = createTraceFieldVisitor();
        text.add(tav.getText());

        if (cv != null) {
            tav.fv = cv.visitField(access, name, desc, signature, value);
        }

        return tav;
    }

    public MethodVisitor visitMethod(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final String[] exceptions)
    {
        buf.setLength(0);
        buf.append('\n');
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            buf.append(tab).append("// DEPRECATED\n");
        }
        buf.append(tab).append("// access flags ").append(access).append('\n');

        if (signature != null) {
            buf.append(tab);
            appendDescriptor(METHOD_SIGNATURE, signature);

            TraceSignatureVisitor v = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.accept(v);
            String genericDecl = v.getDeclaration();
            String genericReturn = v.getReturnType();
            String genericExceptions = v.getExceptions();

            buf.append(tab)
                    .append("// declaration: ")
                    .append(genericReturn)
                    .append(' ')
                    .append(name)
                    .append(genericDecl);
            if (genericExceptions != null) {
                buf.append(" throws ").append(genericExceptions);
            }
            buf.append('\n');
        }

        buf.append(tab);
        appendAccess(access);
        if ((access & Opcodes.ACC_NATIVE) != 0) {
            buf.append("native ");
        }
        if ((access & Opcodes.ACC_VARARGS) != 0) {
            buf.append("varargs ");
        }
        if ((access & Opcodes.ACC_BRIDGE) != 0) {
            buf.append("bridge ");
        }

        buf.append(name);
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        if (exceptions != null && exceptions.length > 0) {
            buf.append(" throws ");
            for (int i = 0; i < exceptions.length; ++i) {
                appendDescriptor(INTERNAL_NAME, exceptions[i]);
                buf.append(' ');
            }
        }

        buf.append('\n');
        text.add(buf.toString());

        TraceMethodVisitor tcv = createTraceMethodVisitor();
        text.add(tcv.getText());

        if (cv != null) {
            tcv.mv = cv.visitMethod(access, name, desc, signature, exceptions);
        }

        return tcv;
    }

    public void visitEnd() {
        text.add("}\n");

        print(pw);
        pw.flush();

        if (cv != null) {
            cv.visitEnd();
        }
    }

    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------

    protected TraceFieldVisitor createTraceFieldVisitor() {
        return new TraceFieldVisitor();
    }

    protected TraceMethodVisitor createTraceMethodVisitor() {
        return new TraceMethodVisitor();
    }

    /**
     * Appends a string representation of the given access modifiers to {@link
     * #buf buf}.
     * 
     * @param access some access modifiers.
     */
    private void appendAccess(final int access) {
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            buf.append("public ");
        }
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            buf.append("private ");
        }
        if ((access & Opcodes.ACC_PROTECTED) != 0) {
            buf.append("protected ");
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            buf.append("final ");
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            buf.append("static ");
        }
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            buf.append("synchronized ");
        }
        if ((access & Opcodes.ACC_VOLATILE) != 0) {
            buf.append("volatile ");
        }
        if ((access & Opcodes.ACC_TRANSIENT) != 0) {
            buf.append("transient ");
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            buf.append("abstract ");
        }
        if ((access & Opcodes.ACC_STRICT) != 0) {
            buf.append("strictfp ");
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            buf.append("enum ");
        }
    }
}
