/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockitousage.annotation.MockAnnotationBuilder.*;

@SuppressWarnings("unchecked")
public class MockWithCustomAnswerAnnotationsTest extends TestBase {

    public static final int MOCKED_LIST_SIZE = 3;
    @Mock(customAnswer = MyCustomAnswer.class)
    IMethods mockWithCustomAnswer;

    @Mock(customAnswer = MyCustomAnswerWithMultipleConstructors.class)
    IMethods mockWithCustomAnswerHavingMultipleConstructors;

    @Mock(customAnswer = ListCustomAnswer.class)
    List<String> mockedList;

    @Test
    public void shouldUseCustomAnswer() {
        assertSame(MyCustomAnswer.returnedObject, mockWithCustomAnswer.objectReturningMethodNoArgs());
    }

    @Test
    public void should_choose_default_constructor() {
        assertSame(MyCustomAnswer.returnedObject, mockWithCustomAnswerHavingMultipleConstructors.objectReturningMethodNoArgs());
    }

    @Test
    public void should_throw_exception_when_custom_answer_cannot_be_instantiated() throws NoSuchFieldException {
        Field aField = MyCustomAnswer.class.getDeclaredFields()[0];

        try {
            new MockAnnotationProcessor().process(aMockAnnotation().withCustomAnswer(NotInstantiableClass.class).build(), aField);
            fail("RuntimeException expected ");
        } catch (MockitoException ex) {
            assertEquals("Custom answer cannot be instantiated",ex.getMessage());
        }
    }

    @Test
    public void should_throw_exception_when_custom_answer_throws_exception_in_constructor() throws NoSuchFieldException {
        Field aField = MyCustomAnswer.class.getDeclaredFields()[0];

        try {
            new MockAnnotationProcessor().process(aMockAnnotation().withCustomAnswer(AnswerThrowingConstructor.class).build(), aField);
            fail("RuntimeException expected ");
        } catch (MockitoException ex) {
            assertEquals("Custom answer cannot be instantiated",ex.getMessage());
        }
    }

    @Test
    public void should_throw_exception_when_both_answer_and_custom_answer_are_defined() throws NoSuchFieldException {
        Field aField = MyCustomAnswer.class.getDeclaredFields()[0];

        try {
            Mock mockWithAnswers = aMockAnnotation().withAnswer(Answers.RETURNS_DEEP_STUBS).withCustomAnswer(MyCustomAnswer.class).build();
            new MockAnnotationProcessor().process(mockWithAnswers, aField);
            fail("Exception expected ");
        } catch (MockitoException ex) {
            assertEquals(ex.getMessage(), "You cannot define answer and customAnswer at the same time for field returnedObject");
        }
    }

    @Test
    public void custom_answer_integration_test() {

        assertEquals(mockedList.size(),MOCKED_LIST_SIZE);

    }

    public static class MyCustomAnswer implements Answer<Object> {

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
            return MyCustomAnswer.returnedObject;
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