/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class MockWithCustomAnswerAnnotationsTest extends TestBase {

    @Mock(customAnswer=MyCustomAnswer.class) IMethods mockWithCustomAnswer;

    @Mock(customAnswer=MyCustomAnswerWithMultipleConstructors.class) IMethods mockWithCustomAnswerHavingMultipleConstructors;

    @Test
    public void shouldUseCustomAnswer() {
        assertSame(MyCustomAnswer.returnedObject,mockWithCustomAnswer.objectReturningMethodNoArgs());
    }

    @Test
    public void shouldChooseDefaultConstructor() {
        assertSame(MyCustomAnswer.returnedObject,mockWithCustomAnswerHavingMultipleConstructors.objectReturningMethodNoArgs());
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
}