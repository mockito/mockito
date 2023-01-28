/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.Primitives.defaultValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
import org.mockito.internal.util.Primitives;

/**
 * Allow flexible verification or stubbing. See also {@link AdditionalMatchers}.
 *
 * <pre class="code"><code class="java">
 * //stubbing using anyInt() argument matcher
 * when(mockedList.get(anyInt())).thenReturn("element");
 *
 * //following prints "element"
 * System.out.println(mockedList.get(999));
 *
 * //you can also verify using argument matcher
 * verify(mockedList).get(anyInt());
 * </code></pre>
 *
 * <p>
 * Since Mockito <code>any(Class)</code> and <code>anyInt</code> family matchers perform a type check, thus they won't
 * match <code>null</code> arguments. Instead use the <code>isNull</code> matcher.
 *
 * <pre class="code"><code class="java">
 * // stubbing using anyBoolean() argument matcher
 * when(mock.dryRun(anyBoolean())).thenReturn("state");
 *
 * // below the stub won't match, and won't return "state"
 * mock.dryRun(null);
 *
 * // either change the stub
 * when(mock.dryRun(isNull())).thenReturn("state");
 * mock.dryRun(null); // ok
 *
 * // or fix the code ;)
 * when(mock.dryRun(anyBoolean())).thenReturn("state");
 * mock.dryRun(true); // ok
 *
 * </code></pre>
 *
 * The same apply for verification.
 * </p>
 *
 *
 * Scroll down to see all methods - full list of matchers.
 *
 * <p>
 * <b>Warning:</b><br/>
 *
 * If you are using argument matchers, <b>all arguments</b> have to be provided by matchers.
 *
 * E.g: (example shows verification but the same applies to stubbing):
 * </p>
 *
 * <pre class="code"><code class="java">
 * verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 * //above is correct - eq() is also an argument matcher
 *
 * verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 * //above is incorrect - exception will be thrown because third argument is given without argument matcher.
 * </code></pre>
 *
 * <p>
 * Matcher methods like <code>any()</code>, <code>eq()</code> <b>do not</b> return matchers.
 * Internally, they record a matcher on a stack and return a dummy value (usually null).
 * This implementation is due to static type safety imposed by java compiler.
 * The consequence is that you cannot use <code>any()</code>, <code>eq()</code> methods outside of verified/stubbed method.
 * </p>
 *
 * <h1>Additional matchers</h1>
 * <p>
 * The class {@link AdditionalMatchers} offers rarely used matchers, although they can be useful, when
 * it is useful to combine multiple matchers or when it is useful to negate a matcher necessary.
 * </p>
 *
 * <h1>Custom Argument ArgumentMatchers</h1>
 * <p>
 * It is important to understand the use cases and available options for dealing with non-trivial arguments
 * <b>before</b> implementing custom argument matchers. This way, you can select the best possible approach
 * for given scenario and produce highest quality test (clean and maintainable).
 * Please read on in the javadoc for {@link ArgumentMatcher} to learn about approaches and see the examples.
 * </p>
 *
 * @see AdditionalMatchers
 */
@CheckReturnValue
@SuppressWarnings("unchecked")
public class ArgumentMatchers {

    /**
     * Matches <strong>anything</strong>, including nulls and varargs.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * <p>
     * <strong>Notes : </strong><br/>
     * <ul>
     *     <li>For primitive types use {@link #anyChar()} family or {@link #isA(Class)} or {@link #any(Class)}.</li>
     *     <li>Since mockito 2.1.0 {@link #any(Class)} is not anymore an alias of this method.</li>
     * </ul>
     * </p>
     *
     * @return <code>null</code>.
     *
     * @see #any(Class)
     * @see #anyChar()
     * @see #anyInt()
     * @see #anyBoolean()
     */
    public static <T> T any() {
        reportMatcher(Any.ANY);
        return null;
    }

