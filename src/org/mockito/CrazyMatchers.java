/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.*;
import org.mockito.internal.progress.LastArguments;

public class CrazyMatchers {
    
    /**
     * Expects a comparable argument greater than or equal the given value. For details, see
     * the documentation.
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
     * Expects a byte argument greater than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a double argument greater than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a float argument greater than or equal to the given value. For
     * details, see the documentation.
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
     * Expects an int argument greater than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a long argument greater than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a short argument greater than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a comparable argument less than or equal the given value. For details, see
     * the documentation.
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
     * Expects a byte argument less than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a double argument less than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a float argument less than or equal to the given value. For
     * details, see the documentation.
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
     * Expects an int argument less than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a long argument less than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a short argument less than or equal to the given value. For
     * details, see the documentation.
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
     * Expects a comparable argument greater than the given value. For details, see
     * the documentation.
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
     * Expects a byte argument greater than the given value. For details, see
     * the documentation.
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
     * Expects a double argument greater than the given value. For details, see
     * the documentation.
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
     * Expects a float argument greater than the given value. For details, see
     * the documentation.
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
     * Expects an int argument greater than the given value. For details, see
     * the documentation.
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
     * Expects a long argument greater than the given value. For details, see
     * the documentation.
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
     * Expects a short argument greater than the given value. For details, see
     * the documentation.
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
     * Expects a comparable argument less than the given value. For details, see
     * the documentation.
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
     * Expects a byte argument less than the given value. For details, see the
     * documentation.
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
     * Expects a double argument less than the given value. For details, see the
     * documentation.
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
     * Expects a float argument less than the given value. For details, see the
     * documentation.
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
     * Expects an int argument less than the given value. For details, see the
     * documentation.
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
     * Expects a long argument less than the given value. For details, see the
     * documentation.
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
     * Expects a short argument less than the given value. For details, see the
     * documentation.
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
     * Expects a string that contains the given substring. For details, see the
     * documentation.
     * 
     * @param substring
     *            the substring.
     * @return <code>null</code>.
     */
    public static String contains(String substring) {
        LastArguments.instance().reportMatcher(new Contains(substring));
        return null;
    }

    /**
     * Expects a boolean that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>false</code>.
     */
    public static boolean and(boolean first, boolean second) {
        LastArguments.instance().reportAnd(2);
        return false;
    }

    /**
     * Expects a byte that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static byte and(byte first, byte second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects a char that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static char and(char first, char second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects a double that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static double and(double first, double second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects a float that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static float and(float first, float second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects an int that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static int and(int first, int second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects a long that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static long and(long first, long second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects a short that matches both given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static short and(short first, short second) {
        LastArguments.instance().reportAnd(2);
        return 0;
    }

    /**
     * Expects an Object that matches both given expectations.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>null</code>.
     */
    public static <T> T and(T first, T second) {
        LastArguments.instance().reportAnd(2);
        return null;
    }

    /**
     * Expects a boolean that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>false</code>.
     */
    public static boolean or(boolean first, boolean second) {
        LastArguments.instance().reportOr(2);
        return false;
    }

    /**
     * Expects a comparable argument equals to the given value according to their
     * compareTo method. For details, see the documentation.
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
     * Expects an Object that is the same as the given value. For details, see
     * the documentation.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T same(T value) {
        LastArguments.instance().reportMatcher(new Same(value));
        return null;
    }

    /**
     * Expects a string that ends with the given suffix. For details, see the
     * documentation.
     * 
     * @param suffix
     *            the suffix.
     * @return <code>null</code>.
     */
    public static String endsWith(String suffix) {
        LastArguments.instance().reportMatcher(new EndsWith(suffix));
        return null;
    }

    /**
     * Expects a string that starts with the given prefix. For details, see the
     * documentation.
     * 
     * @param prefix
     *            the prefix.
     * @return <code>null</code>.
     */
    public static String startsWith(String prefix) {
        LastArguments.instance().reportMatcher(new StartsWith(prefix));
        return null;
    }

    /**
     * Expects a string that contains a substring that matches the given regular
     * expression. For details, see the documentation.
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
     * Expects an Object array that is equal to the given array, i.e. it has to
     * have the same type, length, and each element has to be equal.
     * 
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static <T> T[] aryEq(T[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a short array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static short[] aryEq(short[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a long array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static long[] aryEq(long[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects an int array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static int[] aryEq(int[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a float array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static float[] aryEq(float[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a double array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static double[] aryEq(double[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a char array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static char[] aryEq(char[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a byte array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static byte[] aryEq(byte[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects a boolean array that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * 
     * @param value
     *            the given arry.
     * @return <code>null</code>.
     */
    public static boolean[] aryEq(boolean[] value) {
        LastArguments.instance().reportMatcher(new ArrayEquals(value));
        return null;
    }

    /**
     * Expects an Object that does not match the given expectation.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the expectation.
     * @return <code>null</code>.
     */
    public static <T> T not(T first) {
        LastArguments.instance().reportNot();
        return null;
    }

    /**
     * Expects a short that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static short not(short first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * Expects an int that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static int not(int first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * Expects a long that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static long not(long first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * Expects a float that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static float not(float first) {
        LastArguments.instance().reportNot();
        return first;
    }

    /**
     * Expects a double that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static double not(double first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * Expects a char that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static char not(char first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * Expects a boolean that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>false</code>.
     */
    public static boolean not(boolean first) {
        LastArguments.instance().reportNot();
        return false;
    }

    /**
     * Expects a byte that does not match the given expectation.
     * 
     * @param first
     *            placeholder for the expectation.
     * @return <code>0</code>.
     */
    public static byte not(byte first) {
        LastArguments.instance().reportNot();
        return 0;
    }

    /**
     * Expects an Object that matches one of the given expectations.
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>null</code>.
     */
    public static <T> T or(T first, T second) {
        LastArguments.instance().reportOr(2);
        return null;
    }

    /**
     * Expects a short that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static short or(short first, short second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * Expects a long that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static long or(long first, long second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * Expects an int that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static int or(int first, int second) {
        LastArguments.instance().reportOr(2);
        return first;
    }

    /**
     * Expects a float that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static float or(float first, float second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * Expects a double that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static double or(double first, double second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * Expects a char that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static char or(char first, char second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }

    /**
     * Expects a byte that matches one of the given expectations.
     * 
     * @param first
     *            placeholder for the first expectation.
     * @param second
     *            placeholder for the second expectation.
     * @return <code>0</code>.
     */
    public static byte or(byte first, byte second) {
        LastArguments.instance().reportOr(2);
        return 0;
    }
}