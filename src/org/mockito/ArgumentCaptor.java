/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.List;

import org.mockito.internal.matchers.CapturingMatcher;

/**
 * Use it to capture argument values for further assertions.
 * <p>
 * Mockito verifies argument values in natural java style: by using an equals() method.
 * This is also the recommended way of matching arguments because it makes tests clean & simple.
 * In some situations though, it is helpful to assert on certain arguments after the actual verification.
 * For example:
 * <pre>
 *   ArgumentCaptor&lt;Person&gt; argument = new ArgumentCaptor&lt;Person&gt;();
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.getValue().getName());
 * </pre>
 *
 * <p>
 * <b>Warning:</b> it is recommended to use ArgumentCaptor with verification <b>but not</b> with stubbing.
 * Using ArgumentCaptor with stubbing may decrease test readability because captor is created outside of assert (aka verify or 'then') block.
 * Also it may reduce defect localization because if stubbed method was not called then no argument is captured.
 * <p>
 * In a way ArgumentCaptor is related to custom argument matchers (see javadoc for {@link ArgumentMatcher} class).
 * Both techniques can be used for making sure certain arguments where passed to mocks. 
 * However, ArgumentCaptor may be a better fit if:
 * <ul>  
 * <li>custom argument matcher is not likely to be reused</li>
 * <li>you just need it to assert on argument values to complete verification</li>
 * </ul>
 * Custom argument matchers via {@link ArgumentMatcher} are usually better for stubbing.
 * <p>
 * See the full documentation on Mockito in javadoc for {@link Mockito} class.    
 */
public class ArgumentCaptor<T> {
    
    private CapturingMatcher<T> capturingMatcher = new CapturingMatcher<T>();

    /**
     * Use it to capture the argument. This method <b>must be used inside of verification</b>.
     * <p>
     * Internally, this method registers a special implementation of an {@link ArgumentMatcher}.
     * This argument matcher stores the argument value so that you can use it later to perform assertions.  
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return null
     */
    public T capture() {
        Mockito.argThat(capturingMatcher);
        return null;
    }

    /**
     * Returns the captured value of the argument.
     * <p>
     * If the method was called multiple times then it returns the latest captured value
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public T getValue() {
        return this.capturingMatcher.getLastValue();
    }

    /**
     * Returns all captured values. Use it in case the verified method was called multiple times.
     * <p>
     * Example: 
     * <pre>
     *   ArgumentCaptor&lt;Person&gt; peopleCaptor = new ArgumentCaptor&lt;Person&gt;();
     *   verify(mock, times(2)).doSomething(peopleCaptor.capture());
     *   
     *   List&lt;Person&gt; capturedPeople = peopleCaptor.getAllValues();
     *   assertEquals("John", capturedPeople.get(0).getName());
     *   assertEquals("Jane", capturedPeople.get(1).getName());
     * </pre>
     * See more examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public List<T> getAllValues() {
        return this.capturingMatcher.getAllValues();
    }
}