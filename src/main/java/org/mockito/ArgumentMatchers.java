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
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.util.Primitives;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.Primitives.defaultValue;

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
 * Matcher methods like <code>anyObject()</code>, <code>eq()</code> <b>do not</b> return matchers.
 * Internally, they record a matcher on a stack and return a dummy value (usually null).
 * This implementation is due static type safety imposed by java compiler.
 * The consequence is that you cannot use <code>anyObject()</code>, <code>eq()</code> methods outside of verified/stubbed method.
 * </p>
 *
 * <h1>Custom Argument ArgumentMatchers</h1>
 * <p>
 * It is important to understand the use cases and available options for dealing with non-trivial arguments
 * <b>before</b> implementing custom argument matchers. This way, you can select the best possible approach
 * for given scenario and produce highest quality test (clean and maintainable).
 * Please read on in the javadoc for {@link ArgumentMatcher} to learn about approaches and see the examples.
 * </p>
 */
@SuppressWarnings("unchecked")
public class ArgumentMatchers {

    /**
     * Matches <strong>anything</strong>, including nulls and varargs.
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * This is an alias of: {@link #anyObject()} and {@link #any(java.lang.Class)}
     * </p>
     *
     * <p>
     * <strong>Notes : </strong><br/>
     * <ul>
     *     <li>For primitive types use {@link #anyChar()} family or {@link #isA(Class)} or {@link #any(Class)}.</li>
     *     <li>Since mockito 2.0 {@link #any(Class)} is not anymore an alias of this method.</li>
     * </ul>
     * </p>
     *
     * @return <code>null</code>.
     *
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
     * @return <code>null</code>.
     * @see #any()
     * @see #any(Class)
     * @see #notNull()
     */
    public static <T> T anyObject() {
        reportMatcher(Any.ANY);
        return null;
    }

