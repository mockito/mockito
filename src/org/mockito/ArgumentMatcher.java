package org.mockito;

/**
 * Allows creating customized argument matchers.
 * This API was changed in Mockito 2.* in an effort to decouple Mockito from Hamcrest
 * and reduce the risk of version incompatibility.
 * <p>
 * For non-trivial method arguments used in stubbing or verification, you have following options
 * (in no particular order):
 * <li>
 *     <ul>refactor the code so that the interactions with collaborators are easier to test with mocks.
 *     Perhaps it is possible to pass a different argument to the method so that mocking is easier?
 *     If stuff is hard to test it usually indicates the design could be better, so refactor for testability.
 *     </ul>
 *     <ul>don't match the argument strictly, just use one of the lenient argument matchers like
 *     {@link Mockito#notNull()}. Some times it is better to have a simple test that works than
 *     a complicated test that seem to work.
 *     </ul>
 *     <ul>implement equals() method in the objects that are used as arguments to mocks.
 *     Mockito naturally uses equals() for argument matching.
 *     Many times, this is option is clean and simple.
 *     </ul>
 *     <ul>use {@link ArgumentCaptor} to capture the arguments and perform assertions on their state.
 *     Useful when you need to verify the arguments. Captor is not useful if you need argument matching for stubbing.
 *     Many times, this option leads to clean and readable tests with fine-grained validation of arguments.
 *     </ul>
 *     <ul>use customized argument matchers by implementing {@link ArgumentMatcher} interface
 *     and passing the implementation to the {@link Mockito#argThat} method.
 *     This option is useful if custom matcher is needed for stubbing and can be reused a lot
 *     </ul>
 *     <ul>use an instance of hamcrest matcher and pass it to
 *     {@link org.mockito.hamcrest.MockitoHamcrest#argThat(org.hamcrest.Matcher)}
 *     Useful if you already have a hamcrest matcher. Reuse and win!
 *     </ul>
 * </li>
 *
 * <p>
 * Implementations of this interface can be used with {@link Matchers#argThat} method:
 *
 * <pre class="code"><code class="java">
 * class ListOfTwoElements implements ArgumentMatcher&lt;List&gt; {
 *     public boolean matches(Object list) {
 *         return ((List) list).size() == 2;
 *     }
 * }
 *
 * List mock = mock(List.class);
 *
 * when(mock.addAll(argThat(new ListOfTwoElements))).thenReturn(true);
 *
 * mock.addAll(Arrays.asList(&quot;one&quot;, &quot;two&quot;));
 *
 * verify(mock).addAll(argThat(new ListOfTwoElements()));
 * </code></pre>
 *
 * To keep it readable you can extract method, e.g:
 *
 * <pre class="code"><code class="java">
 *   verify(mock).addAll(<b>argThat(new ListOfTwoElements())</b>);
 *   //becomes
 *   verify(mock).addAll(<b>listOfTwoElements()</b>);
 * </code></pre>
 *
 * <p>
 * Read more about other matchers in javadoc for {@link Matchers} class
 *
 * @param <T> type of argument
 */
public interface ArgumentMatcher<T> {

    /**
     * Informs if this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     * <p>
     * The argument is not using generic type in order to force explicit casting in the implementation.
     * This way it is easier to debug when incompatible arguments are passed to the matchers.
     * You have to trust us on this one.
     * //TODO SF make sure it makes sense to receive an Object here instead of T and document it.
     * <p>
     * See the example in the top level javadoc for {@link ArgumentMatcher}
     *
     * @param argument
     *            the argument
     * @return true if this matcher accepts the given argument.
     */
    public boolean matches(Object argument);
}
