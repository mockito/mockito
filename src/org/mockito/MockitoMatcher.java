package org.mockito;

/**
 * Allows creating customized argument matchers.
 * This API was changed in Mockito 2.*
 * <p>
 * ArgumentMatcher is an hamcrest {@link org.hamcrest.Matcher} with predefined describeTo() method.
 * In case of failure, ArgumentMatcher generates description based on <b>decamelized class name</b> - to promote meaningful class names.
 * For example <b>StringWithStrongLanguage</b> matcher will generate 'String with strong language' description.
 * You can always override describeTo() method and provide detailed description.
 * <p>
 * Use {@link Matchers#argThat} method and pass an instance of hamcrest {@link org.hamcrest.Matcher}, e.g:
 *
 * <pre class="code"><code class="java">
 * class ListOfTwoElements extends ArgumentMatcher&lt;List&gt; {
 *     public boolean matches(Object list) {
 *         return ((List) list).size() == 2;
 *     }
 * }
 *
 * List mock = mock(List.class);
 *
 * when(mock.addAll(argThat(new IsListOfTwoElements))).thenReturn(true);
 *
 * mock.addAll(Arrays.asList(&quot;one&quot;, &quot;two&quot;));
 *
 * verify(mock).addAll(argThat(new IsLiListOfTwoElements;
 * </code></pre>
 *
 * To keep it readable you may want to extract method, e.g:
 *
 * <pre class="code"><code class="java">
 *   verify(mock).addAll(<b>argThat(new IsListOfTwoElements())</b>);
 *   //becomes
 *   verify(mock).addAll(<b>listOfTwoElements()</b>);
 * </code></pre>
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
public interface MockitoMatcher<T> {

    /**
     * Informs if this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     *
     * @param argument
     *            the argument
     * @return true if this matcher accepts the given argument.
     */
    public boolean matches(Object argument);

    //TODO SF should it extend serializable?
}
