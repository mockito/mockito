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
package org.mockito.asm.tree.analysis;

import java.util.List;

import org.mockito.asm.Type;
import org.mockito.asm.tree.AbstractInsnNode;
import org.mockito.asm.tree.FieldInsnNode;
import org.mockito.asm.tree.MethodInsnNode;

/**
 * An extended {@link BasicInterpreter} that checks that bytecode instructions
 * are correctly used.
 * 
 * @author Eric Bruneton
 * @author Bing Ran
 */
public class BasicVerifier extends BasicInterpreter {

    public Value copyOperation(final AbstractInsnNode insn, final Value value)
            throws AnalyzerException
    {
        Value expected;
        switch (insn.getOpcode()) {
            case ILOAD:
            case ISTORE:
                expected = BasicValue.INT_VALUE;
                break;
            case FLOAD:
            case FSTORE:
                expected = BasicValue.FLOAT_VALUE;
                break;
            case LLOAD:
            case LSTORE:
                expected = BasicValue.LONG_VALUE;
                break;
            case DLOAD:
            case DSTORE:
                expected = BasicValue.DOUBLE_VALUE;
                break;
            case ALOAD:
                if (!((BasicValue) value).isReference()) {
                    throw new AnalyzerException(null,
                            "an object reference",
                            value);
                }
                return value;
            case ASTORE:
                if (!((BasicValue) value).isReference()
                        && value != BasicValue.RETURNADDRESS_VALUE)
                {
                    throw new AnalyzerException(null,
                            "an object reference or a return address",
                            value);
                }
                return value;
            default:
                return value;
        }
        // type is necessarily a primitive type here,
        // so value must be == to expected value
        if (value != expected) {
            throw new AnalyzerException(null, expected, value);
        }
        return value;
    }

    public Value unaryOperation(final AbstractInsnNode insn, final Value value)
            throws AnalyzerException
    {
        Value expected;
        switch (insn.getOpcode()) {
            case INEG:
            case IINC:
            case I2F:
            case I2L:
            case I2D:
            case I2B:
            case I2C:
            case I2S:
            case IFEQ:
            case IFNE:
            case IFLT:
            case IFGE:
            case IFGT:
            case IFLE:
            case TABLESWITCH:
            case LOOKUPSWITCH:
            case IRETURN:
            case NEWARRAY:
            case ANEWARRAY:
                expected = BasicValue.INT_VALUE;
                break;
            case FNEG:
            case F2I:
            case F2L:
            case F2D:
            case FRETURN:
                expected = BasicValue.FLOAT_VALUE;
                break;
            case LNEG:
            case L2I:
            case L2F:
            case L2D:
            case LRETURN:
                expected = BasicValue.LONG_VALUE;
                break;
            case DNEG:
            case D2I:
            case D2F:
            case D2L:
            case DRETURN:
                expected = BasicValue.DOUBLE_VALUE;
                break;
            case GETFIELD:
                expected = newValue(Type.getObjectType(((FieldInsnNode) insn).owner));
                break;
            case CHECKCAST:
                if (!((BasicValue) value).isReference()) {
                    throw new AnalyzerException(null,
                            "an object reference",
                            value);
                }
                return super.unaryOperation(insn, value);
            case ARRAYLENGTH:
                if (!isArrayValue(value)) {
                    throw new AnalyzerException(null,
                            "an array reference",
                            value);
                }
                return super.unaryOperation(insn, value);
            case ARETURN:
            case ATHROW:
            case INSTANCEOF:
            case MONITORENTER:
            case MONITOREXIT:
            case IFNULL:
            case IFNONNULL:
                if (!((BasicValue) value).isReference()) {
                    throw new AnalyzerException(null,
                            "an object reference",
                            value);
                }
                return super.unaryOperation(insn, value);
            case PUTSTATIC:
                expected = newValue(Type.getType(((FieldInsnNode) insn).desc));
                break;
            default:
                throw new Error("Internal error.");
        }
        if (!isSubTypeOf(value, expected)) {
            throw new AnalyzerException(null, expected, value);
        }
        return super.unaryOperation(insn, value);
    }

