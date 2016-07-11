/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.util.Primitives;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.Primitives.defaultValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Allow flexible verification or stubbing. See also {@link AdditionalMatchers}.
 * <p>
 * {@link Mockito} extends Matchers so to get access to all matchers just import Mockito class statically.
 * <pre class="code"><code class="java">
 *  //stubbing using anyInt() argument matcher
 *  when(mockedList.get(anyInt())).thenReturn("element");
 *  
 *  //following prints "element"
 *  System.out.println(mockedList.get(999));
 *  
 *  //you can also verify using argument matcher
 *  verify(mockedList).get(anyInt());
 * </code></pre>
 * Scroll down to see all methods - full list of matchers.
 * <p>
 * <b>Warning:</b>
 * <p>
 * If you are using argument matchers, <b>all arguments</b> have to be provided by matchers.
 * <p>
 * E.g: (example shows verification but the same applies to stubbing):
 * <pre class="code"><code class="java">
 *   verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 *   //above is correct - eq() is also an argument matcher
 *   
 *   verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 *   //above is incorrect - exception will be thrown because third argument is given without argument matcher.
 * </code></pre>
 * <p>
 * Matcher methods like <code>anyObject()</code>, <code>eq()</code> <b>do not</b> return matchers.
 * Internally, they record a matcher on a stack and return a dummy value (usually null).
 * This implementation is due static type safety imposed by java compiler.
 * The consequence is that you cannot use <code>anyObject()</code>, <code>eq()</code> methods outside of verified/stubbed method.
 *
 * <h1>Custom Argument Matchers</h1>
 *
 * It is important to understand the use cases and available options for dealing with non-trivial arguments
 * <b>before</b> implementing custom argument matchers. This way, you can select the best possible approach
 * for given scenario and produce highest quality test (clean and maintainable).
 * Please read on in the javadoc for {@link ArgumentMatcher} to learn about approaches and see the examples.
 */
@SuppressWarnings("unchecked")
public class Matchers {
    
    /**
     * Any <code>boolean</code> or non-null <code>Boolean</code>
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        reportMatcher(new InstanceOf(Boolean.class)); 
        return false;
    }

    /**
     * Any <code>byte</code> or non-null <code>Byte</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        reportMatcher(new InstanceOf(Byte.class));
        return 0;
    }

    /**
     * Any <code>char</code> or non-null <code>Character</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static char anyChar() {
        reportMatcher(new InstanceOf(Character.class));
        return 0;
    }

    /**
     * Any int or non-null Integer.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static int anyInt() {
        reportMatcher(new InstanceOf(Integer.class));
        return 0;
    }

    /**
     * Any <code>long</code> or non-null <code>Long</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static long anyLong() {
        reportMatcher(new InstanceOf(Long.class));
        return 0;
    }

    /**
     * Any <code>float</code> or non-null <code>Float</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        reportMatcher(new InstanceOf(Float.class));
        return 0;
    }

    /**
     * Any <code>double</code> or non-null <code>Double</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        reportMatcher(new InstanceOf(Double.class));
        return 0;
    }

    /**
     * Any <code>short</code> or non-null <code>Short</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static short anyShort() {
        reportMatcher(new InstanceOf(Short.class));
        return 0;
    }

    /**
     * Matches anything, including null.
     * <p>
     * This is an alias of: {@link #any()} and {@link #any(java.lang.Class)}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static <T> T anyObject() {
        reportMatcher(Any.ANY);
        return null;
    }

    /**
     * Any vararg, meaning any number and values of arguments.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   //verification:
     *   mock.foo(1, 2);
     *   mock.foo(1, 2, 3, 4);
     *
     *   verify(mock, times(2)).foo(anyVararg());
     *
     *   //stubbing:
     *   when(mock.foo(anyVararg()).thenReturn(100);
     *
     *   //prints 100
     *   System.out.println(mock.foo(1, 2));
     *   //also prints 100
     *   System.out.println(mock.foo(1, 2, 3, 4));
     * </code></pre>
     * See examples in javadoc for {@link Matchers} class
     *
     * @return <code>null</code>.
     * 
     * @deprecated as of 2.0 use {@link #any()}
     */
    @Deprecated
    public static <T> T anyVararg() {
        any();
        return null;
    }
    
    /**
     * Matches any object, including nulls
     * <p>
     * This method doesn't do type checks with the given parameter, it is only there
     * to avoid casting in your code. This might however change (type checks could
     * be added) in a future major release.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * <p>
     * This is an alias of: {@link #any()} and {@link #anyObject()}
     * <p>
     * @return <code>null</code>.
     */
    public static <T> T any(Class<T> clazz) {
        reportMatcher(Any.ANY);
        return defaultValue(clazz);
    }
    
