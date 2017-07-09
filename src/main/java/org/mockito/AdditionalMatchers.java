/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

/**
 * @deprecated As of Mockito 2.8.54, this <code>AdditionalMatchers</code> has been deprecated,
 * and all it's functionality has been moved to {@link ArgumentMatchers}. This class is kept
 * for backwards compatibility reasons only, and will be removed in the future.
 */
@Deprecated
public class AdditionalMatchers {
    /**
     * argument greater than or equal the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T geq(T value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * byte argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte geq(byte value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * double argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double geq(double value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * float argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float geq(float value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * int argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int geq(int value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * long argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long geq(long value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * short argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short geq(short value) {
        return ArgumentMatchers.geq(value);
    }

    /**
     * comparable argument less than or equal the given value details.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T leq(T value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * byte argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte leq(byte value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * double argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double leq(double value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * float argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float leq(float value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * int argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int leq(int value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * long argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long leq(long value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * short argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short leq(short value) {
        return ArgumentMatchers.leq(value);
    }

    /**
     * comparable argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T gt(T value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * byte argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte gt(byte value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * double argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double gt(double value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * float argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float gt(float value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * int argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int gt(int value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * long argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long gt(long value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * short argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short gt(short value) {
        return ArgumentMatchers.gt(value);
    }

    /**
     * comparable argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T lt(T value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * byte argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte lt(byte value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * double argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double lt(double value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * float argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float lt(float value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * int argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int lt(int value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * long argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long lt(long value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * short argument less than the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short lt(short value) {
        return ArgumentMatchers.lt(value);
    }

    /**
     * comparable argument equals to the given value according to their
     * compareTo method.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T cmpEq(T value) {
        return ArgumentMatchers.cmpEq(value);
    }

    /**
     * String argument that contains a substring that matches the given regular
     * expression.
     *
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String find(String regex) {
        return ArgumentMatchers.find(regex);
    }

    /**
     * Object array argument that is equal to the given array, i.e. it has to
     * have the same type, length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static <T> T[] aryEq(T[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * short array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static short[] aryEq(short[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * long array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static long[] aryEq(long[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * int array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static int[] aryEq(int[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * float array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static float[] aryEq(float[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * double array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static double[] aryEq(double[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * char array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static char[] aryEq(char[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * byte array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static byte[] aryEq(byte[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * boolean array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static boolean[] aryEq(boolean[] value) {
        return ArgumentMatchers.aryEq(value);
    }

    /**
     * boolean argument that matches both given matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean and(boolean first, boolean second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * byte argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte and(byte first, byte second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * char argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char and(char first, char second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * double argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double and(double first, double second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * float argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float and(float first, float second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * int argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int and(int first, int second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * long argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long and(long first, long second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * short argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short and(short first, short second) {
        return ArgumentMatchers.and(first, second);
    }

    /**
     * Object argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
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
        return ArgumentMatchers.and(first, second);
    }

    /**
     * boolean argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean or(boolean first, boolean second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * Object argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
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
        return ArgumentMatchers.or(first, second);
    }

    /**
     * short argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short or(short first, short second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * long argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long or(long first, long second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * int argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int or(int first, int second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * float argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float or(float first, float second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * double argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double or(double first, double second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * char argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char or(char first, char second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * byte argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte or(byte first, byte second) {
        return ArgumentMatchers.or(first, second);
    }

    /**
     * Object argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T not(T first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * short argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static short not(short first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * int argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static int not(int first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * long argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static long not(long first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * float argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static float not(float first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * double argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static double not(double first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * char argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static char not(char first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * boolean argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>false</code>.
     */
    public static boolean not(boolean first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * byte argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static byte not(byte first) {
        return ArgumentMatchers.not(first);
    }

    /**
     * double argument that has an absolute difference to the given value that
     * is less than the given delta details.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static double eq(double value, double delta) {
        return ArgumentMatchers.eq(value, delta);
    }

    /**
     * float argument that has an absolute difference to the given value that is
     * less than the given delta details.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static float eq(float value, float delta) {
        return ArgumentMatchers.eq(value, delta);
    }
}
