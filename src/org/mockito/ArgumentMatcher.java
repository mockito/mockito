/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.hamcrest.*;
import org.mockito.internal.util.Decamelizer;

/**
 * Allows creating customized argument matchers. 
 * <p>
 * ArgumentMatcher is an hamcrest {@link Matcher} with predefined describeTo() method.
 * In case of failure, ArgumentMatcher generates description based on <b>decamelized class name</b> - to promote meaningful class names. 
 * For example <b>StringWithStrongLanguage</b> matcher will generate 'String with strong language' description.
 * You can always override describeTo() method and provide detailed description.
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
 * when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);
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
 * <b>Warning:</b> Be reasonable with using complicated argument matching, especially custom argument matchers, as it can make the test less readable. 
 * Sometimes it's better to implement equals() for arguments that are passed to mocks 
 * (Mockito naturally uses equals() for argument matching). 
 * This can make the test cleaner. 
 * <p>
 * Also, <b>sometimes {@link ArgumentCaptor} may be a better fit</b> than custom matcher.
 * For example, if custom argument matcher is not likely to be reused
 * or you just need it to assert on argument values to complete verification of behavior.
 * <p>
 * Read more about other matchers in javadoc for {@link Matchers} class
 * 
 * @param <T> type of argument
 */
public abstract class ArgumentMatcher<T> extends BaseMatcher<T> {

    private static final long serialVersionUID = -2145234737829370369L;

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
     * By default this method decamlizes matchers name to promote meaningful names for matchers.
     * <p>
     * For example <b>StringWithStrongLanguage</b> matcher will generate 'String with strong language' description in case of failure.
     * <p>
     * You might want to override this method to
     * provide more specific description of the matcher (useful when
     * verification failures are reported).
     * 
     * @param description the description to which the matcher description is
     * appended.
     */
    public void describeTo(Description description) {
        String className = getClass().getSimpleName();
        description.appendText(Decamelizer.decamelizeMatcher(className));
    }
}