    /**
     * Matches any object of given type, excluding nulls.
     *
     * <p>
     * This matcher will perform a type check with the given type, thus excluding values.
     * See examples in javadoc for {@link ArgumentMatchers} class.
     *
     * This is an alias of: {@link #isA(Class)}}
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null instance of <code></code>, thus <code>null</code> is not anymore a valid value.
     * As reference are nullable, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p><strong>Notes : </strong><br/>
     * <ul>
     *     <li>For primitive types use {@link #anyChar()} family.</li>
     *     <li>Since Mockito 2.1.0 this method will perform a type check thus <code>null</code> values are not authorized.</li>
     *     <li>Since mockito 2.1.0 {@link #any()} is no longer an alias of this method.</li>
     * </ul>
     * </p>
     *
     * @param <T> The accepted type
     * @param type the class of the accepted type.
     * @return <code>null</code>.
     * @see #any()
     * @see #isA(Class)
     * @see #notNull()
     * @see #isNull()
     */
    public static <T> T any(Class<T> type) {
        reportMatcher(new InstanceOf(type, "<any " + type.getCanonicalName() + ">"));
        return defaultValue(type);
    }

    /**
     * <code>Object</code> argument that implements the given class.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param <T>  the accepted type.
     * @param type the class of the accepted type.
     * @return <code>null</code>.
     * @see #any(Class)
     */
    public static <T> T isA(Class<T> type) {
        reportMatcher(new InstanceOf(type));
        return defaultValue(type);
    }

    /**
     * Any <code>boolean</code> or <strong>non-null</strong> <code>Boolean</code>
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Boolean</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>false</code>.
     * @see #isNull()
     */
    public static boolean anyBoolean() {
        reportMatcher(new InstanceOf(Boolean.class, "<any boolean>"));
        return false;
    }

    /**
     * Any <code>byte</code> or <strong>non-null</strong> <code>Byte</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Byte</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static byte anyByte() {
        reportMatcher(new InstanceOf(Byte.class, "<any byte>"));
        return 0;
    }

    /**
     * Any <code>char</code> or <strong>non-null</strong> <code>Character</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Character</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static char anyChar() {
        reportMatcher(new InstanceOf(Character.class, "<any char>"));
        return 0;
    }

    /**
     * Any int or <strong>non-null</strong> <code>Integer</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Integer</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static int anyInt() {
        reportMatcher(new InstanceOf(Integer.class, "<any integer>"));
        return 0;
    }

    /**
     * Any <code>long</code> or <strong>non-null</strong> <code>Long</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Long</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static long anyLong() {
        reportMatcher(new InstanceOf(Long.class, "<any long>"));
        return 0;
    }

    /**
     * Any <code>float</code> or <strong>non-null</strong> <code>Float</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Float</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static float anyFloat() {
        reportMatcher(new InstanceOf(Float.class, "<any float>"));
        return 0;
    }

    /**
     * Any <code>double</code> or <strong>non-null</strong> <code>Double</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Double</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static double anyDouble() {
        reportMatcher(new InstanceOf(Double.class, "<any double>"));
        return 0;
    }

    /**
     * Any <code>short</code> or <strong>non-null</strong> <code>Short</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Short</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see #isNull()
     */
    public static short anyShort() {
        reportMatcher(new InstanceOf(Short.class, "<any short>"));
        return 0;
    }

    /**
     * Any <strong>non-null</strong> <code>String</code>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>String</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty String ("")
     * @see #isNull()
     */
    public static String anyString() {
        reportMatcher(new InstanceOf(String.class, "<any string>"));
        return "";
    }

    /**
     * Any <strong>non-null</strong> <code>List</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>List</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty List.
     * @see #isNull()
     */
    public static <T> List<T> anyList() {
        reportMatcher(new InstanceOf(List.class, "<any List>"));
        return new ArrayList<T>(0);
    }

    /**
     * Any <strong>non-null</strong> <code>Set</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Set</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Set
     * @see #isNull()
     */
    public static <T> Set<T> anySet() {
        reportMatcher(new InstanceOf(Set.class, "<any set>"));
        return new HashSet<T>(0);
    }

