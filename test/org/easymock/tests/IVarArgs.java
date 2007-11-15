/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

public interface IVarArgs {
    public void withVarargsString(int value, String... s);

    public void withVarargsObject(int value, Object... o);

    public void withVarargsBoolean(int value, boolean... b);

    public void withVarargsByte(int value, byte... b);

    public void withVarargsChar(int value, char... c);

    public void withVarargsDouble(int value, double... d);

    public void withVarargsFloat(int value, float... f);

    public void withVarargsInt(int value, int... i);

    public void withVarargsLong(int value, long... l);

    public void withVarargsShort(int value, short... s);
}
