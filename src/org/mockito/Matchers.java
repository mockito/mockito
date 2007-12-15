/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

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
import org.mockito.internal.progress.LastArguments;

/**
 * Argument matchers
 * <p>
 * TODO where is that documentation?
 * Read more about matchers: http://code.google.com/p/mockito/matchers
 */
public class Matchers {

    /**
     * any boolean argument.
     * 
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return false;
    }

    /**
     * any byte argument.
     * 
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any char argument.
     * 
     * @return <code>0</code>.
     */
    public static char anyChar() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any int argument.
     * 
     * @return <code>0</code>.
     */
    public static int anyInt() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any long argument.
     * 
     * @return <code>0</code>.
     */
    public static long anyLong() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any float argument.
     * 
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any double argument.
     * 
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any short argument.
     * 
     * @return <code>0</code>.
     */
    public static short anyShort() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * any Object argument.
     * 
     * @return <code>null</code>.
     */
    public static Object anyObject() {
        LastArguments.instance().reportMatcher(Any.ANY);
        return null;
    }

    /**
     * any String argument.
     * 
     * @return <code>null</code>.
     */
    public static String anyString() {
        isA(String.class);
        return null;
    }

    /**
     * Object argument that implements the given class. 
     * 
     * @param <T>
     *            the accepted type.
     * @param clazz
     *            the class of the accepted type.
     * @return <code>null</code>.
     */
    public static <T> T isA(Class<T> clazz) {
        LastArguments.instance().reportMatcher(new InstanceOf(clazz));
        return null;
    }

    /**
     * boolean argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(boolean value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return false;
    }

    /**
     * byte argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte eq(byte value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * char argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static char eq(char value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * double argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double eq(double value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * float argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float eq(float value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * int argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int eq(int value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * long argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long eq(long value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * short argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short eq(short value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Object argument that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(T value) {
        LastArguments.instance().reportMatcher(new Equals(value));
        return null;
    }

    /**
     * Object argument that is the same as the given value.
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
     * null argument.
     * 
     * @return <code>null</code>.
     */
    public static Object isNull() {
        LastArguments.instance().reportMatcher(Null.NULL);
        return null;
    }

    /**
     * not null argument.
     * 
     * @return <code>null</code>.
     */
    public static Object notNull() {
        LastArguments.instance().reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    /**
     * String argument that contains the given substring.
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
     * String argument that matches the given regular expression.
     * 
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String matches(String regex) {
        LastArguments.instance().reportMatcher(new Matches(regex));
        return null;
    }

    /**
     * String argument that ends with the given suffix.
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
     * String argument that starts with the given prefix.
     * 
     * @param prefix
     *            the prefix.
     * @return <code>null</code>.
     */
    public static String startsWith(String prefix) {
        LastArguments.instance().reportMatcher(new StartsWith(prefix));
        return null;
    }
}