    /**
     * Matches any object of given type, excluding nulls.
     *
     * <p>
     * This matcher will perform a type check with the given type, thus excluding values.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class.
     *
     * This is an alias of: {@link #isA(Class)}}
     * </p>
     *
     * <p><strong>Notes : </strong><br/>
     * <ul>
     *     <li>For primitive types use {@link #anyChar()} family.</li>
     *     <li>Since Mockito 2.0 this method will perform a type check thus <code>null</code> values are not authorized.</li>
     *     <li>Since mockito 2.0 {@link #any()} and {@link #anyObject()} are not anymore aliases of this method.</li>
     * </ul>
     * </p>
     *
     * @return <code>null</code>.
     * @see #any()
     * @see #anyObject()
     * @see #anyVararg()
     * @see #isA(Class)
     * @see #notNull()
     */
    public static <T> T any(Class<T> type) {
        reportMatcher(new InstanceOf.VarArgAware(type, "<any " + type.getCanonicalName() + ">"));
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
     */
    public static <T> T isA(Class<T> type) {
        reportMatcher(new InstanceOf(type));
        return defaultValue(type);
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
     * @return <code>null</code>.
     * @see #any()
     * @see #any(Class)
     * @deprecated as of 2.0 use {@link #any()
     */
    @Deprecated
    public static <T> T anyVararg() {
        any();
        return null;
    }

    /**
     * Any <code>boolean</code> or <strong>non-null</strong> <code>Boolean</code>
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        reportMatcher(new InstanceOf(Boolean.class));
        return false;
    }

    /**
     * Any <code>byte</code> or <strong>non-null</strong> <code>Byte</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        reportMatcher(new InstanceOf(Byte.class));
        return 0;
    }

    /**
     * Any <code>char</code> or <strong>non-null</strong> <code>Character</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static char anyChar() {
        reportMatcher(new InstanceOf(Character.class));
        return 0;
    }

    /**
     * Any int or <strong>non-null</strong> Integer.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static int anyInt() {
        reportMatcher(new InstanceOf(Integer.class));
        return 0;
    }

    /**
     * Any <code>long</code> or <strong>non-null</strong> <code>Long</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static long anyLong() {
        reportMatcher(new InstanceOf(Long.class));
        return 0;
    }

    /**
     * Any <code>float</code> or <strong>non-null</strong> <code>Float</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        reportMatcher(new InstanceOf(Float.class));
        return 0;
    }

    /**
     * Any <code>double</code> or <strong>non-null</strong> <code>Double</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        reportMatcher(new InstanceOf(Double.class));
        return 0;
    }

    /**
     * Any <code>short</code> or <strong>non-null</strong> <code>Short</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return <code>0</code>.
     */
    public static short anyShort() {
        reportMatcher(new InstanceOf(Short.class));
        return 0;
    }

    /**
     * Any <strong>non-null</strong> <code>String</code>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @return empty String ("")
     */
    public static String anyString() {
        reportMatcher(new InstanceOf(String.class));
        return "";
    }

    /**
     * Any <strong>non-null</strong> <code>List</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return empty List.
     * @see #anyListOf(Class)
     */
    public static List anyList() {
        reportMatcher(new InstanceOf(List.class));
        return new ArrayList(0);
    }

    /**
     * Any <strong>non-null</strong> <code>List</code>.
     *
     * Generic friendly alias to {@link ArgumentMatchers#anyList()}. It's an alternative to
     * <code>&#064;SuppressWarnings("unchecked")</code> to keep code clean of compiler warnings.
     *
     * <p>
     * This method doesn't do type checks of the list content with the given type parameter, it is only there
     * to avoid casting in the code.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type owned by the list to avoid casting
     * @return empty List.
     * @see #anyList()
     */
    public static <T> List<T> anyListOf(Class<T> clazz) {
        return anyList();
    }

    /**
     * Any <strong>non-null</strong> <code>Set</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return empty Set
     * @see #anySetOf(Class)
     */
    public static Set anySet() {
        reportMatcher(new InstanceOf(Set.class));
        return new HashSet(0);
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
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type owned by the Set to avoid casting
     * @return empty Set
     * @see #anySet()
     */
    public static <T> Set<T> anySetOf(Class<T> clazz) {
        return anySet();
    }

    /**
     * Any <strong>non-null</strong> <code>Map</code>.
     *
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return empty Map.
     * @see #anyMapOf(Class, Class)
     */
    public static Map anyMap() {
        reportMatcher(new InstanceOf(Map.class));
        return new HashMap(0);
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
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param keyClazz   Type of the map key to avoid casting
     * @param valueClazz Type of the value to avoid casting
     * @return empty Map.
     * @see #anyMap()
     */
    public static <K, V> Map<K, V> anyMapOf(Class<K> keyClazz, Class<V> valueClazz) {
        return anyMap();
    }

    /**
     * Any <strong>non-null</strong> <code>Collection</code>.
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     *
     * @return empty Collection.
     * @see #anyCollectionOf(Class)
     */
    public static Collection anyCollection() {
        reportMatcher(new InstanceOf(Collection.class));
        return new ArrayList(0);
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
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type owned by the collection to avoid casting
     * @return empty Collection.
     * @see #anyCollection()
     */
    public static <T> Collection<T> anyCollectionOf(Class<T> clazz) {
        return anyCollection();
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
        if (value == null)
            return null;
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
     * Works similarly to <code>EqualsBuilder.reflectionEquals(this, other, exlucdeFields)</code> from
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
        if (value == null)
            return null;
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
     * @see #isNull(Class)
     * @see #isNotNull()
     * @see #isNotNull(Class)
     */
    public static <T> T isNull() {
        reportMatcher(Null.NULL);
        return null;
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
     * @return <code>null</code>.
     * @see #isNull()
     * @see #isNotNull()
     * @see #isNotNull(Class)
     */
    public static <T> T isNull(Class<T> clazz) {
        reportMatcher(Null.NULL);
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
     * Not <code>null</code> argument, not necessary of the given class.
     *
     * <p>
     * The class argument is provided to avoid casting.
     *
     * Alias to {@link ArgumentMatchers#isNotNull(Class)}
     * <p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class
     * </p>
     *
     * @param clazz Type to avoid casting
     * @return <code>null</code>.
     * @see #isNotNull()
     * @see #isNull()
     * @see #isNull(Class)
     */
    public static <T> T notNull(Class<T> clazz) {
        reportMatcher(NotNull.NOT_NULL);
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
     * @return <code>null</code>.
     */
    public static <T> T isNotNull(Class<T> clazz) {
        return notNull(clazz);
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
     */
    public static String matches(String regex) {
        reportMatcher(new Matches(regex));
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
     * This API has changed in 2.0, please read {@link ArgumentMatcher} for rationale and migration guide.
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