    /**
     * Matches anything, including nulls and varargs
     * <p>
     * Shorter alias to {@link Matchers#anyObject()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * <p>
     * This is an alias of: {@link #anyObject()} and {@link #any(java.lang.Class)}
     * <p>
     * @return <code>null</code>.
     */
    public static <T> T any() {
        return anyObject();
    }

    /**
     * Any non-null <code>String</code>
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty String ("")
     */
    public static String anyString() {
        reportMatcher(new InstanceOf(String.class));
        return "";
    }
    
    /**
     * Any non-null <code>List</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty List.
     */
    public static  List anyList() {
        reportMatcher(new InstanceOf(List.class));
        return new LinkedList();
    }
    
    /**
     * Generic friendly alias to {@link Matchers#anyList()}.
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.
     * <p>
     * Any non-null <code>List</code>.
     * <p>
     * This method doesn't do type checks with the given parameter, it is only there
     * to avoid casting in your code. This might however change (type checks could
     * be added) in a future major release.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param clazz Type owned by the list to avoid casting
     * @return empty List.
     */
    public static <T> List<T> anyListOf(Class<T> clazz) {
        return anyList();
    }
    
    /**
     * Any non-null <code>Set</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @return empty Set
     */
    public static  Set anySet() {
        reportMatcher(new InstanceOf(Set.class));
        return new HashSet();
    }
    
    /**
     * Generic friendly alias to {@link Matchers#anySet()}.
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.
     * <p>
     * Any non-null <code>Set</code>.
     * <p>
     * This method doesn't do type checks with the given parameter, it is only there
     * to avoid casting in your code. This might however change (type checks could
     * be added) in a future major release.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @param clazz Type owned by the Set to avoid casting
     * @return empty Set
     */
    public static <T> Set<T> anySetOf(Class<T> clazz) {
        return anySet();
    }

    /**
     * Any non-null <code>Map</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty Map.
     */
    public static Map anyMap() {
        reportMatcher(new InstanceOf(Map.class));
        return new HashMap();
    }

    /**
     * Generic friendly alias to {@link Matchers#anyMap()}.
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.
     * <p>
     * Any non-null <code>Map</code>.
     * <p>
     * This method doesn't do type checks with the given parameter, it is only there
     * to avoid casting in your code. This might however change (type checks could
     * be added) in a future major release.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @param keyClazz Type of the map key to avoid casting
     * @param valueClazz Type of the value to avoid casting
     * @return empty Map.
     */
    public static <K, V>  Map<K, V> anyMapOf(Class<K> keyClazz, Class<V> valueClazz) {
        return anyMap();
    }
    
    /**
     * Any non-null <code>Collection</code>.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty Collection.
     */
    public static Collection anyCollection() {
        reportMatcher(new InstanceOf(Collection.class));
        return new LinkedList();
    }    
    
    /**
     * Generic friendly alias to {@link Matchers#anyCollection()}.
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.
     * <p>
     * Any non-null <code>Collection</code>.
     * <p>
     * This method doesn't do type checks with the given parameter, it is only there
     * to avoid casting in your code. This might however change (type checks could
     * be added) in a future major release.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param clazz Type owned by the collection to avoid casting
     * @return empty Collection.
     */
    public static <T> Collection<T> anyCollectionOf(Class<T> clazz) {
        return anyCollection();
    }    

    /**
     * <code>Object</code> argument that implements the given class.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param <T>
     *           the accepted type.
     * @param type
     *           the class of the accepted type.
     * @return <code>null</code>.
     */
    public static <T> T isA(Class<T> type) {
        reportMatcher(new InstanceOf(type));
        
        return defaultValue(type);
    }

    /**
     * <code>boolean</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(boolean value) {
        reportMatcher(new Equals(value)); 
        return false;
    }

    /**
     * <code>byte</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static byte eq(byte value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>char</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static char eq(char value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>double</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static double eq(double value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>float</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static float eq(float value) {
        reportMatcher(new Equals(value));
        return 0;
    }
    
    /**
     * <code>int</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static int eq(int value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>long</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static long eq(long value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * <code>short</code> argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>0</code>.
     */
    public static short eq(short value) {
        reportMatcher(new Equals(value));
        return 0;
    }

