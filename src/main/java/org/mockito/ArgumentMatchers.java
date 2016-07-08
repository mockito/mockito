/* 
* Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import java.util.Arrays;

import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.EqualsWithDelta;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.LessThan;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Or;
import org.mockito.internal.matchers.StartsWith;

public class ArgumentMatchers {

    /**
     * argument greater than or equal to the given value.
     * 
     * @param value
     *            the given value.
     * @return {@link ArgumentMatcher}.
     */
    public static <T extends Comparable<T>> ArgumentMatcher<T> geq(Comparable<T> value) {
        return new GreaterOrEqual<T>(value);
    }

    /**
     * comparable argument less than or equal than the given value details.
     * 
     * @param value
     *            the given value.
     * @return {@link ArgumentMatcher}.
     */
    public static <T extends Comparable<T>> ArgumentMatcher<T> leq(Comparable<T> value) {
        return new LessOrEqual<T>(value);
    }

    /**
     * comparable argument greater than the given value.
     * 
     * @param value
     *            the given value.
     * @return {@link ArgumentMatcher}.
     */
    public static <T extends Comparable<T>> ArgumentMatcher<T> gt(Comparable<T> value) {
        return new GreaterThan<T>(value);
    }

    /**
     * comparable argument less than the given value.
     * 
     * @param value
     *            the given value.
     * @return {@link ArgumentMatcher}.
     */
    public static <T extends Comparable<T>> ArgumentMatcher<T> lt(Comparable<T> value) {
        return new LessThan<T>(value);
    }

    /**
     * comparable argument equals to than the given value according to their
     * compareTo method.
     * 
     * @param value
     *            the given value.
     * @return {@link ArgumentMatcher}.
     */
    public static <T extends Comparable<T>> ArgumentMatcher<T> cmpEq(Comparable<T> value) {
        return new CompareEqual<T>(value);
    }

    /**
     * String argument that contains a substring that matches the given regular
     * expression.
     * 
     * @param regex
     *            the regular expression.
     * @return {@link ArgumentMatcher}.
     */
    public static ArgumentMatcher<String> find(String regex) {
        return new Find(regex);
    }

    /**
     * argument that is equal to the given value
     * 
     * @param value
     *            the given value.
     * @return {@link ArgumentMatcher}.
     */
    public static ArgumentMatcher eq(Object value) {
        return new Equals(value);
    }

    /**
     * Object array argument that is equal to the given array, i.e. it has to
     * have the same type, length, and each element has to be equal.
     * 
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given array.
     * @return {@link ArgumentMatcher}.
     */
    public static <T> ArgumentMatcher aryEq(T[] value) {
        return new ArrayEquals(value);
    }

    /**
     * number argument that has an absolute difference to the given value that
     * is less than the given delta details.
     * 
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return {@link ArgumentMatcher}.
     */
    public static <T extends Number> ArgumentMatcher eq(T value, T delta) {
        return new EqualsWithDelta(value, delta);
    }

    /**
     * Object argument that is not null
     * 
     * @return {@link ArgumentMatcher}.
     */
    public static ArgumentMatcher isNotNull() {
        return NotNull.NOT_NULL;
    }

    /**
     * String argument that contains the given substring
     * 
     * @param substring 
     *              the given substring
     * @return
     *      * @return {@link ArgumentMatcher}.
     */
    public static ArgumentMatcher<String> contains(String substring) {
        return new Contains(substring);
    }

    /**
     * String argument that starts with the given prefix.
     * 
     * @param prefix
     *            the prefix.
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher<String> startsWith(String prefix) {
        return new StartsWith(prefix);
    }

    /**
     * String argument that ends with the given suffix.
     * 
     * @param suffix
     *            the suffix.
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher<String> endsWith(String suffix) {
        return new EndsWith(suffix);
    }

    /**
     * Argument that is instanceOf the given argument
     * @param clazz the given class type
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher isA(Class clazz) {
        return new InstanceOf(clazz);
    }

    /**
     * Matches any type of argument
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher any() {
        return Any.ANY;
    }

    /**
     * Matches null arguments
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher isNull() {
        return Null.NULL;
    }

    /**
     * Performs an and operation between matchers
     * @param matchers the matchers that have to match the arguments
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher and(ArgumentMatcher ...matchers) {
        return new And(Arrays.asList(matchers));
    }

    /**
     * Performs an or operation between matchers
     * @param matchers the matchers that have to match the arguments
     * @return {@link ArgumentMatcher}
     */
    public static ArgumentMatcher or(ArgumentMatcher ...matchers) {
        return new Or(Arrays.asList(matchers));
    }

}