    public Value binaryOperation(
        final AbstractInsnNode insn,
        final Value value1,
        final Value value2) throws AnalyzerException
    {
        Value expected1;
        Value expected2;
        switch (insn.getOpcode()) {
            case IALOAD:
                expected1 = newValue(Type.getType("[I"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case BALOAD:
                if (isSubTypeOf(value1, newValue(Type.getType("[Z")))) {
                    expected1 = newValue(Type.getType("[Z"));
                } else {
                    expected1 = newValue(Type.getType("[B"));
                }
                expected2 = BasicValue.INT_VALUE;
                break;
            case CALOAD:
                expected1 = newValue(Type.getType("[C"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case SALOAD:
                expected1 = newValue(Type.getType("[S"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case LALOAD:
                expected1 = newValue(Type.getType("[J"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case FALOAD:
                expected1 = newValue(Type.getType("[F"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case DALOAD:
                expected1 = newValue(Type.getType("[D"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case AALOAD:
                expected1 = newValue(Type.getType("[Ljava/lang/Object;"));
                expected2 = BasicValue.INT_VALUE;
                break;
            case IADD:
            case ISUB:
            case IMUL:
            case IDIV:
            case IREM:
            case ISHL:
            case ISHR:
            case IUSHR:
            case IAND:
            case IOR:
            case IXOR:
            case IF_ICMPEQ:
            case IF_ICMPNE:
            case IF_ICMPLT:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
                expected1 = BasicValue.INT_VALUE;
                expected2 = BasicValue.INT_VALUE;
                break;
            case FADD:
            case FSUB:
            case FMUL:
            case FDIV:
            case FREM:
            case FCMPL:
            case FCMPG:
                expected1 = BasicValue.FLOAT_VALUE;
                expected2 = BasicValue.FLOAT_VALUE;
                break;
            case LADD:
            case LSUB:
            case LMUL:
            case LDIV:
            case LREM:
            case LAND:
            case LOR:
            case LXOR:
            case LCMP:
                expected1 = BasicValue.LONG_VALUE;
                expected2 = BasicValue.LONG_VALUE;
                break;
            case LSHL:
            case LSHR:
            case LUSHR:
                expected1 = BasicValue.LONG_VALUE;
                expected2 = BasicValue.INT_VALUE;
                break;
            case DADD:
            case DSUB:
            case DMUL:
            case DDIV:
            case DREM:
            case DCMPL:
            case DCMPG:
                expected1 = BasicValue.DOUBLE_VALUE;
                expected2 = BasicValue.DOUBLE_VALUE;
                break;
            case IF_ACMPEQ:
            case IF_ACMPNE:
                expected1 = BasicValue.REFERENCE_VALUE;
                expected2 = BasicValue.REFERENCE_VALUE;
                break;
            case PUTFIELD:
                FieldInsnNode fin = (FieldInsnNode) insn;
                expected1 = newValue(Type.getObjectType(fin.owner));
                expected2 = newValue(Type.getType(fin.desc));
                break;
            default:
                throw new Error("Internal error.");
        }
        if (!isSubTypeOf(value1, expected1)) {
            throw new AnalyzerException("First argument", expected1, value1);
        } else if (!isSubTypeOf(value2, expected2)) {
            throw new AnalyzerException("Second argument", expected2, value2);
        }
        if (insn.getOpcode() == AALOAD) {
            return getElementValue(value1);
        } else {
            return super.binaryOperation(insn, value1, value2);
        }
    }

    public Value ternaryOperation(
        final AbstractInsnNode insn,
        final Value value1,
        final Value value2,
        final Value value3) throws AnalyzerException
    {
        Value expected1;
        Value expected3;
        switch (insn.getOpcode()) {
            case IASTORE:
                expected1 = newValue(Type.getType("[I"));
                expected3 = BasicValue.INT_VALUE;
                break;
            case BASTORE:
                if (isSubTypeOf(value1, newValue(Type.getType("[Z")))) {
                    expected1 = newValue(Type.getType("[Z"));
                } else {
                    expected1 = newValue(Type.getType("[B"));
                }
                expected3 = BasicValue.INT_VALUE;
                break;
            case CASTORE:
                expected1 = newValue(Type.getType("[C"));
                expected3 = BasicValue.INT_VALUE;
                break;
            case SASTORE:
                expected1 = newValue(Type.getType("[S"));
                expected3 = BasicValue.INT_VALUE;
                break;
            case LASTORE:
                expected1 = newValue(Type.getType("[J"));
                expected3 = BasicValue.LONG_VALUE;
                break;
            case FASTORE:
                expected1 = newValue(Type.getType("[F"));
                expected3 = BasicValue.FLOAT_VALUE;
                break;
            case DASTORE:
                expected1 = newValue(Type.getType("[D"));
                expected3 = BasicValue.DOUBLE_VALUE;
                break;
            case AASTORE:
                expected1 = value1;
                expected3 = BasicValue.REFERENCE_VALUE;
                break;
            default:
                throw new Error("Internal error.");
        }
        if (!isSubTypeOf(value1, expected1)) {
            throw new AnalyzerException("First argument", "a " + expected1
                    + " array reference", value1);
        } else if (value2 != BasicValue.INT_VALUE) {
            throw new AnalyzerException("Second argument",
                    BasicValue.INT_VALUE,
                    value2);
        } else if (!isSubTypeOf(value3, expected3)) {
            throw new AnalyzerException("Third argument", expected3, value3);
        }
        return null;
    }

    public Value naryOperation(final AbstractInsnNode insn, final List values)
            throws AnalyzerException
    {
        int opcode = insn.getOpcode();
        if (opcode == MULTIANEWARRAY) {
            for (int i = 0; i < values.size(); ++i) {
                if (values.get(i) != BasicValue.INT_VALUE) {
                    throw new AnalyzerException(null,
                            BasicValue.INT_VALUE,
                            (Value) values.get(i));
                }
            }
        } else {
            int i = 0;
            int j = 0;
            if (opcode != INVOKESTATIC) {
                Type owner = Type.getObjectType(((MethodInsnNode) insn).owner);
                if (!isSubTypeOf((Value) values.get(i++), newValue(owner))) {
                    throw new AnalyzerException("Method owner",
                            newValue(owner),
                            (Value) values.get(0));
                }
            }
            Type[] args = Type.getArgumentTypes(((MethodInsnNode) insn).desc);
            while (i < values.size()) {
                Value expected = newValue(args[j++]);
                Value encountered = (Value) values.get(i++);
                if (!isSubTypeOf(encountered, expected)) {
                    throw new AnalyzerException("Argument " + j,
                            expected,
                            encountered);
                }
            }
        }
        return super.naryOperation(insn, values);
    }

    protected boolean isArrayValue(final Value value) {
        return ((BasicValue) value).isReference();
    }

    protected Value getElementValue(final Value objectArrayValue)
            throws AnalyzerException
    {
        return BasicValue.REFERENCE_VALUE;
    }

    protected boolean isSubTypeOf(final Value value, final Value expected) {
        return value == expected;
    }
}