    /**
     * Object argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(T value) {
        reportMatcher(new Equals(value));
        if (value==null)
        	return null;
        return (T) Primitives.defaultValue(value.getClass());
    }

    /**
     * Object argument that is reflection-equal to the given value with support for excluding
     * selected fields from a class.
     * <p>
     * This matcher can be used when equals() is not implemented on compared objects.
     * Matcher uses java reflection API to compare fields of wanted and actual object.
     * <p>
     * Works similarly to EqualsBuilder.reflectionEquals(this, other, exlucdeFields) from
     * apache commons library.
     * <p>
     * <b>Warning</b> The equality check is shallow!
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *           the given value.
     * @param excludeFields
     *           fields to exclude, if field does not exist it is ignored.
     * @return <code>null</code>.
     */
    public static <T> T refEq(T value, String... excludeFields) {
        reportMatcher(new ReflectionEquals(value, excludeFields));return null;
    }
    
    /**
     * Object argument that is the same as the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param <T>
     *           the type of the object, it is passed through to prevent casts.
     * @param value
     *           the given value.
     * @return <code>null</code>.
     */
    public static <T> T same(T value) {
        reportMatcher(new Same(value));
        if (value==null)
        	return null;
        return (T) Primitives.defaultValue(value.getClass());
    }

    /**
     * <code>null</code> argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object isNull() {
        reportMatcher(Null.NULL);
        return null;
    }

    /**
     * <code>null</code> argument.
     * The class argument is provided to avoid casting.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @param clazz Type to avoid casting
     * @return <code>null</code>.
     */
    public static <T> T isNull(Class<T> clazz) {
        reportMatcher(Null.NULL);
        return null;
    }

    /**
     * Not <code>null</code> argument.
     * <p>
     * alias to {@link Matchers#isNotNull()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object notNull() {
        reportMatcher(NotNull.NOT_NULL);
        return null;
    }

    /**
     * Not <code>null</code> argument, not necessary of the given class.
     * The class argument is provided to avoid casting.
     * <p>
     * alias to {@link Matchers#isNotNull(Class)}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @param clazz Type to avoid casting
     * @return <code>null</code>.
     */
    public static <T> T notNull(Class<T> clazz) {
        reportMatcher(NotNull.NOT_NULL);
        return null;
    }
    
    /**
     * Not <code>null</code> argument.
     * <p>
     * alias to {@link Matchers#notNull()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object isNotNull() {
        return notNull();
    }

    /**
     * Not <code>null</code> argument, not necessary of the given class.
     * The class argument is provided to avoid casting.
     * <p>
     * alias to {@link Matchers#notNull(Class)}
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param substring
     *           the substring.
     * @return empty String ("").
     */
    public static String contains(String substring) {
        reportMatcher(new Contains(substring));return "";
    }

    /**
     * <code>String</code> argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param regex
     *           the regular expression.
     * @return empty String ("").
     */
    public static String matches(String regex) {
        reportMatcher(new Matches(regex));
        return "";
    }

    /**
     * <code>String</code> argument that ends with the given suffix.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param suffix
     *           the suffix.
     * @return empty String ("").
     */
    public static String endsWith(String suffix) {
        reportMatcher(new EndsWith(suffix));
        return "";
    }

    /**
     * <code>String</code> argument that starts with the given prefix.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param prefix
     *           the prefix.
     * @return empty String ("").
     */
    public static String startsWith(String prefix) {
        reportMatcher(new StartsWith(prefix));
        return "";
    }

    /**
     * Allows creating custom argument matchers.
     * This API has changed in 2.0, please read {@link ArgumentMatcher} for rationale and migration guide.
     * <b>NullPointerException</b> auto-unboxing caveat is described below.
     * <p>
     * It is important to understand the use cases and available options for dealing with non-trivial arguments
     * <b>before</b> implementing custom argument matchers. This way, you can select the best possible approach
     * for given scenario and produce highest quality test (clean and maintainable).
     * Please read the documentation for {@link ArgumentMatcher} to learn about approaches and see the examples.
     * <p>
     * <b>NullPointerException</b> auto-unboxing caveat.
     * In rare cases when matching primitive parameter types you <b>*must*</b> use relevant intThat(), floatThat(), etc. method.
     * This way you will avoid <code>NullPointerException</code> during auto-unboxing.
     * Due to how java works we don't really have a clean way of detecting this scenario and protecting the user from this problem.
     * Hopefully, the javadoc describes the problem and solution well.
     * If you have an idea how to fix the problem, let us know via the mailing list or the issue tracker.
     * <p>
     * See examples in javadoc for {@link ArgumentMatcher} class
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
     * Note that {@link #argThat} will not work with primitive <code>char</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>boolean</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>byte</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>short</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>int</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>long</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>float</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
     * Note that {@link #argThat} will not work with primitive <code>double</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p>
     * See examples in javadoc for {@link Matchers} class
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
