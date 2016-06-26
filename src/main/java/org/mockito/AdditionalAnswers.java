/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.mockito.internal.stubbing.answers.ReturnsElementsOf;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.mockito.stubbing.Answer;

import java.util.Collection;

/**
 * Additional answers provides factory methods for answers.
 *
 * <p>Currently offer answers that can return the parameter of an invocation at a certain position,
 * along with answers that draw on a strongly typed interface from {@link AnswerFunctionalInterfaces}
 * to provide a neater way to write custom answers that either return a value or are void.
 *
 * <p>See factory methods for more information : {@link #returnsFirstArg}, {@link #returnsSecondArg},
 * {@link #returnsLastArg}, {@link #returnsArgAt}, {@link #answer} and {@link #answerVoid}
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
     * doAnswer(returnsFirstArg()).when(carKeyFob).authenticate(carKey)</code></pre>
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
     * doAnswer(returnsSecondArg()).when(trader).apply(leesFormula, onCreditDefaultSwap)</code></pre>
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
     * doAnswer(returnsLastArg()).when(person).remember(dream1, dream2, dream3, dream4)</code></pre>
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
     * doAnswer(returnsArgAt(3)).when(person).remember(dream1, dream2, dream3, dream4)</code></pre>
     *
     * @param <T> Return type of the invocation.
     * @param position index of the argument from the list of arguments.
     * @return Answer that will return the argument from the given position in the argument's list
     *
     * @since 1.9.5
     */
    public static <T> Answer<T> returnsArgAt(int position) {
        return (Answer<T>) new ReturnsArgumentAt(position);
    }

    /**
     * An answer that directly forwards the calls to the delegate. The delegate may or may not be of the same type as the mock.
     * If the type is different, a matching method needs to be found on delegate type otherwise an exception is thrown.
     * <p>
     * Useful for spies or partial mocks of objects that are difficult to mock
     * or spy using the usual spy API. Possible use cases:
     * <ul>
     *     <li>Final classes but with an interface</li>
     *     <li>Already custom proxied object</li>
     *     <li>Special objects with a finalize method, i.e. to avoid executing it 2 times</li>
     * </ul>
     * For more details including the use cases reported by users take a look at
     * <a link="http://code.google.com/p/mockito/issues/detail?id=145">issue 145</a>.
     * <p>
     * The difference with the regular spy:
     * <ul>
     *   <li>
     *     The regular spy ({@link Mockito#spy(Object)}) contains <strong>all</strong> state from the spied instance
     *     and the methods are invoked on the spy. The spied instance is only used at mock creation to copy the state from.
     *     If you call a method on a regular spy and it internally calls other methods on this spy, those calls are remembered
     *     for verifications, and they can be effectively stubbed.
     *   </li>
     *   <li>
     *     The mock that delegates simply delegates all methods to the delegate.
     *     The delegate is used all the time as methods are delegated onto it.
     *     If you call a method on a mock that delegates and it internally calls other methods on this mock,
     *     those calls are <strong>not</strong> remembered for verifications, stubbing does not have effect on them, too.
     *     Mock that delegates is less powerful than the regular spy but it is useful when the regular spy cannot be created.
     *   </li>
     * </ul>
     * An example with a final class that we want to delegate to:
     * <p>
     * <pre class="code"><code class="java">
     *   final class DontYouDareToMockMe implements list { ... }
     *
     *   DontYouDareToMockMe awesomeList = new DontYouDareToMockMe();
     *
     *   List mock = mock(List.class, delegatesTo(awesomeList));
     * </code></pre>
     *
     * <p>
     * This feature suffers from the same drawback as the spy.
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
     * @param delegate The delegate to forward calls to. It does not have to be of the same type as the mock (although it usually is).
     *                 The only requirement is that the instance should have compatible method signatures including the return values.
     *                 Only the methods that were actually executed on the mock need to be present on the delegate type.
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
     *   when(mock.foo()).thenAnswer(new ReturnsElementsOf(Arrays.asList(1, 2, 3)));
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

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - which is expected to return something
     * @param <T> return type
     * @param <A> input parameter type 1
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <T, A, B> Answer<T> answer(AnswerFunctionalInterfaces.Answer1<T, A> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - a void method
     * @param <A> input parameter type 1
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <A, B> Answer<Void> answerVoid(AnswerFunctionalInterfaces.VoidAnswer1<A> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - which is expected to return something
     * @param <T> return type
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <T, A, B> Answer<T> answer(AnswerFunctionalInterfaces.Answer2<T, A, B> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - a void method
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <A, B> Answer<Void> answerVoid(AnswerFunctionalInterfaces.VoidAnswer2<A, B> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - which is expected to return something
     * @param <T> return type
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @param <C> input parameter type 3
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <T, A, B, C> Answer<T> answer(AnswerFunctionalInterfaces.Answer3<T, A, B, C> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - a void method
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @param <C> input parameter type 3
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <A, B, C> Answer<Void> answerVoid(AnswerFunctionalInterfaces.VoidAnswer3<A, B, C> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - which is expected to return something
     * @param <T> return type
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @param <C> input parameter type 3
     * @param <D> input parameter type 4
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <T, A, B, C, D> Answer<T> answer(AnswerFunctionalInterfaces.Answer4<T, A, B, C, D> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - a void method
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @param <C> input parameter type 3
     * @param <D> input parameter type 4
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <A, B, C, D> Answer<Void> answerVoid(AnswerFunctionalInterfaces.VoidAnswer4<A, B, C, D> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     * @param answer interface to the answer - which is expected to return something
     * @param <T> return type
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @param <C> input parameter type 3
     * @param <D> input parameter type 4
     * @param <E> input parameter type 5
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <T, A, B, C, D, E> Answer<T> answer(AnswerFunctionalInterfaces.Answer5<T, A, B, C, D, E> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }

    /**
     * Creates an answer from a functional interface - allows for a strongly typed answer to be created
     * ideally in Java 8
     *
     * @param answer interface to the answer - a void method
     * @param <A> input parameter type 1
     * @param <B> input parameter type 2
     * @param <C> input parameter type 3
     * @param <D> input parameter type 4
     * @param <E> input parameter type 5
     * @return the answer object to use
     * @since 2.0.0
     */
    public static <A, B, C, D, E> Answer<Void> answerVoid(AnswerFunctionalInterfaces.VoidAnswer5<A, B, C, D, E> answer) {
        return AnswerFunctionalInterfaces.toAnswer(answer);
    }
}
