/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.internal.matchers.Matches;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.progress.LastArguments;
import org.mockito.internal.progress.EmptyReturnValues;

/**
 * Allow flexible verification or stubbing. See also {@link AdditionalMatchers}.
 * <p>
 * {@link Mockito} extends Matchers so to get access to matchers just import Mockito class statically.
 * <pre>
 *  //stubbing using anyInt() argument matcher
 *  stub(mockedList.get(anyInt())).toReturn("element");
 *  
 *  //following prints "element"
 *  System.out.println(mockedList.get(999));
 *  
 *  //you can also verify using argument matcher
 *  verify(mockedList).get(anyInt());
 * </pre>
 * Scroll down to see all methods - full list of matchers.
 * <p>
 * <b>Warning:</b>
 * <p>
 * If you are using argument matchers, <b>all arguments</b> have to be provided by matchers.
 * <p>
 * E.g: (example shows verification but the same applies to stubbing):
 * <pre>
 *   verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 *   //above is correct - eq() is also an argument matcher
 *   
 *   verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 *   //above is incorrect - exception will be thrown because third argument is given without argument matcher.
 * </pre>
 * 
 * <h1>Custom Argument Matchers</h1>
 * 
 * Use {@link Matchers#argThat} method and pass an instance of hamcrest {@link Matcher}.
 * <p>
 * You can use {@link ArgumentMatcher} which is an hamcrest matcher with predefined describeTo() method.
 * In case of failure {@link ArgumentMatcher} generates description based on <b>decamelized class name</b> - to promote meaningful class names.
 * <p>
 * Example:
 * 
 * <pre>
 *   class IsListOfTwoElements extends ArgumentMatcher&lt;List&gt; {
 *      public boolean matches(Object list) {
 *          return ((List) list).size() == 2;
 *      }
 *   }
 *   
 *   List mock = mock(List.class);
 *   
 *   stub(mock.addAll(argThat(new IsListOfTwoElements()))).toReturn(true);
 *   
 *   mock.addAll(Arrays.asList("one", "two"));
 *   
 *   verify(mock).addAll(argThat(new IsListOfTwoElements()));
 * </pre>
 * 
 * To keep it readable you may want to extract method, e.g:
 * <pre>
 *   verify(mock).addAll(<b>argThat(new IsListOfTwoElements())</b>);
 *   //becomes
 *   verify(mock).addAll(<b>listOfTwoElements()</b>);
 * </pre>
 *
 * Custom argument matchers can make the test less readable. 
 * Sometimes it's better to implement equals() for arguments that are passed to mocks 
 * (Mockito naturally uses equals() for argument matching). 
 * This can make the test cleaner.
 */
public class Matchers {

    /**
     * any boolean argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        return reportMatcher(Any.ANY).returnFalse();
    }

    /**
     * any byte argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any char argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static char anyChar() {
        return reportMatcher(Any.ANY).returnChar();
    }

    /**
     * any int argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static int anyInt() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any long argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static long anyLong() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any float argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any double argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any short argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static short anyShort() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any Object argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public static <T> T anyObject() {
        return (T) reportMatcher(Any.ANY).returnNull();
    }

    /**
     * any String argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static String anyString() {
        return isA(String.class);
    }

    /**
     * Object argument that implements the given class. 
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param <T>
     *            the accepted type.
     * @param clazz
     *            the class of the accepted type.
     * @return <code>null</code>.
     */
    public static <T> T isA(Class<T> clazz) {
        return reportMatcher(new InstanceOf(clazz)).<T>returnNull();
    }

    /**
     * boolean argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(boolean value) {
        return reportMatcher(new Equals(value)).returnFalse();
    }

    /**
     * byte argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte eq(byte value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * char argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static char eq(char value) {
        return reportMatcher(new Equals(value)).returnChar();
    }

    /**
     * double argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double eq(double value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * float argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float eq(float value) {
        return reportMatcher(new Equals(value)).returnZero();
    }
    
    /**
     * int argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int eq(int value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * long argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long eq(long value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * short argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short eq(short value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * Object argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(T value) {
        return reportMatcher(new Equals(value)).<T>returnNull();
    }

    /**
     * Object argument that is reflection-equal to the given value.
     * <p>
     * This matcher can be used when equals() is not implemented on compared objects.
     * Matcher uses java reflection API to compare fields of wanted and actual object.
     * <p>
     * Works similarly to EqualsBuilder.reflectionEquals(this, other) from apache commons library.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T refEq(T value) {
        return reportMatcher(new ReflectionEquals(value)).<T>returnNull();
    }
    
    /**
     * Object argument that is the same as the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T same(T value) {
        return reportMatcher(new Same(value)).<T>returnNull();
    }

    /**
     * null argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object isNull() {
        return reportMatcher(Null.NULL).returnNull();
    }

    /**
     * not null argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object notNull() {
        return reportMatcher(NotNull.NOT_NULL).returnNull();
    }

    /**
     * String argument that contains the given substring.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param substring
     *            the substring.
     * @return <code>null</code>.
     */
    public static String contains(String substring) {
        return reportMatcher(new Contains(substring)).<String>returnNull();
    }

    /**
     * String argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String matches(String regex) {
        return reportMatcher(new Matches(regex)).<String>returnNull();
    }

    /**
     * String argument that ends with the given suffix.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param suffix
     *            the suffix.
     * @return <code>null</code>.
     */
    public static String endsWith(String suffix) {
        return reportMatcher(new EndsWith(suffix)).<String>returnNull();
    }

    /**
     * String argument that starts with the given prefix.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param prefix
     *            the prefix.
     * @return <code>null</code>.
     */
    public static String startsWith(String prefix) {
        return reportMatcher(new StartsWith(prefix)).<String>returnNull();
    }

    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>null</code>.
     */
    public static <T> T argThat(Matcher<T> matcher) {
        return reportMatcher(matcher).<T>returnNull();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static char charThat(Matcher<Character> matcher) {
        return reportMatcher(matcher).returnChar();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>false</code>.
     */
    public static boolean booleanThat(Matcher<Boolean> matcher) {
        return reportMatcher(matcher).returnFalse();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static byte byteThat(Matcher<Byte> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static short shortThat(Matcher<Short> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static int intThat(Matcher<Integer> matcher) {
        return reportMatcher(matcher).returnZero();
    }

    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static long longThat(Matcher<Long> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static float floatThat(Matcher<Float> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static double doubleThat(Matcher<Double> matcher) {
        return reportMatcher(matcher).returnZero();
    }

    private static EmptyReturnValues reportMatcher(Matcher<?> matcher) {
        return LastArguments.instance().reportMatcher(matcher);
    }
}