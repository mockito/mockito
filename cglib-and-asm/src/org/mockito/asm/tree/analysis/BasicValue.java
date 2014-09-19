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

import org.mockito.asm.Type;

/**
 * A {@link Value} that is represented by its type in a seven types type system.
 * This type system distinguishes the UNINITIALZED, INT, FLOAT, LONG, DOUBLE,
 * REFERENCE and RETURNADDRESS types.
 * 
 * @author Eric Bruneton
 */
public class BasicValue implements Value {

    public static final BasicValue UNINITIALIZED_VALUE = new BasicValue(null);

    public static final BasicValue INT_VALUE = new BasicValue(Type.INT_TYPE);

    public static final BasicValue FLOAT_VALUE = new BasicValue(Type.FLOAT_TYPE);

    public static final BasicValue LONG_VALUE = new BasicValue(Type.LONG_TYPE);

    public static final BasicValue DOUBLE_VALUE = new BasicValue(
            Type.DOUBLE_TYPE);

    public static final BasicValue REFERENCE_VALUE = new BasicValue(
            Type.getObjectType("java/lang/Object"));

    public static final BasicValue RETURNADDRESS_VALUE = new BasicValue(
            Type.VOID_TYPE);

    private final Type type;

    public BasicValue(final Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getSize() {
        return type == Type.LONG_TYPE || type == Type.DOUBLE_TYPE ? 2 : 1;
    }

    public boolean isReference() {
        return type != null
                && (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY);
    }

    @Override
    public boolean equals(final Object value) {
        if (value == this) {
            return true;
        } else if (value instanceof BasicValue) {
            if (type == null) {
                return ((BasicValue) value).type == null;
            } else {
                return type.equals(((BasicValue) value).type);
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return type == null ? 0 : type.hashCode();
    }

    @Override
    public String toString() {
        if (this == UNINITIALIZED_VALUE) {
            return ".";
        } else if (this == RETURNADDRESS_VALUE) {
            return "A";
        } else if (this == REFERENCE_VALUE) {
            return "R";
        } else {
            return type.getDescriptor();
        }
    }
}
