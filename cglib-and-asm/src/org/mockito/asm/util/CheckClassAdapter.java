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
import java.util.List;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;
import org.mockito.asm.ClassAdapter;
import org.mockito.asm.ClassReader;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;
import org.mockito.asm.tree.ClassNode;
import org.mockito.asm.tree.MethodNode;
import org.mockito.asm.tree.TryCatchBlockNode;
import org.mockito.asm.tree.analysis.Analyzer;
import org.mockito.asm.tree.analysis.Frame;
import org.mockito.asm.tree.analysis.SimpleVerifier;

/**
 * A {@link ClassAdapter} that checks that its methods are properly used. More
 * precisely this class adapter checks each method call individually, based
 * <i>only</i> on its arguments, but does <i>not</i> check the <i>sequence</i>
 * of method calls. For example, the invalid sequence
 * <tt>visitField(ACC_PUBLIC, "i", "I", null)</tt> <tt>visitField(ACC_PUBLIC,
 * "i", "D", null)</tt>
 * will <i>not</i> be detected by this class adapter.
 * 
 * <p><code>CheckClassAdapter</code> can be also used to verify bytecode
 * transformations in order to make sure transformed bytecode is sane. For
 * example:
 * 
 * <pre>
 *   InputStream is = ...; // get bytes for the source class
 *   ClassReader cr = new ClassReader(is);
 *   ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
 *   ClassVisitor cv = new <b>MyClassAdapter</b>(new CheckClassAdapter(cw));
 *   cr.accept(cv, 0);
 * 
 *   StringWriter sw = new StringWriter();
 *   PrintWriter pw = new PrintWriter(sw);
 *   CheckClassAdapter.verify(new ClassReader(cw.toByteArray()), false, pw);
 *   assertTrue(sw.toString(), sw.toString().length()==0);
 * </pre>
 * 
 * Above code runs transformed bytecode trough the
 * <code>CheckClassAdapter</code>. It won't be exactly the same verification
 * as JVM does, but it run data flow analysis for the code of each method and
 * checks that expectations are met for each method instruction.
 * 
 * <p>If method bytecode has errors, assertion text will show the erroneous
 * instruction number and dump of the failed method with information about
 * locals and stack slot for each instruction. For example (format is -
 * insnNumber locals : stack):
 * 
 * <pre>
 * org.mockito.asm.tree.analysis.AnalyzerException: Error at instruction 71: Expected I, but found .
 *   at org.mockito.asm.tree.analysis.Analyzer.analyze(Analyzer.java:289)
 *   at org.mockito.asm.util.CheckClassAdapter.verify(CheckClassAdapter.java:135)
 * ...
 * remove()V
 * 00000 LinkedBlockingQueue$Itr . . . . . . . .  :
 *   ICONST_0
 * 00001 LinkedBlockingQueue$Itr . . . . . . . .  : I
 *   ISTORE 2
 * 00001 LinkedBlockingQueue$Itr <b>.</b> I . . . . . .  :
 * ...
 * 
 * 00071 LinkedBlockingQueue$Itr <b>.</b> I . . . . . .  : 
 *   ILOAD 1
 * 00072 <b>?</b>                
 *   INVOKESPECIAL java/lang/Integer.<init> (I)V
 * ...
 * </pre>
 * 
 * In the above output you can see that variable 1 loaded by
 * <code>ILOAD 1</code> instruction at position <code>00071</code> is not
 * initialized. You can also see that at the beginning of the method (code
 * inserted by the transformation) variable 2 is initialized.
 * 
 * <p>Note that when used like that, <code>CheckClassAdapter.verify()</code>
 * can trigger additional class loading, because it is using
 * <code>SimpleVerifier</code>.
 * 
 * @author Eric Bruneton
 */
public class CheckClassAdapter extends ClassAdapter {

    /**
     * <tt>true</tt> if the visit method has been called.
     */
    private boolean start;

    /**
     * <tt>true</tt> if the visitSource method has been called.
     */
    private boolean source;

    /**
     * <tt>true</tt> if the visitOuterClass method has been called.
     */
    private boolean outer;

    /**
     * <tt>true</tt> if the visitEnd method has been called.
     */
    private boolean end;

