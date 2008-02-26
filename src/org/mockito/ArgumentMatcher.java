/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Allows creating customized argument matchers. 
 * <p>
 * ArgumentMatcher is an hamcrest {@link Matcher} with predefined describeTo() method for convenience.
 * <p>
 * Use {@link Matchers#argThat} method and pass an instance of hamcrest {@link Matcher}, e.g:
 * 
 * <pre>
 * class IsListOfTwoElements extends ArgumentMatcher&lt;List&gt; {
 *     public boolean matches(Object list) {
 *         return ((List) list).size() == 2;
 *     }
 * }
 * 
 * List mock = mock(List.class);
 * 
 * stub(mock.addAll(argThat(new IsListOfTwoElements()))).toReturn(true);
 * 
 * mock.addAll(Arrays.asList(&quot;one&quot;, &quot;two&quot;));
 * 
 * verify(mock).addAll(argThat(new IsListOfTwoElements()));
 * </pre>
 * 
 * To keep it readable you may want to extract method, e.g:
 * 
 * <pre>
 *   verify(mock).addAll(<b>argThat(new IsListOfTwoElements())</b>);
 *   //becomes
 *   verify(mock).addAll(<b>listOfTwoElements()</b>);
 * </pre>
 *
 * Custom argument matchers can make the test less readable. 
 * Sometimes it's better to implement equals() for arguments that are passed to mocks 
 * (Mockito naturally uses equals() for argument matching). 
 * This can make the test cleaner.
 * <p>
 * Read more about {@link Matchers}
 * 
 * @param <T> type of argument
 */
public abstract class ArgumentMatcher<T> extends BaseMatcher<T> {

    /**
     * Returns whether this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     * 
     * @param argument
     *            the argument
     * @return whether this matcher accepts the given argument.
     */
    public abstract boolean matches(Object argument);

    /* 
     * Usually not necessary but you might want to override this method to
     * provide more specific description of the matcher (useful when
     * verification failures are reported).
     * 
     * @param description the description to which the matcher description is
     * appended.
     */
    public void describeTo(Description description) {
        description.appendText("<custom argument matcher>");
    }
}