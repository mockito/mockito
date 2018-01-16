/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Mockito;

/**
 * Simply put: "<b>When</b> the x method is called <b>then</b> return y". E.g:
 *
 * <pre class="code"><code class="java">
 * <b>when</b>(mock.someMethod()).<b>thenReturn</b>(10);
 *
 * //you can use flexible argument matchers, e.g:
 * when(mock.someMethod(<b>anyString()</b>)).thenReturn(10);
 *
 * //setting exception to be thrown:
 * when(mock.someMethod("some arg")).thenThrow(new RuntimeException());
 *
 * //you can set different behavior for consecutive method calls.
 * //Last stubbing (e.g: thenReturn("foo")) determines the behavior of further consecutive calls.
 * when(mock.someMethod("some arg"))
 *  .thenThrow(new RuntimeException())
 *  .thenReturn("foo");
 *
 * //There is a shorter way of consecutive stubbing:
 * when(mock.someMethod()).thenReturn(1,2,3);
 * when(mock.otherMethod()).thenThrow(exc1, exc2);
 * </code></pre>
 *
 * See examples in javadoc for {@link Mockito#when}
 */
public interface OngoingStubbing<T> {

    /**
     * Sets a return value to be returned when the method is called. E.g:
     * <pre class="code"><code class="java">
     * when(mock.someMethod()).thenReturn(10);
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param value return value
     *
     * @return object that allows stubbing consecutive calls
     */
    OngoingStubbing<T> thenReturn(T value);

    /**
     * Sets consecutive return values to be returned when the method is called. E.g:
     * <pre class="code"><code class="java">
     * when(mock.someMethod()).thenReturn(1, 2, 3);
     * </code></pre>
     *
     * Last return value in the sequence (in example: 3) determines the behavior of further consecutive calls.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param value first return value
     * @param values next return values
     *
     * @return object that allows stubbing consecutive calls
     */
    // Additional method helps users of JDK7+ to hide heap pollution / unchecked generics array creation warnings (on call site)
    @SuppressWarnings ({"unchecked", "varargs"})
    OngoingStubbing<T> thenReturn(T value, T... values);

    /**
     * Sets Throwable objects to be thrown when the method is called. E.g:
     * <pre class="code"><code class="java">
     * when(mock.someMethod()).thenThrow(new RuntimeException());
     * </code></pre>
     *
     * If throwables contain a checked exception then it has to
     * match one of the checked exceptions of method signature.
     * <p>
     * You can specify throwables to be thrown for consecutive calls.
     * In that case the last throwable determines the behavior of further consecutive calls.
     * <p>
     * If throwable is null then exception will be thrown.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * @param throwables to be thrown on method invocation
     *
     * @return object that allows stubbing consecutive calls
     */
    OngoingStubbing<T> thenThrow(Throwable... throwables);

