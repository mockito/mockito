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
package org.mockito.asm.tree;

import java.util.Map;

import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;

/**
 * A node that represents an LDC instruction.
 * 
 * @author Eric Bruneton
 */
public class LdcInsnNode extends AbstractInsnNode {

    /**
     * The constant to be loaded on the stack. This parameter must be a non null
     * {@link Integer}, a {@link Float}, a {@link Long}, a {@link Double}, a
     * {@link String} or a {@link org.mockito.asm.Type}.
     */
    public Object cst;

    /**
     * Constructs a new {@link LdcInsnNode}.
     * 
     * @param cst
     *            the constant to be loaded on the stack. This parameter must be
     *            a non null {@link Integer}, a {@link Float}, a {@link Long}, a
     *            {@link Double} or a {@link String}.
     */
    public LdcInsnNode(final Object cst) {
        super(Opcodes.LDC);
        this.cst = cst;
    }

    @Override
    public int getType() {
        return LDC_INSN;
    }

    @Override
    public void accept(final MethodVisitor mv) {
        mv.visitLdcInsn(cst);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> labels) {
        return new LdcInsnNode(cst);
    }
}
