/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.Collection;
import java.util.List;

import javax.swing.text.ChangedCharSetException;

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

    String throwsNothing(boolean value);

    String throwsIOException(int count) throws IOException;

    String throwsError(int count);

    String simpleMethod();
    
    String differentMethod();
    
    String differentMethod(String argument);
    
    String otherMethod();

    String simpleMethod(String argument);
    
    String simpleMethod(Collection<?> collection);
    
    String simpleMethod(Object argument);
    
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
    
    String canThrowException() throws ChangedCharSetException, CharacterCodingException; 

    String oneArray(String[] array);

    void varargsString(int i, String... string);

    Object varargsObject(int i, Object... object);
    
    void varargs(Object ... object);
    
    void varargs(String ... string);

    List<String> listReturningMethod(Object ... objects);
}