    /**
     * Sets a Throwable type to be thrown when the method is called. E.g:
     * <pre class="code"><code class="java">
     * when(mock.someMethod()).thenThrow(RuntimeException.class);
     * </code></pre>
     *
     * <p>
     * If the throwable class is a checked exception then it has to
     * match one of the checked exceptions of the stubbed method signature.
     * <p>
     * If throwable is null then exception will be thrown.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * <p>Note depending on the JVM, stack trace information may not be available in
     * the generated throwable instance.  If you require stack trace information,
     * use {@link OngoingStubbing#thenThrow(Throwable...)} instead.
     *
     * @param throwableType to be thrown on method invocation
     *
     * @return object that allows stubbing consecutive calls
     * @since 2.1.0
     */
    OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableType);

    /**
     * Sets Throwable classes to be thrown when the method is called. E.g:
     * <pre class="code"><code class="java">
     * when(mock.someMethod()).thenThrow(RuntimeException.class);
     * </code></pre>
     *
     * <p>
     * Each throwable class will be instantiated for each method invocation.
     * <p>
     * If <code>throwableTypes</code> contain a checked exception then it has to
     * match one of the checked exceptions of method signature.
     * <p>
     * You can specify <code>throwableTypes</code> to be thrown for consecutive calls.
     * In that case the last throwable determines the behavior of further consecutive calls.
     * <p>
     * If throwable is null then exception will be thrown.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * <p>Note since JDK 7, invoking this method will raise a compiler warning "possible heap pollution",
     * this API is safe to use. If you don't want to see this warning it is possible to chain {@link #thenThrow(Class)}
     * <p>Note depending on the JVM, stack trace information may not be available in
     * the generated throwable instance.  If you require stack trace information,
     * use {@link OngoingStubbing#thenThrow(Throwable...)} instead.
     *
     * @param toBeThrown to be thrown on method invocation
     * @param nextToBeThrown next to be thrown on method invocation
     *
     * @return object that allows stubbing consecutive calls
     * @since 2.1.0
     */
    // Additional method helps users of JDK7+ to hide heap pollution / unchecked generics array creation warnings (on call site)
    @SuppressWarnings ({"unchecked", "varargs"})
    OngoingStubbing<T> thenThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown);

    /**
     * Sets the real implementation to be called when the method is called on a mock object.
     * <p>
     * As usual you are going to read <b>the partial mock warning</b>:
     * Object oriented programming is more less tackling complexity by dividing the complexity into separate, specific, SRPy objects.
     * How does partial mock fit into this paradigm? Well, it just doesn't...
     * Partial mock usually means that the complexity has been moved to a different method on the same object.
     * In most cases, this is not the way you want to design your application.
     * <p>
     * However, there are rare cases when partial mocks come handy:
     * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
     * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
     * <pre class="code"><code class="java">
     *   // someMethod() must be safe (e.g. doesn't throw, doesn't have dependencies to the object state, etc.)
     *   // if it isn't safe then you will have trouble stubbing it using this api. Use Mockito.doCallRealMethod() instead.
     *   when(mock.someMethod()).thenCallRealMethod();
     *
     *   // calls real method:
     *   mock.someMethod();
     *
     * </code></pre>
     * See also javadoc {@link Mockito#spy(Object)} to find out more about partial mocks.
     * <b>Mockito.spy() is a recommended way of creating partial mocks.</b>
     * The reason is it guarantees real methods are called against correctly constructed object because you're responsible for constructing the object passed to spy() method.
     * <p>
     * See examples in javadoc for {@link Mockito#when}
     *
     * @return object that allows stubbing consecutive calls
     */
    OngoingStubbing<T> thenCallRealMethod();

    /**
     * Sets a generic Answer for the method. E.g:
     * <pre class="code"><code class="java">
     * when(mock.someMethod(10)).thenAnswer(new Answer&lt;Integer&gt;() {
     *     public Integer answer(InvocationOnMock invocation) throws Throwable {
     *         return (Integer) invocation.getArguments()[0];
     *     }
     * }
     * </code></pre>
     *
     * @param answer the custom answer to execute.
     *
     * @return object that allows stubbing consecutive calls
     */
    OngoingStubbing<T> thenAnswer(Answer<?> answer);

    /**
     * Sets a generic Answer for the method.
     *
     * This method is an alias of {@link #thenAnswer(Answer)}. This alias allows
     * more readable tests on occasion, for example:
     * <pre class="code"><code class="java">
     * //using 'then' alias:
     * when(mock.foo()).then(returnCoolValue());
     *
     * //versus good old 'thenAnswer:
     * when(mock.foo()).thenAnswer(byReturningCoolValue());
     * </code></pre>
     *
     * @param answer the custom answer to execute.
     * @return object that allows stubbing consecutive calls
     *
     * @see #thenAnswer(Answer)
     * @since 1.9.0
     */
    OngoingStubbing<T> then(Answer<?> answer);

    /**
     * Returns the mock that was used for this stub.
     * <p>
     * It allows to create a stub in one line of code.
     * This can be helpful to keep test code clean.
     * For example, some boring stub can be created & stubbed at field initialization in a test:
     * <pre class="code"><code class="java">
     * public class CarTest {
     *   Car boringStubbedCar = when(mock(Car.class).shiftGear()).thenThrow(EngineNotStarted.class).getMock();
     *
     *   &#064;Test public void should... {}
     * </code></pre>
     *
     * @param <M> The mock type given by the variable type.
     * @return Mock used in this ongoing stubbing.
     * @since 1.9.0
     */
    <M> M getMock();

}
