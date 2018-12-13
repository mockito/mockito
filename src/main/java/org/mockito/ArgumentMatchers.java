/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.util.*;
import java.util.regex.Pattern;

import static org.mockito.internal.matchers.MatcherMarkers.genericMarker;
import static org.mockito.internal.matchers.MatcherMarkers.markerOf;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

/**
 * Allow flexible verification or stubbing. See also {@link AdditionalMatchers}.
 *
 * <p>
 * {@link Mockito} extends ArgumentMatchers so to get access to all matchers just import Mockito class statically.
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
 * <p>
 * The same apply for verification.
 * </p>
 * <p>
 * <p>
 * Scroll down to see all methods - full list of matchers.
 *
 * <p>
 * <b>Warning:</b><br/>
 * <p>
 * One can mix concrete arguments with argument-matchers; however, note that there are some exceptions.
 * Since <code>null</code> is used as a "marker" value by many argument-matchers, it is impossible to mix concrete
 * <code>null</code>s with other argument matchers; use <code>isNull</code> instead.
 * In some edge cases, concrete arguments may collide with the "marker" values for the primitive types (which are quite
 * arbitrary). In such cases, one must use concrete arguments only or argument-matchers only.
 * <p>
 * E.g: (example shows verification but the same applies to stubbing):
 * </p>
 *
 * <pre class="code"><code class="java">
 * verify(mock).someMethod(anyInt(), anyString(), "third argument");
 * //above is correct
 *
 * verify(mock).someMethod(anyInt(), anyString(), <b>null</b>);
 * //above is incorrect - exception will be thrown because third argument is a concrete null mixed with matchers.
 *
 * verify(mock).someMethod(<b>13981398</b>, anyString(), "third argument");
 * //above is incorrect - exception will be thrown because first argument happens to be the marker value for int.
 * </code></pre>
 *
 * <p>
 * Matcher methods like <code>anyObject()</code>, <code>eq()</code> <b>do not</b> return matchers.
 * Internally, they record a matcher on a stack and return a "marker" value (usually null).
 * This implementation is due to static type safety imposed by java compiler.
 * The consequence is that you cannot use <code>anyObject()</code>, <code>eq()</code> methods outside of verified/stubbed method.
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
@SuppressWarnings("unchecked")
public class ArgumentMatchers {

    /**
     * Matches <strong>anything</strong>, including nulls and varargs.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * <p>
     * This is an alias of: {@link #anyObject()} and {@link #any(java.lang.Class)}
     * </p>
     *
     * <p>
     * <strong>Notes : </strong><br/>
     * <ul>
     * <li>For primitive types use {@link #anyChar()} family or {@link #isA(Class)} or {@link #any(Class)}.</li>
     * <li>Since mockito 2.1.0 {@link #any(Class)} is not anymore an alias of this method.</li>
     * </ul>
     * </p>
     *
     * @return A marker value.
     * @see #any(Class)
     * @see #anyObject()
     * @see #anyVararg()
     * @see #anyChar()
     * @see #anyInt()
     * @see #anyBoolean()
     * @see #anyCollectionOf(Class)
     */
    public static <T> T any() {
        return anyObject();
    }

    /**
     * Matches anything, including <code>null</code>.
     *
     * <p>
     * This is an alias of: {@link #any()} and {@link #any(java.lang.Class)}.
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #any()
     * @see #any(Class)
     * @see #notNull()
     * @see #notNull(Class)
     * @deprecated This will be removed in Mockito 3.0 (which will be java 8 only)
     */
    @Deprecated
    public static <T> T anyObject() {
        reportMatcher(Any.ANY);
        return (T) genericMarker();
    }

    /**
     * Matches any object of given type, excluding nulls.
     *
     * <p>
     * This matcher will perform a type check with the given type, thus excluding values.
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * <p>
     * This is an alias of: {@link #isA(Class)}}
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null instance of <code></code>, thus <code>null</code> is not anymore a valid value.
     * As reference are nullable, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p><strong>Notes : </strong><br/>
     * <ul>
     * <li>For primitive types use {@link #anyChar()} family.</li>
     * <li>Since Mockito 2.1.0 this method will perform a type check thus <code>null</code> values are not authorized.</li>
     * <li>Since mockito 2.1.0 {@link #any()} and {@link #anyObject()} are not anymore aliases of this method.</li>
     * </ul>
     * </p>
     *
     * @param <T>  The accepted type
     * @param type the class of the accepted type.
     * @return A marker value.
     * @see #any()
     * @see #anyObject()
     * @see #anyVararg()
     * @see #isA(Class)
     * @see #notNull()
     * @see #notNull(Class)
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <T> T any(Class<T> type) {
        reportMatcher(new InstanceOf.VarArgAware(type, "<any " + type.getCanonicalName() + ">"));
        return markerOf(type);
    }

    /**
     * <code>Object</code> argument that implements the given class.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param <T>  the accepted type.
     * @param type the class of the accepted type.
     * @return A marker value.
     * @see #any(Class)
     */
    public static <T> T isA(Class<T> type) {
        reportMatcher(new InstanceOf(type));
        return markerOf(type);
    }

    /**
     * Any vararg, meaning any number and values of arguments.
     *
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     * //verification:
     * mock.foo(1, 2);
     * mock.foo(1, 2, 3, 4);
     *
     * verify(mock, times(2)).foo(anyVararg());
     *
     * //stubbing:
     * when(mock.foo(anyVararg()).thenReturn(100);
     *
     * //prints 100
     * System.out.println(mock.foo(1, 2));
     * //also prints 100
     * System.out.println(mock.foo(1, 2, 3, 4));
     * </code></pre>
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #any()
     * @see #any(Class)
     * @deprecated as of 2.1.0 use {@link #any()}
     */
    @Deprecated
    public static <T> T anyVararg() {
        return any();
    }

    /**
     * Any <code>boolean</code> or <strong>non-null</strong> <code>Boolean</code>
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Boolean</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static boolean anyBoolean() {
        reportMatcher(new InstanceOf(Boolean.class, "<any boolean>"));
        return markerOf(boolean.class);
    }

    /**
     * Any <code>byte</code> or <strong>non-null</strong> <code>Byte</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Byte</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static byte anyByte() {
        reportMatcher(new InstanceOf(Byte.class, "<any byte>"));
        return markerOf(byte.class);
    }

    /**
     * Any <code>char</code> or <strong>non-null</strong> <code>Character</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Character</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static char anyChar() {
        reportMatcher(new InstanceOf(Character.class, "<any char>"));
        return markerOf(char.class);
    }

    /**
     * Any int or <strong>non-null</strong> <code>Integer</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Integer</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static int anyInt() {
        reportMatcher(new InstanceOf(Integer.class, "<any integer>"));
        return markerOf(int.class);
    }

    /**
     * Any <code>long</code> or <strong>non-null</strong> <code>Long</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Long</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static long anyLong() {
        reportMatcher(new InstanceOf(Long.class, "<any long>"));
        return markerOf(long.class);
    }

    /**
     * Any <code>float</code> or <strong>non-null</strong> <code>Float</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Float</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static float anyFloat() {
        reportMatcher(new InstanceOf(Float.class, "<any float>"));
        return markerOf(float.class);
    }

    /**
     * Any <code>double</code> or <strong>non-null</strong> <code>Double</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Double</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static double anyDouble() {
        reportMatcher(new InstanceOf(Double.class, "<any double>"));
        return markerOf(double.class);
    }

    /**
     * Any <code>short</code> or <strong>non-null</strong> <code>Short</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Short</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static short anyShort() {
        reportMatcher(new InstanceOf(Short.class, "<any short>"));
        return markerOf(short.class);
    }

    /**
     * Any <strong>non-null</strong> <code>String</code>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>String</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static String anyString() {
        reportMatcher(new InstanceOf(String.class, "<any string>"));
        return markerOf(String.class);
    }

    /**
     * Any <strong>non-null</strong> <code>List</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>List</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #anyListOf(Class)
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <T> List<T> anyList() {
        reportMatcher(new InstanceOf(List.class, "<any List>"));
        return markerOf(List.class);
    }

    /**
     * Any <strong>non-null</strong> <code>List</code>.
     * <p>
     * Generic friendly alias to {@link ArgumentMatchers#anyList()}. It's an alternative to
     * <code>&#064;SuppressWarnings("unchecked")</code> to keep code clean of compiler warnings.
     *
     * <p>
     * This method doesn't do type checks of the list content with the given type parameter, it is only there
     * to avoid casting in the code.
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>List</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @param clazz Type owned by the list to avoid casting
     * @return A marker value.
     * @see #anyList()
     * @see #isNull()
     * @see #isNull(Class)
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> List<T> anyListOf(Class<T> clazz) {
        return anyList();
    }

    /**
     * Any <strong>non-null</strong> <code>Set</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Set</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #anySetOf(Class)
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <T> Set<T> anySet() {
        reportMatcher(new InstanceOf(Set.class, "<any set>"));
        return markerOf(Set.class);
    }

    /**
     * Any <strong>non-null</strong> <code>Set</code>.
     *
     * <p>
     * Generic friendly alias to {@link ArgumentMatchers#anySet()}.
     * It's an alternative to <code>&#064;SuppressWarnings("unchecked")</code> to keep code clean of compiler warnings.
     * </p>
     *
     * <p>
     * This method doesn't do type checks of the set content with the given type parameter, it is only there
     * to avoid casting in the code.
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Set</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @param clazz Type owned by the Set to avoid casting
     * @return A marker value.
     * @see #anySet()
     * @see #isNull()
     * @see #isNull(Class)
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> Set<T> anySetOf(Class<T> clazz) {
        return anySet();
    }

    /**
     * Any <strong>non-null</strong> <code>Map</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Map</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #anyMapOf(Class, Class)
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <K, V> Map<K, V> anyMap() {
        reportMatcher(new InstanceOf(Map.class, "<any map>"));
        return markerOf(Map.class);
    }

    /**
     * Any <strong>non-null</strong> <code>Map</code>.
     *
     * <p>
     * Generic friendly alias to {@link ArgumentMatchers#anyMap()}.
     * It's an alternative to <code>&#064;SuppressWarnings("unchecked")</code> to keep code clean of compiler warnings.
     * </p>
     *
     * <p>
     * This method doesn't do type checks of the map content with the given type parameter, it is only there
     * to avoid casting in the code.
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Map</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @param keyClazz   Type of the map key to avoid casting
     * @param valueClazz Type of the value to avoid casting
     * @return empty Map.
     * @see #anyMap()
     * @see #isNull()
     * @see #isNull(Class)
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <K, V> Map<K, V> anyMapOf(Class<K> keyClazz, Class<V> valueClazz) {
        return anyMap();
    }

    /**
     * Any <strong>non-null</strong> <code>Collection</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Collection</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #anyCollectionOf(Class)
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <T> Collection<T> anyCollection() {
        reportMatcher(new InstanceOf(Collection.class, "<any collection>"));
        return markerOf(Collection.class);
    }

    /**
     * Any <strong>non-null</strong> <code>Collection</code>.
     *
     * <p>
     * Generic friendly alias to {@link ArgumentMatchers#anyCollection()}.
     * It's an alternative to <code>&#064;SuppressWarnings("unchecked")</code> to keep code clean of compiler warnings.
     * </p>
     *
     * <p>
     * This method doesn't do type checks of the collection content with the given type parameter, it is only there
     * to avoid casting in the code.
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Collection</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @param clazz Type owned by the collection to avoid casting
     * @return A marker value.
     * @see #anyCollection()
     * @see #isNull()
     * @see #isNull(Class)
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> Collection<T> anyCollectionOf(Class<T> clazz) {
        return anyCollection();
    }

    /**
     * Any <strong>non-null</strong> <code>Iterable</code>.
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>Iterable</code>.
     * As this is a nullable reference, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return A marker value.
     * @see #anyIterableOf(Class)
     * @see #isNull()
     * @see #isNull(Class)
     * @since 2.1.0
     */
    public static <T> Iterable<T> anyIterable() {
        reportMatcher(new InstanceOf(Iterable.class, "<any iterable>"));
        return markerOf(Iterable.class);
    }

    /**
     * Any <strong>non-null</strong> <code>Iterable</code>.
     *
     * <p>
     * Generic friendly alias to {@link ArgumentMatchers#anyIterable()}.
     * It's an alternative to <code>&#064;SuppressWarnings("unchecked")</code> to keep code clean of compiler warnings.
     * </p>
     *
     * <p>
     * This method doesn't do type checks of the iterable content with the given type parameter, it is only there
     * to avoid casting in the code.
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null <code>String</code>.
     * As strings are nullable reference, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link #isNull()}. We felt this change would make tests harness much safer that it was with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @param clazz Type owned by the collection to avoid casting
     * @return A marker value.
     * @see #anyIterable()
     * @see #isNull()
     * @see #isNull(Class)
     * @since 2.1.0
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> Iterable<T> anyIterableOf(Class<T> clazz) {
        return anyIterable();
    }


    /**
     * <code>boolean</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static boolean eq(boolean value) {
        reportMatcher(new Equals(value));
        return markerOf(boolean.class);
    }

    /**
     * <code>byte</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static byte eq(byte value) {
        reportMatcher(new Equals(value));
        return markerOf(byte.class);
    }

    /**
     * <code>char</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static char eq(char value) {
        reportMatcher(new Equals(value));
        return markerOf(char.class);
    }

    /**
     * <code>double</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static double eq(double value) {
        reportMatcher(new Equals(value));
        return markerOf(double.class);
    }

    /**
     * <code>float</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static float eq(float value) {
        reportMatcher(new Equals(value));
        return markerOf(float.class);
    }

    /**
     * <code>int</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static int eq(int value) {
        reportMatcher(new Equals(value));
        return markerOf(int.class);
    }

    /**
     * <code>long</code> argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static long eq(long value) {
        reportMatcher(new Equals(value));
        return markerOf(long.class);
    }

    /**
     * <code>short</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static short eq(short value) {
        reportMatcher(new Equals(value));
        return markerOf(short.class);
    }

    /**
     * Object argument that is equal to the given value.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param value the given value.
     * @return A marker value.
     */
    public static <T> T eq(T value) {
        reportMatcher(new Equals(value));
        return (T) markerOf(value);
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
     * @return A marker value.
     */
    public static <T> T refEq(T value, String... excludeFields) {
        reportMatcher(new ReflectionEquals(value, excludeFields));
        return (T) markerOf(value);
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
     * @return A marker value.
     */
    public static <T> T same(T value) {
        reportMatcher(new Same(value));
        return (T) markerOf(value);
    }

    /**
     * <code>null</code> argument.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @return A marker value.
     * @see #isNull(Class)
     * @see #isNotNull()
     * @see #isNotNull(Class)
     */
    public static <T> T isNull() {
        reportMatcher(Null.NULL);
        return (T) genericMarker();
    }

    /**
     * <code>null</code> argument.
     *
     * <p>
     * The class argument is provided to avoid casting.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type to avoid casting
     * @return A marker value.
     * @see #isNull()
     * @see #isNotNull()
     * @see #isNotNull(Class)
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> T isNull(Class<T> clazz) {
        return isNull();
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
     * @return A marker value.
     */
    public static <T> T notNull() {
        reportMatcher(NotNull.NOT_NULL);
        return (T) genericMarker();
    }

    /**
     * Not <code>null</code> argument, not necessary of the given class.
     *
     * <p>
     * The class argument is provided to avoid casting.
     * <p>
     * Alias to {@link ArgumentMatchers#isNotNull(Class)}
     * <p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type to avoid casting
     * @return A marker value.
     * @see #isNotNull()
     * @see #isNull()
     * @see #isNull(Class)
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> T notNull(Class<T> clazz) {
        return notNull();
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
     * @return A marker value.
     * @see #isNotNull(Class)
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <T> T isNotNull() {
        return notNull();
    }

    /**
     * Not <code>null</code> argument, not necessary of the given class.
     *
     * <p>
     * The class argument is provided to avoid casting.
     * Alias to {@link ArgumentMatchers#notNull(Class)}
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type to avoid casting
     * @return A marker value.
     * @deprecated With Java 8 this method will be removed in Mockito 3.0. This method is only used for generic
     * friendliness to avoid casting, this is not anymore needed in Java 8.
     */
    public static <T> T isNotNull(Class<T> clazz) {
        return notNull(clazz);
    }


    /**
     * Argument that is either <code>null</code> or of the given type.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type to avoid casting
     * @return A marker value.
     */
    public static <T> T nullable(Class<T> clazz) {
        AdditionalMatchers.or(isNull(), isA(clazz));
        return markerOf(clazz);
    }

    /**
     * <code>String</code> argument that contains the given substring.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param substring the substring.
     * @return A marker value.
     */
    public static String contains(String substring) {
        reportMatcher(new Contains(substring));
        return markerOf(String.class);
    }

    /**
     * <code>String</code> argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param regex the regular expression.
     * @return A marker value.
     * @see AdditionalMatchers#not(boolean)
     */
    public static String matches(String regex) {
        reportMatcher(new Matches(regex));
        return markerOf(String.class);
    }

    /**
     * <code>Pattern</code> argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param pattern the regular expression pattern.
     * @return A marker value.
     * @see AdditionalMatchers#not(boolean)
     */
    public static String matches(Pattern pattern) {
        reportMatcher(new Matches(pattern));
        return markerOf(String.class);
    }

    /**
     * <code>String</code> argument that ends with the given suffix.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param suffix the suffix.
     * @return A marker value.
     */
    public static String endsWith(String suffix) {
        reportMatcher(new EndsWith(suffix));
        return markerOf(String.class);
    }

    /**
     * <code>String</code> argument that starts with the given prefix.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param prefix the prefix.
     * @return A marker value.
     */
    public static String startsWith(String prefix) {
        reportMatcher(new StartsWith(prefix));
        return markerOf(String.class);
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
     * @return A marker value.
     */
    public static <T> T argThat(ArgumentMatcher<T> matcher) {
        reportMatcher(matcher);
        return (T) genericMarker();
    }

    /**
     * Allows creating custom <code>char</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>char</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static char charThat(ArgumentMatcher<Character> matcher) {
        reportMatcher(matcher);
        return markerOf(char.class);
    }

    /**
     * Allows creating custom <code>boolean</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>boolean</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static boolean booleanThat(ArgumentMatcher<Boolean> matcher) {
        reportMatcher(matcher);
        return markerOf(boolean.class);
    }

    /**
     * Allows creating custom <code>byte</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>byte</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static byte byteThat(ArgumentMatcher<Byte> matcher) {
        reportMatcher(matcher);
        return markerOf(byte.class);
    }

    /**
     * Allows creating custom <code>short</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>short</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static short shortThat(ArgumentMatcher<Short> matcher) {
        reportMatcher(matcher);
        return markerOf(short.class);
    }

    /**
     * Allows creating custom <code>int</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>int</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static int intThat(ArgumentMatcher<Integer> matcher) {
        reportMatcher(matcher);
        return markerOf(int.class);
    }

    /**
     * Allows creating custom <code>long</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>long</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static long longThat(ArgumentMatcher<Long> matcher) {
        reportMatcher(matcher);
        return markerOf(long.class);
    }

    /**
     * Allows creating custom <code>float</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>float</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static float floatThat(ArgumentMatcher<Float> matcher) {
        reportMatcher(matcher);
        return markerOf(float.class);
    }

    /**
     * Allows creating custom <code>double</code> argument matchers.
     * <p>
     * Note that {@link #argThat} will not work with primitive <code>double</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @param matcher decides whether argument matches
     * @return A marker value.
     */
    public static double doubleThat(ArgumentMatcher<Double> matcher) {
        reportMatcher(matcher);
        return markerOf(double.class);
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
