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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;
import org.mockito.asm.tree.AbstractInsnNode;
import org.mockito.asm.tree.FieldInsnNode;
import org.mockito.asm.tree.LdcInsnNode;
import org.mockito.asm.tree.MethodInsnNode;

/**
 * An {@link Interpreter} for {@link SourceValue} values.
 * 
 * @author Eric Bruneton
 */
public class SourceInterpreter implements Opcodes, Interpreter {

    public Value newValue(final Type type) {
        return new SourceValue(type == null ? 1 : type.getSize());
    }

    public Value newOperation(final AbstractInsnNode insn) {
        int size;
        switch (insn.getOpcode()) {
            case LCONST_0:
            case LCONST_1:
            case DCONST_0:
            case DCONST_1:
                size = 2;
                break;
            case LDC:
                Object cst = ((LdcInsnNode) insn).cst;
                size = cst instanceof Long || cst instanceof Double ? 2 : 1;
                break;
            case GETSTATIC:
                size = Type.getType(((FieldInsnNode) insn).desc).getSize();
                break;
            default:
                size = 1;
        }
        return new SourceValue(size, insn);
    }

    public Value copyOperation(final AbstractInsnNode insn, final Value value) {
        return new SourceValue(value.getSize(), insn);
    }

    public Value unaryOperation(final AbstractInsnNode insn, final Value value)
    {
        int size;
        switch (insn.getOpcode()) {
            case LNEG:
            case DNEG:
            case I2L:
            case I2D:
            case L2D:
            case F2L:
            case F2D:
            case D2L:
                size = 2;
                break;
            case GETFIELD:
                size = Type.getType(((FieldInsnNode) insn).desc).getSize();
                break;
            default:
                size = 1;
        }
        return new SourceValue(size, insn);
    }

    public Value binaryOperation(
        final AbstractInsnNode insn,
        final Value value1,
        final Value value2)
    {
        int size;
        switch (insn.getOpcode()) {
            case LALOAD:
            case DALOAD:
            case LADD:
            case DADD:
            case LSUB:
            case DSUB:
            case LMUL:
            case DMUL:
            case LDIV:
            case DDIV:
            case LREM:
            case DREM:
            case LSHL:
            case LSHR:
            case LUSHR:
            case LAND:
            case LOR:
            case LXOR:
                size = 2;
                break;
            default:
                size = 1;
        }
        return new SourceValue(size, insn);
    }

    public Value ternaryOperation(
        final AbstractInsnNode insn,
        final Value value1,
        final Value value2,
        final Value value3)
    {
        return new SourceValue(1, insn);
    }

    public Value naryOperation(final AbstractInsnNode insn, final List values) {
        int size;
        if (insn.getOpcode() == MULTIANEWARRAY) {
            size = 1;
        } else {
            size = Type.getReturnType(((MethodInsnNode) insn).desc).getSize();
        }
        return new SourceValue(size, insn);
    }

    public Value merge(final Value v, final Value w) {
        SourceValue dv = (SourceValue) v;
        SourceValue dw = (SourceValue) w;
        if (dv.insns instanceof SmallSet && dw.insns instanceof SmallSet) {
            Set s = ((SmallSet) dv.insns).union((SmallSet) dw.insns);
            if (s == dv.insns && dv.size == dw.size) {
                return v;
            } else {
                return new SourceValue(Math.min(dv.size, dw.size), s);
            }
        }
        if (dv.size != dw.size || !dv.insns.containsAll(dw.insns)) {
            Set s = new HashSet();
            s.addAll(dv.insns);
            s.addAll(dw.insns);
            return new SourceValue(Math.min(dv.size, dw.size), s);
        }
        return v;
    }
}
