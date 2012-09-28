/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.VarargCapturingMatcher;
import org.mockito.internal.progress.HandyReturnValues;

import java.util.List;

/**
 * Use it to capture argument values for further assertions.
 *
 * <p>
 * Mockito verifies argument values in natural java style: by using an equals() method.
 * This is also the recommended way of matching arguments because it makes tests clean & simple.
 * In some situations though, it is helpful to assert on certain arguments after the actual verification.
 * For example:
 * <pre class="code"><code class="java">
 *   ArgumentCaptor&lt;Person&gt; argument = ArgumentCaptor.forClass(Person.class);
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.getValue().getName());
 * </code></pre>
 *
 * <p>
 * <strong>Warning:</strong> it is recommended to use ArgumentCaptor with verification <strong>but not</strong> with stubbing.
 * Using ArgumentCaptor with stubbing may decrease test readability because captor is created outside of assert (aka verify or 'then') block.
 * Also it may reduce defect localization because if stubbed method was not called then no argument is captured.
 *
 * <p>
 * In a way ArgumentCaptor is related to custom argument matchers (see javadoc for {@link ArgumentMatcher} class).
 * Both techniques can be used for making sure certain arguments where passed to mocks. 
 * However, ArgumentCaptor may be a better fit if:
 * <ul>  
 * <li>custom argument matcher is not likely to be reused</li>
 * <li>you just need it to assert on argument values to complete verification</li>
 * </ul>
 * Custom argument matchers via {@link ArgumentMatcher} are usually better for stubbing.
 *
 * <p>
 * This utility class <strong>*don't do any type checks*</strong>, the generic signatures are only there to avoid casting
 * in your code. If you want specific types, then you should do that the captured values.
 * This behavior might change (type checks could be added) in a
 * future major release.
 * <p>
 * There is an <strong>annotation</strong> that you might find useful: &#64;{@link Captor}
 * <p>
 * See the full documentation on Mockito in javadoc for {@link Mockito} class.
 *
 * @see Captor
 * @since 1.8.0
 */
public class ArgumentCaptor<T> {
    
    HandyReturnValues handyReturnValues = new HandyReturnValues();

    private final CapturingMatcher<T> capturingMatcher = new CapturingMatcher<T>();
    private final VarargCapturingMatcher<T> varargCapturingMatcher = new VarargCapturingMatcher<T>();
    private final Class<T> clazz;

    /**
     * @deprecated
     * 
     * <b>Please use factory method {@link ArgumentCaptor#forClass(Class)} to create captors</b>
     * <p>
     * This is required to avoid NullPointerExceptions when autoUnboxing primitive types.
     * See issue 99.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   ArgumentCaptor&lt;Person&gt; argument = ArgumentCaptor.forClass(Person.class);
     *   verify(mock).doSomething(argument.capture());
     *   assertEquals("John", argument.getValue().getName());
     * </code></pre>
     */
    @Deprecated
    public ArgumentCaptor() {
        this.clazz = null;
    }

    ArgumentCaptor(Class<T> clazz) {
        this.clazz = clazz;
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
        Mockito.argThat(capturingMatcher);
        return handyReturnValues.returnFor(clazz);
    }

    /**
     * Use it to capture the variable arguments. This method <b>must be used inside of verification</b>.
     * <p>
     * Internally, this method registers a special implementation of an {@link ArgumentMatcher}.
     * This argument matcher stores the variable arguments values so that you can use it later to perform assertions.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     *
     * @return null or default values
     */
    public T captureVararg() {
        Mockito.argThat(varargCapturingMatcher);
        return handyReturnValues.returnFor(clazz);
    }


    /**
     * Returns the captured value of the argument.
     * <p>
     * If the method was called multiple times then it returns the latest captured value.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public T getValue() {
        return this.capturingMatcher.getLastValue();
    }

    /**
     * Returns the captured value of the variable arguments.
     * <p>
     * If the method was called multiple times then it returns the latest captured variable arguments.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     *
     * @return captured varargs
     */
    public List<T> getVarargsValues() {
        return this.varargCapturingMatcher.getLastVarargs();
    }

    /**
     * Returns all captured values. Use it in case the verified method was called multiple times.
     * <p>
     * Example: 
     * <pre class="code"><code class="java">
     *   ArgumentCaptor&lt;Person&gt; peopleCaptor = ArgumentCaptor.forClass(Person.class);
     *   verify(mock, times(2)).doSomething(peopleCaptor.capture());
     *   
     *   List&lt;Person&gt; capturedPeople = peopleCaptor.getAllValues();
     *   assertEquals("John", capturedPeople.get(0).getName());
     *   assertEquals("Jane", capturedPeople.get(1).getName());
     * </code></pre>
     * See more examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public List<T> getAllValues() {
        return this.capturingMatcher.getAllValues();
    }

    /**
     * Returns all captured variable arguments. Use it in case the verified method was called multiple times.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   ArgumentCaptor&lt;Person&gt; peopleFornamesCaptor = ArgumentCaptor.forClass(String.class);
     *   verify(mock, times(2)).doSomething(peopleFornamesCaptor.captureVarargs());
     *
     *   List&lt;String&gt; peopleFornames = peopleFornamesCaptor.getAllVarargs();
     *   assertThat(peopleFornames.get(0)).contains("John", "Carl");
     *   assertThat(peopleFornames.get(1)).contains("Janes", "Eloise", "Lois");
     * </code></pre>
     * See more examples in javadoc for {@link ArgumentCaptor} class.
     *
     * @return all captured varargs
     */
    public List<List<T>> getAllVarargsValues() {
        return this.varargCapturingMatcher.getAllVarargs();
    }

    /**
     * Build a new <code>ArgumentCaptor</code>.
     * <p>
     * Note that an <code>ArgumentCaptor</code> <b>*don't do any type checks*</b>, it is only there to avoid casting
     * in your code. This might however change (type checks could be added) in a
     * future major release.
     *
     * @param clazz Type matching the parameter to be captured.
     * @param <T> Type of clazz
     * @return A new ArgumentCaptor
     */
    public static <T> ArgumentCaptor<T> forClass(Class<T> clazz) {
        return new ArgumentCaptor<T>(clazz);
    }
}