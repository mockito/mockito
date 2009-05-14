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

import java.util.ArrayList;
import java.util.List;

import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;
import org.mockito.asm.tree.AbstractInsnNode;
import org.mockito.asm.tree.IincInsnNode;
import org.mockito.asm.tree.MethodInsnNode;
import org.mockito.asm.tree.MultiANewArrayInsnNode;
import org.mockito.asm.tree.VarInsnNode;

/**
 * A symbolic execution stack frame. A stack frame contains a set of local
 * variable slots, and an operand stack. Warning: long and double values are
 * represented by <i>two</i> slots in local variables, and by <i>one</i> slot
 * in the operand stack.
 * 
 * @author Eric Bruneton
 */
public class Frame {

    /**
     * The local variables and operand stack of this frame.
     */
    private Value[] values;

    /**
     * The number of local variables of this frame.
     */
    private int locals;

    /**
     * The number of elements in the operand stack.
     */
    private int top;

    /**
     * Constructs a new frame with the given size.
     * 
     * @param nLocals the maximum number of local variables of the frame.
     * @param nStack the maximum stack size of the frame.
     */
    public Frame(final int nLocals, final int nStack) {
        this.values = new Value[nLocals + nStack];
        this.locals = nLocals;
    }

    /**
     * Constructs a new frame that is identical to the given frame.
     * 
     * @param src a frame.
     */
    public Frame(final Frame src) {
        this(src.locals, src.values.length - src.locals);
        init(src);
    }

    /**
     * Copies the state of the given frame into this frame.
     * 
     * @param src a frame.
     * @return this frame.
     */
    public Frame init(final Frame src) {
        System.arraycopy(src.values, 0, values, 0, values.length);
        top = src.top;
        return this;
    }

    /**
     * Returns the maximum number of local variables of this frame.
     * 
     * @return the maximum number of local variables of this frame.
     */
    public int getLocals() {
        return locals;
    }

    /**
     * Returns the value of the given local variable.
     * 
     * @param i a local variable index.
     * @return the value of the given local variable.
     * @throws IndexOutOfBoundsException if the variable does not exist.
     */
    public Value getLocal(final int i) throws IndexOutOfBoundsException {
        if (i >= locals) {
            throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
        }
        return values[i];
    }

    /**
     * Sets the value of the given local variable.
     * 
     * @param i a local variable index.
     * @param value the new value of this local variable.
     * @throws IndexOutOfBoundsException if the variable does not exist.
     */
    public void setLocal(final int i, final Value value)
            throws IndexOutOfBoundsException
    {
        if (i >= locals) {
            throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
        }
        values[i] = value;
    }

    /**
     * Returns the number of values in the operand stack of this frame. Long and
     * double values are treated as single values.
     * 
     * @return the number of values in the operand stack of this frame.
     */
    public int getStackSize() {
        return top;
    }

    /**
     * Returns the value of the given operand stack slot.
     * 
     * @param i the index of an operand stack slot.
     * @return the value of the given operand stack slot.
     * @throws IndexOutOfBoundsException if the operand stack slot does not
     *         exist.
     */
    public Value getStack(final int i) throws IndexOutOfBoundsException {
        return values[i + locals];
    }

    /**
     * Clears the operand stack of this frame.
     */
    public void clearStack() {
        top = 0;
    }

    /**
     * Pops a value from the operand stack of this frame.
     * 
     * @return the value that has been popped from the stack.
     * @throws IndexOutOfBoundsException if the operand stack is empty.
     */
    public Value pop() throws IndexOutOfBoundsException {
        if (top == 0) {
            throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
        }
        return values[--top + locals];
    }

    /**
     * Pushes a value into the operand stack of this frame.
     * 
     * @param value the value that must be pushed into the stack.
     * @throws IndexOutOfBoundsException if the operand stack is full.
     */
    public void push(final Value value) throws IndexOutOfBoundsException {
        if (top + locals >= values.length) {
            throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
        }
        values[top++ + locals] = value;
    }

