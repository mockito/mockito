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

/**
 * Functional interfaces to make it easy to implement answers in Java 8
 *
 * @since 2.1.0
 */
public class AnswerFunctionalInterfaces {
	/**
     * Hide constructor to avoid instantiation of class with only static methods
     */
    private AnswerFunctionalInterfaces() {
    }

    /**
     * Construct an answer from a two parameter answer interface
     * @param answer answer interface
     * @param <T> return type
     * @param <A> input parameter 1 type
     * @return a new answer object
     */
    public static <T, A> Answer<T> toAnswer(final Answer1<T, A> answer) {
        return new Answer<T>() {
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer((A)invocation.getArgument(0));
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
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer((A)invocation.getArgument(0));
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
        return new Answer<T>() {
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1));
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
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1));
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
        return new Answer<T>() {
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2));
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
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2));
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
        return new Answer<T>() {
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2),
                        (D)invocation.getArgument(3));
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
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2),
                        (D)invocation.getArgument(3));
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
        return new Answer<T>() {
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2),
                        (D)invocation.getArgument(3),
                        (E)invocation.getArgument(4));
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
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2),
                        (D)invocation.getArgument(3),
                        (E)invocation.getArgument(4));
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
    public static <T, A, B, C, D, E, F> Answer<T> toAnswer(final Answer6<T, A, B, C, D, E, F> answer) {
        return new Answer<T>() {
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocation) throws Throwable {
                return answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2),
                        (D)invocation.getArgument(3),
                        (E)invocation.getArgument(4),
                        (F)invocation.getArgument(5));
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
     * @param <F> input parameter 6 type
     * @return a new answer object
     */
    public static <A, B, C, D, E, F> Answer<Void> toAnswer(final VoidAnswer6<A, B, C, D, E, F> answer) {
        return new Answer<Void>() {
            @SuppressWarnings("unchecked")
            public Void answer(InvocationOnMock invocation) throws Throwable {
                answer.answer(
                        (A)invocation.getArgument(0),
                        (B)invocation.getArgument(1),
                        (C)invocation.getArgument(2),
                        (D)invocation.getArgument(3),
                        (E)invocation.getArgument(4),
                        (F)invocation.getArgument(5));
                return null;
            }
        };
    }
}
