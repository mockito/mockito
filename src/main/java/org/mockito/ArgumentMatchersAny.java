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
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.InstanceOf;

import static org.mockito.internal.util.Primitives.defaultValue;

public class ArgumentMatchersAny {

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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>false</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return <code>0</code>.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty String ("")
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty List.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Set
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Map.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Collection.
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
     * would be . We felt this change would make test harnesses much safer than they were with Mockito
     * 1.x.
     * </p>
     *
     * <p>
     * See examples in javadoc for {@link ArgumentMatchers} class.
     * </p>
     *
     * @return empty Iterable.
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