    /**
     * Checks a given class. <p> Usage: CheckClassAdapter &lt;fully qualified
     * class name or class file name&gt;
     * 
     * @param args the command line arguments.
     * 
     * @throws Exception if the class cannot be found, or if an IO exception
     *         occurs.
     */
    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Verifies the given class.");
            System.err.println("Usage: CheckClassAdapter "
                    + "<fully qualified class name or class file name>");
            return;
        }
        ClassReader cr;
        if (args[0].endsWith(".class")) {
            cr = new ClassReader(new FileInputStream(args[0]));
        } else {
            cr = new ClassReader(args[0]);
        }

        verify(cr, false, new PrintWriter(System.err));
    }

    /**
     * Checks a given class
     * 
     * @param cr a <code>ClassReader</code> that contains bytecode for the
     *        analysis.
     * @param dump true if bytecode should be printed out not only when errors
     *        are found.
     * @param pw write where results going to be printed
     */
    public static void verify(
        final ClassReader cr,
        final boolean dump,
        final PrintWriter pw)
    {
        ClassNode cn = new ClassNode();
        cr.accept(new CheckClassAdapter(cn), ClassReader.SKIP_DEBUG);

        Type syperType = cn.superName == null
                ? null
                : Type.getObjectType(cn.superName);
        List methods = cn.methods;
        for (int i = 0; i < methods.size(); ++i) {
            MethodNode method = (MethodNode) methods.get(i);
            Analyzer a = new Analyzer(new SimpleVerifier(Type.getObjectType(cn.name),
                    syperType,
                    false));
            try {
                a.analyze(cn.name, method);
                if (!dump) {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace(pw);
            }
            Frame[] frames = a.getFrames();

            TraceMethodVisitor mv = new TraceMethodVisitor();

            pw.println(method.name + method.desc);
            for (int j = 0; j < method.instructions.size(); ++j) {
                method.instructions.get(j).accept(mv);

                StringBuffer s = new StringBuffer();
                Frame f = frames[j];
                if (f == null) {
                    s.append('?');
                } else {
                    for (int k = 0; k < f.getLocals(); ++k) {
                        s.append(getShortName(f.getLocal(k).toString()))
                                .append(' ');
                    }
                    s.append(" : ");
                    for (int k = 0; k < f.getStackSize(); ++k) {
                        s.append(getShortName(f.getStack(k).toString()))
                                .append(' ');
                    }
                }
                while (s.length() < method.maxStack + method.maxLocals + 1) {
                    s.append(' ');
                }
                pw.print(Integer.toString(j + 100000).substring(1));
                pw.print(" " + s + " : " + mv.buf); // mv.text.get(j));
            }
            for (int j = 0; j < method.tryCatchBlocks.size(); ++j) {
                ((TryCatchBlockNode) method.tryCatchBlocks.get(j)).accept(mv);
                pw.print(" " + mv.buf);
            }
            pw.println();
        }
        pw.flush();
    }

    private static String getShortName(final String name) {
        int n = name.lastIndexOf('/');
        int k = name.length();
        if (name.charAt(k - 1) == ';') {
            k--;
        }
        return n == -1 ? name : name.substring(n + 1, k);
    }

    /**
     * Constructs a new {@link CheckClassAdapter}.
     * 
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public CheckClassAdapter(final ClassVisitor cv) {
        super(cv);
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
        if (start) {
            throw new IllegalStateException("visit must be called only once");
        }
        start = true;
        checkState();
        checkAccess(access, Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL
                + Opcodes.ACC_SUPER + Opcodes.ACC_INTERFACE
                + Opcodes.ACC_ABSTRACT + Opcodes.ACC_SYNTHETIC
                + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ENUM
                + Opcodes.ACC_DEPRECATED);
        if (name == null || !name.endsWith("package-info")) {
            CheckMethodAdapter.checkInternalName(name, "class name");
        }
        if ("java/lang/Object".equals(name)) {
            if (superName != null) {
                throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
            }
        } else {
            CheckMethodAdapter.checkInternalName(superName, "super class name");
        }
        if (signature != null) {
            CheckMethodAdapter.checkClassSignature(signature);
        }
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            if (!"java/lang/Object".equals(superName)) {
                throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
            }
        }
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; ++i) {
                CheckMethodAdapter.checkInternalName(interfaces[i],
                        "interface name at index " + i);
            }
        }
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    public void visitSource(final String file, final String debug) {
        checkState();
        if (source) {
            throw new IllegalStateException("visitSource can be called only once.");
        }
        source = true;
        cv.visitSource(file, debug);
    }

    public void visitOuterClass(
        final String owner,
        final String name,
        final String desc)
    {
        checkState();
        if (outer) {
            throw new IllegalStateException("visitOuterClass can be called only once.");
        }
        outer = true;
        if (owner == null) {
            throw new IllegalArgumentException("Illegal outer class owner");
        }
        if (desc != null) {
            CheckMethodAdapter.checkMethodDesc(desc);
        }
        cv.visitOuterClass(owner, name, desc);
    }

    public void visitInnerClass(
        final String name,
        final String outerName,
        final String innerName,
        final int access)
    {
        checkState();
        CheckMethodAdapter.checkInternalName(name, "class name");
        if (outerName != null) {
            CheckMethodAdapter.checkInternalName(outerName, "outer class name");
        }
        if (innerName != null) {
            CheckMethodAdapter.checkIdentifier(innerName, "inner class name");
        }
        checkAccess(access, Opcodes.ACC_PUBLIC + Opcodes.ACC_PRIVATE
                + Opcodes.ACC_PROTECTED + Opcodes.ACC_STATIC
                + Opcodes.ACC_FINAL + Opcodes.ACC_INTERFACE
                + Opcodes.ACC_ABSTRACT + Opcodes.ACC_SYNTHETIC
                + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ENUM);
        cv.visitInnerClass(name, outerName, innerName, access);
    }

    public FieldVisitor visitField(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final Object value)
    {
        checkState();
        checkAccess(access, Opcodes.ACC_PUBLIC + Opcodes.ACC_PRIVATE
                + Opcodes.ACC_PROTECTED + Opcodes.ACC_STATIC
                + Opcodes.ACC_FINAL + Opcodes.ACC_VOLATILE
                + Opcodes.ACC_TRANSIENT + Opcodes.ACC_SYNTHETIC
                + Opcodes.ACC_ENUM + Opcodes.ACC_DEPRECATED);
        CheckMethodAdapter.checkIdentifier(name, "field name");
        CheckMethodAdapter.checkDesc(desc, false);
        if (signature != null) {
            CheckMethodAdapter.checkFieldSignature(signature);
        }
        if (value != null) {
            CheckMethodAdapter.checkConstant(value);
        }
        FieldVisitor av = cv.visitField(access, name, desc, signature, value);
        return new CheckFieldAdapter(av);
    }

    public MethodVisitor visitMethod(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final String[] exceptions)
    {
        checkState();
        checkAccess(access, Opcodes.ACC_PUBLIC + Opcodes.ACC_PRIVATE
                + Opcodes.ACC_PROTECTED + Opcodes.ACC_STATIC
                + Opcodes.ACC_FINAL + Opcodes.ACC_SYNCHRONIZED
                + Opcodes.ACC_BRIDGE + Opcodes.ACC_VARARGS + Opcodes.ACC_NATIVE
                + Opcodes.ACC_ABSTRACT + Opcodes.ACC_STRICT
                + Opcodes.ACC_SYNTHETIC + Opcodes.ACC_DEPRECATED);
        CheckMethodAdapter.checkMethodIdentifier(name, "method name");
        CheckMethodAdapter.checkMethodDesc(desc);
        if (signature != null) {
            CheckMethodAdapter.checkMethodSignature(signature);
        }
        if (exceptions != null) {
            for (int i = 0; i < exceptions.length; ++i) {
                CheckMethodAdapter.checkInternalName(exceptions[i],
                        "exception name at index " + i);
            }
        }
        return new CheckMethodAdapter(cv.visitMethod(access,
                name,
                desc,
                signature,
                exceptions));
    }

    public AnnotationVisitor visitAnnotation(
        final String desc,
        final boolean visible)
    {
        checkState();
        CheckMethodAdapter.checkDesc(desc, false);
        return new CheckAnnotationAdapter(cv.visitAnnotation(desc, visible));
    }

    public void visitAttribute(final Attribute attr) {
        checkState();
        if (attr == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        cv.visitAttribute(attr);
    }

    public void visitEnd() {
        checkState();
        end = true;
        cv.visitEnd();
    }

    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------

    /**
     * Checks that the visit method has been called and that visitEnd has not
     * been called.
     */
    private void checkState() {
        if (!start) {
            throw new IllegalStateException("Cannot visit member before visit has been called.");
        }
        if (end) {
            throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
        }
    }

    /**
     * Checks that the given access flags do not contain invalid flags. This
     * method also checks that mutually incompatible flags are not set
     * simultaneously.
     * 
     * @param access the access flags to be checked
     * @param possibleAccess the valid access flags.
     */
    static void checkAccess(final int access, final int possibleAccess) {
        if ((access & ~possibleAccess) != 0) {
            throw new IllegalArgumentException("Invalid access flags: "
                    + access);
        }
        int pub = (access & Opcodes.ACC_PUBLIC) == 0 ? 0 : 1;
        int pri = (access & Opcodes.ACC_PRIVATE) == 0 ? 0 : 1;
        int pro = (access & Opcodes.ACC_PROTECTED) == 0 ? 0 : 1;
        if (pub + pri + pro > 1) {
            throw new IllegalArgumentException("public private and protected are mutually exclusive: "
                    + access);
        }
        int fin = (access & Opcodes.ACC_FINAL) == 0 ? 0 : 1;
        int abs = (access & Opcodes.ACC_ABSTRACT) == 0 ? 0 : 1;
        if (fin + abs > 1) {
            throw new IllegalArgumentException("final and abstract are mutually exclusive: "
                    + access);
        }
    }
}