    /**
     * Any <strong>non-null</strong> <code>Map</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Map</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Map.
     * @see #isNull()
     */
    public static <K, V> Map<K, V> anyMap() {
        reportMatcher(new InstanceOf(Map.class, "<any map>"));
        return new HashMap<K, V>(0);
    }

    /**
     * Any <strong>non-null</strong> <code>Collection</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Collection</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Collection.
     * @see #isNull()
     */
    public static <T> Collection<T> anyCollection() {
        reportMatcher(new InstanceOf(Collection.class, "<any collection>"));
        return new ArrayList<T>(0);
    }

    /**
     * Any <strong>non-null</strong> <code>Iterable</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Iterable</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Iterable.
     * @see #isNull()
     * @since 2.1.0
     */
    public static <T> Iterable<T> anyIterable() {
        reportMatcher(new InstanceOf(Iterable.class, "<any iterable>"));
        return new ArrayList<T>(0);
    }

    /**
     * <code>boolean</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(boolean value) {
        reportMatcher(new Equals(value));
        return false;
    }

    /**
     * <code>byte</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static byte eq(byte value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>char</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static char eq(char value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>double</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static double eq(double value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>float</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static float eq(float value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>int</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static int eq(int value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>long</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static long eq(long value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>short</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value the given value.
     * @return <code>0</code>.
     */
    public static short eq(short value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Object argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(T value) {
        reportMatcher(new Equals(value));
        if (value == null) return null;
        return (T) Primitives.defaultValue(value.getClass());
    }

    /**
     * Object argument that is reflection-equal to the given value with support for excluding
     * selected fields from a class.
     *
     * <p>
     * This matcher can be used when equals() is not implemented on compared objects.
     * Matcher uses java reflection API to compare fields of wanted and actual object.
     * </p>
     *
     * <p>
     * Works similarly to <code>EqualsBuilder.reflectionEquals(this, other, excludeFields)</code> from
     * apache commons library.
     * <p>
     * <b>Warning</b> The equality check is shallow!
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value         the given value.
     * @param excludeFields fields to exclude, if field does not exist it is ignored.
     * @return <code>null</code>.
     */
    public static <T> T refEq(T value, String... excludeFields) {
        reportMatcher(new ReflectionEquals(value, excludeFields));
        return null;
    }

    /**
     * Object argument that is the same as the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param <T>   the type of the object, it is passed through to prevent casts.
     * @param value the given value.
     * @return <code>null</code>.
     */
    public static <T> T same(T value) {
        reportMatcher(new Same(value));
        if (value == null) {
            return null;
        }
        return (T) Primitives.defaultValue(value.getClass());
    }

    /**
     * <code>null</code> argument.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @return <code>null</code>.
     * @see #isNotNull()
     */
    public static <T> T isNull() {
        reportMatcher(Null.NULL);
        return null;
    }

    /**
     * <code>null</code> argument.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param type the type of the argument being matched.
     * @return <code>null</code>.
     * @see #isNotNull(Class)
     * @since 4.11.0
     */
    public static <T> T isNull(Class<T> type) {
        reportMatcher(new Null<>(type));
        return null;
    }

    /**
     * Not <code>null</code> argument.
     *
     * <p>
     * Alias to {@link ArgumentMatchers#isNotNull()}
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @return <code>null</code>.
     */
    public static <T> T notNull() {
        reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    /**
     * Not <code>null</code> argument.
     *
     * <p>
     * Alias to {@link ArgumentMatchers#isNotNull()}
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param type the type of the argument being matched.
     * @return <code>null</code>.
     * @since 4.11.0
     */
    public static <T> T notNull(Class<T> type) {
        reportMatcher(new NotNull<>(type));
        return null;
    }

    /**
     * Not <code>null</code> argument.
     *
     * <p>
     * Alias to {@link ArgumentMatchers#notNull()}
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @return <code>null</code>.
     * @see #isNull()
     */
    public static <T> T isNotNull() {
        return notNull();
    }

    /**
     * Not <code>null</code> argument.
     *
     * <p>
     * Alias to {@link ArgumentMatchers#notNull(Class)}
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param type the type of the argument being matched.
     * @return <code>null</code>.
     * @see #isNull()
     * @since 4.11.0
     */
    public static <T> T isNotNull(Class<T> type) {
        return notNull(type);
    }

    /**
     * Argument that is either <code>null</code> or of the given type.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type to avoid casting
     * @return <code>null</code>.
     */
    public static <T> T nullable(Class<T> clazz) {
        AdditionalMatchers.or(isNull(), isA(clazz));
        return Primitives.defaultValue(clazz);
    }

    /**
     * <code>String</code> argument that contains the given substring.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param substring the substring.
     * @return empty String ("").
     */
    public static String contains(String substring) {
        reportMatcher(new Contains(substring));
        return "";
    }

    /**
     * <code>String</code> argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param regex the regular expression.
     * @return empty String ("").
     *
     * @see AdditionalMatchers#not(boolean)
     */
    public static String matches(String regex) {
        reportMatcher(new Matches(regex));
        return "";
    }

    /**
     * <code>Pattern</code> argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param pattern the regular expression pattern.
     * @return empty String ("").
     *
     * @see AdditionalMatchers#not(boolean)
     */
    public static String matches(Pattern pattern) {
        reportMatcher(new Matches(pattern));
        return "";
    }

    /**
     * <code>String</code> argument that ends with the given suffix.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param suffix the suffix.
     * @return empty String ("").
     */
    public static String endsWith(String suffix) {
        reportMatcher(new EndsWith(suffix));
        return "";
    }

    /**
     * <code>String</code> argument that starts with the given prefix.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param prefix the prefix.
     * @return empty String ("").
     */
    public static String startsWith(String prefix) {
        reportMatcher(new StartsWith(prefix));
        return "";
    }

    /**
     * Allows creating custom argument matchers.
     *
     * <p>
     * This API has changed in 2.1.0, please read {@link ArgumentMatcher} for rationale and migration guide.
     * <b>NullPointerException</b> auto-unboxing caveat is described below.
     * </p>
     *
     * <p>
     * It is important to understand the use cases and available options for dealing with non-trivial arguments
     * <b>before</b> implementing custom argument matchers. This way, you can select the best possible approach
     * for given scenario and produce highest quality test (clean and maintainable).
     * Please read the documentation for {@link ArgumentMatcher} to learn about approaches and see the examples.
     * </p>
     *
     * <p>
     * <b>NullPointerException</b> auto-unboxing caveat.
     * In rare cases when matching primitive parameter types you <b>*must*</b> use relevant intThat(), floatThat(), etc. method.
     * This way you will avoid <code>NullPointerException</code> during auto-unboxing.
     * Due to how java works we don't really have a clean way of detecting this scenario and protecting the user from this problem.
     * Hopefully, the javadoc describes the problem and solution well.
     * If you have an idea how to fix the problem, let us know via the mailing list or the issue tracker.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatcher} class
     * </p>
     *
     * @param matcher decides whether argument matches
     * @return <code>null</code>.
     */
    public static <T> T argThat(ArgumentMatcher<T> matcher) {
        reportMatcher(matcher);
        return null;
    }

    /**
     * Allows creating custom <code>char</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>char</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static char charThat(ArgumentMatcher<Character> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Allows creating custom <code>boolean</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>boolean</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>false</code>.
     */
    public static boolean booleanThat(ArgumentMatcher<Boolean> matcher) {
        reportMatcher(matcher);
        return false;
    }

    /**
     * Allows creating custom <code>byte</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>byte</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static byte byteThat(ArgumentMatcher<Byte> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Allows creating custom <code>short</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>short</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static short shortThat(ArgumentMatcher<Short> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Allows creating custom <code>int</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>int</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static int intThat(ArgumentMatcher<Integer> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Allows creating custom <code>long</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>long</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static long longThat(ArgumentMatcher<Long> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Allows creating custom <code>float</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>float</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static float floatThat(ArgumentMatcher<Float> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Allows creating custom <code>double</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>double</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static double doubleThat(ArgumentMatcher<Double> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