    public void execute(
        final AbstractInsnNode insn,
        final Interpreter interpreter) throws AnalyzerException
    {
        Value value1, value2, value3, value4;
        List values;
        int var;

        switch (insn.getOpcode()) {
            case Opcodes.NOP:
                break;
            case Opcodes.ACONST_NULL:
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
            case Opcodes.LDC:
                push(interpreter.newOperation(insn));
                break;
            case Opcodes.ILOAD:
            case Opcodes.LLOAD:
            case Opcodes.FLOAD:
            case Opcodes.DLOAD:
            case Opcodes.ALOAD:
                push(interpreter.copyOperation(insn,
                        getLocal(((VarInsnNode) insn).var)));
                break;
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.FALOAD:
            case Opcodes.DALOAD:
            case Opcodes.AALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
                value2 = pop();
                value1 = pop();
                push(interpreter.binaryOperation(insn, value1, value2));
                break;
            case Opcodes.ISTORE:
            case Opcodes.LSTORE:
            case Opcodes.FSTORE:
            case Opcodes.DSTORE:
            case Opcodes.ASTORE:
                value1 = interpreter.copyOperation(insn, pop());
                var = ((VarInsnNode) insn).var;
                setLocal(var, value1);
                if (value1.getSize() == 2) {
                    setLocal(var + 1, interpreter.newValue(null));
                }
                if (var > 0) {
                    Value local = getLocal(var - 1);
                    if (local != null && local.getSize() == 2) {
                        setLocal(var - 1, interpreter.newValue(null));
                    }
                }
                break;
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                value3 = pop();
                value2 = pop();
                value1 = pop();
                interpreter.ternaryOperation(insn, value1, value2, value3);
                break;
            case Opcodes.POP:
                if (pop().getSize() == 2) {
                    throw new AnalyzerException("Illegal use of POP");
                }
                break;
            case Opcodes.POP2:
                if (pop().getSize() == 1) {
                    if (pop().getSize() != 1) {
                        throw new AnalyzerException("Illegal use of POP2");
                    }
                }
                break;
            case Opcodes.DUP:
                value1 = pop();
                if (value1.getSize() != 1) {
                    throw new AnalyzerException("Illegal use of DUP");
                }
                push(interpreter.copyOperation(insn, value1));
                push(interpreter.copyOperation(insn, value1));
                break;
            case Opcodes.DUP_X1:
                value1 = pop();
                value2 = pop();
                if (value1.getSize() != 1 || value2.getSize() != 1) {
                    throw new AnalyzerException("Illegal use of DUP_X1");
                }
                push(interpreter.copyOperation(insn, value1));
                push(interpreter.copyOperation(insn, value2));
                push(interpreter.copyOperation(insn, value1));
                break;
            case Opcodes.DUP_X2:
                value1 = pop();
                if (value1.getSize() == 1) {
                    value2 = pop();
                    if (value2.getSize() == 1) {
                        value3 = pop();
                        if (value3.getSize() == 1) {
                            push(interpreter.copyOperation(insn, value1));
                            push(interpreter.copyOperation(insn, value3));
                            push(interpreter.copyOperation(insn, value2));
                            push(interpreter.copyOperation(insn, value1));
                            break;
                        }
                    } else {
                        push(interpreter.copyOperation(insn, value1));
                        push(interpreter.copyOperation(insn, value2));
                        push(interpreter.copyOperation(insn, value1));
                        break;
                    }
                }
                throw new AnalyzerException("Illegal use of DUP_X2");
            case Opcodes.DUP2:
                value1 = pop();
                if (value1.getSize() == 1) {
                    value2 = pop();
                    if (value2.getSize() == 1) {
                        push(interpreter.copyOperation(insn, value2));
                        push(interpreter.copyOperation(insn, value1));
                        push(interpreter.copyOperation(insn, value2));
                        push(interpreter.copyOperation(insn, value1));
                        break;
                    }
                } else {
                    push(interpreter.copyOperation(insn, value1));
                    push(interpreter.copyOperation(insn, value1));
                    break;
                }
                throw new AnalyzerException("Illegal use of DUP2");
            case Opcodes.DUP2_X1:
                value1 = pop();
                if (value1.getSize() == 1) {
                    value2 = pop();
                    if (value2.getSize() == 1) {
                        value3 = pop();
                        if (value3.getSize() == 1) {
                            push(interpreter.copyOperation(insn, value2));
                            push(interpreter.copyOperation(insn, value1));
                            push(interpreter.copyOperation(insn, value3));
                            push(interpreter.copyOperation(insn, value2));
                            push(interpreter.copyOperation(insn, value1));
                            break;
                        }
                    }
                } else {
                    value2 = pop();
                    if (value2.getSize() == 1) {
                        push(interpreter.copyOperation(insn, value1));
                        push(interpreter.copyOperation(insn, value2));
                        push(interpreter.copyOperation(insn, value1));
                        break;
                    }
                }
                throw new AnalyzerException("Illegal use of DUP2_X1");
            case Opcodes.DUP2_X2:
                value1 = pop();
                if (value1.getSize() == 1) {
                    value2 = pop();
                    if (value2.getSize() == 1) {
                        value3 = pop();
                        if (value3.getSize() == 1) {
                            value4 = pop();
                            if (value4.getSize() == 1) {
                                push(interpreter.copyOperation(insn, value2));
                                push(interpreter.copyOperation(insn, value1));
                                push(interpreter.copyOperation(insn, value4));
                                push(interpreter.copyOperation(insn, value3));
                                push(interpreter.copyOperation(insn, value2));
                                push(interpreter.copyOperation(insn, value1));
                                break;
                            }
                        } else {
                            push(interpreter.copyOperation(insn, value2));
                            push(interpreter.copyOperation(insn, value1));
                            push(interpreter.copyOperation(insn, value3));
                            push(interpreter.copyOperation(insn, value2));
                            push(interpreter.copyOperation(insn, value1));
                            break;
                        }
                    }
                } else {
                    value2 = pop();
                    if (value2.getSize() == 1) {
                        value3 = pop();
                        if (value3.getSize() == 1) {
                            push(interpreter.copyOperation(insn, value1));
                            push(interpreter.copyOperation(insn, value3));
                            push(interpreter.copyOperation(insn, value2));
                            push(interpreter.copyOperation(insn, value1));
                            break;
                        }
                    } else {
                        push(interpreter.copyOperation(insn, value1));
                        push(interpreter.copyOperation(insn, value2));
                        push(interpreter.copyOperation(insn, value1));
                        break;
                    }
                }
                throw new AnalyzerException("Illegal use of DUP2_X2");
            case Opcodes.SWAP:
                value2 = pop();
                value1 = pop();
                if (value1.getSize() != 1 || value2.getSize() != 1) {
                    throw new AnalyzerException("Illegal use of SWAP");
                }
                push(interpreter.copyOperation(insn, value2));
                push(interpreter.copyOperation(insn, value1));
                break;
            case Opcodes.IADD:
            case Opcodes.LADD:
            case Opcodes.FADD:
            case Opcodes.DADD:
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
            case Opcodes.IMUL:
            case Opcodes.LMUL:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
            case Opcodes.IREM:
            case Opcodes.LREM:
            case Opcodes.FREM:
            case Opcodes.DREM:
                value2 = pop();
                value1 = pop();
                push(interpreter.binaryOperation(insn, value1, value2));
                break;
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
                push(interpreter.unaryOperation(insn, pop()));
                break;
            case Opcodes.ISHL:
            case Opcodes.LSHL:
            case Opcodes.ISHR:
            case Opcodes.LSHR:
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
            case Opcodes.IAND:
            case Opcodes.LAND:
            case Opcodes.IOR:
            case Opcodes.LOR:
            case Opcodes.IXOR:
            case Opcodes.LXOR:
                value2 = pop();
                value1 = pop();
                push(interpreter.binaryOperation(insn, value1, value2));
                break;
            case Opcodes.IINC:
                var = ((IincInsnNode) insn).var;
                setLocal(var, interpreter.unaryOperation(insn, getLocal(var)));
                break;
            case Opcodes.I2L:
            case Opcodes.I2F:
            case Opcodes.I2D:
            case Opcodes.L2I:
            case Opcodes.L2F:
            case Opcodes.L2D:
            case Opcodes.F2I:
            case Opcodes.F2L:
            case Opcodes.F2D:
            case Opcodes.D2I:
            case Opcodes.D2L:
            case Opcodes.D2F:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                push(interpreter.unaryOperation(insn, pop()));
                break;
            case Opcodes.LCMP:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                value2 = pop();
                value1 = pop();
                push(interpreter.binaryOperation(insn, value1, value2));
                break;
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
                interpreter.unaryOperation(insn, pop());
                break;
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
                value2 = pop();
                value1 = pop();
                interpreter.binaryOperation(insn, value1, value2);
                break;
            case Opcodes.GOTO:
                break;
            case Opcodes.JSR:
                push(interpreter.newOperation(insn));
                break;
            case Opcodes.RET:
                break;
            case Opcodes.TABLESWITCH:
            case Opcodes.LOOKUPSWITCH:
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
                interpreter.unaryOperation(insn, pop());
                break;
            case Opcodes.RETURN:
                break;
            case Opcodes.GETSTATIC:
                push(interpreter.newOperation(insn));
                break;
            case Opcodes.PUTSTATIC:
                interpreter.unaryOperation(insn, pop());
                break;
            case Opcodes.GETFIELD:
                push(interpreter.unaryOperation(insn, pop()));
                break;
            case Opcodes.PUTFIELD:
                value2 = pop();
                value1 = pop();
                interpreter.binaryOperation(insn, value1, value2);
                break;
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEINTERFACE:
                values = new ArrayList();
                String desc = ((MethodInsnNode) insn).desc;
                for (int i = Type.getArgumentTypes(desc).length; i > 0; --i) {
                    values.add(0, pop());
                }
                if (insn.getOpcode() != Opcodes.INVOKESTATIC) {
                    values.add(0, pop());
                }
                if (Type.getReturnType(desc) == Type.VOID_TYPE) {
                    interpreter.naryOperation(insn, values);
                } else {
                    push(interpreter.naryOperation(insn, values));
                }
                break;
            case Opcodes.NEW:
                push(interpreter.newOperation(insn));
                break;
            case Opcodes.NEWARRAY:
            case Opcodes.ANEWARRAY:
            case Opcodes.ARRAYLENGTH:
                push(interpreter.unaryOperation(insn, pop()));
                break;
            case Opcodes.ATHROW:
                interpreter.unaryOperation(insn, pop());
                break;
            case Opcodes.CHECKCAST:
            case Opcodes.INSTANCEOF:
                push(interpreter.unaryOperation(insn, pop()));
                break;
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
                interpreter.unaryOperation(insn, pop());
                break;
            case Opcodes.MULTIANEWARRAY:
                values = new ArrayList();
                for (int i = ((MultiANewArrayInsnNode) insn).dims; i > 0; --i) {
                    values.add(0, pop());
                }
                push(interpreter.naryOperation(insn, values));
                break;
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                interpreter.unaryOperation(insn, pop());
                break;
            default:
                throw new RuntimeException("Illegal opcode");
        }
    }

