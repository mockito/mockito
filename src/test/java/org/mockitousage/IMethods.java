/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.*;

public interface IMethods {

    boolean booleanReturningMethod();
    
    Boolean booleanObjectReturningMethod();

    byte byteReturningMethod();
    
    Byte byteObjectReturningMethod();

    short shortReturningMethod();
    
    Short shortObjectReturningMethod();

    char charReturningMethod();
    
    Character charObjectReturningMethod();

    int intReturningMethod();
    
    Integer integerReturningMethod();

    long longReturningMethod();
    
    Long longObjectReturningMethod();

    float floatReturningMethod();
    
    Float floatObjectReturningMethod();

    double doubleReturningMethod();
    
    Double doubleObjectReturningMethod();

    Object objectReturningMethod(Object ... objects);
    
    Object objectReturningMethodNoArgs();

    String oneArg(boolean value);
    
    String oneArg(Boolean value);

    String forBoolean(Boolean value);

    String oneArg(byte value);
    
    String oneArg(Byte value);

    String forByte(Byte value);
    
    String oneArg(short value);

    String oneArg(Short value);
    
    String forShort(Short value);
    
    String oneArg(char value);
    
    String oneArg(Character value);
    
    String forCharacter(Character value);

    String oneArg(int value);
    
    String oneArg(Integer value);
    
    String forInteger(Integer value);

    String oneArg(long value);

    String oneArg(Long value);
    
    String forLong(Long value);
    
    String oneArg(float value);

    String oneArg(Float value);
    
    String forFloat(Float value);
    
    String oneArg(double value);

    String oneArg(Double value);
    
    String forDouble(Double value);
    
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
    
    String simpleMethod(String one, Integer two, Integer three, Integer four, Integer five);
    
    String simpleMethod(String one, String[] two);

    Object threeArgumentMethod(int valueOne, Object valueTwo, String valueThree);

    String threeArgumentMethodWithStrings(int valueOne, String valueTwo, String valueThree);

    String fourArgumentMethod(int valueOne, String valueTwo, String valueThree, boolean[] array);

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
    
    String canThrowException() throws CharacterCodingException; 

    String oneArray(String[] array);

    void varargsString(int i, String... string);

    Object varargsObject(int i, Object... object);

    void varargsbyte(byte... bytes);

    int varargs(Object ... object);

    String varargsReturningString(Object ... object);

    int varargs(String ... string);

    void mixedVarargs(Object i, String ... string);

    List<String> listReturningMethod(Object ... objects);

    LinkedList<String> linkedListReturningMethod();

    String toString();

    String toString(String foo);

    void voidMethod();

    String forList(List<String> list);

    String forSet(Set<String> anySet);

    String forMap(Map<String, String> map);

    String forCollection(Collection<String> collection);

    String forIterable(Iterable<String> iterable);

    Object[] arrayReturningMethod();

    IMethods iMethodsReturningMethod();

    String stringReturningMethod();

    Object objectArgMethod(Object str);

    Object listArgMethod(List<String> list);

    Object collectionArgMethod(Collection<String> collection);

    Object iterableArgMethod(Iterable<String> collection);

    Object setArgMethod(Set<String> set);

    void longArg(long longArg);

    void intArgumentMethod(int i);

    int intArgumentReturningInt(int i);

    boolean equals(String str);

    boolean equals();

    int hashCode(String str);

    int toIntPrimitive(Integer i);

    Integer toIntWrapper(int i);

    String forObject(Object object);
}