/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class MockWithCustomAnswerAnnotationsTest {

    public static final int MOCKED_LIST_SIZE = 3;
    @Mock(answerClass = MyCustomAnswerClass.class)
    IMethods mockWithCustomAnswer;

    @Mock(answerClass = MyCustomAnswerWithMultipleConstructors.class)
    IMethods mockWithCustomAnswerHavingMultipleConstructors;

    @Mock(answerClass = ListCustomAnswer.class)
    List<String> mockedList;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldUseCustomAnswer() {
        assertSame(MyCustomAnswerClass.returnedObject, mockWithCustomAnswer.objectReturningMethodNoArgs());
    }

    @Test
    public void should_choose_default_constructor() {
        assertSame(MyCustomAnswerClass.returnedObject, mockWithCustomAnswerHavingMultipleConstructors.objectReturningMethodNoArgs());
    }


    @Test
    public void should_throw_exception_when_custom_answer_cannot_be_instantiated() throws NoSuchFieldException {
        //This roundabout way of testing is necessary because initMocks() will attempt to inialize all @Mocks
        //in a class at once. Thus, our "failing @Mocks" must be in different classes

        class should_throw_exception_when_custom_answer_cannot_be_instantiated_test_class {
            @Mock(answerClass = NotInstantiableClass.class)
            private Object mockObjectWithAnswerClassThatCannotBeInstantiated;

            public void innerTest() {
                try {
                    initMocks(this);
                    fail("RuntimeException expected ");
                } catch (MockitoException ex) {
                    assertEquals("Custom answer cannot be instantiated",ex.getMessage());
                }
            }
        }

        new should_throw_exception_when_custom_answer_cannot_be_instantiated_test_class().innerTest();

    }

    @Test
    public void should_throw_exception_when_custom_answer_throws_exception_in_constructor() throws NoSuchFieldException {
        class should_throw_exception_when_custom_answer_throws_exception_in_constructor_test_class {
            @Mock(answerClass=AnswerThrowingConstructor.class)
            private Object mockObjectWithAnswerClassThatThrowsException;

            public void innerTest() {
                try {
                    initMocks(this);
                    fail("RuntimeException expected ");
                } catch (MockitoException ex) {
                    assertEquals("Custom answer cannot be instantiated",ex.getMessage());
                }
            }
        }

        new should_throw_exception_when_custom_answer_throws_exception_in_constructor_test_class().innerTest();

    }

    @Test
    public void should_throw_exception_when_both_answer_and_answer_class_are_defined() throws NoSuchFieldException {
        class should_throw_exception_when_both_answer_and_answer_class_are_defined_test_class {
            @Mock(answerClass=MyCustomAnswerClass.class, answer=Answers.RETURNS_DEEP_STUBS)
            private Object mockObjectWithBothAnswerAndAnswerClassDefined;

            public void innerTest() {
                try {
                    initMocks(this);
                    fail("RuntimeException expected");
                } catch (MockitoException ex) {
                    assertEquals(ex.getMessage(), "You cannot define answer and customAnswer at the same time for field returnedObject");
                }
            }
        }
    }

    @Test
    public void custom_answer_integration_test() {

        assertEquals(mockedList.size(),MOCKED_LIST_SIZE);

    }

    public static class MyCustomAnswerClass implements Answer<Object> {

        static Object returnedObject = new Object();

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return returnedObject;
        }
    }

    public static class MyCustomAnswerWithMultipleConstructors implements Answer<Object> {

        public MyCustomAnswerWithMultipleConstructors() {
        }

        public MyCustomAnswerWithMultipleConstructors(String arg1) {
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return MyCustomAnswerClass.returnedObject;
        }
    }

    private class NotInstantiableClass implements Answer<Object> {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return null;
        }
    }

    public static class AnswerThrowingConstructor implements Answer<Object> {

        public AnswerThrowingConstructor() {
            throw new RuntimeException("throwing");
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return null;
        }
    }


    public static class ListCustomAnswer implements Answer<Integer> {
        @Override
        public Integer answer(InvocationOnMock invocation) throws Throwable {
            return MOCKED_LIST_SIZE;
        }
    }
}