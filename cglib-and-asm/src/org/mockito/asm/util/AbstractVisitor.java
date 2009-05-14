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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.mockito.asm.Attribute;

/**
 * An abstract visitor.
 * 
 * @author Eric Bruneton
 */
public abstract class AbstractVisitor {

    /**
     * The names of the Java Virtual Machine opcodes.
     */
    public static final String[] OPCODES;
    /**
     * Types for <code>operand</code> parameter of the
     * {@link org.mockito.asm.MethodVisitor#visitIntInsn} method when
     * <code>opcode</code> is <code>NEWARRAY</code>.
     */
    public static final String[] TYPES;

    static {
        String s = "NOP,ACONST_NULL,ICONST_M1,ICONST_0,ICONST_1,ICONST_2,"
                + "ICONST_3,ICONST_4,ICONST_5,LCONST_0,LCONST_1,FCONST_0,"
                + "FCONST_1,FCONST_2,DCONST_0,DCONST_1,BIPUSH,SIPUSH,LDC,,,"
                + "ILOAD,LLOAD,FLOAD,DLOAD,ALOAD,,,,,,,,,,,,,,,,,,,,,IALOAD,"
                + "LALOAD,FALOAD,DALOAD,AALOAD,BALOAD,CALOAD,SALOAD,ISTORE,"
                + "LSTORE,FSTORE,DSTORE,ASTORE,,,,,,,,,,,,,,,,,,,,,IASTORE,"
                + "LASTORE,FASTORE,DASTORE,AASTORE,BASTORE,CASTORE,SASTORE,POP,"
                + "POP2,DUP,DUP_X1,DUP_X2,DUP2,DUP2_X1,DUP2_X2,SWAP,IADD,LADD,"
                + "FADD,DADD,ISUB,LSUB,FSUB,DSUB,IMUL,LMUL,FMUL,DMUL,IDIV,LDIV,"
                + "FDIV,DDIV,IREM,LREM,FREM,DREM,INEG,LNEG,FNEG,DNEG,ISHL,LSHL,"
                + "ISHR,LSHR,IUSHR,LUSHR,IAND,LAND,IOR,LOR,IXOR,LXOR,IINC,I2L,"
                + "I2F,I2D,L2I,L2F,L2D,F2I,F2L,F2D,D2I,D2L,D2F,I2B,I2C,I2S,LCMP,"
                + "FCMPL,FCMPG,DCMPL,DCMPG,IFEQ,IFNE,IFLT,IFGE,IFGT,IFLE,"
                + "IF_ICMPEQ,IF_ICMPNE,IF_ICMPLT,IF_ICMPGE,IF_ICMPGT,IF_ICMPLE,"
                + "IF_ACMPEQ,IF_ACMPNE,GOTO,JSR,RET,TABLESWITCH,LOOKUPSWITCH,"
                + "IRETURN,LRETURN,FRETURN,DRETURN,ARETURN,RETURN,GETSTATIC,"
                + "PUTSTATIC,GETFIELD,PUTFIELD,INVOKEVIRTUAL,INVOKESPECIAL,"
                + "INVOKESTATIC,INVOKEINTERFACE,,NEW,NEWARRAY,ANEWARRAY,"
                + "ARRAYLENGTH,ATHROW,CHECKCAST,INSTANCEOF,MONITORENTER,"
                + "MONITOREXIT,,MULTIANEWARRAY,IFNULL,IFNONNULL,";
        OPCODES = new String[200];
        int i = 0;
        int j = 0;
        int l;
        while ((l = s.indexOf(',', j)) > 0) {
            OPCODES[i++] = j + 1 == l ? null : s.substring(j, l);
            j = l + 1;
        }

        s = "T_BOOLEAN,T_CHAR,T_FLOAT,T_DOUBLE,T_BYTE,T_SHORT,T_INT,T_LONG,";
        TYPES = new String[12];
        j = 0;
        i = 4;
        while ((l = s.indexOf(',', j)) > 0) {
            TYPES[i++] = s.substring(j, l);
            j = l + 1;
        }
    }

    /**
     * The text to be printed. Since the code of methods is not necessarily
     * visited in sequential order, one method after the other, but can be
     * interlaced (some instructions from method one, then some instructions
     * from method two, then some instructions from method one again...), it is
     * not possible to print the visited instructions directly to a sequential
     * stream. A class is therefore printed in a two steps process: a string
     * tree is constructed during the visit, and printed to a sequential stream
     * at the end of the visit. This string tree is stored in this field, as a
     * string list that can contain other string lists, which can themselves
     * contain other string lists, and so on.
     */
    public final List text;

    /**
     * A buffer that can be used to create strings.
     */
    protected final StringBuffer buf;

    /**
     * Constructs a new {@link AbstractVisitor}.
     */
    protected AbstractVisitor() {
        this.text = new ArrayList();
        this.buf = new StringBuffer();
    }

    /**
     * Returns the text constructed by this visitor.
     * 
     * @return the text constructed by this visitor.
     */
    public List getText() {
        return text;
    }

    /**
     * Prints the text constructed by this visitor.
     * 
     * @param pw the print writer to be used.
     */
    public void print(final PrintWriter pw) {
        printList(pw, text);
    }
    
    /**
     * Appends a quoted string to a given buffer.
     * 
     * @param buf the buffer where the string must be added.
     * @param s the string to be added.
     */
    public static void appendString(final StringBuffer buf, final String s) {
        buf.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '\n') {
                buf.append("\\n");
            } else if (c == '\r') {
                buf.append("\\r");
            } else if (c == '\\') {
                buf.append("\\\\");
            } else if (c == '"') {
                buf.append("\\\"");
            } else if (c < 0x20 || c > 0x7f) {
                buf.append("\\u");
                if (c < 0x10) {
                    buf.append("000");
                } else if (c < 0x100) {
                    buf.append("00");
                } else if (c < 0x1000) {
                    buf.append('0');
                }
                buf.append(Integer.toString(c, 16));
            } else {
                buf.append(c);
            }
        }
        buf.append('\"');
    }

    /**
     * Prints the given string tree.
     * 
     * @param pw the writer to be used to print the tree.
     * @param l a string tree, i.e., a string list that can contain other string
     *        lists, and so on recursively.
     */
    static void printList(final PrintWriter pw, final List l) {
        for (int i = 0; i < l.size(); ++i) {
            Object o = l.get(i);
            if (o instanceof List) {
                printList(pw, (List) o);
            } else {
                pw.print(o.toString());
            }
        }
    }

    /**
     * Returns the default {@link ASMifiable} prototypes.
     * 
     * @return the default {@link ASMifiable} prototypes.
     */
    public static Attribute[] getDefaultAttributes() {
        return new Attribute[0];
    }
}
