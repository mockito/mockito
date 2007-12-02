/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.io.IOException;
import java.util.List;

public interface IMethods {

    boolean booleanReturningMethod(int index);

    byte byteReturningMethod(int index);

    short shortReturningMethod(int index);

    char charReturningMethod(int index);

    int intReturningMethod(int index);

    long longReturningMethod(int index);

    float floatReturningMethod(int index);

    double doubleReturningMethod(int index);

    Object objectReturningMethod(Object ... objects);

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

    boolean simpleMethod();
    
    void differentMethod();

    String simpleMethod(String argument);
    
    String simpleMethod(int argument);
    
    String simpleMethod(String argOne, Integer argTwo);

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

    Object varargsObject(int i, Object... object);

    List<String> listReturningMethod(Object ... objects);
}
