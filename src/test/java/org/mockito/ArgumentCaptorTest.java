/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.After;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockitoutil.TestBase;

import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentCaptorTest extends TestBase {

    @Test
    public void tell_handy_return_values_to_return_value_for() throws Exception {
        //given
        final Object expected = new Object();
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        argumentCaptor.handyReturnValues = will_return(expected);

        //when
        Object returned = argumentCaptor.capture();

        //then
        assertEquals(expected, returned);
    }

    @Test
    public void should_capture_matched_arguments() throws Exception {
        //given
        final String testString = "myTestString";
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class, new ArgumentMatcher() {
            public boolean matches(Object argument) {
                return argument.equals(testString);
            }
        });

        List list = Mockito.mock(List.class);

        //when
        list.add(testString);

        //then
        Mockito.verify(list).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue(), testString);
    }

    @Test
    public void should_exclude_unmatched_arguments() {
        //given
        final String testString = "myTestString";
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class, new ArgumentMatcher() {
            public boolean matches(Object argument) {
                return argument.equals(testString);
            }
        });

        List list = Mockito.mock(List.class);

        //when
        list.add("notMyTestString");

        //then
        Mockito.verify(list, Mockito.never()).add(argumentCaptor.capture());

        try {
            assertEquals(argumentCaptor.getValue(), testString);
            fail();
        } catch (MockitoException e) {

        }
    }

    private HandyReturnValues will_return(final Object expected) {
        return new HandyReturnValues() {
            @Override
            public <T> T returnFor(Class<T> clazz) {
                return (T) expected;
            }
        };
    }

    @After
    public void yes_I_know_some_matchers_are_misplaced() {
        resetState();
    }
}