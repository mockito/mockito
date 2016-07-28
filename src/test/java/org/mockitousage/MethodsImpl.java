/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.*;

public class MethodsImpl implements IMethods {
    public boolean booleanReturningMethod() {
        return false;
    }

    public Boolean booleanObjectReturningMethod() {
        return null;
    }

    public byte byteReturningMethod() {
        return 0;
    }

    public Byte byteObjectReturningMethod() {
        return null;
    }

    public short shortReturningMethod() {
        return 0;
    }

    public Short shortObjectReturningMethod() {
        return null;
    }

    public char charReturningMethod() {
        return 0;
    }

    public Character charObjectReturningMethod() {
        return null;
    }

    public int intReturningMethod() {
        return 0;
    }

    public Integer integerReturningMethod() {
        return null;
    }

    public long longReturningMethod() {
        return 0;
    }

    public Long longObjectReturningMethod() {
        return null;
    }

    public float floatReturningMethod() {
        return 0;
    }

    public Float floatObjectReturningMethod() {
        return null;
    }

    public double doubleReturningMethod() {
        return 0;
    }

    public Double doubleObjectReturningMethod() {
        return null;
    }

    public Object objectReturningMethod(Object... objects) {
        return null;
    }

    public Object objectReturningMethodNoArgs() {
        return null;
    }

    public String oneArg(boolean value) {
        return null;
    }

    public String oneArg(Boolean value) {
        return null;
    }

    public String forBoolean(Boolean value) {
        return null;
    }

    public String oneArg(byte value) {
        return null;
    }

    public String oneArg(Byte value) {
        return null;
    }

    public String forByte(Byte value) {
        return null;
    }

    public String oneArg(short value) {
        return null;
    }

    public String oneArg(Short value) {
        return null;
    }

    public String forShort(Short value) {
        return null;
    }

    public String oneArg(char value) {
        return null;
    }

    public String oneArg(Character value) {
        return null;
    }

    public String forCharacter(Character value) {
        return null;
    }

    public String oneArg(int value) {
        return null;
    }

    public String oneArg(Integer value) {
        return null;
    }

    public String forInteger(Integer value) {
        return null;
    }

    public String oneArg(long value) {
        return null;
    }

    public String oneArg(Long value) {
        return null;
    }

    public String forLong(Long value) {
        return null;
    }

    public String oneArg(float value) {
        return null;
    }

    public String oneArg(Float value) {
        return null;
    }

    public String forFloat(Float value) {
        return null;
    }

    public String oneArg(double value) {
        return null;
    }

    public String oneArg(Double value) {
        return null;
    }

    public String forDouble(Double value) {
        return null;
    }

    public String oneArg(Object value) {
        return null;
    }

    public String oneArg(String value) {
        return null;
    }

    public String throwsNothing(boolean value) {
        return null;
    }

    public String throwsIOException(int count) throws IOException {
        return null;
    }

    public String throwsError(int count) {
        return null;
    }

    public String simpleMethod() {
        return null;
    }

    public String differentMethod() {
        return null;
    }

    public String differentMethod(String argument) {
        return null;
    }

    public String otherMethod() {
        return null;
    }

    public String simpleMethod(String argument) {
        return null;
    }

    public String simpleMethod(Collection<?> collection) {
        return null;
    }

    public String simpleMethod(Object argument) {
        return null;
    }

    public String simpleMethod(int argument) {
        return null;
    }

    public String simpleMethod(String argOne, Integer argTwo) {
        return null;
    }

    public String simpleMethod(String one, Integer two, Integer three, Integer four, Integer five) {
        return null;
    }

    public String simpleMethod(String one, String[] two) {
        return null;
    }

    public Object threeArgumentMethod(int valueOne, Object valueTwo, String valueThree) {
        return null;
    }

    public String threeArgumentMethodWithStrings(int valueOne, String valueTwo, String valueThree) {
        return null;
    }

    public String fourArgumentMethod(int valueOne, String valueTwo, String valueThree, boolean[] array) {
        return null;
    }

    public void twoArgumentMethod(int one, int two) {
      
    }

    public void arrayMethod(String[] strings) {
      
    }

    public String oneArray(boolean[] array) {
        return null;
    }

    public String oneArray(byte[] array) {
        return null;
    }

    public String oneArray(char[] array) {
        return null;
    }

    public String oneArray(double[] array) {
        return null;
    }

    public String oneArray(float[] array) {
        return null;
    }

    public String oneArray(int[] array) {
        return null;
    }

    public String oneArray(long[] array) {
        return null;
    }

    public String oneArray(short[] array) {
        return null;
    }

    public String oneArray(Object[] array) {
        return null;
    }

    public String canThrowException() throws CharacterCodingException {
        return null;
    }

    public String oneArray(String[] array) {
        return null;
    }

    public void varargsString(int i, String... string) {
      
    }

    public Object varargsObject(int i, Object... object) {
        return null;
    }

    public int varargs(Object... object) {
        return -1;
    }

    public String varargsReturningString(Object... object) {
        return null;
    }

    public int varargs(String... string) {
        return -1;
    }

    public void mixedVarargs(Object i, String... string) {
    }

    public void varargsbyte(byte... bytes) {
    }

    public List<String> listReturningMethod(Object... objects) {
        return null;
    }

    public LinkedList<String> linkedListReturningMethod() {
        return null;
    }

    public String toString(String foo) {
        return null;
    }

    public void voidMethod() {
      
    }

    public String forList(List<String> list) {
        return null;
    }

    public String forSet(Set<String> anySet) {
        return null;
    }

    public String forMap(Map<String, String> map) {
        return null;
    }

    public String forCollection(Collection<String> collection) {
        return null;
    }

    public String forIterable(Iterable<String> iterable) {
        return null;
    }

    public Object[] arrayReturningMethod() {
        return new Object[0];
    }

    public IMethods iMethodsReturningMethod() {
        return null;
    }

    public String stringReturningMethod() {
        return null;
    }

    public Object objectArgMethod(Object str) {
        return null;
    }

    public Object listArgMethod(List<String> list) {
        return null;
    }

    public Object collectionArgMethod(Collection<String> collection) {
        return null;
    }

    public Object iterableArgMethod(Iterable<String> iterable) {
        return null;
    }

    public Object setArgMethod(Set<String> set) {
        return null;
    }

    public void longArg(long longArg) {
      
    }

    public void intArgumentMethod(int i) {
      
    }

    public int intArgumentReturningInt(int i) {
        return 0;
    }

    public boolean equals(String str) {
        return false;
    }

    public boolean equals() {
        return false;
    }

    public int hashCode(String str) {
        return 0;
    }

    public int toIntPrimitive(Integer i) {
        return 0;
    }

    public Integer toIntWrapper(int i) {
        return null;
    }

    public String forObject(Object object) {
        return null;
    }
}
