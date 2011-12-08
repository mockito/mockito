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
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

/**
 * See {@link Matchers} for general info about matchers.
 * <p>
 * AdditionalMatchers provides rarely used matchers, kept only for somewhat compatibility with EasyMock. 
 * Use additional matchers very judiciously because they may impact readability of a test.
 * It is recommended to use matchers from {@link Matchers} and keep stubbing and verification simple.
 * <p>
 * Example of using logical and(), not(), or() matchers: 
 *  
 * <pre class="code"><code class="java">
 *   //anything but not "ejb"
 *   mock.someMethod(not(eq("ejb")));
 *   
 *   //not "ejb" and not "michael jackson"
 *   mock.someMethod(and(not(eq("ejb")), not(eq("michael jackson"))));
 *   
 *   //1 or 10
 *   mock.someMethod(or(eq(1), eq(10)));
 * </code></pre>
 * 
 * Scroll down to see all methods - full list of matchers.
 */
public class AdditionalMatchers {
    
    private static MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    /**
     * argument greater than or equal the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T geq(Comparable<T> value) {
        return reportMatcher(new GreaterOrEqual<T>(value)).<T>returnNull();
    }

    /**
     * byte argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte geq(byte value) {
        return reportMatcher(new GreaterOrEqual<Byte>(value)).returnZero();
    }

    /**
     * double argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double geq(double value) {
        return reportMatcher(new GreaterOrEqual<Double>(value)).returnZero();
    }

    /**
     * float argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float geq(float value) {
        return reportMatcher(new GreaterOrEqual<Float>(value)).returnZero();
    }

    /**
     * int argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int geq(int value) {
        return reportMatcher(new GreaterOrEqual<Integer>(value)).returnZero();
    }

    /**
     * long argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long geq(long value) {
        return reportMatcher(new GreaterOrEqual<Long>(value)).returnZero();
    }

    /**
     * short argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short geq(short value) {
        return reportMatcher(new GreaterOrEqual<Short>(value)).returnZero();
    }

    /**
     * comparable argument less than or equal the given value details.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T leq(Comparable<T> value) {
        return reportMatcher(new LessOrEqual<T>(value)).<T>returnNull();
    }

    /**
     * byte argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte leq(byte value) {
        return reportMatcher(new LessOrEqual<Byte>(value)).returnZero();
    }

    /**
     * double argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double leq(double value) {
        return reportMatcher(new LessOrEqual<Double>(value)).returnZero();
    }

    /**
     * float argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float leq(float value) {
        return reportMatcher(new LessOrEqual<Float>(value)).returnZero();
    }

    /**
     * int argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int leq(int value) {
        return reportMatcher(new LessOrEqual<Integer>(value)).returnZero();
    }

    /**
     * long argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long leq(long value) {
        return reportMatcher(new LessOrEqual<Long>(value)).returnZero();
    }

    /**
     * short argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class 
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short leq(short value) {
        return reportMatcher(new LessOrEqual<Short>(value)).returnZero();
    }

    /**
     * comparable argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T gt(Comparable<T> value) {
        return reportMatcher(new GreaterThan<T>(value)).<T>returnNull();
    }

    /**
     * byte argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte gt(byte value) {
        return reportMatcher(new GreaterThan<Byte>(value)).returnZero();
    }

    /**
     * double argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double gt(double value) {
        return reportMatcher(new GreaterThan<Double>(value)).returnZero();
    }

    /**
     * float argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float gt(float value) {
        return reportMatcher(new GreaterThan<Float>(value)).returnZero();
    }

    /**
     * int argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int gt(int value) {
        return reportMatcher(new GreaterThan<Integer>(value)).returnZero();
    }

    /**
     * long argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long gt(long value) {
        return reportMatcher(new GreaterThan<Long>(value)).returnZero();
    }

    /**
     * short argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short gt(short value) {
        return reportMatcher(new GreaterThan<Short>(value)).returnZero();
    }

    /**
     * comparable argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T lt(Comparable<T> value) {
        return reportMatcher(new LessThan<T>(value)).<T>returnNull();
    }

    /**
     * byte argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte lt(byte value) {
        return reportMatcher(new LessThan<Byte>(value)).returnZero();
    }

    /**
     * double argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double lt(double value) {
        return reportMatcher(new LessThan<Double>(value)).returnZero();
    }

    /**
     * float argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float lt(float value) {
        return reportMatcher(new LessThan<Float>(value)).returnZero();
    }

    /**
     * int argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int lt(int value) {
        return reportMatcher(new LessThan<Integer>(value)).returnZero();
    }

    /**
     * long argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long lt(long value) {
        return reportMatcher(new LessThan<Long>(value)).returnZero();
    }

    /**
     * short argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short lt(short value) {
        return reportMatcher(new LessThan<Short>(value)).returnZero();
    }

    /**
     * comparable argument equals to the given value according to their
     * compareTo method.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T cmpEq(Comparable<T> value) {
        return reportMatcher(new CompareEqual<T>(value)).<T>returnNull();
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
        return reportMatcher(new Find(regex)).<String>returnNull();
    }

    /**
     * Object array argument that is equal to the given array, i.e. it has to
     * have the same type, length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static <T> T[] aryEq(T[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * short array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static short[] aryEq(short[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * long array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static long[] aryEq(long[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * int array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static int[] aryEq(int[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();       
    }

    /**
     * float array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static float[] aryEq(float[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * double array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static double[] aryEq(double[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * char array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static char[] aryEq(char[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * byte array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static byte[] aryEq(byte[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * boolean array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static boolean[] aryEq(boolean[] value) {
        return reportMatcher(new ArrayEquals(value)).returnNull();
    }

    /**
     * boolean argument that matches both given matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean and(boolean first, boolean second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnFalse();
    }

    /**
     * byte argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte and(byte first, byte second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnZero();
    }

    /**
     * char argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char and(char first, char second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnChar();
    }

    /**
     * double argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double and(double first, double second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnZero();
    }

    /**
     * float argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float and(float first, float second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnZero();
    }

    /**
     * int argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int and(int first, int second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnZero();
    }

    /**
     * long argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long and(long first, long second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnZero();
    }

    /**
     * short argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short and(short first, short second) {
        return mockingProgress.getArgumentMatcherStorage().reportAnd().returnZero();
    }

    /**
     * Object argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
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
        return mockingProgress.getArgumentMatcherStorage().reportAnd().<T>returnNull();
    }

    /**
     * boolean argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean or(boolean first, boolean second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnFalse();
    }

    /**
     * Object argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
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
        return mockingProgress.getArgumentMatcherStorage().reportOr().<T>returnNull();
    }

    /**
     * short argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short or(short first, short second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnZero();
    }

    /**
     * long argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long or(long first, long second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnZero();
    }

    /**
     * int argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int or(int first, int second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnZero();
    }

    /**
     * float argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float or(float first, float second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnZero();
    }

    /**
     * double argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double or(double first, double second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnZero();
    }

    /**
     * char argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char or(char first, char second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnChar();
    }

    /**
     * byte argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte or(byte first, byte second) {
        return mockingProgress.getArgumentMatcherStorage().reportOr().returnZero();
    }

    /**
     * Object argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T not(T first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().<T>returnNull();
    }

    /**
     * short argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static short not(short first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnZero();
    }

    /**
     * int argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static int not(int first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnZero();
    }

    /**
     * long argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static long not(long first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnZero();
    }

    /**
     * float argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static float not(float first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnZero();
    }

    /**
     * double argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static double not(double first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnZero();
    }

    /**
     * char argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static char not(char first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnChar();
    }

    /**
     * boolean argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>false</code>.
     */
    public static boolean not(boolean first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnFalse();
    }

    /**
     * byte argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static byte not(byte first) {
        return mockingProgress.getArgumentMatcherStorage().reportNot().returnZero();
    }

    /**
     * double argument that has an absolute difference to the given value that
     * is less than the given delta details.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static double eq(double value, double delta) {
        return reportMatcher(new EqualsWithDelta(value, delta)).returnZero();
    }
    
    /**
     * float argument that has an absolute difference to the given value that is
     * less than the given delta details.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static float eq(float value, float delta) {
        return reportMatcher(new EqualsWithDelta(value, delta)).returnZero();
    }
    
    private static HandyReturnValues reportMatcher(ArgumentMatcher<?> matcher) {
        return mockingProgress.getArgumentMatcherStorage().reportMatcher(matcher);
    }
}