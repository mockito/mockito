/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.EqualsWithDelta;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.LessThan;
import org.mockito.internal.progress.LastArguments;

/**
 * Very rarely used matchers are kept here
 */
public class AdditionalMatchers {

    /**
     * argument greater than or equal the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T geq(Comparable<T> value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<T>(value));
        return null;
    }

    /**
     * byte argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte geq(byte value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<Byte>(value));
        return 0;
    }

    /**
     * double argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double geq(double value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<Double>(value));
        return 0;
    }

    /**
     * float argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float geq(float value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<Float>(value));
        return 0;
    }

    /**
     * int argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int geq(int value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<Integer>(value));
        return 0;
    }

    /**
     * long argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long geq(long value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<Long>(value));
        return 0;
    }

    /**
     * short argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short geq(short value) {
        LastArguments.instance().reportMatcher(new GreaterOrEqual<Short>(value));
        return 0;
    }

    /**
     * comparable argument less than or equal the given value details.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T leq(Comparable<T> value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<T>(value));
        return null;
    }

    /**
     * byte argument less than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte leq(byte value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<Byte>(value));
        return 0;
    }

    /**
     * double argument less than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double leq(double value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<Double>(value));
        return 0;
    }

    /**
     * float argument less than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float leq(float value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<Float>(value));
        return 0;
    }

    /**
     * int argument less than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int leq(int value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<Integer>(value));
        return 0;
    }

    /**
     * long argument less than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long leq(long value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<Long>(value));
        return 0;
    }

    /**
     * short argument less than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short leq(short value) {
        LastArguments.instance().reportMatcher(new LessOrEqual<Short>(value));
        return 0;
    }

    /**
     * comparable argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T gt(Comparable<T> value) {
        LastArguments.instance().reportMatcher(new GreaterThan<T>(value));
        return null;
    }

    /**
     * byte argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte gt(byte value) {
        LastArguments.instance().reportMatcher(new GreaterThan<Byte>(value));
        return 0;
    }

    /**
     * double argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double gt(double value) {
        LastArguments.instance().reportMatcher(new GreaterThan<Double>(value));
        return 0;
    }

    /**
     * float argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float gt(float value) {
        LastArguments.instance().reportMatcher(new GreaterThan<Float>(value));
        return 0;
    }

    /**
     * int argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int gt(int value) {
        LastArguments.instance().reportMatcher(new GreaterThan<Integer>(value));
        return 0;
    }

    /**
     * long argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long gt(long value) {
        LastArguments.instance().reportMatcher(new GreaterThan<Long>(value));
        return 0;
    }

    /**
     * short argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short gt(short value) {
        LastArguments.instance().reportMatcher(new GreaterThan<Short>(value));
        return 0;
    }

    /**
     * comparable argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T lt(Comparable<T> value) {
        LastArguments.instance().reportMatcher(new LessThan<T>(value));
        return null;
    }

    /**
     * byte argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte lt(byte value) {
        LastArguments.instance().reportMatcher(new LessThan<Byte>(value));
        return 0;
    }

    /**
     * double argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double lt(double value) {
        LastArguments.instance().reportMatcher(new LessThan<Double>(value));
        return 0;
    }

    /**
     * float argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float lt(float value) {
        LastArguments.instance().reportMatcher(new LessThan<Float>(value));
        return 0;
    }

    /**
     * int argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int lt(int value) {
        LastArguments.instance().reportMatcher(new LessThan<Integer>(value));
        return 0;
    }

    /**
     * long argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long lt(long value) {
        LastArguments.instance().reportMatcher(new LessThan<Long>(value));
        return 0;
    }

    /**
     * short argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short lt(short value) {
        LastArguments.instance().reportMatcher(new LessThan<Short>(value));
        return 0;
    }

    /**
     * comparable argument equals to the given value according to their
     * compareTo method.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T cmpEq(Comparable<T> value) {
        LastArguments.instance().reportMatcher(new CompareEqual<T>(value));
        return null;
    }

    /**
     * string that contains a substring that matches the given regular
     * expression.
     * 
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String find(String regex) {
        LastArguments.instance().reportMatcher(new Find(regex));
        return null;
    }

    /**
     * Object array that is equal to the given array, i.e. it has to have the
     * same type, length, and each element has to be equal.
     * 
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static <T> T[] aryEq(T[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * short array that is equal to the given array, i.e. it has to have the
     * same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static short[] aryEq(short[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * long array that is equal to the given array, i.e. it has to have the same
     * length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static long[] aryEq(long[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * int array that is equal to the given array, i.e. it has to have the same
     * length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static int[] aryEq(int[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * float array that is equal to the given array, i.e. it has to have the
     * same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static float[] aryEq(float[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * double array that is equal to the given array, i.e. it has to have the
     * same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static double[] aryEq(double[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * char array that is equal to the given array, i.e. it has to have the same
     * length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static char[] aryEq(char[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * byte array that is equal to the given array, i.e. it has to have the same
     * length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static byte[] aryEq(byte[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * boolean array that is equal to the given array, i.e. it has to have the
     * same length, and each element has to be equal.
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static boolean[] aryEq(boolean[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * boolean that matches both given matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean and(boolean first, boolean second) {
        LastArguments.instance().reportAnd(2);
        return false;
    }

    /**
     * byte that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte and(byte first, byte second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * char that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char and(char first, char second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * double that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double and(double first, double second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * float that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float and(float first, float second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * int that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int and(int first, int second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * long that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long and(long first, long second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * short that matches both given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short and(short first, short second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Object that matches both given argument matchers.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T and(T first, T second) {
        LastArguments.instance().reportAnd(2);
        return null;
    }

    /**
     * boolean that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean or(boolean first, boolean second) {
        LastArguments.instance().reportOr(2);
        return false;
    }

    /**
     * Object that matches any of the given argument matchers.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T or(T first, T second) {
        LastArguments.instance().reportOr(2);
        return null;
    }

    /**
     * short that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short or(short first, short second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * long that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long or(long first, long second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * int that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int or(int first, int second) {
        LastArguments.instance().reportOr(2);
        return first;
    }

    /**
     * float that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float or(float first, float second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * double that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double or(double first, double second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * char that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char or(char first, char second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * byte that matches any of the given argument matchers.
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte or(byte first, byte second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * Object that does not match the given argument matcher.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T not(T first) {
        LastArguments.instance().reportNot();
        return null;
    }

    /**
     * short that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static short not(short first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * int that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static int not(int first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * long that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static long not(long first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * float that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static float not(float first) {
        LastArguments.instance().reportNot();
        return first;
    }

    /**
     * double that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static double not(double first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * char that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static char not(char first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * boolean that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>false</code>.
     */
    public static boolean not(boolean first) {
        LastArguments.instance().reportNot();
        return false;
    }

    /**
     * byte that does not match the given argument matcher.
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static byte not(byte first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * double that has an absolute difference to the given value that is less
     * than the given delta details.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static double eq(double value, double delta) {
        LastArguments.instance().reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }

    /**
     * float that has an absolute difference to the given value that is less
     * than the given delta details.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static float eq(float value, float delta) {
        LastArguments.instance().reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }
}