/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.EqualsWithDelta;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.LessThan;

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
@SuppressWarnings("ALL")
public class AdditionalMatchers {
    
    /**
     * argument greater than or equal the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T geq(T value) {
        reportMatcher(new GreaterOrEqual<T>(value));
        return null;
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
        reportMatcher(new GreaterOrEqual<Byte>(value));
        return 0;
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
        reportMatcher(new GreaterOrEqual<Double>(value));
        return 0;
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
        reportMatcher(new GreaterOrEqual<Float>(value));
        return 0;
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
        reportMatcher(new GreaterOrEqual<Integer>(value));
        return 0;
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
        reportMatcher(new GreaterOrEqual<Long>(value));
        return 0;
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
        reportMatcher(new GreaterOrEqual<Short>(value));
        return 0;
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
    public static <T extends Comparable<T>> T leq(T value) {
        reportMatcher(new LessOrEqual<T>(value));
        return null;
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
        reportMatcher(new LessOrEqual<Byte>(value));
        return 0;
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
        reportMatcher(new LessOrEqual<Double>(value));
        return 0;
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
        reportMatcher(new LessOrEqual<Float>(value));
        return 0;
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
        reportMatcher(new LessOrEqual<Integer>(value));
        return 0;
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
        reportMatcher(new LessOrEqual<Long>(value));
        return 0;
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
        reportMatcher(new LessOrEqual<Short>(value));
        return 0;
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
    public static <T extends Comparable<T>> T gt(T value) {
        reportMatcher(new GreaterThan<T>(value));
        return null;
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
        reportMatcher(new GreaterThan<Byte>(value));
        return 0;
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
        reportMatcher(new GreaterThan<Double>(value));
        return 0;
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
        reportMatcher(new GreaterThan<Float>(value));
        return 0;
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
        reportMatcher(new GreaterThan<Integer>(value));
        return 0;
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
        reportMatcher(new GreaterThan<Long>(value));
        return 0;
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
        reportMatcher(new GreaterThan<Short>(value));
        return 0;
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
    public static <T extends Comparable<T>> T lt(T value) {
        reportMatcher(new LessThan<T>(value));
        return null;
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
        reportMatcher(new LessThan<Byte>(value));
        return 0;
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
        reportMatcher(new LessThan<Double>(value));
        return 0;
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
        reportMatcher(new LessThan<Float>(value));
        return 0;
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
        reportMatcher(new LessThan<Integer>(value));
        return 0;
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
        reportMatcher(new LessThan<Long>(value));
        return 0;
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
        reportMatcher(new LessThan<Short>(value));
        return 0;
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
    public static <T extends Comparable<T>> T cmpEq(T value) {
        reportMatcher(new CompareEqual<T>(value));
        return null;
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
        reportMatcher(new Find(regex));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;       
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        reportMatcher(new ArrayEquals(value));
        return null;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd(); 
        return false;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportAnd();
        return null;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return false;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return null;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportOr();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return null;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        mockingProgress().getArgumentMatcherStorage().reportNot(); 
        return false;
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
        mockingProgress().getArgumentMatcherStorage().reportNot();
        return 0;
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
        reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
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
        reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }
    
    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}