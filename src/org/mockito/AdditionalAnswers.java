package org.mockito;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.answers.ReturnsIdentity;

/**
 * Additional answers provides factory methods for less common answers.
 *
 * <p>Currently offer answers that can return the parameter of an invocation at a certain position.
 *
 * <p>See factory methods for more information : {@link #returnsFirstArg}, {@link #returnsSecondArg},
 * {@link #returnsLastArg} and {@link #returnsArgAtPosition(int)}
 *
 * @since 1.9.5
 */
@SuppressWarnings("unchecked")
public class AdditionalAnswers {
    private static final ReturnsIdentity RETURNS_FIRST_ARGUMENT = new ReturnsIdentity(0);
    private static final ReturnsIdentity RETURNS_SECOND_ARGUMENT = new ReturnsIdentity(1);
    private static final ReturnsIdentity RETURNS_LAST_ARGUMENT = new ReturnsIdentity(-1);

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
     * <pre class="code"><code class="java">given(person.remember(dream1, dream2, dream3, dream4)).will(returnsArgAtPosition(3));
     * daAnswer(returnsArgAtPosition(3)).when(person).remember(dream1, dream2, dream3, dream4)</code></pre>
     *
     * @param <T> Return type of the invocation.
     * @return Answer that will return the second argument of the invocation.
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsArgAtPosition(int position) {
        return (Answer<T>) new ReturnsIdentity(position);
    }

}
