package org.mockito;

/**
 * Allows creating customized argument matchers.
 * This API was changed in Mockito 2.* in an effort to decouple Mockito from Hamcrest
 * and reduce the risk of version incompatibility.
 * Migration guide is included close to the bottom of this javadoc.
 * <p>
 * For non-trivial method arguments used in stubbing or verification, you have following options
 * (in no particular order):
 * <ul>
 *     <li>refactor the code so that the interactions with collaborators are easier to test with mocks.
 *     Perhaps it is possible to pass a different argument to the method so that mocking is easier?
 *     If stuff is hard to test it usually indicates the design could be better, so do refactor for testability!
 *     </li>
 *     <li>don't match the argument strictly, just use one of the lenient argument matchers like
 *     {@link Mockito#notNull()}. Some times it is better to have a simple test that works than
 *     a complicated test that seem to work.
 *     </li>
 *     <li>implement equals() method in the objects that are used as arguments to mocks.
 *     Mockito naturally uses equals() for argument matching.
 *     Many times, this is option is clean and simple.
 *     </li>
 *     <li>use {@link ArgumentCaptor} to capture the arguments and perform assertions on their state.
 *     Useful when you need to verify the arguments. Captor is not useful if you need argument matching for stubbing.
 *     Many times, this option leads to clean and readable tests with fine-grained validation of arguments.
 *     </li>
 *     <li>use customized argument matchers by implementing {@link ArgumentMatcher} interface
 *     and passing the implementation to the {@link Mockito#argThat} method.
 *     This option is useful if custom matcher is needed for stubbing and can be reused a lot.
 *     Note that {@link Mockito#argThat} demonstrates <b>NullPointerException</b> auto-unboxing caveat.
 *     </li>
 *     <li>use an instance of hamcrest matcher and pass it to
 *     {@link org.mockito.hamcrest.MockitoHamcrest#argThat(org.hamcrest.Matcher)}
 *     Useful if you already have a hamcrest matcher. Reuse and win!
 *     Note that {@link org.mockito.hamcrest.MockitoHamcrest#argThat(org.hamcrest.Matcher)} demonstrates <b>NullPointerException</b> auto-unboxing caveat.
 *     </li>
 *     <li>Java 8 only - use a lambda in place of an {@link ArgumentMatcher} since {@link ArgumentMatcher}
 *     is effectively a functional interface. A lambda can be used with the {@link Mockito#argThat} method.</li>
 * </ul>
 *
 * <p>
 * Implementations of this interface can be used with {@link Matchers#argThat} method.
 * Use <code>toString()</code> method for description of the matcher
 * - it is printed in verification errors.
 *
 * <pre class="code"><code class="java">
 * class ListOfTwoElements implements ArgumentMatcher&lt;List&gt; {
 *     public boolean matches(List list) {
 *         return list.size() == 2;
 *     }
 *     public String toString() {
 *         //printed in verification errors
 *         return "[list of 2 elements]";
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
 * In Java 8 you can treat ArgumentMatcher as a functional interface
 * and use a lambda, e.g.:
 *
 * <pre class="code"><code class="java">
 *   verify(mock).addAll(<b>argThat(list -> list.size() == 2)</b>);
 * </code></pre>
 *
 * <p>
 * Read more about other matchers in javadoc for {@link Matchers} class.
 * <h2>2.0 migration guide</h2>
 *
 * All existing custom implementations of <code>ArgumentMatcher</code> will no longer compile.
 * All locations where hamcrest matchers are passed to <code>argThat()</code> will no longer compile.
 * There are 2 approaches to fix the problems:
 * <ul>
 * <li>a) Refactor the hamcrest matcher to Mockito matcher:
 * Use "implements ArgumentMatcher" instead of "extends ArgumentMatcher".
 * Then refactor <code>describeTo()</code> method into <code>toString()</code> method.
 * </li>
 * <li>
 * b) Use <code>org.mockito.hamcrest.MockitoHamcrest.argThat()</code> instead of <code>Mockito.argThat()</code>.
 * Ensure that there is <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a> dependency on classpath
 * (Mockito does not depend on hamcrest any more).
 *
 * </li>
 * </ul>
 * What option is right for you? If you don't mind compile dependency to hamcrest
 * then option b) is probably right for you.
 * Your choice should not have big impact and is fully reversible -
 * you can choose different option in future (and refactor the code)
 *
 * @param <T> type of argument
 * @since 2.0
 */
public interface ArgumentMatcher<T> {

    /**
     * Informs if this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     * <p>
     * See the example in the top level javadoc for {@link ArgumentMatcher}
     *
     * @param argument
     *            the argument
     * @return true if this matcher accepts the given argument.
     */
    boolean matches(T argument);
}
