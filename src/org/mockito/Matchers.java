package org.mockito;

import org.mockito.internal.LastArguments;
import org.mockito.internal.matchers.*;

public class Matchers {
    /**
     * Expects any boolean argument. For details, see the EasyMock
     * documentation.
     * 
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        LastArguments.reportMatcher(Any.ANY);
        return false;
    }

    /**
     * Expects any byte argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any char argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static char anyChar() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any int argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static int anyInt() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any long argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static long anyLong() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any float argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any double argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any short argument. For details, see the EasyMock documentation.
     * 
     * @return <code>0</code>.
     */
    public static short anyShort() {
        LastArguments.reportMatcher(Any.ANY);
        return 0;
    }

    /**
     * Expects any Object argument. For details, see the EasyMock documentation.
     * 
     * @return <code>null</code>.
     */
    public static Object anyObject() {
        LastArguments.reportMatcher(Any.ANY);
        return null;
    }

    /**
     * Expects an object implementing the given class. For details, see the
     * EasyMock documentation.
     * 
     * @param <T>
     *            the accepted type.
     * @param clazz
     *            the class of the accepted type.
     * @return <code>null</code>.
     */
    public static <T> T isA(Class<T> clazz) {
        LastArguments.reportMatcher(new InstanceOf(clazz));
        return null;
    }

    /**
     * Expects a boolean that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(boolean value) {
        LastArguments.reportMatcher(new Equals(value));
        return false;
    }

    /**
     * Expects a byte that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte eq(byte value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a char that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static char eq(char value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a double that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double eq(double value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a float that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float eq(float value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects an int that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int eq(int value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a long that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long eq(long value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects a short that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short eq(short value) {
        LastArguments.reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Expects an Object that is equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(T value) {
        LastArguments.reportMatcher(new Equals(value));
        return null;
    }

    /**
     * Expects null.
     * 
     * @return <code>null</code>.
     */
    public static Object isNull() {
        LastArguments.reportMatcher(Null.NULL);
        return null;
    }

    /**
     * Expects not null.
     * 
     * @return <code>null</code>.
     */
    public static Object notNull() {
        LastArguments.reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    /**
     * Expects a string that matches the given regular expression. For details,
     * see the EasyMock documentation.
     * 
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String matches(String regex) {
        LastArguments.reportMatcher(new Matches(regex));
        return null;
    }

    /**
     * Expects a double that has an absolute difference to the given value that
     * is less than the given delta. For details, see the EasyMock
     * documentation.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static double eq(double value, double delta) {
        LastArguments.reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }

    /**
     * Expects a float that has an absolute difference to the given value that
     * is less than the given delta. For details, see the EasyMock
     * documentation.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static float eq(float value, float delta) {
        LastArguments.reportMatcher(new EqualsWithDelta(value, delta));
        return 0;
    }
}
