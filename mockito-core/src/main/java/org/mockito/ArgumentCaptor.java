/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.internal.util.Primitives.defaultValue;

import java.util.List;

import org.mockito.internal.matchers.CapturingMatcher;

/**
 * Use it to capture argument values for further assertions.
 *
 * <p>
 * Mockito verifies argument values in natural java style: by using an equals() method.
 * This is also the recommended way of matching arguments because it makes tests clean and simple.
 * In some situations though, it is helpful to assert on certain arguments after the actual verification.
 * For example:
 * <pre class="code"><code class="java">
 *   ArgumentCaptor&lt;Person&gt; argument = ArgumentCaptor.forClass(Person.class);
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.getValue().getName());
 * </code></pre>
 *
 * Example of capturing varargs:
 * <pre class="code"><code class="java">
 *   //capturing varargs:
 *   ArgumentCaptor&lt;Person&gt; varArgs = ArgumentCaptor.forClass(Person.class);
 *   verify(mock).varArgMethod(varArgs.capture());
 *   List expected = asList(new Person("John"), new Person("Jane"));
 *   assertEquals(expected, varArgs.getAllValues());
 * </code></pre>
 *
 * <p>
 * <strong>Warning:</strong> it is recommended to use ArgumentCaptor with verification <strong>but not</strong> with stubbing.
 * Using ArgumentCaptor with stubbing may decrease test readability because captor is created
 * outside of assertion (aka verify or 'then') blocks.
 * It may also reduce defect localization because if the stubbed method was not called, then no argument is captured.
 *
 * <p>
 * In a way ArgumentCaptor is related to custom argument matchers (see javadoc for {@link ArgumentMatcher} class).
 * Both techniques can be used for making sure certain arguments were passed to mock objects.
 * However, ArgumentCaptor may be a better fit if:
 * <ul>
 * <li>custom argument matcher is not likely to be reused</li>
 * <li>you just need it to assert on argument values to complete verification</li>
 * </ul>
 * Custom argument matchers via {@link ArgumentMatcher} are usually better for stubbing.
 *
 * <p>
 * This utility class <strong>will</strong> perform type checking on the generic type (since Mockito 5.0.0).
 * <p>
 * There is an <strong>annotation</strong> that you might find useful: &#64;{@link Captor}
 * <p>
 * See the full documentation on Mockito in javadoc for {@link Mockito} class.
 *
 * @see Captor
 * @since 1.8.0
 */
@CheckReturnValue
public class ArgumentCaptor<T> {

    private final CapturingMatcher<T> capturingMatcher;
    private final Class<? extends T> clazz;

    private ArgumentCaptor(Class<? extends T> clazz) {
        this.clazz = clazz;
        this.capturingMatcher = new CapturingMatcher<>(clazz);
    }

    /**
     * Use it to capture the argument. This method <b>must be used inside of verification</b>.
     * <p>
     * Internally, this method registers a special implementation of an {@link ArgumentMatcher}.
     * This argument matcher stores the argument value so that you can use it later to perform assertions.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     *
     * @return null or default values
     */
    public T capture() {
        T ignored = Mockito.argThat(capturingMatcher);
        return defaultValue(clazz);
    }

    /**
     * Returns the captured value of the argument. When capturing varargs use {@link #getAllValues()}.
     * <p>
     * If verified method was called multiple times then this method returns the latest captured value.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     *
     * @return captured argument value
     */
    public T getValue() {
        return this.capturingMatcher.getLastValue();
    }

    /**
     * Returns all captured values. Use it when capturing varargs or when the verified method was called multiple times.
     * When varargs method was called multiple times, this method returns merged list of all values from all invocations.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   mock.doSomething(new Person("John");
     *   mock.doSomething(new Person("Jane");
     *
     *   ArgumentCaptor&lt;Person&gt; peopleCaptor = ArgumentCaptor.forClass(Person.class);
     *   verify(mock, times(2)).doSomething(peopleCaptor.capture());
     *
     *   List&lt;Person&gt; capturedPeople = peopleCaptor.getAllValues();
     *   assertEquals("John", capturedPeople.get(0).getName());
     *   assertEquals("Jane", capturedPeople.get(1).getName());
     * </pre>
     *
     * Example of capturing varargs:
     * <pre class="code"><code class="java">
     *   mock.countPeople(new Person("John"), new Person("Jane"); //vararg method
     *
     *   ArgumentCaptor&lt;Person&gt; peopleCaptor = ArgumentCaptor.forClass(Person.class);
     *
     *   verify(mock).countPeople(peopleCaptor.capture());
     *
     *   List expected = asList(new Person("John"), new Person("Jane"));
     *   assertEquals(expected, peopleCaptor.getAllValues());
     * </code></pre>
     * See more examples in javadoc for {@link ArgumentCaptor} class.
     *
     * @return captured argument value
     */
    public List<T> getAllValues() {
        return this.capturingMatcher.getAllValues();
    }

    /**
     * Build a new <code>ArgumentCaptor</code>.
     * <p>
     * An <code>ArgumentCaptor</code> will perform type checks (since Mockito 5.0.0).
     *
     * @param clazz Type matching the parameter to be captured.
     * @param <S> Type of clazz
     * @param <U> Type of object captured by the newly built ArgumentCaptor
     * @return A new ArgumentCaptor
     */
    public static <U, S extends U> ArgumentCaptor<U> forClass(Class<S> clazz) {
        return new ArgumentCaptor<>(clazz);
    }

    /**
     * Build a new <code>ArgumentCaptor</code> by inferring the class type.
     * <p>
     * This enables inferring the generic type of an argument captor without
     * providing a raw class reference, which enables working around generic
     * limitations of the Java compiler without producing compile-time warnings
     * unlike {@link #forClass} which would require explicit casting or warning
     * suppression.
     * <p>
     * Example usage:
     *
     * <pre class="code"><code class="java">
     *   // Given
     *   UserRepository repository = mock();
     *   UserService service = new UserService(repository);
     *
     *   Map&lt;String, User&gt; expectedUsers = Map.of(
     *       "12345", new User("12345", "Bob"),
     *       "45678", new User("45678", "Dave")
     *   );
     *
     *   ArgumentCaptor&lt;Map&lt;String, User&gt;&gt; captor = ArgumentCaptor.captor();
     *
     *   doNothing().when(repository).storeUsers(captor.capture());
     *
     *   // When
     *   service.createUsers(List.of(
     *       new User("12345", "Bob"),
     *       new User("45678", "Dave")
     *   ));
     *
     *   // Then
     *   Map&lt;String, User&gt; actualUsers = captor.getValue();
     *
     *   assertThat(expectedUsers).isEqualTo(actualUsers);
     * </code></pre>
     *
     * @param reified do not pass any value here. This is used to trick the compiler
     *     into reifying the return type without needing casts.
     * @param <U> the type of argument to be captured by this captor.
     * @return A new ArgumentCaptor.
     * @throws IllegalArgumentException if any arguments are passed to this method.
     * @since 5.7.0
     */
    @SafeVarargs
    @SuppressWarnings({"varargs", "unchecked"})
    public static <U> ArgumentCaptor<U> captor(U... reified) {
        if (reified == null || reified.length > 0) {
            throw new IllegalArgumentException("Do not provide any arguments to the 'captor' call");
        }

        return forClass((Class<U>) reified.getClass().getComponentType());
    }

    /**
     * Get the raw class being captured.
     *
     * @return the raw class that is being captured by this captor.
     */
    Class<? extends T> getCaptorType() {
        return clazz;
    }
}
