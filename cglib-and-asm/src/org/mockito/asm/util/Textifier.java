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
package org.mockito.asm.util;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.mockito.asm.Attribute;
import org.mockito.asm.ClassReader;
import org.mockito.asm.Handle;
import org.mockito.asm.Label;
import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;
import org.mockito.asm.signature.SignatureReader;

/**
 * A {@link Printer} that prints a disassembled view of the classes it visits.
 * 
 * @author Eric Bruneton
 */
public class Textifier extends Printer {

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
     * Constant used in {@link #appendDescriptor appendDescriptor} for handle
     * descriptors, formatted in bytecode notation
     */
    public static final int HANDLE_DESCRIPTOR = 9;

    /**
     * Tab for class members.
     */
    protected String tab = "  ";

    /**
     * Tab for bytecode instructions.
     */
    protected String tab2 = "    ";

    /**
     * Tab for table and lookup switch instructions.
     */
    protected String tab3 = "      ";

    /**
     * Tab for labels.
     */
    protected String ltab = "   ";

    /**
     * The label names. This map associate String values to Label keys.
     */
    protected Map<Label, String> labelNames;

    private int valueNumber = 0;

    /**
     * Constructs a new {@link Textifier}. <i>Subclasses must not use this
     * constructor</i>. Instead, they must use the {@link #Textifier(int)}
     * version.
     */
    public Textifier() {
        this(Opcodes.ASM4);
    }

    /**
     * Constructs a new {@link Textifier}.
     * 
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}.
     */
    protected Textifier(final int api) {
        super(api);
    }

    /**
     * Prints a disassembled view of the given class to the standard output.
     * <p>
     * Usage: Textifier [-debug] &lt;binary class name or class file name &gt;
     * 
     * @param args
     *            the command line arguments.
     * 
     * @throws Exception
     *             if the class cannot be found, or if an IO exception occurs.
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
            System.err
                    .println("Prints a disassembled view of the given class.");
            System.err.println("Usage: Textifier [-debug] "
                    + "<fully qualified class name or class file name>");
            return;
        }
        ClassReader cr;
        if (args[i].endsWith(".class") || args[i].indexOf('\\') > -1
                || args[i].indexOf('/') > -1) {
            cr = new ClassReader(new FileInputStream(args[i]));
        } else {
            cr = new ClassReader(args[i]);
        }
        cr.accept(new TraceClassVisitor(new PrintWriter(System.out)), flags);
    }

    // ------------------------------------------------------------------------
    // Classes
    // ------------------------------------------------------------------------

    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName,
            final String[] interfaces) {
        int major = version & 0xFFFF;
        int minor = version >>> 16;
        buf.setLength(0);
        buf.append("// class version ").append(major).append('.').append(minor)
                .append(" (").append(version).append(")\n");
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            buf.append("// DEPRECATED\n");
        }
        buf.append("// access flags 0x")
                .append(Integer.toHexString(access).toUpperCase()).append('\n');

        appendDescriptor(CLASS_SIGNATURE, signature);
        if (signature != null) {
            TraceSignatureVisitor sv = new TraceSignatureVisitor(access);
            SignatureReader r = new SignatureReader(signature);
            r.accept(sv);
            buf.append("// declaration: ").append(name)
                    .append(sv.getDeclaration()).append('\n');
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
    }

    @Override
    public void visitSource(final String file, final String debug) {
        buf.setLength(0);
        if (file != null) {
            buf.append(tab).append("// compiled from: ").append(file)
                    .append('\n');
        }
        if (debug != null) {
            buf.append(tab).append("// debug info: ").append(debug)
                    .append('\n');
        }
        if (buf.length() > 0) {
            text.add(buf.toString());
        }
    }

    @Override
    public void visitOuterClass(final String owner, final String name,
            final String desc) {
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
    }

    @Override
    public Textifier visitClassAnnotation(final String desc,
            final boolean visible) {
        text.add("\n");
        return visitAnnotation(desc, visible);
    }

    @Override
    public void visitClassAttribute(final Attribute attr) {
        text.add("\n");
        visitAttribute(attr);
    }

    @Override
    public void visitInnerClass(final String name, final String outerName,
            final String innerName, final int access) {
        buf.setLength(0);
        buf.append(tab).append("// access flags 0x");
        buf.append(
                Integer.toHexString(access & ~Opcodes.ACC_SUPER).toUpperCase())
                .append('\n');
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
    }

    @Override
    public Textifier visitField(final int access, final String name,
            final String desc, final String signature, final Object value) {
        buf.setLength(0);
        buf.append('\n');
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            buf.append(tab).append("// DEPRECATED\n");
        }
        buf.append(tab).append("// access flags 0x")
                .append(Integer.toHexString(access).toUpperCase()).append('\n');
        if (signature != null) {
            buf.append(tab);
            appendDescriptor(FIELD_SIGNATURE, signature);

            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            buf.append(tab).append("// declaration: ")
                    .append(sv.getDeclaration()).append('\n');
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

        Textifier t = createTextifier();
        text.add(t.getText());
        return t;
    }

    @Override
    public Textifier visitMethod(final int access, final String name,
            final String desc, final String signature, final String[] exceptions) {
        buf.setLength(0);
        buf.append('\n');
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            buf.append(tab).append("// DEPRECATED\n");
        }
        buf.append(tab).append("// access flags 0x")
                .append(Integer.toHexString(access).toUpperCase()).append('\n');

        if (signature != null) {
            buf.append(tab);
            appendDescriptor(METHOD_SIGNATURE, signature);

            TraceSignatureVisitor v = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.accept(v);
            String genericDecl = v.getDeclaration();
            String genericReturn = v.getReturnType();
            String genericExceptions = v.getExceptions();

            buf.append(tab).append("// declaration: ").append(genericReturn)
                    .append(' ').append(name).append(genericDecl);
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

        Textifier t = createTextifier();
        text.add(t.getText());
        return t;
    }

    @Override
    public void visitClassEnd() {
        text.add("}\n");
    }

    // ------------------------------------------------------------------------
    // Annotations
    // ------------------------------------------------------------------------

    @Override
    public void visit(final String name, final Object value) {
        buf.setLength(0);
        appendComa(valueNumber++);

        if (name != null) {
            buf.append(name).append('=');
        }

        if (value instanceof String) {
            visitString((String) value);
        } else if (value instanceof Type) {
            visitType((Type) value);
        } else if (value instanceof Byte) {
            visitByte(((Byte) value).byteValue());
        } else if (value instanceof Boolean) {
            visitBoolean(((Boolean) value).booleanValue());
        } else if (value instanceof Short) {
            visitShort(((Short) value).shortValue());
        } else if (value instanceof Character) {
            visitChar(((Character) value).charValue());
        } else if (value instanceof Integer) {
            visitInt(((Integer) value).intValue());
        } else if (value instanceof Float) {
            visitFloat(((Float) value).floatValue());
        } else if (value instanceof Long) {
            visitLong(((Long) value).longValue());
        } else if (value instanceof Double) {
            visitDouble(((Double) value).doubleValue());
        } else if (value.getClass().isArray()) {
            buf.append('{');
            if (value instanceof byte[]) {
                byte[] v = (byte[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitByte(v[i]);
                }
            } else if (value instanceof boolean[]) {
                boolean[] v = (boolean[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitBoolean(v[i]);
                }
            } else if (value instanceof short[]) {
                short[] v = (short[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitShort(v[i]);
                }
            } else if (value instanceof char[]) {
                char[] v = (char[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitChar(v[i]);
                }
            } else if (value instanceof int[]) {
                int[] v = (int[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitInt(v[i]);
                }
            } else if (value instanceof long[]) {
                long[] v = (long[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitLong(v[i]);
                }
            } else if (value instanceof float[]) {
                float[] v = (float[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitFloat(v[i]);
                }
            } else if (value instanceof double[]) {
                double[] v = (double[]) value;
                for (int i = 0; i < v.length; i++) {
                    appendComa(i);
                    visitDouble(v[i]);
                }
            }
            buf.append('}');
        }

        text.add(buf.toString());
    }

    private void visitInt(final int value) {
        buf.append(value);
    }

    private void visitLong(final long value) {
        buf.append(value).append('L');
    }

    private void visitFloat(final float value) {
        buf.append(value).append('F');
    }

    private void visitDouble(final double value) {
        buf.append(value).append('D');
    }

    private void visitChar(final char value) {
        buf.append("(char)").append((int) value);
    }

    private void visitShort(final short value) {
        buf.append("(short)").append(value);
    }

    private void visitByte(final byte value) {
        buf.append("(byte)").append(value);
    }

    private void visitBoolean(final boolean value) {
        buf.append(value);
    }

    private void visitString(final String value) {
        appendString(buf, value);
    }

    private void visitType(final Type value) {
        buf.append(value.getClassName()).append(".class");
    }

    @Override
    public void visitEnum(final String name, final String desc,
            final String value) {
        buf.setLength(0);
        appendComa(valueNumber++);
        if (name != null) {
            buf.append(name).append('=');
        }
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append('.').append(value);
        text.add(buf.toString());
    }

    @Override
    public Textifier visitAnnotation(final String name, final String desc) {
        buf.setLength(0);
        appendComa(valueNumber++);
        if (name != null) {
            buf.append(name).append('=');
        }
        buf.append('@');
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append('(');
        text.add(buf.toString());
        Textifier t = createTextifier();
        text.add(t.getText());
        text.add(")");
        return t;
    }

    @Override
    public Textifier visitArray(final String name) {
        buf.setLength(0);
        appendComa(valueNumber++);
        if (name != null) {
            buf.append(name).append('=');
        }
        buf.append('{');
        text.add(buf.toString());
        Textifier t = createTextifier();
        text.add(t.getText());
        text.add("}");
        return t;
    }

    @Override
    public void visitAnnotationEnd() {
    }

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    @Override
    public Textifier visitFieldAnnotation(final String desc,
            final boolean visible) {
        return visitAnnotation(desc, visible);
    }

    @Override
    public void visitFieldAttribute(final Attribute attr) {
        visitAttribute(attr);
    }

    @Override
    public void visitFieldEnd() {
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

    @Override
    public Textifier visitAnnotationDefault() {
        text.add(tab2 + "default=");
        Textifier t = createTextifier();
        text.add(t.getText());
        text.add("\n");
        return t;
    }

    @Override
    public Textifier visitMethodAnnotation(final String desc,
            final boolean visible) {
        return visitAnnotation(desc, visible);
    }

    @Override
    public Textifier visitParameterAnnotation(final int parameter,
            final String desc, final boolean visible) {
        buf.setLength(0);
        buf.append(tab2).append('@');
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append('(');
        text.add(buf.toString());
        Textifier t = createTextifier();
        text.add(t.getText());
        text.add(visible ? ") // parameter " : ") // invisible, parameter ");
        text.add(new Integer(parameter));
        text.add("\n");
        return t;
    }

    @Override
    public void visitMethodAttribute(final Attribute attr) {
        buf.setLength(0);
        buf.append(tab).append("ATTRIBUTE ");
        appendDescriptor(-1, attr.type);

        if (attr instanceof Textifiable) {
            ((Textifiable) attr).textify(buf, labelNames);
        } else {
            buf.append(" : unknown\n");
        }

        text.add(buf.toString());
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(final int type, final int nLocal,
            final Object[] local, final int nStack, final Object[] stack) {
        buf.setLength(0);
        buf.append(ltab);
        buf.append("FRAME ");
        switch (type) {
        case Opcodes.F_NEW:
        case Opcodes.F_FULL:
            buf.append("FULL [");
            appendFrameTypes(nLocal, local);
            buf.append("] [");
            appendFrameTypes(nStack, stack);
            buf.append(']');
            break;
        case Opcodes.F_APPEND:
            buf.append("APPEND [");
            appendFrameTypes(nLocal, local);
            buf.append(']');
            break;
        case Opcodes.F_CHOP:
            buf.append("CHOP ").append(nLocal);
            break;
        case Opcodes.F_SAME:
            buf.append("SAME");
            break;
        case Opcodes.F_SAME1:
            buf.append("SAME1 ");
            appendFrameTypes(1, stack);
            break;
        }
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitInsn(final int opcode) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        buf.setLength(0);
        buf.append(tab2)
                .append(OPCODES[opcode])
                .append(' ')
                .append(opcode == Opcodes.NEWARRAY ? TYPES[operand] : Integer
                        .toString(operand)).append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ').append(var)
                .append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, type);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner,
            final String name, final String desc) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, owner);
        buf.append('.').append(name).append(" : ");
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner,
            final String name, final String desc) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, owner);
        buf.append('.').append(name).append(' ');
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
            Object... bsmArgs) {
        buf.setLength(0);
        buf.append(tab2).append("INVOKEDYNAMIC").append(' ');
        buf.append(name);
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        buf.append(" [");
        appendHandle(bsm);
        buf.append(tab3).append("// arguments:");
        if (bsmArgs.length == 0) {
            buf.append(" none");
        } else {
            buf.append('\n').append(tab3);
            for (int i = 0; i < bsmArgs.length; i++) {
                Object cst = bsmArgs[i];
                if (cst instanceof String) {
                    Printer.appendString(buf, (String) cst);
                } else if (cst instanceof Type) {
                    buf.append(((Type) cst).getDescriptor()).append(".class");
                } else if (cst instanceof Handle) {
                    appendHandle((Handle) cst);
                } else {
                    buf.append(cst);
                }
                buf.append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append('\n');
        buf.append(tab2).append("]\n");
        text.add(buf.toString());
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendLabel(label);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitLabel(final Label label) {
        buf.setLength(0);
        buf.append(ltab);
        appendLabel(label);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        buf.setLength(0);
        buf.append(tab2).append("LDC ");
        if (cst instanceof String) {
            Printer.appendString(buf, (String) cst);
        } else if (cst instanceof Type) {
            buf.append(((Type) cst).getDescriptor()).append(".class");
        } else {
            buf.append(cst);
        }
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        buf.setLength(0);
        buf.append(tab2).append("IINC ").append(var).append(' ')
                .append(increment).append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitTableSwitchInsn(final int min, final int max,
            final Label dflt, final Label... labels) {
        buf.setLength(0);
        buf.append(tab2).append("TABLESWITCH\n");
        for (int i = 0; i < labels.length; ++i) {
            buf.append(tab3).append(min + i).append(": ");
            appendLabel(labels[i]);
            buf.append('\n');
        }
        buf.append(tab3).append("default: ");
        appendLabel(dflt);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys,
            final Label[] labels) {
        buf.setLength(0);
        buf.append(tab2).append("LOOKUPSWITCH\n");
        for (int i = 0; i < labels.length; ++i) {
            buf.append(tab3).append(keys[i]).append(": ");
            appendLabel(labels[i]);
            buf.append('\n');
        }
        buf.append(tab3).append("default: ");
        appendLabel(dflt);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        buf.setLength(0);
        buf.append(tab2).append("MULTIANEWARRAY ");
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append(' ').append(dims).append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitTryCatchBlock(final Label start, final Label end,
            final Label handler, final String type) {
        buf.setLength(0);
        buf.append(tab2).append("TRYCATCHBLOCK ");
        appendLabel(start);
        buf.append(' ');
        appendLabel(end);
        buf.append(' ');
        appendLabel(handler);
        buf.append(' ');
        appendDescriptor(INTERNAL_NAME, type);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitLocalVariable(final String name, final String desc,
            final String signature, final Label start, final Label end,
            final int index) {
        buf.setLength(0);
        buf.append(tab2).append("LOCALVARIABLE ").append(name).append(' ');
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append(' ');
        appendLabel(start);
        buf.append(' ');
        appendLabel(end);
        buf.append(' ').append(index).append('\n');

        if (signature != null) {
            buf.append(tab2);
            appendDescriptor(FIELD_SIGNATURE, signature);

            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            buf.append(tab2).append("// declaration: ")
                    .append(sv.getDeclaration()).append('\n');
        }
        text.add(buf.toString());
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        buf.setLength(0);
        buf.append(tab2).append("LINENUMBER ").append(line).append(' ');
        appendLabel(start);
        buf.append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        buf.setLength(0);
        buf.append(tab2).append("MAXSTACK = ").append(maxStack).append('\n');
        text.add(buf.toString());

        buf.setLength(0);
        buf.append(tab2).append("MAXLOCALS = ").append(maxLocals).append('\n');
        text.add(buf.toString());
    }

    @Override
    public void visitMethodEnd() {
    }

    // ------------------------------------------------------------------------
    // Common methods
    // ------------------------------------------------------------------------

    /**
     * Prints a disassembled view of the given annotation.
     * 
     * @param desc
     *            the class descriptor of the annotation class.
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values.
     */
    public Textifier visitAnnotation(final String desc, final boolean visible) {
        buf.setLength(0);
        buf.append(tab).append('@');
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append('(');
        text.add(buf.toString());
        Textifier t = createTextifier();
        text.add(t.getText());
        text.add(visible ? ")\n" : ") // invisible\n");
        return t;
    }

    /**
     * Prints a disassembled view of the given attribute.
     * 
     * @param attr
     *            an attribute.
     */
    public void visitAttribute(final Attribute attr) {
        buf.setLength(0);
        buf.append(tab).append("ATTRIBUTE ");
        appendDescriptor(-1, attr.type);

        if (attr instanceof Textifiable) {
            ((Textifiable) attr).textify(buf, null);
        } else {
            buf.append(" : unknown\n");
        }

        text.add(buf.toString());
    }

    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------

    /**
     * Creates a new TraceVisitor instance.
     * 
     * @return a new TraceVisitor.
     */
    protected Textifier createTextifier() {
        return new Textifier();
    }

    /**
     * Appends an internal name, a type descriptor or a type signature to
     * {@link #buf buf}.
     * 
     * @param type
     *            indicates if desc is an internal name, a field descriptor, a
     *            method descriptor, a class signature, ...
     * @param desc
     *            an internal name, type descriptor, or type signature. May be
     *            <tt>null</tt>.
     */
    protected void appendDescriptor(final int type, final String desc) {
        if (type == CLASS_SIGNATURE || type == FIELD_SIGNATURE
                || type == METHOD_SIGNATURE) {
            if (desc != null) {
                buf.append("// signature ").append(desc).append('\n');
            }
        } else {
            buf.append(desc);
        }
    }

    /**
     * Appends the name of the given label to {@link #buf buf}. Creates a new
     * label name if the given label does not yet have one.
     * 
     * @param l
     *            a label.
     */
    protected void appendLabel(final Label l) {
        if (labelNames == null) {
            labelNames = new HashMap<Label, String>();
        }
        String name = labelNames.get(l);
        if (name == null) {
            name = "L" + labelNames.size();
            labelNames.put(l, name);
        }
        buf.append(name);
    }

    /**
     * Appends the information about the given handle to {@link #buf buf}.
     * 
     * @param h
     *            a handle, non null.
     */
    protected void appendHandle(final Handle h) {
        buf.append('\n').append(tab3);
        int tag = h.getTag();
        buf.append("// handle kind 0x").append(Integer.toHexString(tag))
                .append(" : ");
        switch (tag) {
        case Opcodes.H_GETFIELD:
            buf.append("GETFIELD");
            break;
        case Opcodes.H_GETSTATIC:
            buf.append("GETSTATIC");
            break;
        case Opcodes.H_PUTFIELD:
            buf.append("PUTFIELD");
            break;
        case Opcodes.H_PUTSTATIC:
            buf.append("PUTSTATIC");
            break;
        case Opcodes.H_INVOKEINTERFACE:
            buf.append("INVOKEINTERFACE");
            break;
        case Opcodes.H_INVOKESPECIAL:
            buf.append("INVOKESPECIAL");
            break;
        case Opcodes.H_INVOKESTATIC:
            buf.append("INVOKESTATIC");
            break;
        case Opcodes.H_INVOKEVIRTUAL:
            buf.append("INVOKEVIRTUAL");
            break;
        case Opcodes.H_NEWINVOKESPECIAL:
            buf.append("NEWINVOKESPECIAL");
            break;
        }
        buf.append('\n');
        buf.append(tab3);
        appendDescriptor(INTERNAL_NAME, h.getOwner());
        buf.append('.');
        buf.append(h.getName());
        buf.append('(');
        appendDescriptor(HANDLE_DESCRIPTOR, h.getDesc());
        buf.append(')').append('\n');
    }

    /**
     * Appends a string representation of the given access modifiers to
     * {@link #buf buf}.
     * 
     * @param access
     *            some access modifiers.
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
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            buf.append("synthetic ");
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            buf.append("enum ");
        }
    }

    private void appendComa(final int i) {
        if (i != 0) {
            buf.append(", ");
        }
    }

    private void appendFrameTypes(final int n, final Object[] o) {
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                buf.append(' ');
            }
            if (o[i] instanceof String) {
                String desc = (String) o[i];
                if (desc.startsWith("[")) {
                    appendDescriptor(FIELD_DESCRIPTOR, desc);
                } else {
                    appendDescriptor(INTERNAL_NAME, desc);
                }
            } else if (o[i] instanceof Integer) {
                switch (((Integer) o[i]).intValue()) {
                case 0:
                    appendDescriptor(FIELD_DESCRIPTOR, "T");
                    break;
                case 1:
                    appendDescriptor(FIELD_DESCRIPTOR, "I");
                    break;
                case 2:
                    appendDescriptor(FIELD_DESCRIPTOR, "F");
                    break;
                case 3:
                    appendDescriptor(FIELD_DESCRIPTOR, "D");
                    break;
                case 4:
                    appendDescriptor(FIELD_DESCRIPTOR, "J");
                    break;
                case 5:
                    appendDescriptor(FIELD_DESCRIPTOR, "N");
                    break;
                case 6:
                    appendDescriptor(FIELD_DESCRIPTOR, "U");
                    break;
                }
            } else {
                appendLabel((Label) o[i]);
            }
        }
    }
}
