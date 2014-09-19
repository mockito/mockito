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
package org.mockito.asm.tree.analysis;

import java.util.List;

import org.mockito.asm.Type;
import org.mockito.asm.tree.AbstractInsnNode;

/**
 * A semantic bytecode interpreter. More precisely, this interpreter only
 * manages the computation of values from other values: it does not manage the
 * transfer of values to or from the stack, and to or from the local variables.
 * This separation allows a generic bytecode {@link Analyzer} to work with
 * various semantic interpreters, without needing to duplicate the code to
 * simulate the transfer of values.
 * 
 * @param <V>
 *            type of the Value used for the analysis.
 * 
 * @author Eric Bruneton
 */
public abstract class Interpreter<V extends Value> {

    protected final int api;

    protected Interpreter(final int api) {
        this.api = api;
    }

    /**
     * Creates a new value that represents the given type.
     * 
     * Called for method parameters (including <code>this</code>), exception
     * handler variable and with <code>null</code> type for variables reserved
     * by long and double types.
     * 
     * @param type
     *            a primitive or reference type, or <tt>null</tt> to represent
     *            an uninitialized value.
     * @return a value that represents the given type. The size of the returned
     *         value must be equal to the size of the given type.
     */
    public abstract V newValue(Type type);

    /**
     * Interprets a bytecode instruction without arguments. This method is
     * called for the following opcodes:
     * 
     * ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4,
     * ICONST_5, LCONST_0, LCONST_1, FCONST_0, FCONST_1, FCONST_2, DCONST_0,
     * DCONST_1, BIPUSH, SIPUSH, LDC, JSR, GETSTATIC, NEW
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @return the result of the interpretation of the given instruction.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract V newOperation(AbstractInsnNode insn)
            throws AnalyzerException;

    /**
     * Interprets a bytecode instruction that moves a value on the stack or to
     * or from local variables. This method is called for the following opcodes:
     * 
     * ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE,
     * ASTORE, DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @param value
     *            the value that must be moved by the instruction.
     * @return the result of the interpretation of the given instruction. The
     *         returned value must be <tt>equal</tt> to the given value.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract V copyOperation(AbstractInsnNode insn, V value)
            throws AnalyzerException;

    /**
     * Interprets a bytecode instruction with a single argument. This method is
     * called for the following opcodes:
     * 
     * INEG, LNEG, FNEG, DNEG, IINC, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L,
     * F2D, D2I, D2L, D2F, I2B, I2C, I2S, IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE,
     * TABLESWITCH, LOOKUPSWITCH, IRETURN, LRETURN, FRETURN, DRETURN, ARETURN,
     * PUTSTATIC, GETFIELD, NEWARRAY, ANEWARRAY, ARRAYLENGTH, ATHROW, CHECKCAST,
     * INSTANCEOF, MONITORENTER, MONITOREXIT, IFNULL, IFNONNULL
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @param value
     *            the argument of the instruction to be interpreted.
     * @return the result of the interpretation of the given instruction.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract V unaryOperation(AbstractInsnNode insn, V value)
            throws AnalyzerException;

    /**
     * Interprets a bytecode instruction with two arguments. This method is
     * called for the following opcodes:
     * 
     * IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IADD,
     * LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV,
     * LDIV, FDIV, DDIV, IREM, LREM, FREM, DREM, ISHL, LSHL, ISHR, LSHR, IUSHR,
     * LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, LCMP, FCMPL, FCMPG, DCMPL,
     * DCMPG, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
     * IF_ACMPEQ, IF_ACMPNE, PUTFIELD
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @param value1
     *            the first argument of the instruction to be interpreted.
     * @param value2
     *            the second argument of the instruction to be interpreted.
     * @return the result of the interpretation of the given instruction.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract V binaryOperation(AbstractInsnNode insn, V value1, V value2)
            throws AnalyzerException;

    /**
     * Interprets a bytecode instruction with three arguments. This method is
     * called for the following opcodes:
     * 
     * IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @param value1
     *            the first argument of the instruction to be interpreted.
     * @param value2
     *            the second argument of the instruction to be interpreted.
     * @param value3
     *            the third argument of the instruction to be interpreted.
     * @return the result of the interpretation of the given instruction.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract V ternaryOperation(AbstractInsnNode insn, V value1,
            V value2, V value3) throws AnalyzerException;

    /**
     * Interprets a bytecode instruction with a variable number of arguments.
     * This method is called for the following opcodes:
     * 
     * INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE,
     * MULTIANEWARRAY and INVOKEDYNAMIC
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @param values
     *            the arguments of the instruction to be interpreted.
     * @return the result of the interpretation of the given instruction.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract V naryOperation(AbstractInsnNode insn,
            List<? extends V> values) throws AnalyzerException;

    /**
     * Interprets a bytecode return instruction. This method is called for the
     * following opcodes:
     * 
     * IRETURN, LRETURN, FRETURN, DRETURN, ARETURN
     * 
     * @param insn
     *            the bytecode instruction to be interpreted.
     * @param value
     *            the argument of the instruction to be interpreted.
     * @param expected
     *            the expected return type of the analyzed method.
     * @throws AnalyzerException
     *             if an error occured during the interpretation.
     */
    public abstract void returnOperation(AbstractInsnNode insn, V value,
            V expected) throws AnalyzerException;

    /**
     * Merges two values. The merge operation must return a value that
     * represents both values (for instance, if the two values are two types,
     * the merged value must be a common super type of the two types. If the two
     * values are integer intervals, the merged value must be an interval that
     * contains the previous ones. Likewise for other types of values).
     * 
     * @param v
     *            a value.
     * @param w
     *            another value.
     * @return the merged value. If the merged value is equal to <tt>v</tt>,
     *         this method <i>must</i> return <tt>v</tt>.
     */
    public abstract V merge(V v, V w);
}
