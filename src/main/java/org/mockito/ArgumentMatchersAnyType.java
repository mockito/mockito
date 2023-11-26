/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.util.Primitives;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.Primitives.defaultValue;

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
public class ArgumentMatchersAnyType {

    /**
     * Matches <strong>anything</strong>, including nulls.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class
     * </p>
     *
     * <p>
     * <strong>Notes : </strong><br/>
     * <ul>
     *     <li>For primitive types use {@link #anyChar()} family or {@link ArgumentMatchers##isA(Class)} or {@link #any(Class)}.</li>
     *     <li>Since Mockito 2.1.0 {@link #any(Class)} is not anymore an alias of this method.</li>
     *     <li>Since Mockito 5.0.0 this no longer matches varargs. Use {@link #any(Class)} instead.</li>
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
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     *
     * This is an alias of: {@link ArgumentMatchers##isA(Class)}}
     * </p>
     *
     * <p>
     * Since Mockito 2.1.0, only allow non-null instance of <code></code>, thus <code>null</code> is not anymore a valid value.
     * As reference are nullable, the suggested API to <strong>match</strong> <code>null</code>
     * would be {@link ArgumentMatchers##isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p><strong>Notes : </strong><br/>
     * <ul>
     *     <li>For primitive types use {@link #anyChar()} family.</li>
     *     <li>Since Mockito 2.1.0 this method will perform a type check thus <code>null</code> values are not authorized.</li>
     *     <li>Since Mockito 2.1.0 {@link #any()} is no longer an alias of this method.</li>
     *     <li>Since Mockito 5.0.0 this method can match varargs if the array type is specified, for example <code>any(String[].class)</code>.</li>
     * </ul>
     * </p>
     *
     * @param <T> The accepted type
     * @param type the class of the accepted type.
     * @return <code>null</code>.
     * @see #any()
     * @see ArgumentMatchers#isA(Class)
     * @see ArgumentMatchers#notNull()
     * @see ArgumentMatchers#isNull()
     */
    public static <T> T any(Class<T> type) {
        reportMatcher(new InstanceOf(type, "<any " + type.getCanonicalName() + ">"));
        return defaultValue(type);
    }

    /**
     * Any <code>boolean</code> or <strong>non-null</strong> <code>Boolean</code>
     *
     * <p>
     * Since Mockito 2.1.0, only allow valued <code>Boolean</code>, thus <code>null</code> is not anymore a valid value.
     * As primitive wrappers are nullable, the suggested API to <strong>match</strong> <code>null</code> wrapper
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>false</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return <code>0</code>.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return empty String ("")
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return empty List.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return empty Set
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return empty Map.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return empty Collection.
     * @see ArgumentMatchers#isNull()
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
     * would be {@link ArgumentMatchers#isNull()}. We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchersAnyType} class.
     * </p>
     *
     * @return empty Iterable.
     * @see ArgumentMatchers#isNull()
     * @since 2.1.0
     */
    public static <T> Iterable<T> anyIterable() {
        reportMatcher(new InstanceOf(Iterable.class, "<any iterable>"));
        return new ArrayList<T>(0);
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
