package org.mockito;

import org.mockito.internal.stubbing.answers.ReturnsElementsOf;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;

import java.util.Collection;

/**
 * Additional answers provides factory methods for less common answers.
 *
 * <p>Currently offer answers that can return the parameter of an invocation at a certain position.
 *
 * <p>See factory methods for more information : {@link #returnsFirstArg}, {@link #returnsSecondArg},
 * {@link #returnsLastArg} and {@link #returnsArgAt}
 *
 * @since 1.9.5
 */
@SuppressWarnings("unchecked")
public class AdditionalAnswers {
    private static final ReturnsArgumentAt RETURNS_FIRST_ARGUMENT = new ReturnsArgumentAt(0);
    private static final ReturnsArgumentAt RETURNS_SECOND_ARGUMENT = new ReturnsArgumentAt(1);
    private static final ReturnsArgumentAt RETURNS_LAST_ARGUMENT = new ReturnsArgumentAt(-1);

    /**
     * Returns the first parameter of an invocation.
     *
     * <p>
     *     This additional answer could be used at stub time using the
     *     <code>then|do|will{@link org.mockito.stubbing.Answer}</code> methods. For example :
     * </p>
     *
     * <pre class="code"><code class="java">given(carKeyFob.authenticate(carKey)).will(returnsFirstArg());
     * daAnswer(returnsFirstArg()).when(carKeyFob).authenticate(carKey)</code></pre>
     *
     * @param <T> Return type of the invocation.
     * @return Answer that will return the first argument of the invocation.
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsFirstArg() {
        return (Answer<T>) RETURNS_FIRST_ARGUMENT;
    }

    /**
     * Returns the second parameter of an invocation.
     *
     * <p>
     *     This additional answer could be used at stub time using the
     *     <code>then|do|will{@link org.mockito.stubbing.Answer}</code> methods. For example :
     * </p>
     *
     * <pre class="code"><code class="java">given(trader.apply(leesFormula, onCreditDefaultSwap)).will(returnsSecondArg());
     * daAnswer(returnsSecondArg()).when(trader).apply(leesFormula, onCreditDefaultSwap)</code></pre>
     *
     * @param <T> Return type of the invocation.
     * @return Answer that will return the second argument of the invocation.
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsSecondArg() {
        return (Answer<T>) RETURNS_SECOND_ARGUMENT;
    }

    /**
     * Returns the last parameter of an invocation.
     *
     * <p>
     *     This additional answer could be used at stub time using the
     *     <code>then|do|will{@link org.mockito.stubbing.Answer}</code> methods. For example :
     * </p>
     *
     * <pre class="code"><code class="java">given(person.remember(dream1, dream2, dream3, dream4)).will(returnsLastArg());
     * daAnswer(returnsLastArg()).when(person).remember(dream1, dream2, dream3, dream4)</code></pre>
     *
     * @param <T> Return type of the invocation.
     * @return Answer that will return the last argument of the invocation.
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsLastArg() {
        return (Answer<T>) RETURNS_LAST_ARGUMENT;
    }

    /**
     * Returns the parameter of an invocation at the given position.
     *
     * <p>
     * This additional answer could be used at stub time using the
     * <code>then|do|will{@link org.mockito.stubbing.Answer}</code> methods. For example :
     * </p>
     *
     * <pre class="code"><code class="java">given(person.remember(dream1, dream2, dream3, dream4)).will(returnsArgAt(3));
     * daAnswer(returnsArgAt(3)).when(person).remember(dream1, dream2, dream3, dream4)</code></pre>
     *
     * @param <T> Return type of the invocation.
     * @return Answer that will return the second argument of the invocation.
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsArgAt(int position) {
        return (Answer<T>) new ReturnsArgumentAt(position);
    }

    /**
     * An answer that directly forwards the calls to the delegate.
     *
     * Makes sense only for spies or partial mocks of objects that are difficult to mock or spy using the usual spy API.
     * Possible use cases:
     * <ul>
     *     <li>Final classes but with an interface</li>
     *     <li>Already custom proxied object</li>
     *     <li>Special objects with a finalize method, i.e. to avoid executing it 2 times</li>
     *     <li>...</li>
     * </ul>
     * Sets the real implementation to be called when the method is called on a mock object.
     * <p>
     * <pre class="code"><code class="java">
     *   final class DontMessTheCodeOfThisList implements list { ... }
     *
     *   DontMessTheCodeOfThisList awesomeList = new DontMessTheCodeOfThisList();
     *
     *   List listWithDelegate = mock(List.class, delegatesTo(awesomeList));
     * </code></pre>
     *
     * <p>
     * This features suffer from the same drawback as the spy.
     * The mock will call the delegate if you use regular when().then() stubbing style.
     * Since the real implementation is called this might have some side effects.
     * Therefore you should to use the doReturn|Throw|Answer|CallRealMethod stubbing style. Example:
     *
     * <pre class="code"><code class="java">
     *   List listWithDelegate = mock(List.class, AdditionalAnswers.delegatesTo(awesomeList));
     *
     *   //Impossible: real method is called so listWithDelegate.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   when(listWithDelegate.get(0)).thenReturn("foo");
     *
     *   //You have to use doReturn() for stubbing
     *   doReturn("foo").when(listWithDelegate).get(0);
     * </code></pre>
     *
     * @param delegate The delegate to forward calls to.
     * @return the answer
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> delegatesTo(Object delegate) {
        return (Answer<T>) new ForwardsInvocations(delegate);
    }

    /**
     * Returns elements of the collection. Keeps returning the last element forever.
     * Might be useful on occasion when you have a collection of elements to return.
     * <p>
     * <pre class="code"><code class="java">
     *   //this:
     *   when(mock.foo()).thenReturn(1, 2, 3);
     *
     *   //is equivalent to:
     *   when(mock.foo()).thenReturn(new ReturnsElementsOf(Arrays.asList(1, 2, 3)));
     * </code></pre>
     *
     * @param elements The collection of elements to return.
     * @return the answer
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsElementsOf(Collection<?> elements) {
        return (Answer<T>) new ReturnsElementsOf(elements);
    }
}
