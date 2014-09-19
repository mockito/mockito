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
package org.mockito.asm.optimizer;

import java.util.HashMap;

import org.mockito.asm.Handle;
import org.mockito.asm.Opcodes;
import org.mockito.asm.Type;

/**
 * A constant pool.
 * 
 * @author Eric Bruneton
 */
public class ConstantPool extends HashMap<Constant, Constant> {

    private final Constant key1 = new Constant();

    private final Constant key2 = new Constant();

    private final Constant key3 = new Constant();

    private final Constant key4 = new Constant();

    private final Constant key5 = new Constant();

    public Constant newInteger(final int value) {
        key1.set(value);
        Constant result = get(key1);
        if (result == null) {
            result = new Constant(key1);
            put(result);
        }
        return result;
    }

    public Constant newFloat(final float value) {
        key1.set(value);
        Constant result = get(key1);
        if (result == null) {
            result = new Constant(key1);
            put(result);
        }
        return result;
    }

    public Constant newLong(final long value) {
        key1.set(value);
        Constant result = get(key1);
        if (result == null) {
            result = new Constant(key1);
            put(result);
        }
        return result;
    }

    public Constant newDouble(final double value) {
        key1.set(value);
        Constant result = get(key1);
        if (result == null) {
            result = new Constant(key1);
            put(result);
        }
        return result;
    }

    public Constant newUTF8(final String value) {
        key1.set('s', value, null, null);
        Constant result = get(key1);
        if (result == null) {
            result = new Constant(key1);
            put(result);
        }
        return result;
    }

    private Constant newString(final String value) {
        key2.set('S', value, null, null);
        Constant result = get(key2);
        if (result == null) {
            newUTF8(value);
            result = new Constant(key2);
            put(result);
        }
        return result;
    }

    public Constant newClass(final String value) {
        key2.set('C', value, null, null);
        Constant result = get(key2);
        if (result == null) {
            newUTF8(value);
            result = new Constant(key2);
            put(result);
        }
        return result;
    }

    public Constant newMethodType(final String methodDescriptor) {
        key2.set('t', methodDescriptor, null, null);
        Constant result = get(key2);
        if (result == null) {
            newUTF8(methodDescriptor);
            result = new Constant(key2);
            put(result);
        }
        return result;
    }

    public Constant newHandle(final int tag, final String owner,
            final String name, final String desc) {
        key4.set((char) ('h' - 1 + tag), owner, name, desc);
        Constant result = get(key4);
        if (result == null) {
            if (tag <= Opcodes.H_PUTSTATIC) {
                newField(owner, name, desc);
            } else {
                newMethod(owner, name, desc, tag == Opcodes.H_INVOKEINTERFACE);
            }
            result = new Constant(key4);
            put(result);
        }
        return result;
    }

    public Constant newConst(final Object cst) {
        if (cst instanceof Integer) {
            int val = ((Integer) cst).intValue();
            return newInteger(val);
        } else if (cst instanceof Float) {
            float val = ((Float) cst).floatValue();
            return newFloat(val);
        } else if (cst instanceof Long) {
            long val = ((Long) cst).longValue();
            return newLong(val);
        } else if (cst instanceof Double) {
            double val = ((Double) cst).doubleValue();
            return newDouble(val);
        } else if (cst instanceof String) {
            return newString((String) cst);
        } else if (cst instanceof Type) {
            Type t = (Type) cst;
            int s = t.getSort();
            if (s == Type.OBJECT) {
                return newClass(t.getInternalName());
            } else if (s == Type.METHOD) {
                return newMethodType(t.getDescriptor());
            } else { // s == primitive type or array
                return newClass(t.getDescriptor());
            }
        } else if (cst instanceof Handle) {
            Handle h = (Handle) cst;
            return newHandle(h.getTag(), h.getOwner(), h.getName(), h.getDesc());
        } else {
            throw new IllegalArgumentException("value " + cst);
        }
    }

    public Constant newField(final String owner, final String name,
            final String desc) {
        key3.set('G', owner, name, desc);
        Constant result = get(key3);
        if (result == null) {
            newClass(owner);
            newNameType(name, desc);
            result = new Constant(key3);
            put(result);
        }
        return result;
    }

    public Constant newMethod(final String owner, final String name,
            final String desc, final boolean itf) {
        key3.set(itf ? 'N' : 'M', owner, name, desc);
        Constant result = get(key3);
        if (result == null) {
            newClass(owner);
            newNameType(name, desc);
            result = new Constant(key3);
            put(result);
        }
        return result;
    }

    public Constant newInvokeDynamic(String name, String desc, Handle bsm,
            Object... bsmArgs) {
        key5.set(name, desc, bsm, bsmArgs);
        Constant result = get(key5);
        if (result == null) {
            newNameType(name, desc);
            newHandle(bsm.getTag(), bsm.getOwner(), bsm.getName(),
                    bsm.getDesc());
            for (int i = 0; i < bsmArgs.length; i++) {
                newConst(bsmArgs[i]);
            }
            result = new Constant(key5);
            put(result);
        }
        return result;
    }

    public Constant newNameType(final String name, final String desc) {
        key2.set('T', name, desc, null);
        Constant result = get(key2);
        if (result == null) {
            newUTF8(name);
            newUTF8(desc);
            result = new Constant(key2);
            put(result);
        }
        return result;
    }

    private Constant get(final Constant key) {
        return get((Object) key);
    }

    private void put(final Constant cst) {
        put(cst, cst);
    }
}
