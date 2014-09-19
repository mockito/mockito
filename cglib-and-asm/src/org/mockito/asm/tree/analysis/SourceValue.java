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

import java.util.Set;

import org.mockito.asm.tree.AbstractInsnNode;

/**
 * A {@link Value} that is represented by its type in a two types type system.
 * This type system distinguishes the ONEWORD and TWOWORDS types.
 * 
 * @author Eric Bruneton
 */
public class SourceValue implements Value {

    /**
     * The size of this value.
     */
    public final int size;

    /**
     * The instructions that can produce this value. For example, for the Java
     * code below, the instructions that can produce the value of <tt>i</tt> at
     * line 5 are the txo ISTORE instructions at line 1 and 3:
     * 
     * <pre>
     * 1: i = 0;
     * 2: if (...) {
     * 3:   i = 1;
     * 4: }
     * 5: return i;
     * </pre>
     * 
     * This field is a set of {@link AbstractInsnNode} objects.
     */
    public final Set<AbstractInsnNode> insns;

    public SourceValue(final int size) {
        this(size, SmallSet.<AbstractInsnNode> emptySet());
    }

    public SourceValue(final int size, final AbstractInsnNode insn) {
        this.size = size;
        this.insns = new SmallSet<AbstractInsnNode>(insn, null);
    }

    public SourceValue(final int size, final Set<AbstractInsnNode> insns) {
        this.size = size;
        this.insns = insns;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(final Object value) {
        if (!(value instanceof SourceValue)) {
            return false;
        }
        SourceValue v = (SourceValue) value;
        return size == v.size && insns.equals(v.insns);
    }

    @Override
    public int hashCode() {
        return insns.hashCode();
    }
}
