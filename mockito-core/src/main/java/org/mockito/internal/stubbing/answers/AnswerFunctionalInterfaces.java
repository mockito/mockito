/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer1;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.Answer5;
import org.mockito.stubbing.Answer6;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.stubbing.VoidAnswer2;
import org.mockito.stubbing.VoidAnswer3;
import org.mockito.stubbing.VoidAnswer4;
import org.mockito.stubbing.VoidAnswer5;
import org.mockito.stubbing.VoidAnswer6;

import java.lang.reflect.Method;

/**
 * Functional interfaces to make it easy to implement answers in Java 8
 *
 * @since 2.1.0
 */
public class AnswerFunctionalInterfaces {
    /**
     * Hide constructor to avoid instantiation of class with only static methods
     */
    private AnswerFunctionalInterfaces() {}

    /**
     * Construct an answer from a two parameter answer interface
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @return a new answer object
     */
    public static <T, A> Answer<T> toAnswer(final Answer1<T, A> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 1);
        return new Answer<T>() {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(lastParameter(invocation, answerMethod, 0));
            }
        };
    }

    /**
     * Construct an answer from a two parameter answer interface
     * @param answer answer interface
     * @param <A> input parameter 1 type
     * @return a new answer object
     */
    public static <A> Answer<Void> toAnswer(final VoidAnswer1<A> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 1);
        return new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(lastParameter(invocation, answerMethod, 0));
                return null;
            }
        };
    }

    /**
     * Construct an answer from a two parameter answer interface
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @return a new answer object
     */
    public static <T, A, B> Answer<T> toAnswer(final Answer2<T, A, B> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 2);
        return new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A) invocation.getArgument(0), lastParameter(invocation, answerMethod, 1));
            }
        };
    }

    /**
     * Construct an answer from a two parameter answer interface
     * @param answer answer interface
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @return a new answer object
     */
    public static <A, B> Answer<Void> toAnswer(final VoidAnswer2<A, B> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 2);
        return new Answer<Void>() {
            @Override
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A) invocation.getArgument(0), lastParameter(invocation, answerMethod, 1));
                return null;
            }
        };
    }

    /**
     * Construct an answer from a three parameter answer interface
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @return a new answer object
     */
    public static <T, A, B, C> Answer<T> toAnswer(final Answer3<T, A, B, C> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 3);
        return new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        lastParameter(invocation, answerMethod, 2));
            }
        };
    }

    /**
     * Construct an answer from a three parameter answer interface
     * @param answer answer interface
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @return a new answer object
     */
    public static <A, B, C> Answer<Void> toAnswer(final VoidAnswer3<A, B, C> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 3);
        return new Answer<Void>() {
            @Override
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        lastParameter(invocation, answerMethod, 2));
                return null;
            }
        };
    }

    /**
     * Construct an answer from a four parameter answer interface
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @param <D> input parameter 4 type
     * @return a new answer object
     */
    public static <T, A, B, C, D> Answer<T> toAnswer(final Answer4<T, A, B, C, D> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 4);
        return new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        (C) invocation.getArgument(2),
                        lastParameter(invocation, answerMethod, 3));
            }
        };
    }

    /**
     * Construct an answer from a four parameter answer interface
     * @param answer answer interface
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @param <D> input parameter 4 type
     * @return a new answer object
     */
    public static <A, B, C, D> Answer<Void> toAnswer(final VoidAnswer4<A, B, C, D> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 4);
        return new Answer<Void>() {
            @Override
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        (C) invocation.getArgument(2),
                        lastParameter(invocation, answerMethod, 3));
                return null;
            }
        };
    }

    /**
     * Construct an answer from a five parameter answer interface
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @param <D> input parameter 4 type
     * @param <E> input parameter 5 type
     * @return a new answer object
     */
    public static <T, A, B, C, D, E> Answer<T> toAnswer(final Answer5<T, A, B, C, D, E> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 5);
        return new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        (C) invocation.getArgument(2),
                        (D) invocation.getArgument(3),
                        lastParameter(invocation, answerMethod, 4));
            }
        };
    }

    /**
     * Construct an answer from a five parameter answer interface
     * @param answer answer interface
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @param <D> input parameter 4 type
     * @param <E> input parameter 5 type
     * @return a new answer object
     */
    public static <A, B, C, D, E> Answer<Void> toAnswer(final VoidAnswer5<A, B, C, D, E> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 5);
        return new Answer<Void>() {
            @Override
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        (C) invocation.getArgument(2),
                        (D) invocation.getArgument(3),
                        lastParameter(invocation, answerMethod, 4));
                return null;
            }
        };
    }

    /**
     * Construct an answer from a six parameter answer interface
     *
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @param <D> input parameter 4 type
     * @param <E> input parameter 5 type
     * @param <F> input parameter 6 type
     * @return a new answer object
     */
    public static <T, A, B, C, D, E, F> Answer<T> toAnswer(
            final Answer6<T, A, B, C, D, E, F> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 6);
        return new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        (C) invocation.getArgument(2),
                        (D) invocation.getArgument(3),
                        (E) invocation.getArgument(4),
                        lastParameter(invocation, answerMethod, 5));
            }
        };
    }

    /**
     * Construct an answer from a five parameter answer interface
     *
     * @param answer answer interface
     * @param <A> input parameter 1 type
     * @param <B> input parameter 2 type
     * @param <C> input parameter 3 type
     * @param <D> input parameter 4 type
     * @param <E> input parameter 5 type
     * @param <F> input parameter 6 type
     * @return a new answer object
     */
    public static <A, B, C, D, E, F> Answer<Void> toAnswer(
            final VoidAnswer6<A, B, C, D, E, F> answer) {
        final Method answerMethod = findAnswerMethod(answer.getClass(), 6);
        return new Answer<Void>() {
            @Override
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A) invocation.getArgument(0),
                        (B) invocation.getArgument(1),
                        (C) invocation.getArgument(2),
                        (D) invocation.getArgument(3),
                        (E) invocation.getArgument(4),
                        lastParameter(invocation, answerMethod, 5));
                return null;
            }
        };
    }

    private static Method findAnswerMethod(final Class<?> type, final int numberOfParameters) {
        for (final Method m : type.getDeclaredMethods()) {
            if (!m.isBridge()
                    && m.getName().equals("answer")
                    && m.getParameterTypes().length == numberOfParameters) {
                return m;
            }
        }
        throw new IllegalStateException(
                "Failed to find answer() method on the supplied class: "
                        + type.getName()
                        + ", with the supplied number of parameters: "
                        + numberOfParameters);
    }

    @SuppressWarnings("unchecked")
    private static <A> A lastParameter(
            InvocationOnMock invocation, Method answerMethod, int argumentIndex) {
        final Method invocationMethod = invocation.getMethod();

        if (invocationMethod.isVarArgs()
                && invocationMethod.getParameterTypes().length == (argumentIndex + 1)) {
            final Class<?> invocationRawArgType =
                    invocationMethod.getParameterTypes()[argumentIndex];
            final Class<?> answerRawArgType = answerMethod.getParameterTypes()[argumentIndex];
            if (answerRawArgType.isAssignableFrom(invocationRawArgType)) {
                return (A) invocation.getRawArguments()[argumentIndex];
            }
        }

        return invocation.getArgument(argumentIndex);
    }
}
