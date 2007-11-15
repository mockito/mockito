/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import java.io.IOException;

public interface IMethods {

    boolean booleanReturningMethod(int index);

    byte byteReturningMethod(int index);

    short shortReturningMethod(int index);

    char charReturningMethod(int index);

    int intReturningMethod(int index);

    long longReturningMethod(int index);

    float floatReturningMethod(int index);

    double doubleReturningMethod(int index);

    Object objectReturningMethod(int index);

    String oneArg(boolean value);

    String oneArg(byte value);

    String oneArg(short value);

    String oneArg(char value);

    String oneArg(int value);

    String oneArg(long value);

    String oneArg(float value);

    String oneArg(double value);

    String oneArg(Object value);

    String oneArg(String value);

    public String throwsNothing(boolean value);

    public String throwsIOException(int count) throws IOException;

    public String throwsError(int count) throws Error;

    void simpleMethod();

    void simpleMethodWithArgument(String argument);

    Object threeArgumentMethod(int valueOne, Object valueTwo, String valueThree);

    void twoArgumentMethod(int one, int two);

    void arrayMethod(String[] strings);

    String oneArray(boolean[] array);

    String oneArray(byte[] array);

    String oneArray(char[] array);

    String oneArray(double[] array);

    String oneArray(float[] array);

    String oneArray(int[] array);

    String oneArray(long[] array);

    String oneArray(short[] array);

    String oneArray(Object[] array);

    String oneArray(String[] array);

    void varargsString(int i, String... string);

    void varargsObject(int i, Object... object);
}