    /**
     * Merges this frame with the given frame.
     * 
     * @param frame a frame.
     * @param interpreter the interpreter used to merge values.
     * @return <tt>true</tt> if this frame has been changed as a result of the
     *         merge operation, or <tt>false</tt> otherwise.
     * @throws AnalyzerException if the frames have incompatible sizes.
     */
    public boolean merge(final Frame frame, final Interpreter interpreter)
            throws AnalyzerException
    {
        if (top != frame.top) {
            throw new AnalyzerException("Incompatible stack heights");
        }
        boolean changes = false;
        for (int i = 0; i < locals + top; ++i) {
            Value v = interpreter.merge(values[i], frame.values[i]);
            if (v != values[i]) {
                values[i] = v;
                changes |= true;
            }
        }
        return changes;
    }

    /**
     * Merges this frame with the given frame (case of a RET instruction).
     * 
     * @param frame a frame
     * @param access the local variables that have been accessed by the
     *        subroutine to which the RET instruction corresponds.
     * @return <tt>true</tt> if this frame has been changed as a result of the
     *         merge operation, or <tt>false</tt> otherwise.
     */
    public boolean merge(final Frame frame, final boolean[] access) {
        boolean changes = false;
        for (int i = 0; i < locals; ++i) {
            if (!access[i] && !values[i].equals(frame.values[i])) {
                values[i] = frame.values[i];
                changes = true;
            }
        }
        return changes;
    }

    /**
     * Returns a string representation of this frame.
     * 
     * @return a string representation of this frame.
     */
    public String toString() {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < getLocals(); ++i) {
            b.append(getLocal(i));
        }
        b.append(' ');
        for (int i = 0; i < getStackSize(); ++i) {
            b.append(getStack(i).toString());
        }
        return b.toString();
    }
}
