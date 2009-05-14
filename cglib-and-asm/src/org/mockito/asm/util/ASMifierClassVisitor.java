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
import org.mockito.asm.ClassReader;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;

/**
 * A {@link ClassVisitor} that prints the ASM code that generates the classes it
 * visits. This class visitor can be used to quickly write ASM code to generate
 * some given bytecode: <ul> <li>write the Java source code equivalent to the
 * bytecode you want to generate;</li> <li>compile it with <tt>javac</tt>;</li>
 * <li>make a {@link ASMifierClassVisitor} visit this compiled class (see the
 * {@link #main main} method);</li> <li>edit the generated source code, if
 * necessary.</li> </ul> The source code printed when visiting the
 * <tt>Hello</tt> class is the following: <p> <blockquote>
 * 
 * <pre>
 * import org.mockito.asm.*;
 *
 * public class HelloDump implements Opcodes {
 *
 *     public static byte[] dump() throws Exception {
 *
 *         ClassWriter cw = new ClassWriter(0);
 *         FieldVisitor fv;
 *         MethodVisitor mv;
 *         AnnotationVisitor av0;
 *
 *         cw.visit(49,
 *                 ACC_PUBLIC + ACC_SUPER,
 *                 &quot;Hello&quot;,
 *                 null,
 *                 &quot;java/lang/Object&quot;,
 *                 null);
 *
 *         cw.visitSource(&quot;Hello.java&quot;, null);
 *
 *         {
 *             mv = cw.visitMethod(ACC_PUBLIC, &quot;&lt;init&gt;&quot;, &quot;()V&quot;, null, null);
 *             mv.visitVarInsn(ALOAD, 0);
 *             mv.visitMethodInsn(INVOKESPECIAL,
 *                     &quot;java/lang/Object&quot;,
 *                     &quot;&lt;init&gt;&quot;,
 *                     &quot;()V&quot;);
 *             mv.visitInsn(RETURN);
 *             mv.visitMaxs(1, 1);
 *             mv.visitEnd();
 *         }
 *         {
 *             mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
 *                     &quot;main&quot;,
 *                     &quot;([Ljava/lang/String;)V&quot;,
 *                     null,
 *                     null);
 *             mv.visitFieldInsn(GETSTATIC,
 *                     &quot;java/lang/System&quot;,
 *                     &quot;out&quot;,
 *                     &quot;Ljava/io/PrintStream;&quot;);
 *             mv.visitLdcInsn(&quot;hello&quot;);
 *             mv.visitMethodInsn(INVOKEVIRTUAL,
 *                     &quot;java/io/PrintStream&quot;,
 *                     &quot;println&quot;,
 *                     &quot;(Ljava/lang/String;)V&quot;);
 *             mv.visitInsn(RETURN);
 *             mv.visitMaxs(2, 1);
 *             mv.visitEnd();
 *         }
 *         cw.visitEnd();
 *
 *         return cw.toByteArray();
 *     }
 * }
 *
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
public class ASMifierClassVisitor extends ASMifierAbstractVisitor implements
        ClassVisitor
{

    /**
     * Pseudo access flag used to distinguish class access flags.
     */
    private static final int ACCESS_CLASS = 262144;

    /**
     * Pseudo access flag used to distinguish field access flags.
     */
    private static final int ACCESS_FIELD = 524288;

    /**
     * Pseudo access flag used to distinguish inner class flags.
     */
    private static final int ACCESS_INNER = 1048576;

    /**
     * The print writer to be used to print the class.
     */
    protected final PrintWriter pw;

    /**
     * Prints the ASM source code to generate the given class to the standard
     * output. <p> Usage: ASMifierClassVisitor [-debug] &lt;fully qualified
     * class name or class file name&gt;
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
            System.err.println("Prints the ASM code to generate the given class.");
            System.err.println("Usage: ASMifierClassVisitor [-debug] "
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
        cr.accept(new ASMifierClassVisitor(new PrintWriter(System.out)),
                getDefaultAttributes(),
                flags);
    }

    /**
     * Constructs a new {@link ASMifierClassVisitor} object.
     * 
     * @param pw the print writer to be used to print the class.
     */
    public ASMifierClassVisitor(final PrintWriter pw) {
        super("cw");
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
        String simpleName;
        int n = name.lastIndexOf('/');
        if (n == -1) {
            simpleName = name;
        } else {
            text.add("package asm." + name.substring(0, n).replace('/', '.')
                    + ";\n");
            simpleName = name.substring(n + 1);
        }
        text.add("import java.util.*;\n");
        text.add("import org.mockito.asm.*;\n");
        text.add("import org.mockito.asm.attrs.*;\n");
        text.add("public class " + simpleName + "Dump implements Opcodes {\n\n");
        text.add("public static byte[] dump () throws Exception {\n\n");
        text.add("ClassWriter cw = new ClassWriter(0);\n");
        text.add("FieldVisitor fv;\n");
        text.add("MethodVisitor mv;\n");
        text.add("AnnotationVisitor av0;\n\n");

        buf.setLength(0);
        buf.append("cw.visit(");
        switch (version) {
            case Opcodes.V1_1:
                buf.append("V1_1");
                break;
            case Opcodes.V1_2:
                buf.append("V1_2");
                break;
            case Opcodes.V1_3:
                buf.append("V1_3");
                break;
            case Opcodes.V1_4:
                buf.append("V1_4");
                break;
            case Opcodes.V1_5:
                buf.append("V1_5");
                break;
            case Opcodes.V1_6:
                buf.append("V1_6");
                break;
            default:
                buf.append(version);
                break;
        }
        buf.append(", ");
        appendAccess(access | ACCESS_CLASS);
        buf.append(", ");
        appendConstant(name);
        buf.append(", ");
        appendConstant(signature);
        buf.append(", ");
        appendConstant(superName);
        buf.append(", ");
        if (interfaces != null && interfaces.length > 0) {
            buf.append("new String[] {");
            for (int i = 0; i < interfaces.length; ++i) {
                buf.append(i == 0 ? " " : ", ");
                appendConstant(interfaces[i]);
            }
            buf.append(" }");
        } else {
            buf.append("null");
        }
        buf.append(");\n\n");
        text.add(buf.toString());
    }

    public void visitSource(final String file, final String debug) {
        buf.setLength(0);
        buf.append("cw.visitSource(");
        appendConstant(file);
        buf.append(", ");
        appendConstant(debug);
        buf.append(");\n\n");
        text.add(buf.toString());
    }

    public void visitOuterClass(
        final String owner,
        final String name,
        final String desc)
    {
        buf.setLength(0);
        buf.append("cw.visitOuterClass(");
        appendConstant(owner);
        buf.append(", ");
        appendConstant(name);
        buf.append(", ");
        appendConstant(desc);
        buf.append(");\n\n");
        text.add(buf.toString());
    }

    public void visitInnerClass(
        final String name,
        final String outerName,
        final String innerName,
        final int access)
    {
        buf.setLength(0);
        buf.append("cw.visitInnerClass(");
        appendConstant(name);
        buf.append(", ");
        appendConstant(outerName);
        buf.append(", ");
        appendConstant(innerName);
        buf.append(", ");
        appendAccess(access | ACCESS_INNER);
        buf.append(");\n\n");
        text.add(buf.toString());
    }

    public FieldVisitor visitField(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final Object value)
    {
        buf.setLength(0);
        buf.append("{\n");
        buf.append("fv = cw.visitField(");
        appendAccess(access | ACCESS_FIELD);
        buf.append(", ");
        appendConstant(name);
        buf.append(", ");
        appendConstant(desc);
        buf.append(", ");
        appendConstant(signature);
        buf.append(", ");
        appendConstant(value);
        buf.append(");\n");
        text.add(buf.toString());
        ASMifierFieldVisitor aav = new ASMifierFieldVisitor();
        text.add(aav.getText());
        text.add("}\n");
        return aav;
    }

    public MethodVisitor visitMethod(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final String[] exceptions)
    {
        buf.setLength(0);
        buf.append("{\n");
        buf.append("mv = cw.visitMethod(");
        appendAccess(access);
        buf.append(", ");
        appendConstant(name);
        buf.append(", ");
        appendConstant(desc);
        buf.append(", ");
        appendConstant(signature);
        buf.append(", ");
        if (exceptions != null && exceptions.length > 0) {
            buf.append("new String[] {");
            for (int i = 0; i < exceptions.length; ++i) {
                buf.append(i == 0 ? " " : ", ");
                appendConstant(exceptions[i]);
            }
            buf.append(" }");
        } else {
            buf.append("null");
        }
        buf.append(");\n");
        text.add(buf.toString());
        ASMifierMethodVisitor acv = createASMifierMethodVisitor();
        text.add(acv.getText());
        text.add("}\n");
        return acv;
    }

    protected ASMifierMethodVisitor createASMifierMethodVisitor() {
        return new ASMifierMethodVisitor();
    }

    public AnnotationVisitor visitAnnotation(
        final String desc,
        final boolean visible)
    {
        buf.setLength(0);
        buf.append("{\n");
        buf.append("av0 = cw.visitAnnotation(");
        appendConstant(desc);
        buf.append(", ");
        buf.append(visible);
        buf.append(");\n");
        text.add(buf.toString());
        ASMifierAnnotationVisitor av = new ASMifierAnnotationVisitor(0);
        text.add(av.getText());
        text.add("}\n");
        return av;
    }

    public void visitEnd() {
        text.add("cw.visitEnd();\n\n");
        text.add("return cw.toByteArray();\n");
        text.add("}\n");
        text.add("}\n");
        printList(pw, text);
        pw.flush();
    }

    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------

    /**
     * Appends a string representation of the given access modifiers to {@link
     * #buf buf}.
     * 
     * @param access some access modifiers.
     */
    void appendAccess(final int access) {
        boolean first = true;
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            buf.append("ACC_PUBLIC");
            first = false;
        }
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            buf.append("ACC_PRIVATE");
            first = false;
        }
        if ((access & Opcodes.ACC_PROTECTED) != 0) {
            buf.append("ACC_PROTECTED");
            first = false;
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_FINAL");
            first = false;
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_STATIC");
            first = false;
        }
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            if ((access & ACCESS_CLASS) == 0) {
                buf.append("ACC_SYNCHRONIZED");
            } else {
                buf.append("ACC_SUPER");
            }
            first = false;
        }
        if ((access & Opcodes.ACC_VOLATILE) != 0
                && (access & ACCESS_FIELD) != 0)
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_VOLATILE");
            first = false;
        }
        if ((access & Opcodes.ACC_BRIDGE) != 0 && (access & ACCESS_CLASS) == 0
                && (access & ACCESS_FIELD) == 0)
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_BRIDGE");
            first = false;
        }
        if ((access & Opcodes.ACC_VARARGS) != 0 && (access & ACCESS_CLASS) == 0
                && (access & ACCESS_FIELD) == 0)
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_VARARGS");
            first = false;
        }
        if ((access & Opcodes.ACC_TRANSIENT) != 0
                && (access & ACCESS_FIELD) != 0)
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_TRANSIENT");
            first = false;
        }
        if ((access & Opcodes.ACC_NATIVE) != 0 && (access & ACCESS_CLASS) == 0
                && (access & ACCESS_FIELD) == 0)
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_NATIVE");
            first = false;
        }
        if ((access & Opcodes.ACC_ENUM) != 0
                && ((access & ACCESS_CLASS) != 0
                        || (access & ACCESS_FIELD) != 0 || (access & ACCESS_INNER) != 0))
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_ENUM");
            first = false;
        }
        if ((access & Opcodes.ACC_ANNOTATION) != 0
                && (access & ACCESS_CLASS) != 0)
        {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_ANNOTATION");
            first = false;
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_ABSTRACT");
            first = false;
        }
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_INTERFACE");
            first = false;
        }
        if ((access & Opcodes.ACC_STRICT) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_STRICT");
            first = false;
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_SYNTHETIC");
            first = false;
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            if (!first) {
                buf.append(" + ");
            }
            buf.append("ACC_DEPRECATED");
            first = false;
        }
        if (first) {
            buf.append('0');
        }
    }
}
